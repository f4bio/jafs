package client.ui;

import client.Main;
import common.net.Network;
import common.net.ProtocolCmd;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author Julian Sanio
 */
public class UiKeyListener implements KeyListener {

    public void keyTyped(KeyEvent e) {  }

    public void keyPressed(KeyEvent e) {
//        System.out.println("Pressed "+e.getKeyChar());
    }

    public void keyReleased(KeyEvent e) {
        // InGame Chat
        if(e.getKeyCode() == KeyEvent.VK_Y) {
            Main.getUiInGameChat().setVisible(true);
            Main.getUiInGameChat().requestFocus();
        }
        // MainMenu
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            Main.getFrame().setVisible(false);
            Main.getScreen().setVisible(false);
            // server logoff
            if(Main.getNetwork().isReallyConnected() && Main.getNetwork().getServer() != null)
                Main.getNetwork().send(Main.getNetwork().getServer().getHostName(),
                                       Main.getNetwork().getServer().getPort(),
                                       ProtocolCmd.CLIENT_SERVER_LOGOFF);
            Main.getNetwork().send(Network.MASTERHOST,
                                   Network.MASTERPORT,
                                   ProtocolCmd.CLIENT_MASTER_LOGOFF);
            Main.getNetwork().send(Network.MASTERHOST,
                                   Network.MASTERPORT,
                                   ProtocolCmd.CLIENT_MASTER_AUTH,
                                   argStr(Main.getGameData().getName()));
            Main.getNetwork().send(Network.MASTERHOST, Network.MASTERPORT, ProtocolCmd.CLIENT_MASTER_AUTH, argStr(Main.getGameData().getName()));
            Main.getMainMenu().enableLobby(true);
            Main.getMainMenu().setVisible(true);
            Main.getMainMenu().requestFocus();
        }
    }
}
