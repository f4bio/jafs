/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

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
    public void s_c_ping(InetSocketAddress adr) {
        net.send(adr, Protocol.client_server_pong, new Object[0]);
//        System.out.println("Client ponged");
    }
    public void s_c_clientcount(Integer i, InetSocketAddress adr) {
        System.out.println(i+ "clients connected on this server");
    }
    public void s_c_clientid(Integer id, InetSocketAddress adr) {
        System.out.println("yout id is: "+id);
    }
    public void m_c_newlist(InetSocketAddress adr) {
        System.out.println("ServerList:");
    }
    public void m_c_listentry(String server, InetSocketAddress adr) {
        Main.serverlist.add(server);
    }
    public void m_c_endlist(InetSocketAddress adr) {
        System.out.println(adr);
        Main.completeServerlist(Main.serverlist);
        System.out.println(":ServerList");
    }
    public void s_c_auth_success(InetSocketAddress adr) {
        System.out.println("client succesfully listed.");
    }
    public void s_c_auth_failure(InetSocketAddress adr) {
        System.out.println("client failed to be listed.");
    }
    // --- chat fkt
    public void s_c_chat(String msg, InetSocketAddress adr) {
        System.out.println("CHAT: "+msg);
    }
    // --- end chat
    public void s_c_logoff_success(InetSocketAddress adr) {
        System.out.println("you have been succesfully logged off.");
    }
    public void s_c_logoff_failure(InetSocketAddress adr) {
        System.out.println("log off failed");
    }
    public void s_c_jointeam_success(InetSocketAddress adr) {
        System.out.println("you have been succesfully join team");
    }
    public void s_c_jointeam_failure(InetSocketAddress adr) {
        System.out.println("join team failed");
    }

}
