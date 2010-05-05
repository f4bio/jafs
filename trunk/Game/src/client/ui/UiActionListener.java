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
    /**
     * ActionCommand
     */
    public static final String CMD_EXIT                     = "1";
    /**
     * ActionCommand
     */
    public static final String CMD_CONNECT                  = "2";
    /**
     * ActionCommand
     */
    public static final String CMD_REFRESH_SERVERBROWSER    = "3";
    /**
     * ActionCommand
     */
    public static final String CMD_LOBBYCHAT_SEND_MSG       = "4";
    /**
     * ActionCommand
     */
    public static final String CMD_NICKCHANGE               = "5";
    /**
     * ActionCommand
     */
    public static final String CMD_INGAMECHAT_SEND_MSG      = "6";
    /**
     * ActionCommand
     */
    public static final String CMD_CREATE_SERVER            = "7";
    /**
     * ActionCommand
     */
    public static final String CMD_AUTH_MASTERSERVER        = "8";
    /**
     * ActionCommand
     */
    public static final String CMD_APPLY_NETWORK_SETTINGS   = "9";

    private Network net;

    /**
     *
     * @param net
     */
    public UiActionListener(Network net) {
        this.net = net;
    }

    public void actionPerformed(ActionEvent e) {
//        System.out.println(e.getActionCommand() + " (actionPerformed by " + e.getSource().getClass().getSimpleName() + ")");

        // Anmeldung: Client -> Masterserver
        if(e.getActionCommand().equals(CMD_AUTH_MASTERSERVER)){
            if(!net.MASTERHOST.equals(Main.getDlgMasterserver().getHost()) ||
                    net.MASTERPORT != Main.getDlgMasterserver().getPort()){
                net.MASTERHOST = Main.getDlgMasterserver().getHost();
                Network.MASTERHOST = Main.getDlgMasterserver().getHost();
                net.MASTERPORT = Main.getDlgMasterserver().getPort();
                Network.MASTERPORT = Main.getDlgMasterserver().getPort();
                Main.getMainMenu().setOptionsMasterHostPort(Main.getDlgMasterserver().getHost(), Main.getDlgMasterserver().getPort());
            }
            Main.getDlgMasterserver().dispose();
            net.send(net.MASTERHOST,
                     net.MASTERPORT,
                     ProtocolCmd.CLIENT_MASTER_AUTH,
                     argStr(Main.getGameData().getName()));
        }
        // Serverliste aktualisieren
        if(e.getActionCommand().equals(CMD_REFRESH_SERVERBROWSER)) {
            net.send(net.MASTERHOST,
                     net.MASTERPORT,
                     ProtocolCmd.CLIENT_MASTER_LISTREQUEST,
                     argShort(Protocol.LIST_TYPE_SERVERLIST));
        }
        // Server erstellen
        else if(e.getActionCommand().equals(CMD_CREATE_SERVER)) {
            Main.getDlgCreateServer().setVisible(Main.getDlgCreateServer().isVisible()?false:true);
        }
        // Nick change
        else if(e.getActionCommand().equals(CMD_NICKCHANGE)) {
            net.send(net.MASTERHOST,
                     net.MASTERPORT,
                     ProtocolCmd.CLIENT_MASTER_NICKCHANGE,
                     argStr(Main.getMainMenu().getSelfName()));
            Main.getMainMenu().enableOptions(false);
        }
        // Mit Server verbinden
        else if(e.getActionCommand().equals(CMD_CONNECT)) {
            if(Main.getSelectedServer() != null) {
//                System.out.println("Connecting to Server: "+Main.getSelectedServer().getHostPort());
                net.send(net.MASTERHOST,
                         net.MASTERPORT,
                         ProtocolCmd.CLIENT_MASTER_JOINSERVER,
                         argStr(Main.getSelectedServer().getHost()),
                         argInt(Main.getSelectedServer().getPort()));
                System.out.println("CLIENT_MASTER_JOINSERVER ("+Main.getSelectedServer().getHostPort()+")");
                net.send(Main.getSelectedServer().getAddress(),
                         ProtocolCmd.CLIENT_SERVER_AUTH);
                System.out.println("CLIENT_SERVER_AUTH");
            }
        }
        // Lobby Chat senden
        else if(e.getActionCommand().equals(CMD_LOBBYCHAT_SEND_MSG)) {
            Main.getMainMenu().sendLobbyMsg();
        }
        // InGame Chat senden
        else if(e.getActionCommand().equals(CMD_INGAMECHAT_SEND_MSG)) {
            String msg = Main.getUiInGameChat().getMSG();
            if(!msg.equals("") && msg != null) {
                // private chat
                if(Main.getUiInGameChat().isPrivateChatMode()){
                    System.out.println("CLIENT_SERVER_CHAT_PRIVATE (server="+net.getServer()+", msg="+msg+")");
                    net.send(net.getServer(),
                             ProtocolCmd.CLIENT_SERVER_CHAT_PRIVATE,
                             argInt(Main.getUiInGameChat().getSelectedPrivateChatID()),
                             argStr(msg));
                }
                // team chat
    //            else if(msg.startsWith("/t")){
    //                //
    //            }
                // public chat
                else {
                    System.out.println("CLIENT_SERVER_CHAT_ALL (server="+net.getServer()+", msg="+msg+")");
                    net.send(net.getServer(),
                             ProtocolCmd.CLIENT_SERVER_CHAT_ALL,
                             argStr(msg));
                }
            }
            Main.getUiInGameChat().setVisible(false);
            Main.getScreen().requestFocus();
            Main.getFrame().requestFocus();
        }
        // Netwerk Einstellungen übernehmen
        else if(e.getActionCommand().equals(CMD_APPLY_NETWORK_SETTINGS)) {
            String hostPort = Main.getMainMenu().getMasterHostPortSettings();
            if(!net.MASTERHOST.equals(hostPort.split(":")[0]) ||
                    net.MASTERPORT != Integer.parseInt(hostPort.split(":")[1])){
                Main.getMainMenu().enableLobby(false);
                // Logoff old masterserver
                net.send(net.MASTERHOST,
                         net.MASTERPORT,
                         ProtocolCmd.CLIENT_MASTER_LOGOFF);
                // apply new settings
                net.MASTERHOST = hostPort.split(":")[0];
                Network.MASTERHOST = hostPort.split(":")[0];
                net.MASTERPORT = Integer.parseInt(hostPort.split(":")[1]);
                Network.MASTERPORT = Integer.parseInt(hostPort.split(":")[1]);
                // login new masterserver
                net.send(net.MASTERHOST,
                         net.MASTERPORT,
                         ProtocolCmd.CLIENT_MASTER_AUTH,
                         argStr(Main.getGameData().getName()));
            }
        }
        // Exit
        else if(e.getActionCommand().equals(CMD_EXIT)) {
            net.send(net.MASTERHOST,
                     net.MASTERPORT,
                     ProtocolCmd.CLIENT_MASTER_LOGOFF);
            System.out.println("CLIENT_MASTER_LOGOFF");
            System.exit(0);
        }
    }
}
