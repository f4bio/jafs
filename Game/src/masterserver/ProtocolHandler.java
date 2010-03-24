/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package masterserver;

import common.net.Client;
import common.net.Network;
import common.net.Protocol;
import common.net.Server;
import java.net.InetSocketAddress;

/**
 *
 * @author miracle
 */
public class ProtocolHandler extends common.net.ProtocolHandler {
    public ProtocolHandler(Network net) {
        super(net);
    }

    public void s_m_auth(InetSocketAddress adr) {
        Server added = Main.addServer(adr);
        if(added != null)
            net.send(adr, Protocol.master_server_auth_success, new Object[0]);
        else
            net.send(adr, Protocol.master_server_auth_failure, new Object[0]);
    }

    public void s_m_pong(InetSocketAddress adr) {
        Main.decreasePingFailures(adr);
    }

    public void s_m_servercount(InetSocketAddress adr){
        net.send(adr, Protocol.master_server_servercount, Main.serverCount());
    }

    public void c_m_listrequest(InetSocketAddress adr) {
        String[] list = Main.getServerlist();
        net.send(adr, Protocol.master_client_newlist, new Object[0]);
        for(String i : list) {
            net.send(adr, Protocol.master_client_listentry, i);
        }
        net.send(adr, Protocol.master_client_endlist, new Object[0]);
    }
    public void c_m_auth(InetSocketAddress adr)
    {
        Client client = Main.addClient(adr);
        if(client != null)
            net.send(adr, Protocol.master_client_auth_success, new Object[0]);
        else
            net.send(adr, Protocol.master_client_auth_failure, new Object[0]);
    }
    public void m_c_chat_lobby(String msg, InetSocketAddress adr) {
        System.out.println(msg);
    }

}
