package masterserver;

import common.net.Client;
import common.net.Network;
import common.net.Packet;
import common.net.Protocol;
import common.net.Server;
import java.net.InetSocketAddress;

/**
 *
 * @author miracle
 */
public class ProtocolHandler extends common.net.ProtocolHandler {
    public ProtocolHandler(Network net)
    {
        super(net);
    }

    public void s_m_auth(InetSocketAddress adr)
    {
        Server added = Main.addServer(adr);
        if(added != null)
            net.send(adr, Protocol.MASTER_SERVER_AUTH_REPLY, Protocol.REPLY_SUCCESS);
        else
            net.send(adr, Protocol.MASTER_SERVER_AUTH_REPLY, Protocol.REPLY_FAILURE);
    }

    public void s_m_pong(InetSocketAddress adr)
    {
        Main.decreasePingFailures(adr);
    }

    public void s_m_servercount(InetSocketAddress adr)
    {
        net.send(adr, Protocol.MASTER_SERVER_SERVERCOUNT, Main.serverCount());
    }

    public void c_m_joinserver(String host, Integer port, InetSocketAddress adr) 
    {
        System.out.println("c_m_joinserver()");
    }

    public void c_m_listrequest(InetSocketAddress adr)
    {
        String[] list = Main.getServerlist();
        net.send(adr, Protocol.MASTER_CLIENT_NEWLIST, new Object[0]);
        for(String i : list) {
            net.send(adr, Protocol.MASTER_CLIENT_LISTENTRY, i);
        }
        net.send(adr, Protocol.MASTER_CLIENT_ENDLIST, new Object[0]);
    }
    public void c_m_auth(InetSocketAddress adr)
    {
        Client client = Main.addClient(adr);
        if(client != null)
            net.send(adr, Protocol.MASTER_CLIENT_AUTH_REPLY, Protocol.REPLY_SUCCESS);
        else
            net.send(adr, Protocol.MASTER_CLIENT_AUTH_REPLY, Protocol.REPLY_FAILURE);
    }
    public void c_m_chat_lobby(String msg, InetSocketAddress adr)
    {
        Main.broadcast(msg, adr);
    }

    public void noReplyReceived(Packet p)
    {
        if(p.getCmd().equals(Protocol.MASTER_SERVER_PING)) {
            Main.removeServer(p.getAddress());
        }
    }
}
