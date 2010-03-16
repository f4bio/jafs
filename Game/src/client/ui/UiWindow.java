package client.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import javax.swing.JPanel;

/**
 *
 * @author Julian Sanio
 *
 * Pflicht-Initialisierungen für jedes Interface
 * (nach dem Init. aller grafischen Elemente):
 *      1. setName("...") - auch im GUI Designer unter den Eigenschaften->name möglich
 *      2. setSize(getPreferredSize().width, getPreferredSize().height);
 *      3. initDecoration() - nur bei !isUndecorated
 * Es scheint, dass die decoration nur bei geraden Werten der Panelgröße korrekt gezeichnet wird.
 */
public abstract class UiWindow extends JPanel implements MouseListener, MouseMotionListener {

    public static final int borderWidth = 6;

    protected Point location;
    protected GradientPaint gradLeft;
    protected GradientPaint gradRight;
    protected GradientPaint gradTop;
    protected GradientPaint gradBottom;

    protected BufferStrategy buffer;

    protected boolean isMoveable;
    protected boolean isMousePressed;
    protected boolean isUndecorated;

    protected int tX = 0;
    protected int tY = 0;

    public UiWindow() {
        super();

        setVisible(false);
        setDoubleBuffered(true);
        setIgnoreRepaint(true);

        isMoveable = true;
        isMousePressed = false;
        isUndecorated = false;
        initDecoration();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void initDecoration(){
        Color from = getBackground();
        Color to = new Color(from.getRed(), from.getGreen(), from.getBlue(), 0);

        gradLeft = new GradientPaint(0, 0, from, borderWidth, 0, to, true);
        gradRight = new GradientPaint(0, 0, to, borderWidth, 0, from, true);
        gradTop = new GradientPaint(0, 0, from, 0, borderWidth, to, true);
        gradBottom = new GradientPaint(0, 0, from, 0, borderWidth, to, true);  
    }

    public void mouseClicked(MouseEvent e) {  }

    public void mousePressed(MouseEvent e) {
        UiManager.setForeground(this);
        isMousePressed = true;
        tX = e.getX();
        tY = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {
    
    }

    public void mouseMoved(MouseEvent e) {
        
    }

    public void mouseDragged(MouseEvent e) {
        if(isMousePressed && isMoveable) {
            setLocation(e.getXOnScreen() - tX,
                    e.getYOnScreen() - tY);
        }
    }

    public void render(Graphics2D g) {
        if(isVisible() && g != null) {
            location = getLocation();
            g.translate(location.x, location.y);
            paint(g);
            if (!isUndecorated)
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

    public boolean isUndecorated(){
        return isUndecorated;
    }

    public void setUndecorated(boolean b){
        isUndecorated = b;
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return true;
    }

    public abstract void addActionListener(ActionListener a);
}
