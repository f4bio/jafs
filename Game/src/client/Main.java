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
    private static MainScreen screen;

    public static void main(String[] args) {
        MainMenu test = new MainMenu();
        test.setLocation(300, 300);
        Serverbrowser br = new Serverbrowser();

        UiManager.init();
        UiManager.addComponent(test);
        UiManager.addComponent(br);

        /*Protocol.init();
        Network net = new Network();
        ProtocolHandler handler = new ProtocolHandler(net);*/

        JFrame frm = new JFrame();
        frm.setVisible(true);

        //MainScreen screen = new MainScreen(frm);
        screen = new MainScreen(frm);
        screen.getContentPane().add(test);
        screen.getContentPane().add(br);

        UpdateLoop loop = new UpdateLoop(60);
        loop.addUpdateObject(screen);
    }

    public static MainScreen getScreen() {
        return screen;
    }
}
