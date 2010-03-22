/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import common.net.Network;
import common.net.Protocol;
import java.util.ArrayList;

/**
 *
 * @author adm1n
 */
public class Main {

    static ArrayList<String> serverlist;
    public static void main(String[] args) {
        Protocol.init();
        serverlist = new ArrayList<String>();
        Network net = new Network();
        ProtocolHandler protocol = new ProtocolHandler(net);
        net.listen(50000);
//        net.send("localhost", 40000, Protocol.client_server_clientcount);
        net.send("localhost", 40000, Protocol.client_server_auth, new Object[0]);
        new Chat(net).start();
    }
    public static void completeServerlist(ArrayList servers){
        System.out.println("not yet implemented");
    }
}
