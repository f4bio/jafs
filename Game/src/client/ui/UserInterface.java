package client.ui;

import client.anim.UpdateObject;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 *
 * @author Julian Sanio
 */
public abstract class UserInterface extends JPanel implements ActionListener, UpdateObject {

    public static final Font DEFAULT_FONT = new Font("Helvetica", Font.PLAIN, 24);


//    public abstract void render(Graphics2D g);
//
//    public abstract UserInterface getPreviousPage();
}
