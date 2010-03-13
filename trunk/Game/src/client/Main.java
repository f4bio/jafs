/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import client.ui.MainMenu;
import client.ui.UiManager;
import client.ui.UiWindow;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author miracle
 */
public class Main {

    /**
     * @param args the command line arguments
     */

    public static class UiTest extends UiWindow {

        public UiTest() {
            super();
            setSize(200, 200);
            setForeground(Color.black);
            setLocation(10, 20);

            add(new JLabel("lol"));
        }

        public void actionPerformed(ActionEvent ae) {

        }
    }

    public static void main(String[] args) {
        MainMenu test = new MainMenu();
        UiManager.init();
        UiManager.addComponent("MainMenu", test);
        
        /*Protocol.init();
        Network net = new Network();
        ProtocolHandler handler = new ProtocolHandler(net);*/

        test.setLocation(300, 300);

        JFrame frm = new JFrame();
        frm.setVisible(true);

        MainScreen screen = new MainScreen(frm);
        screen.add(test);

        UpdateLoop loop = new UpdateLoop(60);
        loop.addUpdateObject(screen);
    }
}
