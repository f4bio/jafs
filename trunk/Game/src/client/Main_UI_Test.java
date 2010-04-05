package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import client.ui.*;
import common.CLog;
import java.awt.EventQueue;
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
    private static UiWindow uiMain;
    private static UiWindow uiCreate;
    private static Serverbrowser uiBrowser;
    private static UiWindow uiLobbyChat;
    private static UiWindow uiOptions;
    private static UiWindow uiCredits;
    private static WeaponSidebar wSidebar;
    private static JFrame frm;

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

        UiActionListener aListener = new UiActionListener();
        UiKeyListener kListener = new UiKeyListener();

        frm = new JFrame();
        frm.setIgnoreRepaint(true);
        frm.addKeyListener(kListener);

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
           }
        });
        
        //wSidebar.setVisible(true);
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

    public static UiWindow getUiLobbyChat() {
        return uiLobbyChat;
    }

    public static UiWindow getUiOptions() {
        return uiOptions;
    }

    public static UiWindow getUiCredits() {
        return uiCredits;
    }

    public static WeaponSidebar getWeaponSidebar() {
        return wSidebar;
    }
}
