package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import client.ui.*;
import common.CLog;
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
    // UI
    private static UiWindow uiMain;
    private static UiWindow uiCreate;
    private static Serverbrowser uiBrowser;
    private static LobbyChat uiLobbyChat;
    private static UiWindow uiOptions;
    private static UiWindow uiCredits;

    private static ArrayList<Server> serverlist = new ArrayList<Server>();
    private static long[] latencylist;
    
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

        // Anmeldung: Client -> Masterserver
        net.send(Network.MASTERHOST, Network.MASTERPORT, ProtocolCmd.CLIENT_MASTER_AUTH);
        new Chat(net).start();

        frame = new JFrame();
        frame.setIgnoreRepaint(true);

        screen = new MainScreen(frame);
        
        // Input
        input = new Input(screen);
        frame.addKeyListener(input);
        screen.addMouseMotionListener(input);

        // Init Interfaces
        UiActionListener aListener = new UiActionListener(net);
        uiMain = new MainMenu(screen);
        uiMain.setLocation(10, 200);
        uiMain.addActionListener(aListener);
        uiMain.setMoveable(false);
        uiCreate = new CreateServer(screen);
        uiCreate.setLocation(200, 200);
        uiCreate.addActionListener(aListener);
        uiBrowser = new Serverbrowser(screen);
        uiBrowser.setLocation(200, 200);
        uiBrowser.addActionListener(aListener);
        uiLobbyChat = new LobbyChat(screen);
        uiLobbyChat.setLocation(200, 200);
        uiLobbyChat.addActionListener(aListener);
        uiOptions = new Options(screen);
        uiOptions.setLocation(200, 200);
        uiOptions.addActionListener(aListener);
        uiCredits = new Credits(screen);
        uiCredits.setLocation(screen.getWidth()/2  - uiCredits.getWidth()/2,
                              screen.getHeight()/2 - uiCredits.getHeight()/2);
 
        // UIManger
        UiManager.addComponent(uiMain);
        UiManager.addComponent(uiCreate);
        UiManager.addComponent(uiBrowser);
        UiManager.addComponent(uiLobbyChat);
        UiManager.addComponent(uiOptions);
        UiManager.addComponent(uiCredits);

        screen.getContentPane().add(uiMain);
        screen.getContentPane().add(uiCreate);
        screen.getContentPane().add(uiBrowser);
        screen.getContentPane().add(uiLobbyChat);
        screen.getContentPane().add(uiOptions);
        screen.getContentPane().add(uiCredits);

        // GameData
        data = new GameData(input);

        // UpdateLoop
        loop = new UpdateLoop(60);
        loop.addUpdateObject(screen);
        loop.addUpdateObject(data);

        EventQueue.invokeLater(new Runnable() {
           public void run() {
               frame.setVisible(true);
               uiMain.setVisible(true);
           }
        });
        
//        net.connect("localhost", 40000);
//        net.send(Protocol.CLIENT_SERVER_AUTH);
    }

    public static MainScreen getScreen() {
        return screen;
    }

    public static GameData getGameData() {
        return data;
    }

    public static Network getNetwork() {
        return net;
    }

    public static void completeServerlist() {
        /* | Server | Map | Spieler | Ping | */
        String[][] server = new String[serverlist.size()][4];
        latencylist = new long[serverlist.size()];
        for(int i=0; i<serverlist.size(); i++){
            server[i][0] = serverlist.get(i).getHost()+":"+serverlist.get(i).getPort();
            server[i][1] = "<pending>";
            server[i][2] = "<pending>";
            server[i][3] = "<pending>";
        }
        uiBrowser.setServerlist(server);
        // Daten aktualisieren
        for(int i=0; i<serverlist.size(); i++){
            String ip = serverlist.get(i).getHost();
            int port = serverlist.get(i).getPort();
            InetSocketAddress adr = new InetSocketAddress(ip, port);
            latencylist[i] = System.nanoTime();
            net.send(adr, ProtocolCmd.CLIENT_SERVER_LATENCY);       // LATENCY
            net.send(adr, ProtocolCmd.CLIENT_SERVER_CURRENT_MAP);   // MAP
            net.send(adr, ProtocolCmd.CLIENT_SERVER_PLAYERS);       // PLAYERS
        }
    }

    public static Server getServer(String host, int port) {
        for(Server cur : serverlist) {
            if(cur.getHost().equals(host) && cur.getPort() == port)
                return cur;
        }

        return null;
    }

    public static void refreshLatency(InetSocketAddress adr, long nanoTime){
        for(int i=0; i<Main.serverlist.size(); i++){
            if(serverlist.get(i).getAddress().equals(adr)){
                latencylist[i] = nanoTime - latencylist[i];
                uiBrowser.refreshValue(new DecimalFormat("#0.00").format(latencylist[i]*0.000001)+"ms", i, 3);
                break;
            }
        }
    }

    public static void refreshCurrentMap(InetSocketAddress adr, String map){
        for(int i=0; i<Main.serverlist.size(); i++){
            if(serverlist.get(i).getAddress().equals(adr)){
                uiBrowser.refreshValue(map, i, 1);
                break;
            }
        }
    }

    public static void refreshPlayers(InetSocketAddress adr, String players){
        for(int i=0; i<Main.serverlist.size(); i++){
            if(serverlist.get(i).getAddress().equals(adr)){
                uiBrowser.refreshValue(players, i, 2);
                break;
            }
        }
    }

    public static UiWindow getUiMainMenu() {
        return uiMain;
    }

    public static UiWindow getUiCreateServer() {
        return uiCreate;
    }

    public static Serverbrowser getUiServerbrowser() {
        return uiBrowser;
    }

    public static LobbyChat getUiLobbyChat() {
        return uiLobbyChat;
    }

    public static UiWindow getUiOptions() {
        return uiOptions;
    }

    public static UiWindow getUiCredits() {
        return uiCredits;
    }

    public static void addServerToServerlist(Server s){
        serverlist.add(s);
    }

    public static void clearServerlist(){
        serverlist.clear();
    }
}
