package client.ui;

import client.Main_UI_Test;
import common.net.Network;
import common.net.Protocol;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Julia Sanio
 */
public class UiActionListener implements ActionListener {

    // Command list
    public static final String CMD_TOGGLE_SERVERBROWSER     = "1";
    public static final String CMD_TOGGLE_LOBBYCHAT         = "2";
    public static final String CMD_TOGGLE_OPTIONS           = "3";
    public static final String CMD_TOGGLE_CREDITS           = "4";
    public static final String CMD_TOGGLE_CREATESERVER      = "5";
    public static final String CMD_EXIT                     = "6";
    public static final String CMD_CONNECT                  = "7";
    public static final String CMD_REFRESH_SERVERBROWSER    = "8";
    public static final String CMD_LOBBYCHAT_SEND_MSG       = "9";
    
    private Network net;

    public UiActionListener(Network network){
        net = network;
    }

    public void actionPerformed(ActionEvent e) {
//        System.out.println(e.getActionCommand() + " (actionPerformed by " + e.getSource().getClass().getSimpleName() + ")");
        // Server erstellen
        if(e.getActionCommand().equals(CMD_TOGGLE_CREATESERVER)) {
            Main_UI_Test.getUiCreateServer().setVisible(Main_UI_Test.getUiCreateServer().isVisible()?false:true);
        }
        // Serverbrowser
        else if(e.getActionCommand().equals(CMD_TOGGLE_SERVERBROWSER)) {
            Main_UI_Test.getUiServerbrowser().setVisible(Main_UI_Test.getUiServerbrowser().isVisible()?false:true);
            net.send("localhost", 30000, Protocol.CLIENT_MASTER_LISTREQUEST);
        }
        // Lobby Chat
        else if(e.getActionCommand().equals(CMD_TOGGLE_LOBBYCHAT)) {
            Main_UI_Test.getUiLobbyChat().setVisible(Main_UI_Test.getUiLobbyChat().isVisible()?false:true);
        }
        // Options
        else if(e.getActionCommand().equals(CMD_TOGGLE_OPTIONS)) {
            Main_UI_Test.getUiOptions().setVisible(Main_UI_Test.getUiOptions().isVisible()?false:true);
        }
        // Serverliste aktualisieren
        else if(e.getActionCommand().equals(CMD_REFRESH_SERVERBROWSER)) {
         /* String[][] s = {{"127.0.0.1:40000", "dust", "0/16", "500"},
                            {"192.0.0.1:40001", "italy", "2/16", "80"},
                            {"162.0.0.1:40002", "aztec", "5/16", "63"}};
            Main_UI_Test.getUiServerbrowser().setServerlist(s); */
            net.send("localhost", 30000, Protocol.CLIENT_MASTER_LISTREQUEST);
        }
        // Mit Server verbinden
        else if(e.getActionCommand().equals(CMD_CONNECT)) {
            if(Main_UI_Test.getUiServerbrowser().getSelectedServer() != null) {
                System.out.println("Connect to Server: "+Main_UI_Test.getUiServerbrowser().getSelectedServer());
                net.send(Main_UI_Test.getUiServerbrowser().getSelectedServer(), Protocol.CLIENT_SERVER_AUTH);
                net.send("localhost", 30000, Protocol.CLIENT_MASTER_JOINSERVER, Main_UI_Test.getUiServerbrowser().getSelectedServer());
            }
        }
        // Credits
        else if(e.getActionCommand().equals(CMD_TOGGLE_CREDITS)) {
            Main_UI_Test.getUiCredits().setVisible(Main_UI_Test.getUiCredits().isVisible()?false:true);
        }
        // Lobby Chat SendMSG
        else if(e.getActionCommand().equals(CMD_LOBBYCHAT_SEND_MSG)) {
            net.send("localhost", 30000, Protocol.CLIENT_MASTER_CHAT_LOBBY, Main_UI_Test.getUiLobbyChat().getMSG());
        }
        // Exit
        else if(e.getActionCommand().equals(CMD_EXIT)) {
            net.send("localhost", 40000, Protocol.CLIENT_SERVER_LOGOFF);
            System.exit(0);
        }
    }
}
