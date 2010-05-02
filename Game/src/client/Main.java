package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import client.ui.*;
import common.CLog;
import common.engine.CPlayer;
import common.net.Client;
import common.net.Network;
import common.net.Protocol;
import common.net.ProtocolCmd;
import common.net.Server;
import common.utils.CUtils;
import java.awt.EventQueue;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JFrame;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author Julian Sanio
 *
 */
public class Main {

    private static JFrame frame;
    private static MainScreen screen;
    private static Network net;
    private static Input input;
    private static GameData data;
    private static UpdateLoop loop;
    // GUI
    private static MainMenu mainMenu;
    // UI
    private static InGameChat uiInGameChat;

    private static ArrayList<Server> serverlist = new ArrayList<Server>();
    private static ArrayList<Client> clientlist = new ArrayList<Client>();
    
    /**
     *
     */
    public static final String PATH = CUtils.getApplicationPath("Game");

    
    /**
     *
     */
    public static class Handler {
        /**
         *
         * @param t
         */
        public void handler(Throwable t) {
            try {
                CLog.log(t.getMessage());
            } catch(Throwable a) {

            }
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        CLog.init("debug.txt");
        System.setProperty("sun.awt.exception.handler", Handler.class.getName());

        // Protocol
        Protocol.init();
        net = new Network();
        ProtocolHandler pHandler = new ProtocolHandler(net);
        net.setProtocolHandler(pHandler);
        net.listen(net.getFreePort(50000, 65000));

        // Frame
        frame = new JFrame();
        frame.setIgnoreRepaint(true);

        // Screen
        screen = new MainScreen(frame);
        screen.setVisible(false);
        
        UiActionListener aListener = new UiActionListener(net);
        UiKeyListener kListener = new UiKeyListener();

        // Main GUI
        mainMenu = new MainMenu(aListener);
        // Input
        input = new Input(screen);
        frame.addKeyListener(input);
        frame.addKeyListener(kListener);
        screen.addMouseMotionListener(input);
        screen.addMouseListener(input);

        // InGame UI
        uiInGameChat = new InGameChat(screen);
        uiInGameChat.setLocation(10, screen.getHeight()-uiInGameChat.getHeight()-10);
        uiInGameChat.addActionListener(aListener);
        uiInGameChat.addKeyListener(kListener);
 
        // UIManger
        UiManager.addComponent(uiInGameChat);

        screen.getContentPane().add(uiInGameChat);

        // GameData
        data = new GameData(input);
        data.setName(System.getProperty("user.name"));
        mainMenu.setSelfName(Main.getGameData().getName());
        // UpdateLoop
        loop = new UpdateLoop(60);
        loop.addUpdateObject(screen);
        loop.addUpdateObject(data);

        // Anmeldung: Client -> Masterserver
        net.send(Network.MASTERHOST, Network.MASTERPORT, ProtocolCmd.CLIENT_MASTER_AUTH, argStr(data.getName()));
//        new Chat(net).start();

        EventQueue.invokeLater(new Runnable() {
           public void run() {
//               frame.setVisible(true);
               mainMenu.setVisible(true);
           }
        });
        
        CLog.close();
    }

    /**
     *
     * @return
     */
    public static MainScreen getScreen() {
        return screen;
    }

    /**
     *
     * @return
     */
    public static JFrame getFrame(){
        return frame;
    }

    /**
     *
     * @return
     */
    public static GameData getGameData() {
        return data;
    }

    /**
     *
     * @return
     */
    public static Network getNetwork() {
        return net;
    }

    /**
     *
     * @return
     */
    public static MainMenu getMainMenu() {
        return mainMenu;
    }

    /**
     *
     * @return
     */
    public static InGameChat getUiInGameChat() {
        return uiInGameChat;
    }


// --- Server ---

    /**
     *
     * @param host
     * @param port
     * @return
     */
    public static Server getServer(String host, int port) {
        for(Server cur : serverlist) {
            if(cur.getHost().equals(host) && cur.getPort() == port)
                return cur;
        }
        return null;
    }

    /**
     *
     * @param adr
     * @return
     */
    public static int getServerlistIndex(InetSocketAddress adr) {
        for(int i=0; i<Main.serverlist.size(); i++){
            if(serverlist.get(i).getAddress().equals(adr)){
                return i;
            }
        }
        return -1;
    }
    
    /**
     *
     * @param s
     */
    public static void addServerToServerlist(Server s){
        serverlist.add(s);
    }
    
    /**
     *
     */
    public static void clearServerlist(){
        serverlist.clear();
    }

    /**
     *
     */
    public static void completeServerlist() {
        if(serverlist.size() == 0){
            String[][] list = new String[1][4];
            list[0][0] = "No server listed.";
            list[0][1] = "";
            list[0][2] = "";
            list[0][3] = "";
            mainMenu.setServerlist(list);
        } else {
            String[][] list = new String[serverlist.size()][4];
            /* | Server | Map | Spieler | Ping | */
            for(int i=0; i<serverlist.size(); i++){
                list[i][0] = serverlist.get(i).getHostPort();
                list[i][1] = "<pending>";
                list[i][2] = "<pending>";
                list[i][3] = "<pending>";
            }
            mainMenu.clearServerinfoPanel();
            mainMenu.setServerlist(list);
            // map, players, latency requests
            for(int i=0; i<serverlist.size(); i++){
                InetSocketAddress adr = serverlist.get(i).getAddress();
                serverlist.get(i).setClientServerLatency(System.nanoTime());
                net.send(adr, ProtocolCmd.CLIENT_SERVER_LATENCY);       // LATENCY
                net.send(adr, ProtocolCmd.CLIENT_SERVER_REQUEST_SERVER_INFO, argStr(data.getName()));
            }
        }
    }

    /**
     *
     * @param adr
     * @param nanoTime
     */
    public static void refreshLatency(InetSocketAddress adr, long nanoTime){
        int i = getServerlistIndex(adr);
        serverlist.get(i).setClientServerLatency(nanoTime);
        mainMenu.refreshServerTableValue(new DecimalFormat("#0.00").format(serverlist.get(i).getClientSserverLatency()*0.000001)+"ms", i, 3);
    }

    /**
     *
     * @param adr
     * @param map
     */
    public static void refreshServerInfo(String name, String map, String players, int highscore, InetSocketAddress adr){
        int i = getServerlistIndex(adr);
        serverlist.get(i).setName(name);
        mainMenu.refreshServerTableValue(name, i, 0);
        serverlist.get(i).setMap(map);
        mainMenu.refreshServerTableValue(map, i, 1);
        serverlist.get(i).setCurPlayers(players);
        mainMenu.refreshServerTableValue(players, i, 2);
        serverlist.get(i).setClientHighscore(highscore);
        mainMenu.refreshServerinfoPanel();
    }

    /**
     *
     * @return
     */
    public static Server getSelectedServer(){
        if(mainMenu.getSelectedServerlistIndex() >= 0)
//            return new Server(serverlist.get(mainMenu.getSelectedServerlistIndex()).getAddress());
            return serverlist.get(mainMenu.getSelectedServerlistIndex());
        else
            return null;
    }


// --- Client ---

    /**
     *
     * @param c
     */
    public static void addClientToClientlist(Client c){
        clientlist.add(c);
        mainMenu.addClientToList(c);
    }

    /**
     *
     */
    public static void clearClientlist(){
        clientlist.clear();
        mainMenu.clearClientlist();
    }

    /**
     *
     */
    public static void refInGameClientlist(){
        System.out.print("refInGameClientlist()");
        clientlist.clear();
        uiInGameChat.clearClientlist();
        CPlayer[] p = data.getPlayers();
        for(int i=0; i<p.length; i++){
            if(p[i] != null){ // && !p[i].getName().equals(data.getName())) {
                uiInGameChat.addClientToList(p[i].getId(), p[i].getName());
                System.out.println(" -> added \""+p[i].getName()+"\" (id="+p[i].getId()+")");
            }
        }
        System.out.println(" finished!");
    }

    /**
     *
     * @param id
     * @return
     */
    public static String getClientName(int id){
        if(id == data.getSelfId()){
            return data.getName();
        } else if(id == -1){
            return "Masterserver";
        }
        for(Client client: clientlist)
            if(client.getId() == id)
                return client.getPlayer().getName();
        return null;
    }
}
