/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import common.net.Network;
import common.net.Protocol;

/**
 *
 * @author miracle
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Protocol.init();
        Network net = new Network();
        ProtocolHandler handler = new ProtocolHandler(net);
    }

}
