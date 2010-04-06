package client.ui;

import client.Main;
import java.awt.Graphics2D;
import java.util.Vector;

/**
 *
 * @author Julian Sanio
 */
public class UiManager {

    public static final int DECO_HEIGHT = 0;
    public static final int DECO_WIDTH = 1;

    private static Vector<UiWindow> content = new Vector<UiWindow>();

    public static boolean addComponent(UiWindow c) {
        return content.add(c);
    }

    public static boolean removeComponent(UiWindow c) {
        return content.remove(c);
    }

    public static void renderAll(Graphics2D g) {
        UiWindow w;

        for(int i=0; i<content.size(); ++i) {
            w = content.get(i);

            if(w != null && w.isVisible() && g != null) {
                g.translate(w.getLocation().x, w.getLocation().y);
                g.drawImage(w.getBuffer(), 0, 0, null);
                g.translate(-w.getLocation().x, -w.getLocation().y);
            }
        }
    }

     public static void preRender() {
        UiWindow w;

        for(int i=0; i<content.size(); ++i) {
            w = content.get(i);
            if(w != null && w.isVisible())
                w.render();
        }
    }

    public static void setForeground(UiWindow u) {
        for(int i=0; i<content.size(); i++) {
            if(content.get(i) != Main.getUiMainMenu())
                content.get(i).setBackground(UiWindow.UI_COLOR_IN_BACKGROUND);
        }

        content.remove(u);
        u.setBackground(UiWindow.UI_COLOR_ON_TOP);
        content.add(u);

        Main.getScreen().getContentPane().setComponentZOrder(u, 0);
    }
}
