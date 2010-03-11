package client.ui;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
*
* @author ubuntu
*/
public class UiWindow extends JPanel implements ActionListener, UpdateObject{
    public UiWindow() {
        super();
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

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(UpdateLoop u) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}