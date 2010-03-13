package client.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import javax.swing.JPanel;

/**
 *
 * @author Julian Sanio
 */
public abstract class UiWindow extends JPanel {
    public static final int borderWidth = 10;

    protected Point location;
    protected GradientPaint gradLeft;
    protected GradientPaint gradRight;
    protected GradientPaint gradTop;
    protected GradientPaint gradBottom;
    protected BufferStrategy buffer;

    public UiWindow() {
        super();

        this.setDoubleBuffered(false);
        this.setIgnoreRepaint(true);

        Color from = this.getBackground();
        Color to = new Color(from.getRed(), from.getGreen(), from.getBlue(), 0);

        gradLeft = new GradientPaint(0, 0, from, borderWidth, 0, to, true);
        gradRight = new GradientPaint(0, 0, from, borderWidth, 0, to, true);
        gradTop = new GradientPaint(0, borderWidth, from, borderWidth, 0, to, true);
        gradBottom = new GradientPaint(0, 0, from, 0, borderWidth, to, true);
    }

    public void render(Graphics2D g) {
        if(isVisible() && g != null) {
            location = getLocation();
            g.translate(location.x, location.y);
            paint(g);
            renderDecoration(g);
            g.translate(-location.x, -location.y);
        }
    }

    public void renderDecoration(Graphics2D g) {
        g.setPaint(gradLeft);
        g.fillRect(getWidth(), 0, borderWidth, getHeight());
        g.setPaint(gradLeft);
        g.fillRect(-borderWidth, 0, borderWidth, getHeight());
        g.setPaint(gradBottom);
        g.fillRect(0, -borderWidth, getWidth(), borderWidth);
        g.setPaint(gradBottom);
        g.fillRect(0, getHeight(), getWidth(), borderWidth);
    }
}
