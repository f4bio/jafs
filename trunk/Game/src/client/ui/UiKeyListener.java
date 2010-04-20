package client.ui;

import client.Main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Julian Sanio
 */
public class UiKeyListener implements KeyListener {

    public void keyTyped(KeyEvent e) {  }

    public void keyPressed(KeyEvent e) {
//        System.out.println("Pressed "+e.getKeyChar());
        
        // InGame Chat
        if(e.getKeyCode() == KeyEvent.VK_Y) {
            Main.getUiInGameChat().setVisible(Main.getUiInGameChat().isVisible() ? false : true);
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
//            Main.hideUi();
        }
    }
}
