package client;

import common.net.Network;
import common.net.Packet;
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
        net.send(adr, Protocol.CLIENT_SERVER_PONG);
//        System.out.println("Client ponged");
    }
    public void s_c_clientcount(Integer i, InetSocketAddress adr) {
        System.out.println(i+ "clients connected on this server");
    }
    public void s_c_clientid(Integer id, InetSocketAddress adr) {
        System.out.println("your id is: "+id);
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
    public void m_c_auth_reply(Integer i, InetSocketAddress adr) {
        if(i == Protocol.REPLY_SUCCESS)
            System.out.println("client succesfully listed. (master)");
        else
            System.out.println("client failed to be listed. (master)");
    }

    public void c_m_chat_lobby(String msg, InetSocketAddress adr) {
        System.out.println(msg);
    }

    public void s_c_auth_reply(Integer i, InetSocketAddress adr) {
        if(i == Protocol.REPLY_SUCCESS)
            System.out.println("client succesfully listed. (master)");
        else
            System.out.println("client failed to be listed. (master)");
    }

    // --- chat fkt
    public void s_c_chat(String msg, InetSocketAddress adr) {
        System.out.println("CHAT: "+msg);
    }

    // --- end chat
    public void s_c_logoff_reply(Integer reply, InetSocketAddress adr) {
        if(reply == Protocol.REPLY_SUCCESS)
            System.out.println("you have been succesfully logged off.");
        else
            System.out.println("log off failed");
    }

    public void s_c_jointeam_reply(Integer reply, InetSocketAddress adr) {
        if(reply == Protocol.REPLY_SUCCESS)
            System.out.println("you have been succesfully join team");
        else
            System.out.println("join team failed");
    }

    public void noReplyReceived(Packet p) {

    }
}
