package masterserver;

import common.net.Client;
import common.net.Network;
import common.net.Packet;
import common.net.Protocol;
import common.net.ProtocolCmd;
import common.net.Server;
import java.net.InetSocketAddress;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public class ProtocolHandler extends common.net.ProtocolHandler {
    public ProtocolHandler(Network net)
    {
        super(net, ProtocolHandler.MODE_MASTER);
    }

    public void s_m_auth(InetSocketAddress adr)
    {
        Server added = Main.addServer(adr);
        if(added != null) {
            net.send(adr, ProtocolCmd.MASTER_SERVER_AUTH_REPLY,
                    argInt(Protocol.REPLY_SUCCESS));
            System.out.println("SERVER_MASTER_AUTH success -> MASTER_SERVER_AUTH_REPLY (REPLY_SUCCESS)");
        } else {
            net.send(adr, ProtocolCmd.MASTER_SERVER_AUTH_REPLY,
                    argInt(Protocol.REPLY_FAILURE));
            System.out.println("SERVER_MASTER_AUTH failure -> MASTER_SERVER_AUTH_REPLY (REPLY_FAILURE)");
        }
    }

    public void s_m_pong(InetSocketAddress adr)
    {
        
    }

    public void s_m_servercount(InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.MASTER_SERVER_SERVERCOUNT, argInt(Main.serverCount()));
    }

    public void c_m_joinserver(String host, int port, InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.MASTER_CLIENT_JOINSERVER_REPLY,
                 argStr("JOINED! ServerInfos: "+host+":"+port));
        System.out.println("CLIENT_MASTER_JOINSERVER -> MASTER_CLIENT_JOINSERVER_REPLY (JOINED! ServerInfos: "+host+":"+port+")");
//        System.out.println("c_m_joinserver()");
    }

    public void c_m_listrequest(InetSocketAddress adr)
    {
        String[] list = Main.getServerlist();
        net.send(adr, ProtocolCmd.MASTER_CLIENT_NEWLIST);
        for(String i : list) {
            net.send(adr, ProtocolCmd.MASTER_CLIENT_LISTENTRY, argStr(i));
        }
        net.send(adr, ProtocolCmd.MASTER_CLIENT_ENDLIST);
    }
    public void c_m_auth(String name, InetSocketAddress adr)
    {
        Client client = Main.addClient(adr);
        client.getPlayer().setName(name);
        System.out.println(client.getPlayer().getName()+" joined");
        if(client != null) {
            net.send(adr, ProtocolCmd.MASTER_CLIENT_AUTH_REPLY,
                    argInt(Protocol.REPLY_SUCCESS));
            System.out.println("CLIENT_MASTER_AUTH success -> MASTER_CLIENT_AUTH_REPLY (REPLY_SUCCESS)");
        }
        else {
            net.send(adr, ProtocolCmd.MASTER_CLIENT_AUTH_REPLY,
                    argInt(Protocol.REPLY_FAILURE));
            System.out.println("CLIENT_MASTER_AUTH failure -> MASTER_CLIENT_AUTH_REPLY (REPLY_FAILURE)");
        }
    }
    public void c_m_chat_lobby(String msg, InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.MASTER_CLIENT_CHAT_OK);
        Main.broadcast(msg, adr);
    }

//    public void c_m_chat_lobby_ok(InetSocketAddress adr)
//    {
//        //
//    }


    public void c_m_chat_private(int id, String msg, InetSocketAddress adr)
    {
        Client client = Main.getClient(id);
        net.send(adr, ProtocolCmd.MASTER_CLIENT_CHAT_OK);
        if(client != null)
            net.send(client.getAddress(), ProtocolCmd.MASTER_CLIENT_CHAT,
                    argInt(id), argStr(msg));
        else
            net.send(adr, ProtocolCmd.MASTER_CLIENT_CHAT, argInt(-1),
                    argStr("No such player"));
    }

    public void c_m_logoff(InetSocketAddress adr){
        Main.removeClient(adr);
    }

    public void noReplyReceived(Packet p)
    {
        if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.MASTER_SERVER_PING) {
            Main.removeServer(p.getAddress());
        } else if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.MASTER_CLIENT_PING) {
            Main.removeClient(Main.getClient(p.getAddress()));
        }
    }
}
