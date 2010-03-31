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
 * @version 0.1
 *
 * ZU BEACHTEN:
 *
 *   Pflicht-Initialisierungen (nach dem Init. aller grafischen Elemente):
 *      1. setName("...") - auch im GUI Designer unter den Eigenschaften->name
 *      2. setSize(getPreferredSize().width, getPreferredSize().height);
 *   Es scheint, die decoration wird nur korrekt gezeichnet, wenn genügend
 *   Abstand zwischen deco und einem grafischen Element besteht.
 *
 *   Bei grafische Elemente mit Hintergrund (z.B. JCheckBox, JPanel etc.) muss
 *   jeweils setBackground(...) angepasst werden mit:
 *                        setBackground(new Color(1, 1, 1, 0))
 *   Dies bewirkt eine totale Tranzparenz, somit kann der UI Hintergrund zur
 *   Laufzeit geändert werden.
 *
 *   Ein UI Objekt muss IMMER erst dem UiManager -> addComponent(...)
 *                           und dem  MainScreen -> getContentPane().add(...)
 *      hinzugefügt werden, erst dann können Funktionalitäten wie setVisible etc.
 *   genutzt werden.
 *
 */
public abstract class UiWindow extends JPanel
                               implements MouseListener, MouseMotionListener {

    public static final int BORDER_WIDTH = 6;
    public static final Color UI_BACKGROUND_ON_TOP = new Color(216, 216, 216);
    public static final Color UI_BACKGROUND_IN_BACKGROUND = new Color(240, 240, 240);

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

        setBackground(UI_BACKGROUND_ON_TOP);
        addMouseListener(this);
        addMouseMotionListener(this);        
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return true;
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        initDecoration();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if(aFlag)
            UiManager.setForeground(this);
    }
    
    public abstract void addActionListener(ActionListener a);

    public void initDecoration(){
        Color from = getBackground();
        Color to = new Color(from.getRed(), from.getGreen(), from.getBlue(), 0);

        gradLeft = new GradientPaint(0, 0, from, BORDER_WIDTH, 0, to, true);
        gradRight = new GradientPaint(0, 0, to, BORDER_WIDTH, 0, from, true);
        gradTop = new GradientPaint(0, 0, from, 0, BORDER_WIDTH, to, true);
        gradBottom = new GradientPaint(0, 0, from, 0, BORDER_WIDTH, to, true);
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
/*      g.setPaint(gradRight);
        g.fillRect(getWidth(), 0, borderWidth, getHeight());    */
        // Left
/*      g.setPaint(gradLeft);
        g.fillRect(-borderWidth, 0, borderWidth, getHeight());  */
        // Top
        g.setPaint(gradTop);
        g.fillRect(0, -BORDER_WIDTH, getWidth(), BORDER_WIDTH);
        // Bottom
        g.setPaint(gradBottom);
        g.fillRect(0, getHeight(), getWidth(), BORDER_WIDTH);
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

    public void mouseEntered(MouseEvent e) {  }

    public void mouseExited(MouseEvent e) {  }

    public void mouseMoved(MouseEvent e) {  }

    public void mouseDragged(MouseEvent e) {
        if(isMousePressed && isMoveable) {
            setLocation(e.getXOnScreen() - tX,
                    e.getYOnScreen() - tY);
        }
    }
}
