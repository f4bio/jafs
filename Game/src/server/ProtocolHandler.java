/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import common.net.Network;
import common.net.Protocol;
import java.net.InetSocketAddress;

/**
 *
 * @author miracle
 */
public class ProtocolHandler extends common.net.ProtocolHandler {
    public ProtocolHandler(Network net) {
        super(net);
    }

    public void m_s_ping(InetSocketAddress adr) {
        net.send(adr, Protocol.server_master_pong, new Object[0]);
    }
    
    public void m_s_auth_success(InetSocketAddress adr) {
        System.out.println("succesfully listed.");
    }

    public void m_s_auth_failure(InetSocketAddress adr) {
        System.out.println("failed to be listed.");
    }
}
