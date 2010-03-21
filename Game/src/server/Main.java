/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import common.net.Network;
import common.net.Protocol;

/**
 *
 * @author miracle
 */
public class Main {
    public static void main(String[] args) {
        Protocol.init();
        Network net = new Network();
        ProtocolHandler protocol = new ProtocolHandler(net);
        net.listen(31330);
        net.send("localhost", 31338, Protocol.server_master_auth, new Object[0]);
    }
}
