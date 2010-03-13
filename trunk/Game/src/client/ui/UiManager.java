package client.ui;

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

    private static Hashtable<String, UiWindow> content;

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
        Point p = cmp.getLocation();
        Dimension d = cmp.getSize();
//        g.setColor(Color.LIGHT_GRAY);
//        g.drawRect(p.x - borderWidth, p.y - borderWidth, d.width, d.height);
//        g.drawString("x", p.x - borderWidth + cmp.getWidth() - 10, p.y - borderWidth + 10);
    }

    public static void changeLocation(String identifier, int x, int y){
        content.get(identifier).setLocation(x, y);
    }
}
