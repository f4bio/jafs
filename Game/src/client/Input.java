package client;

import client.render.MainScreen;
import common.CVector2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author J.A.F.S
 */
public class Input implements KeyListener, MouseMotionListener, MouseListener {
    private boolean key_w;
    private boolean key_a;
    private boolean key_s;
    private boolean key_d;
    private boolean key_m_1;
    private boolean key_e;
    private boolean key_b;
    private boolean key_n;
    private boolean key_m;

    private CVector2 direction;
    private MainScreen scrn;
    
    /**
     * Constructs an Input object
     * @param scrn MainScreen
     */
    public Input(MainScreen scrn) {
        key_w = false;
        key_a = false;
        key_s = false;
        key_d = false;
        key_m_1 = false;
        key_e = false;
        key_b = false;
        key_n = false;
        key_m = false;

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
                break;
            case KeyEvent.VK_E:
                key_e = true;
                break;
            case KeyEvent.VK_B:
                key_b = true;
                break;
            case KeyEvent.VK_N:
                key_n = true;
                break;
            case KeyEvent.VK_M:
                key_m = true;
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
                break;
            case KeyEvent.VK_E:
                key_e = false;
                break;
            case KeyEvent.VK_B:
                key_b = false;
                break;
            case KeyEvent.VK_N:
                key_n = false;
                break;
            case KeyEvent.VK_M:
                key_m = false;
        }
        if(k.getKeyCode() == KeyEvent.VK_ESCAPE){
//            Main.showUi();
        }
    }

    public void mouseMoved(MouseEvent m) {
        double x = (double)m.getXOnScreen() - (double)scrn.getSize().width/2;
        double y = (double)m.getYOnScreen() - (double)scrn.getSize().height/2;

        direction.set(x, y);
    }

    public void mouseDragged(MouseEvent m) {
        double x = (double)m.getXOnScreen() - (double)scrn.getSize().width/2;
        double y = (double)m.getYOnScreen() - (double)scrn.getSize().height/2;

        direction.set(x, y);
    }

    public void mouseExited(MouseEvent m) {

    }

    public void mouseEntered(MouseEvent m) {

    }

    public void mousePressed(MouseEvent m) {
        key_m_1 = true;
    }

    public void mouseClicked(MouseEvent m) {

    }

    public void mouseReleased(MouseEvent m) {
        key_m_1 = false;
    }

    /**
     *
     * @return true if 'W' is pressed, otherwise false
     */
    public boolean isKeyWPressed() {
        return key_w;
    }

    /**
     *
     * @return true if 'A' is pressed, otherwise false
     */
    public boolean isKeyAPressed() {
        return key_a;
    }

    /**
     *
     * @return true if 'S' is pressed, otherwise false
     */
    public boolean isKeySPressed() {
        return key_s;
    }

    /**
     *
     * @return true if 'D' is pressed, otherwise false
     */
    public boolean isKeyDPressed() {
        return key_d;
    }

    /**
     *
     * @return true if 'M1' (Mouse 1) is pressed, otherwise false
     */
    public boolean isKeyM1Pressed() {
        return key_m_1;
    }


    /**
     *
     * @return true if 'E' is pressed, otherwise false
     */
    public boolean isKeyEPressed() {
        return key_e;
    }

    /**
     *
     * @return true if 'B' is pressed, otherwise false
     */
    public boolean isKeyBPressed() {
        return key_b;
    }

    /**
     *
     * @return true if 'N' is pressed, otherwise false
     */
    public boolean isKeyNPressed() {
        return key_n;
    }

    /**
     *
     * @return true if 'M' is pressed, otherwise false
     */
    public boolean isKeyMPressed() {
        return key_m;
    }

    /**
     *
     * @return
     */
    public CVector2 getDirection() {
        return direction;
    }
}
