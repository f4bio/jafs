package client.ui;

import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 *
 * @author Julian Sanio
 */
public abstract class UiWindow extends JPanel implements ActionListener {

    public UiWindow(int width, int height) {
        super();
        //setSize(width, height);
    }

    public void render(Graphics2D g) {
        if(isVisible() && g != null) {
            int x = getLocation().x;
            int y = getLocation().y;
            g.translate(x, y);
            paint(g);
            g.translate(-x, -y);
        }
    }

}
