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

    public void c_m_joinserver(String host, int port, InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.MASTER_CLIENT_JOINSERVER_REPLY,
                 argStr("JOINED! ServerInfos: "+host+":"+port));
        System.out.println("CLIENT_MASTER_JOINSERVER -> MASTER_CLIENT_JOINSERVER_REPLY (JOINED! ServerInfos: "+host+":"+port+")");
//        System.out.println("c_m_joinserver()");
    }

    public void c_m_forced_nickchange_ok(InetSocketAddress adr)
    {
        System.out.println("CLIENT_MASTER_FORCED_NICKCHANGE_OK");
    }

    public void c_m_nickchange(String newNick, InetSocketAddress adr)
    {
        Main.checkNick(newNick, adr);
    }

    public void c_m_listrequest(short type, InetSocketAddress adr)
    {
        System.out.print("CLIENT_MASTER_LISTREQUEST");
        if(type == Protocol.LIST_TYPE_SERVERLIST){
            System.out.println(" (LIST_TYPE_SERVERLIST)");
            String[] list = Main.getServerlist();
            net.send(adr, ProtocolCmd.MASTER_CLIENT_NEWLIST, argShort(Protocol.LIST_TYPE_SERVERLIST));
            for(String i : list) {
                net.send(adr, ProtocolCmd.MASTER_CLIENT_LISTENTRY_SERVER, argStr(i));
            }
            net.send(adr, ProtocolCmd.MASTER_CLIENT_ENDLIST, argShort(Protocol.LIST_TYPE_SERVERLIST));
        } else {
            System.out.println(" (LIST_TYPE_CLIENTLIST)");
            Client[] list = Main.getClientlist();
            net.send(adr, ProtocolCmd.MASTER_CLIENT_NEWLIST, argShort(Protocol.LIST_TYPE_CLIENTLIST));
            for(Client i : list) {
                if(!i.getAddress().equals(adr))
                    net.send(adr, ProtocolCmd.MASTER_CLIENT_LISTENTRY_CLIENT,
                             argStr(i.getHost()+":"+i.getPort()),
                             argInt(i.getId()),
                             argStr(i.getPlayer().getName()));
            }
            net.send(adr, ProtocolCmd.MASTER_CLIENT_ENDLIST, argShort(Protocol.LIST_TYPE_CLIENTLIST));
            System.out.println("MASTER_CLIENT_ENDLIST");
        }
        
    }

    public void c_m_auth(String name, InetSocketAddress adr)
    {
        Client client = Main.addClient(adr);
        client.getPlayer().setName(Main.checkNick(name, adr));
        if(client != null) {
            net.send(adr, ProtocolCmd.MASTER_CLIENT_AUTH_REPLY,
                     argInt(Protocol.REPLY_SUCCESS),
                     argInt(client.getId()));
            System.out.println("CLIENT_MASTER_AUTH success (id="+client.getId()+") -> MASTER_CLIENT_AUTH_REPLY (REPLY_SUCCESS)");
            System.out.println("Client \""+client.getPlayer().getName()+"\" joined!");
            Main.broadcastClientlist();
        }
        else {
            net.send(adr,
                     ProtocolCmd.MASTER_CLIENT_AUTH_REPLY,
                     argInt(Protocol.REPLY_FAILURE));
            System.out.println("CLIENT_MASTER_AUTH failure -> MASTER_CLIENT_AUTH_REPLY (REPLY_FAILURE)");
        }
    }
    public void c_m_chat_lobby(String msg, InetSocketAddress adr)
    {
        System.out.println("CLIENT_MASTER_CHAT_LOBBY msg="+msg);
        net.send(adr, ProtocolCmd.MASTER_CLIENT_CHAT_OK);
        Main.broadcast(msg, adr);
    }

    public void c_m_chat_private(int receiverID, String msg, InetSocketAddress adr)
    {
        System.out.println("CLIENT_MASTER_CHAT_PRIVATE receiverID="+receiverID+", msg="+msg);
        Client receiver = Main.getClient(receiverID);
        Client sender = Main.getClient(adr);
        net.send(sender.getAddress(),
                 ProtocolCmd.MASTER_CLIENT_CHAT_OK);
        if(receiver != null) {
            // to sender
            net.send(sender.getAddress(),
                     ProtocolCmd.MASTER_CLIENT_CHAT_PRIVATE,
                     argInt(sender.getId()),
                     argInt(receiver.getId()),
                     argStr(msg));
            // to id
            net.send(receiver.getAddress(),
                     ProtocolCmd.MASTER_CLIENT_CHAT_PRIVATE,
                     argInt(sender.getId()),
                     argInt(receiver.getId()),
                     argStr(msg));
        } else {
            net.send(adr,
                     ProtocolCmd.MASTER_CLIENT_CHAT,
                     argInt(-1),
                     argStr("No such player"));
        }
    }

    public void c_m_logoff(InetSocketAddress adr){
        System.out.println("CLIENT_MASTER_LOGOFF");
        Main.removeClient(adr);
        Main.broadcastClientlist();
    }

    public void s_m_auth(InetSocketAddress adr)
    {
        Server added = Main.addServer(adr);
        if(added != null) {
            net.send(adr,
                     ProtocolCmd.MASTER_SERVER_AUTH_REPLY,
                     argInt(Protocol.REPLY_SUCCESS));
            System.out.println("SERVER_MASTER_AUTH success -> MASTER_SERVER_AUTH_REPLY (REPLY_SUCCESS)");
        } else {
            net.send(adr,
                     ProtocolCmd.MASTER_SERVER_AUTH_REPLY,
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

    public void noReplyReceived(Packet p)
    {
        if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.MASTER_SERVER_PING) {
            Main.removeServer(p.getAddress());
        } else if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.MASTER_CLIENT_PING) {
            Main.removeClient(Main.getClient(p.getAddress()));
        }
    }
}
