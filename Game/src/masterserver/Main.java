/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package masterserver;

import common.net.Network;
import common.net.Protocol;

/**
 *
 * @author miracle
 */
public class Main {
    private Network net;
    private ProtocolHandler handler;

    public Main() {
        net = new Network();
        net.listen();
        handler = new ProtocolHandler(net);
    }

    public static void main(String[] args) {
        Protocol.init();
        new Main();
    }
}
