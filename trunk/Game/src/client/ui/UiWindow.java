package client.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import javax.swing.JPanel;

/**
 *
 * @author Julian Sanio
 *
 * setName(...) muss gesetzt werden (auch im GUI Designer unter den Eigenschaften möglich)!!!
 *
 */
public abstract class UiWindow extends JPanel implements MouseListener, Runnable {

    public static final int borderWidth = 10;

    protected Point location;
    protected GradientPaint gradLeft;
    protected GradientPaint gradRight;
    protected GradientPaint gradTop;
    protected GradientPaint gradBottom;

    protected BufferStrategy buffer;

    protected boolean isMoveable;
    protected boolean isMousePressed;
    protected MouseEvent mouse = null;
    protected Thread t;

    public UiWindow() {
        super();

        this.setDoubleBuffered(false);
        this.setIgnoreRepaint(true);

        initDecoration();
        isMoveable = true;
        isMousePressed = false;
        addMouseListener(this);
        t = new Thread(this);
        t.start();
    }

    public void initDecoration(){
        Color from = this.getBackground();
        Color to = new Color(from.getRed(), from.getGreen(), from.getBlue(), 0);

        gradLeft = new GradientPaint(0, 0, from, borderWidth, 0, to, true);
        gradRight = new GradientPaint(0, 0, to, borderWidth, 0, from, true);
        gradTop = new GradientPaint(0, 0, from, 0, borderWidth, to, true);
        gradBottom = new GradientPaint(0, 0, from, 0, borderWidth, to, true);  
    }

    public void run(){
        while(t.isAlive()){
            if (mouse != null && isMousePressed)
                UiManager.changeLocation(getName(), mouse.getXOnScreen(), mouse.getYOnScreen()-50);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException ex) {  }
        }
    }

    public void mouseClicked(MouseEvent e) {  }

    public void mousePressed(MouseEvent e) {
        isMousePressed = true;
    }

    public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
    }

    public void mouseEntered(MouseEvent e) {
        mouse = e;
    }

    public void mouseExited(MouseEvent e) {  }

    public void render(Graphics2D g) {
        if(isVisible() && g != null) {
            setSize(getPreferredSize().width, getPreferredSize().height); // !!! bessere Implementierung?
            location = getLocation();
            g.translate(location.x, location.y);
            paint(g);
            renderDecoration(g);
            g.translate(-location.x, -location.y);
        }
    }

    public void renderDecoration(Graphics2D g) {
        // Right
//        g.setPaint(gradRight);
//        g.fillRect(getWidth(), 0, borderWidth, getHeight());
        // Left
//        g.setPaint(gradLeft);
//        g.fillRect(-borderWidth, 0, borderWidth, getHeight());
        // Top
        g.setPaint(gradTop);
        g.fillRect(0, -borderWidth, getWidth(), borderWidth);
        // Bottom
        g.setPaint(gradBottom);
        g.fillRect(0, getHeight(), getWidth(), borderWidth);
    }

    public boolean isMoveable(){
        return isMoveable;
    }

    public void setMoveable(boolean b){
        isMoveable = b;
    }
}
