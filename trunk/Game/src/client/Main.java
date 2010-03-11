/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;

/**
 *
 * @author miracle
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*Protocol.init();
        Network net = new Network();
        ProtocolHandler handler = new ProtocolHandler(net);*/
        MainScreen screen = new MainScreen();
        UpdateLoop loop = new UpdateLoop(60);
        loop.addUpdateObject(screen);
    }
}
