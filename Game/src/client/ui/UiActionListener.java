package client.ui;

import client.Main;
import common.net.Network;
import common.net.Protocol;
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
    public static final String CMD_EXIT                     = "2";
    public static final String CMD_CONNECT                  = "3";
    public static final String CMD_REFRESH_SERVERBROWSER    = "4";
    public static final String CMD_LOBBYCHAT_SEND_MSG       = "5";

    private Network net;

    public UiActionListener(Network net) {
        this.net = net;
    }

    public void actionPerformed(ActionEvent e) {
//        System.out.println(e.getActionCommand() + " (actionPerformed by " + e.getSource().getClass().getSimpleName() + ")");
  
        // Serverbrowser
        if(e.getActionCommand().equals(CMD_TOGGLE_SERVERBROWSER)) {
            net.send(Network.MASTERHOST,
                     Network.MASTERPORT,
                     ProtocolCmd.CLIENT_MASTER_LISTREQUEST,
                     argShort(Protocol.LIST_TYPE_SERVERLIST));
        }
        // Serverliste aktualisieren
        else if(e.getActionCommand().equals(CMD_REFRESH_SERVERBROWSER)) {
            net.send(Network.MASTERHOST,
                     Network.MASTERPORT,
                     ProtocolCmd.CLIENT_MASTER_LISTREQUEST,
                     argShort(Protocol.LIST_TYPE_SERVERLIST));
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
                Main.getScreen().setVisible(true);
                Main.getFrame().setVisible(true);
            }
        }
        // Lobby Chat senden
        else if(e.getActionCommand().equals(CMD_LOBBYCHAT_SEND_MSG)) {
            Main.getMainMenu().sendLobbyMsg();
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
