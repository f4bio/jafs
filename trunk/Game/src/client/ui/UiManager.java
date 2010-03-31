package client.ui;

import client.Main_UI_Test;
import java.awt.Color;
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
        for(int i=0; i<content.size(); i++) {
            renderComponent(content.get(i), g);
        }
    }

    private static void renderComponent(UiWindow cmp, Graphics2D g) {
        cmp.render(g);
    }

    public static void setForeground(UiWindow u) {
        for(int i=0; i<content.size(); i++) {
            if(content.get(i) != Main_UI_Test.getUiMainMenu())
                content.get(i).setBackground(UiWindow.UI_BACKGROUND_IN_BACKGROUND);
        }

        content.remove(u);
        u.setBackground(UiWindow.UI_BACKGROUND_ON_TOP);
        content.add(u);

        Main_UI_Test.getScreen().getContentPane().setComponentZOrder(u, 0);
    }
}
