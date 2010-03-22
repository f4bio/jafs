/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import common.net.Network;
import common.net.Protocol;

/**
 *
 * @author adm1n
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Protocol.init();
        Network net = new Network();
        ProtocolHandler protocol = new ProtocolHandler(net);
        net.listen(50000);
//        net.send("localhost", 40000, Protocol.client_server_clientcount);
        net.send("localhost", 40000, Protocol.client_server_auth, new Object[0]);
        new Chat(net).start();
    }

}
