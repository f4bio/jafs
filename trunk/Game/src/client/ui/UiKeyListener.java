package client.ui;

import client.Main_OLD;
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
        if(e.getKeyCode() == KeyEvent.VK_A){
            Main_OLD.getWeaponSidebar().setAktiveWeapon(Main_OLD.getWeaponSidebar().getAktiveWeapon()+1);
        } else if(e.getKeyCode() == KeyEvent.VK_Q){
            Main_OLD.getWeaponSidebar().setAktiveWeapon(Main_OLD.getWeaponSidebar().getAktiveWeapon()-1);
        }
    }

    public void keyReleased(KeyEvent e) {  }
}
