package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import client.ui.*;
import javax.swing.JFrame;

/**
 *
 * @author miracle
 */
public class Main {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        MainMenu test = new MainMenu();
        test.setLocation(300, 300);

        UiManager.init();
        UiManager.addComponent(test.getName(), test);

        /*Protocol.init();
        Network net = new Network();
        ProtocolHandler handler = new ProtocolHandler(net);*/

        JFrame frm = new JFrame();
        frm.setVisible(true);

        MainScreen screen = new MainScreen(frm);
        screen.add(test);

        UpdateLoop loop = new UpdateLoop(60);
        loop.addUpdateObject(screen);
    }
}
