package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import client.ui.*;
import common.CLog;
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
//    private static UiWindow uiCreate;
//    private static Serverbrowser uiBrowser;
//    private static LobbyChat uiLobbyChat;
//    private static UiWindow uiOptions;
//    private static UiWindow uiCredits;

    private static ArrayList<Server> serverlist = new ArrayList<Server>();
    private static ArrayList<Client> clientlist = new ArrayList<Client>();
    
    public static final String PATH = CUtils.getApplicationPath("Game");

    
    public static class Handler {
        public void handler(Throwable t) {
            try {
                CLog.log(t.getMessage());
            } catch(Throwable a) {

            }
        }
    }

    public static void main(String[] args) {
        CLog.init("debug.txt");
        System.setProperty("sun.awt.exception.handler", Handler.class.getName());

        // Protocol
        Protocol.init();
        net = new Network();
        ProtocolHandler pHandler = new ProtocolHandler(net);
        net.setProtocolHandler(pHandler);
        net.listen(net.getFreePort(50000, 65000));

        frame = new JFrame();
        frame.setIgnoreRepaint(true);

        screen = new MainScreen(frame);
        screen.setVisible(false);
        
        // Input
        input = new Input(screen);
        frame.addKeyListener(input);
        screen.addMouseMotionListener(input);

        // Init Interfaces
        UiActionListener aListener = new UiActionListener(net);
        UiKeyListener kListener = new UiKeyListener();

        mainMenu = new MainMenu(aListener, net);

//        uiCreate = new CreateServer(screen);
//        uiCreate.setLocation(200, 200);
//        uiCreate.addActionListener(aListener);
//        uiCreate.addKeyListener(kListener);
//        uiBrowser = new Serverbrowser(screen);
//        uiBrowser.setLocation(200, 200);
//        uiBrowser.addActionListener(aListener);
//        uiBrowser.addKeyListener(kListener);
//        uiLobbyChat = new LobbyChat(screen);
//        uiLobbyChat.setLocation(200, 200);
//        uiLobbyChat.addActionListener(aListener);
//        uiLobbyChat.addKeyListener(kListener);
//        uiOptions = new Options(screen);
//        uiOptions.setLocation(200, 200);
//        uiOptions.addActionListener(aListener);
//        uiOptions.addKeyListener(kListener);
//        uiCredits = new Credits(screen);
//        uiCredits.setLocation(screen.getWidth()/2  - uiCredits.getWidth()/2,
//                              screen.getHeight()/2 - uiCredits.getHeight()/2);
//        uiCredits.addActionListener(aListener);
//        uiCredits.addKeyListener(kListener);
 
        // UIManger
//        UiManager.addComponent(uiCreate);
//        UiManager.addComponent(uiBrowser);
//        UiManager.addComponent(uiLobbyChat);
//        UiManager.addComponent(uiOptions);
//        UiManager.addComponent(uiCredits);

//        screen.getContentPane().add(uiCreate);
//        screen.getContentPane().add(uiBrowser);
//        screen.getContentPane().add(uiLobbyChat);
//        screen.getContentPane().add(uiOptions);
//        screen.getContentPane().add(uiCredits);

        // GameData
        data = new GameData(input);
        data.setName(System.getProperty("user.name"));
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

    public static MainScreen getScreen() {
        return screen;
    }

    public static JFrame getFrame(){
        return frame;
    }

//    public static void hideUi(){
//        EventQueue.invokeLater(new Runnable() {
//           public void run() {
//                uiCreate.setVisible(false);
//                uiBrowser.setVisible(false);
//                uiLobbyChat.setVisible(false);
//                uiOptions.setVisible(false);
//                uiCredits.setVisible(false);
//                frame.requestFocus();
//           }
//        });
//
//    }
//
//    public static void showUi(){
//        EventQueue.invokeLater(new Runnable() {
//           public void run() {
//                uiMain.setVisible(true);
//                uiMain.requestFocus();
//           }
//        });
//
//    }

    public static GameData getGameData() {
        return data;
    }

    public static Network getNetwork() {
        return net;
    }

    public static Server getServer(String host, int port) {
        for(Server cur : serverlist) {
            if(cur.getHost().equals(host) && cur.getPort() == port)
                return cur;
        }
        return null;
    }

    public static int getServerlistIndex(InetSocketAddress adr) {
        for(int i=0; i<Main.serverlist.size(); i++){
            if(serverlist.get(i).getAddress().equals(adr)){
                return i;
            }
        }
        return -1;
    }

    public static MainMenu getMainMenu() {
        return mainMenu;
    }

//    public static UiWindow getUiCreateServer() {
//        return uiCreate;
//    }

//    public static Serverbrowser getUiServerbrowser() {
//        return uiBrowser;
//    }

//    public static LobbyChat getUiLobbyChat() {
//        return uiLobbyChat;
//    }

//    public static UiWindow getUiOptions() {
//        return uiOptions;
//    }

//    public static UiWindow getUiCredits() {
//        return uiCredits;
//    }
    
    public static void addServerToServerlist(Server s){
        serverlist.add(s);
    }

    public static void addClientToClientlist(Client c){
        clientlist.add(c);
        mainMenu.addClientToList(c);
    }

    public static void clearServerlist(){
        serverlist.clear();
    }

    public static void clearClientlist(){
        clientlist.clear();
        mainMenu.clearClientlist();
    }

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
            mainMenu.setServerlist(list);
            // map, players, latency requests
            for(int i=0; i<serverlist.size(); i++){
                InetSocketAddress adr = serverlist.get(i).getAddress();
                serverlist.get(i).setClientServerLatency(System.nanoTime());
                net.send(adr, ProtocolCmd.CLIENT_SERVER_LATENCY);       // LATENCY
                net.send(adr, ProtocolCmd.CLIENT_SERVER_CURRENT_MAP);   // MAP
                net.send(adr, ProtocolCmd.CLIENT_SERVER_PLAYERS);       // PLAYERS
            }
        }
    }

    public static void refreshLatency(InetSocketAddress adr, long nanoTime){
        int i = getServerlistIndex(adr);
        serverlist.get(i).setClientServerLatency(nanoTime);
        mainMenu.refreshValue(new DecimalFormat("#0.00").format(serverlist.get(i).getClientSserverLatency()*0.000001)+"ms", i, 3);
    }

    public static void refreshCurrentMap(InetSocketAddress adr, String map){
        int i = getServerlistIndex(adr);
        serverlist.get(i).setMap(map);
        mainMenu.refreshValue(map, i, 1);
    }

    public static void refreshPlayers(InetSocketAddress adr, String players){
        int i = getServerlistIndex(adr);
        serverlist.get(i).setCurPlayers(players);
        mainMenu.refreshValue(players, i, 2);
    }

    public static Server getSelectedServer(){
        if(mainMenu.getSelectedServerlistIndex() >= 0)
            return new Server(serverlist.get(mainMenu.getSelectedServerlistIndex()).getAddress());
        else
            return null;
    }

    public static String getClientName(int id){
        if(id == data.getSelfId()){
            return data.getName();
        }
        for(Client client: clientlist)
            if(client.getId() == id)
                return client.getPlayer().getName();
        return null;
    }
}
