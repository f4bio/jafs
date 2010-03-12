/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import client.ui.MainMenu;
import client.ui.TestMenu;
import client.ui.UiManager;
import client.ui.UiWindow;
import java.awt.Color;
import java.awt.event.ActionEvent;
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
            super(200, 200);
            JLabel lb = new JLabel("Test");
            lb.setForeground(Color.BLACK);
            lb.setIgnoreRepaint(true);
            setLayout(null);
            lb.setBounds(10, 10, 100, 100);
            add(lb);
            setVisible(true);
            
        }

        public void actionPerformed(ActionEvent ae) {

        }
    }

    public static void main(String[] args) {
        TestMenu menu = new TestMenu();
        UiManager.addComponent("MainMenu", menu);
        /*Protocol.init();
        Network net = new Network();
        ProtocolHandler handler = new ProtocolHandler(net);*/
        MainScreen screen = new MainScreen();
        UpdateLoop loop = new UpdateLoop(60);
        loop.addUpdateObject(screen);
    }
}
