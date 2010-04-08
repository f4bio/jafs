package client.ui;

import client.Main;
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
            Main.getUiCreateServer().setVisible(Main.getUiCreateServer().isVisible()?false:true);
        }
        // Serverbrowser
        else if(e.getActionCommand().equals(CMD_TOGGLE_SERVERBROWSER)) {
            Main.getUiServerbrowser().setVisible(Main.getUiServerbrowser().isVisible()?false:true);
            net.send(Network.MASTERHOST, Network.MASTERPORT, Protocol.CLIENT_MASTER_LISTREQUEST);
        }
        // Lobby Chat
        else if(e.getActionCommand().equals(CMD_TOGGLE_LOBBYCHAT)) {
            Main.getUiLobbyChat().setVisible(Main.getUiLobbyChat().isVisible()?false:true);
        }
        // Options
        else if(e.getActionCommand().equals(CMD_TOGGLE_OPTIONS)) {
            Main.getUiOptions().setVisible(Main.getUiOptions().isVisible()?false:true);
        }
        // Serverliste aktualisieren
        else if(e.getActionCommand().equals(CMD_REFRESH_SERVERBROWSER)) {
            net.send(Network.MASTERHOST, Network.MASTERPORT, Protocol.CLIENT_MASTER_LISTREQUEST);
        }
        // Mit Server verbinden
        else if(e.getActionCommand().equals(CMD_CONNECT)) {
            if(Main.getUiServerbrowser().getSelectedServer() != null) {
                System.out.println("Connect to Server: "+Main.getUiServerbrowser().getSelectedServer());
                net.send(Main.getUiServerbrowser().getSelectedServer(), Protocol.CLIENT_SERVER_AUTH);
                net.send(Network.MASTERHOST, Network.MASTERPORT, Protocol.CLIENT_MASTER_JOINSERVER, Main.getUiServerbrowser().getSelectedServer());
            }
        }
        // Credits
        else if(e.getActionCommand().equals(CMD_TOGGLE_CREDITS)) {
            Main.getUiCredits().setVisible(Main.getUiCredits().isVisible()?false:true);
        }
        // Lobby Chat SendMSG
        else if(e.getActionCommand().equals(CMD_LOBBYCHAT_SEND_MSG)) {
            net.send(Network.MASTERHOST, Network.MASTERPORT, Protocol.CLIENT_MASTER_CHAT_LOBBY, Main.getUiLobbyChat().getMSG());
        }
        // Exit
        else if(e.getActionCommand().equals(CMD_EXIT)) {
            //net.send("localhost", 40000, Protocol.CLIENT_SERVER_LOGOFF);   //wenn mit server verbunden
            net.send(Network.MASTERHOST, Network.MASTERPORT, Protocol.CLIENT_MASTER_LOGOFF);
            System.exit(0);
        }
    }
}
