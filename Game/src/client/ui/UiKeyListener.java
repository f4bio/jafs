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
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            Main.hideUi();
        }
    }

    public void keyReleased(KeyEvent e) {  }
}
