package client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Julian Sanio
 */
public class UiManager {

    public static final int decoHeight = 0;
    public static final int borderWidth = 1;

    private static Hashtable<String, UiWindow> content = new Hashtable<String, UiWindow>();

    public static void init() {
        content = new Hashtable<String, UiWindow>();
    }

    public static UiWindow addComponent(String identifier, UiWindow c) {
        return content.put(identifier, c);
    }

    public static UiWindow removeComponent(String identifier) {
        return content.remove(identifier);
    }

    public static UiWindow getComponent(String identifier) {
        return content.get(identifier);
    }

    public static void renderAll(Graphics2D g) {
        Enumeration elements = content.elements();
        while(elements.hasMoreElements()) {
            UiWindow element = (UiWindow)elements.nextElement();
            renderComponent(element, g);
        }
    }

    private static void renderComponent(UiWindow cmp, Graphics2D g) {
        cmp.render(g);

        g.setColor(Color.LIGHT_GRAY);
        Point p = cmp.getLocation();
        Dimension d = cmp.getSize();

        g.drawRect(p.x - borderWidth, p.y - borderWidth, d.width, d.height);
        g.drawString("x", p.x - borderWidth, p.y - borderWidth);
    }
}
