package client.ui;

import client.render.MainScreen;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.VolatileImage;
import javax.sound.sampled.Line;
import javax.swing.JPanel;

/**
 *
 * @author Julian Sanio
 * @version 0.1
 *
 *
 * Pflicht-Initialisierungen (nach dem Init. aller grafischen Elemente):
 *     1. setName("...") - auch im GUI Designer unter den Eigenschaften->name
 *     2. setSize(getPreferredSize().width, getPreferredSize().height);
 * Es scheint, die decoration wird nur korrekt gezeichnet, wenn genügend
 * Abstand zwischen deco und einem grafischen Element besteht.
 *
 * Bei grafische Elemente mit Hintergrund (z.B. JCheckBox, JPanel etc.) muss
 * jeweils setBackground(...) angepasst werden mit:
 *                                    setBackground(new Color(1, 1, 1, 0))
 * Dies bewirkt eine totale Tranzparenz, somit kann der UI Hintergrund zur
 * Laufzeit geändert werden.
 *
 * Ein UI Objekt muss IMMER erst dem UiManager -> addComponent(...)
 *                         und dem  MainScreen -> getContentPane().add(...)
 * hinzugefügt werden, erst dann können Funktionalitäten wie setVisible etc.
 * genutzt werden.
 *
 */
public abstract class UiWindow extends JPanel
                               implements MouseListener, MouseMotionListener {

    public static final int BORDER_WIDTH = 6;
    public static final Color UI_COLOR_ON_TOP = new Color(218, 218, 218);
    public static final Color UI_COLOR_IN_BACKGROUND = new Color(240, 240, 240);
    public static final Color UI_COLOR_TRANSPARENT = new Color(1, 1, 1, 0);

    protected Point location;
    protected GradientPaint gradLeft;
    protected GradientPaint gradRight;
    protected GradientPaint gradTop;
    protected GradientPaint gradBottom;

    protected boolean isMoveable;
    protected boolean isMousePressed;
    protected boolean isUndecorated;

    protected int tX = 0;
    protected int tY = 0;

    protected MainScreen scr;
    protected VolatileImage buffer;

    public UiWindow(MainScreen scr) {
        super();

        this.scr = scr;

        setVisible(false);
        setDoubleBuffered(false);
        setIgnoreRepaint(true);

        isMoveable = true;
        isMousePressed = false;
        isUndecorated = false;

        setBackground(UI_COLOR_ON_TOP);
        addMouseListener(this);
        addMouseMotionListener(this);
        createBuffer();
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        initDecoration();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
//        if(aFlag)
//            UiManager.setForeground(this);
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

    public void render() {
        if(buffer == null) {
            createBuffer();
            return;
        }

        Line l;

        //synchronized(buffer) {
            Graphics2D g = null;
            try {
                g = buffer.createGraphics();
                do {
                    int returnCode = buffer.
                            validate(getGraphicsConfiguration());
                    if (returnCode == VolatileImage.IMAGE_RESTORED) {
                        repaint();
                    } else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                        createBuffer();
                        repaint();
                    }

                    paint(g);

                    if (!isUndecorated)
                        renderDecoration(g);

                } while(buffer.contentsLost());
            } catch(Exception e) {

            } finally {
                if(g != null)
                    g.dispose();
            }
        //}
    }

    public void createBuffer() {
        GraphicsConfiguration gc = null;
        gc = this.getGraphicsConfiguration();
        
        if(gc != null)
            buffer = gc.createCompatibleVolatileImage(getWidth(),
                    getHeight(), Transparency.TRANSLUCENT);
    }

    public VolatileImage getBuffer() {
        return buffer;
    }

/*  @Override
    public void paint(Graphics g) {
        render();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
 */
    public void renderDecoration(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, 0, 0, getHeight());                         // Left
        g.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());   // Right
        g.drawLine(0, getHeight(), getWidth()-1, getHeight());    // Right
        // Right
/*      g.setPaint(gradRight);
        g.fillRect(getWidth(), 0, BORDER_WIDTH, getHeight());     */
        // Left
/*      g.setPaint(gradLeft);
        g.fillRect(-BORDER_WIDTH, 0, BORDER_WIDTH, getHeight());  */
        // Top
/*      g.setPaint(gradTop);
        g.fillRect(0, -BORDER_WIDTH, getWidth(), BORDER_WIDTH);   */
        // Bottom
/*      g.setPaint(gradBottom);
        g.fillRect(0, getHeight(), getWidth(), BORDER_WIDTH);     */
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
