package client.ui;

import client.Main;
import common.net.Network;
import common.net.ProtocolCmd;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static common.net.ProtocolCmdArgument.*;

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

    public UiActionListener(Network net) {
        this.net = net;
    }

    public void actionPerformed(ActionEvent e) {
//        System.out.println(e.getActionCommand() + " (actionPerformed by " + e.getSource().getClass().getSimpleName() + ")");
        
        // Server erstellen
        if(e.getActionCommand().equals(CMD_TOGGLE_CREATESERVER)) {
            Main.getUiCreateServer().setVisible(Main.getUiCreateServer().isVisible() ? false : true);
        }
        // Serverbrowser
        else if(e.getActionCommand().equals(CMD_TOGGLE_SERVERBROWSER)) {
            net.send(Network.MASTERHOST,
                     Network.MASTERPORT,
                     ProtocolCmd.CLIENT_MASTER_LISTREQUEST);
            Main.getUiServerbrowser().setVisible(Main.getUiServerbrowser().isVisible() ? false : true);
        }
        // Lobby Chat
        else if(e.getActionCommand().equals(CMD_TOGGLE_LOBBYCHAT)) {
            Main.getUiLobbyChat().setVisible(Main.getUiLobbyChat().isVisible() ? false : true);
        }
        // Options
        else if(e.getActionCommand().equals(CMD_TOGGLE_OPTIONS)) {
            Main.getUiOptions().setVisible(Main.getUiOptions().isVisible() ? false : true);
        }
        // Serverliste aktualisieren
        else if(e.getActionCommand().equals(CMD_REFRESH_SERVERBROWSER)) {
            net.send(Network.MASTERHOST,
                     Network.MASTERPORT,
                     ProtocolCmd.CLIENT_MASTER_LISTREQUEST);
        }
        // Mit Server verbinden
        else if(e.getActionCommand().equals(CMD_CONNECT)) {
            if(Main.getSelectedServer() != null) {
//                System.out.println("Connecting to Server: "+Main.getSelectedServer().getHostPort());
                net.send(Network.MASTERHOST,
                         Network.MASTERPORT,
                         ProtocolCmd.CLIENT_MASTER_JOINSERVER,
                         argStr(Main.getSelectedServer().getHost()),
                         argInt(Main.getSelectedServer().getPort()));
                System.out.println("CLIENT_MASTER_JOINSERVER ("+Main.getSelectedServer().getHostPort()+")");
                net.send(Main.getSelectedServer().getAddress(),
                         ProtocolCmd.CLIENT_SERVER_AUTH);
                System.out.println("CLIENT_SERVER_AUTH");
//                Main.getUiServerbrowser().setVisible(false);
            }
        }
        // Credits
        else if(e.getActionCommand().equals(CMD_TOGGLE_CREDITS)) {
            Main.getUiCredits().setVisible(Main.getUiCredits().isVisible() ? false : true);
        }
        // Lobby Chat SendMSG
        else if(e.getActionCommand().equals(CMD_LOBBYCHAT_SEND_MSG)) {
            String msg = Main.getUiLobbyChat().getMSG();
            if (msg != null && !msg.equals(""))
                net.send(Network.MASTERHOST,
                         Network.MASTERPORT,
                         ProtocolCmd.CLIENT_MASTER_CHAT_LOBBY, argStr(msg));
        }
        // Exit
        else if(e.getActionCommand().equals(CMD_EXIT)) {
            if(net.getServer() != null)
                net.send(net.getServer().getHostName(),
                         net.getServer().getPort(),
                         ProtocolCmd.CLIENT_SERVER_LOGOFF);
            net.send(Network.MASTERHOST,
                     Network.MASTERPORT,
                     ProtocolCmd.CLIENT_MASTER_LOGOFF);
            System.exit(0);
        }
    }
}
