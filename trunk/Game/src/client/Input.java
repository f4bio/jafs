package client;

import client.render.MainScreen;
import common.CVector2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author miracle
 */
public class Input implements KeyListener, MouseMotionListener {
    private boolean key_w;
    private boolean key_a;
    private boolean key_s;
    private boolean key_d;

    private CVector2 direction;
    private MainScreen scrn;
    
    public Input(MainScreen scrn) {
        key_w = false;
        key_a = false;
        key_s = false;
        key_d = false;

        direction = new CVector2();
        this.scrn = scrn;
    }

    public void keyPressed(KeyEvent k) {
        switch(k.getKeyCode()) {
            case KeyEvent.VK_W:
                key_w = true;
                break;
            case KeyEvent.VK_A:
                key_a = true;
                break;
            case KeyEvent.VK_S:
                key_s = true;
                break;
            case KeyEvent.VK_D:
                key_d = true;
        }
    }

    public void keyTyped(KeyEvent k) {

    }

    public void keyReleased(KeyEvent k) {
        switch(k.getKeyCode()) {
            case KeyEvent.VK_W:
                key_w = false;
                break;
            case KeyEvent.VK_A:
                key_a = false;
                break;
            case KeyEvent.VK_S:
                key_s = false;
                break;
            case KeyEvent.VK_D:
                key_d = false;
        }
    }

    public void mouseMoved(MouseEvent m) {
        double x = (double)m.getXOnScreen() - (double)scrn.getSize().width/2;
        double y = (double)m.getYOnScreen() - (double)scrn.getSize().height/2;

        direction.set(x, y);
    }

    public void mouseDragged(MouseEvent m) {

    }

    public boolean isKeyWPressed() {
        return key_w;
    }

    public boolean isKeyAPressed() {
        return key_a;
    }

    public boolean isKeySPressed() {
        return key_s;
    }

    public boolean isKeyDPressed() {
        return key_d;
    }

    public CVector2 getDirection() {
        return direction;
    }
}
