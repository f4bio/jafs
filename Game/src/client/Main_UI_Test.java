package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import client.ui.*;
import common.CLog;
import common.net.Network;
import common.net.Protocol;
import java.awt.EventQueue;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Julian Sanio
 *
 * ONLY TESTING ;)
 *
 */
public class Main_UI_Test {

    private static MainScreen screen;
    private static Network net;
    private static JFrame frm;
    private static UiActionListener aListener;
    // UI
    private static UiWindow uiMain;
    private static UiWindow uiCreate;
    private static Serverbrowser uiBrowser;
    private static LobbyChat uiLobbyChat;
    private static UiWindow uiOptions;
    private static UiWindow uiCredits;
    //private static WeaponSidebar wSidebar;

    public static ArrayList<String> serverlist = new ArrayList<String>();

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

        Protocol.init();
        net = new Network();
        net.listen(31330);


        ProtocolHandler protocol = new ProtocolHandler(net);
        net.setProtocolHandler(protocol);
        net.listen(net.getFreePort(50000, 65000));
//        net.send("localhost", 40000, Protocol.CLIENT_SERVER_AUTH);
        net.send("secureit.ath.cx", Network.MASTERPORT, Protocol.CLIENT_MASTER_AUTH);
//        new Chat(net).start();

        aListener = new UiActionListener(net);
        //UiKeyListener kListener = new UiKeyListener();

        frm = new JFrame();
        frm.setIgnoreRepaint(true);
        //frm.addKeyListener(kListener);

        screen = new MainScreen(frm);

        // Init Interfaces
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
        uiCredits.setLocation(screen.getWidth()/2 - uiCredits.getWidth()/2,
                              screen.getHeight()/2 - uiCredits.getHeight()/2);
/*      wSidebar = new WeaponSidebar(300, 100);
        wSidebar.setLocation(screen.getWidth()-wSidebar.getWidth(),
                             screen.getHeight()-wSidebar.getHeight()-200);    */
 
        // UIManger
        UiManager.addComponent(uiMain);
        UiManager.addComponent(uiCreate);
        UiManager.addComponent(uiBrowser);
        UiManager.addComponent(uiLobbyChat);
        UiManager.addComponent(uiOptions);
        UiManager.addComponent(uiCredits);
        //UiManager.addComponent(wSidebar);

        // MainScreen
        screen.getContentPane().add(uiMain);
        screen.getContentPane().add(uiCreate);
        screen.getContentPane().add(uiBrowser);
        screen.getContentPane().add(uiLobbyChat);
        screen.getContentPane().add(uiOptions);
        screen.getContentPane().add(uiCredits);
        //screen.getContentPane().add(wSidebar);
        
        // UpdateLoop
        UpdateLoop loop = new UpdateLoop(60);
        loop.addUpdateObject(screen);
        //loop.addUpdateObject(wSidebar);

        EventQueue.invokeLater(new Runnable() {
           public void run() {
               frm.setVisible(true);
               uiMain.setVisible(true);
               //wSidebar.setVisible(true);
           }
        });
    }

    public static MainScreen getScreen() {
        return screen;
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

    //public static WeaponSidebar getWeaponSidebar() {
    //    return wSidebar;
    //}

    public static void completeServerlist(ArrayList<String> list) {
        /* | Server | Map | Spieler | Ping | */
        String[][] server = new String[list.size()][4];
        for(int i=0; i<list.size(); i++){
            server[i][0] = list.get(i);
            server[i][1] = "map";
            server[i][2] = "0/16";
            server[i][3] = "5ms";
        }
        uiBrowser.setServerlist(server);
    }
}
