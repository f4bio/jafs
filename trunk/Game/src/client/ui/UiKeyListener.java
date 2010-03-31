package client.ui;

import client.Main_UI_Test;
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

        // Player figure
        if(e.getKeyCode() == KeyEvent.VK_W){
            // WALK UP
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            // WALK DOWN
        }
        if(e.getKeyCode() == KeyEvent.VK_A){
            // WALK LEFT
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            // WALK RIGHT
        }

        // WeaponSidebar
        if(e.getKeyCode() == KeyEvent.VK_Y){
            Main_UI_Test.getWeaponSidebar().setAktiveWeapon(Main_UI_Test.getWeaponSidebar().getAktiveWeapon()+1);
        } else if(e.getKeyCode() == KeyEvent.VK_X){
            Main_UI_Test.getWeaponSidebar().setAktiveWeapon(Main_UI_Test.getWeaponSidebar().getAktiveWeapon()-1);
        }
    }

    public void keyReleased(KeyEvent e) {  }
}
