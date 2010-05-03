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
    /**
     * Constructs a ProtocolHandler object.
     * Overrides every needed method of ProtocolHandler
     * @param net Network
     */
    public ProtocolHandler(Network net)
    {
        super(net, ProtocolHandler.MODE_MASTER);
    }

    @Override
    public void c_m_joinserver(String host, int port, InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.MASTER_CLIENT_JOINSERVER_REPLY,
                 argStr("JOINED! ServerInfos: "+host+":"+port));
        Main.broadcast(Main.getClient(adr).getPlayer().getName()+" betritt den Server: "+Main.getServer(host, port).getName()+" ("+host+":"+port+")", new InetSocketAddress(Network.MASTERHOST, Network.MASTERPORT));
        System.out.println("CLIENT_MASTER_JOINSERVER -> MASTER_CLIENT_JOINSERVER_REPLY (JOINED! ServerInfos: "+host+":"+port+")");
//        System.out.println("c_m_joinserver()");
    }

    @Override
    public void c_m_forced_nickchange_ok(InetSocketAddress adr)
    {
        System.out.println("CLIENT_MASTER_FORCED_NICKCHANGE_OK");
    }

    @Override
    public void c_m_nickchange(String newNick, InetSocketAddress adr)
    {
        System.out.println("CLIENT_MASTER_NICKCHANGE (new nick:"+newNick+")");
        Main.checkNick(newNick, adr);
    }

    @Override
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

    @Override
    public void c_m_auth(String name, InetSocketAddress adr)
    {
        Client client = Main.addClient(adr);
        
        if(client != null) {
            client.getPlayer().setName(Main.checkNick(name, adr));
            net.send(adr, ProtocolCmd.MASTER_CLIENT_AUTH_REPLY,
                     argInt(Protocol.REPLY_SUCCESS),
                     argInt(client.getId()));
            System.out.println("CLIENT_MASTER_AUTH success (id="+client.getId()+") -> MASTER_CLIENT_AUTH_REPLY (REPLY_SUCCESS)");
            System.out.println("Client \""+client.getPlayer().getName()+"\" joined!");
            Main.broadcastClientlist();
            Main.broadcast(client.getPlayer().getName()+" ist nun online!", new InetSocketAddress(Network.MASTERHOST, Network.MASTERPORT));
        }
        else {
            net.send(adr,
                     ProtocolCmd.MASTER_CLIENT_AUTH_REPLY,
                     argInt(Protocol.REPLY_FAILURE));
            System.out.println("CLIENT_MASTER_AUTH failure -> MASTER_CLIENT_AUTH_REPLY (REPLY_FAILURE)");
        }
    }
    @Override
    public void c_m_chat_lobby(String msg, InetSocketAddress adr)
    {
        System.out.println("CLIENT_MASTER_CHAT_LOBBY msg="+msg);
        net.send(adr, ProtocolCmd.MASTER_CLIENT_CHAT_OK);
        Main.broadcast(msg, adr);
    }

    @Override
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

    @Override
    public void c_m_logoff(InetSocketAddress adr){
        System.out.println("CLIENT_MASTER_LOGOFF");
        Client c = Main.getClient(adr);
        if(c != null){
            String clLoggedOff = Main.getClient(adr).getPlayer().getName();
            Main.removeClient(adr);
            Main.broadcastClientlist();
            Main.broadcast(clLoggedOff + " hat sich abgemeldet!", new InetSocketAddress(Network.MASTERHOST, Network.MASTERPORT));
        }
    }

    @Override
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

    @Override
    public void s_m_pong(InetSocketAddress adr)
    {
//        System.out.println("SERVER_MASTER_PONG");
    }

    @Override
    public void s_m_servercount(InetSocketAddress adr)
    {
        System.out.println("SERVER_MASTER_SERVERCOUNT -> MASTER_SERVER_SERVERCOUNT");
        net.send(adr, ProtocolCmd.MASTER_SERVER_SERVERCOUNT, argInt(Main.serverCount()));
    }

    public void noReplyReceived(Packet p)
    {
        if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.MASTER_SERVER_PING) {
            Main.removeServer(p.getAddress());
        } else if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.MASTER_CLIENT_PING) {
            Main.removeClient(Main.getClient(p.getAddress()));
        } else if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.MASTER_SERVER_PING) {
            Main.removeServer(Main.getServer(p.getAddress()));
        }
    }
}
