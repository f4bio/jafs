package masterserver;

import common.net.Client;
import common.net.Network;
import common.net.Protocol;
import common.net.ProtocolCmd;
import common.net.Server;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public class Main {
    public static int maxPingFailures = 2;
    public static final int pingRefreshInterval = 10000;

    private static Network net;
    private static ProtocolHandler handler;
    private static ArrayList<Server> serverlist = new ArrayList<Server>();
    private static ArrayList<Client> clientlist = new ArrayList<Client>();

    private static TimerTask pinger = new TimerTask() {
        public void run() {
            for(int i=0;i<serverlist.size();i++) {
                net.send(serverlist.get(i).getAddress(), ProtocolCmd.MASTER_SERVER_PING);
            }
            
            for(int i=0; i<clientlist.size(); ++i) {
                net.send(clientlist.get(i).getAddress(), ProtocolCmd.MASTER_CLIENT_PING);
            }
        }
    };

    private static Timer pingTimer;

    public static void main(String[] args) {
        Protocol.init();
        net = new Network();
        handler = new ProtocolHandler(net);
        net.setProtocolHandler(handler);
        net.listen(Network.MASTERPORT);

        System.out.println("### MASTERSERVER STARTET ###\n");
        pingTimer = new Timer();
        pingTimer.schedule(pinger, pingRefreshInterval, pingRefreshInterval);
    }

    public static Server addServer(InetSocketAddress adr) {
        for(Server server : serverlist) {
            if(server.getAddress().equals(adr))
                return null;
        }
        Server server = new Server(adr);
        server.setServerId(serverlist.size());
        serverlist.add(server);
        System.out.println("Server " + server.getHost() + ":" + server.getPort() + " listed." );
        
        return server;
    }

    public static void removeServer(Server server) {
        serverlist.remove(server);
        System.out.println("Server dropped.");

        //System.out.println("Server " + server.getHost() + ":" + server.getPort() + " dropped." );
    }

    public static void removeServer(InetSocketAddress adr) {
        removeServer(getServer(adr));
    }

    public static Server getServer(String host, int port) {
        for(Server cur : serverlist) {
            if(cur.getHost().equals(host) && cur.getPort() == port)
                return cur;
        }

        return null;
    }

    public static Server getServer(InetSocketAddress adr) {
        for(Server cur : serverlist) {
            if(cur.getAddress().equals(adr))
                return cur;
        }

        return null;
    }

    public static String[] getServerlist() {
        String[] srv = new String[serverlist.size()];
        int i = 0;
        for(Server current : serverlist) {
            srv[i] = current.getHost() + ":" + current.getPort();
            i++;
        }

        return srv;
    }

    public static Client[] getClientlist() {
        Client[] cl = new Client[clientlist.size()];
        int i = 0;
        for(Client current : clientlist) {
            cl[i] = current;
            i++;
        }

        return cl;
    }

    public static Client getClient(int id){
        for(Client client: clientlist)
            if(client.getId() == id)
                return client;
        return null;
    }

    public static int serverCount(){
        return serverlist.size();
    }

    public static void broadcast(String msg, InetSocketAddress adr){
        Client sender = getClient(adr);
        for(Client client: clientlist)
            if(!client.isInGame())
                net.send(client.getAddress(),
                         ProtocolCmd.MASTER_CLIENT_CHAT,
                         argInt(sender.getId()),
                         argStr(msg));
    }

    public static Client getClient(InetSocketAddress adr) {
        for(Client cur : clientlist) {
            if(cur.getAddress().equals(adr))
                return cur;
        }

        return null;
    }
    public static Client addClient(InetSocketAddress adr) {
        for(Client client: clientlist)
            if(client.getAddress().equals(adr))
                return null;

        Client client = new Client(adr);
        client.setId(clientlist.size());
        clientlist.add(client);
        System.out.println("Client "+client.getHost()+":"+client.getPort()+" listed.");
        
        return client;
    }

    public static void removeClient(Client client) {
        if(clientlist.remove(client))
            System.out.println("removeClient(...) Client dropped.");
        else
            System.out.println("removeClient(...) Couldn't find client to drop.");
    }
    
    public static void removeClient(InetSocketAddress adr) {
        removeClient(getClient(adr));
    }

    public static void broadcastClientlist(){
        for(Client i : clientlist) {
            handler.c_m_listrequest(Protocol.LIST_TYPE_CLIENTLIST, i.getAddress());
        }
    }

    public static boolean nameExists(String name, String ignoreName) {
        if(ignoreName != null){
            for(Client c : clientlist) {
                if(c != null && !c.getPlayer().getName().equals(ignoreName)) {
                    if(name.equalsIgnoreCase(c.getPlayer().getName()))
                        return true;
                }
            }
            return false;
        } else {
            for(Client c : clientlist) {
                if(c != null) {
                    if(name.equalsIgnoreCase(c.getPlayer().getName()))
                        return true;
                }
            }
            return false;
        }
    }

    public static String checkNick(String nick, InetSocketAddress adr){
        System.out.print("CLIENT_MASTER_NICKCHANGE ("+nick+")");

        boolean changed = false;

        while(Main.nameExists(nick, getClient(adr).getPlayer().getName())) {
            nick = nick.concat("*");
            changed = true;
        }

        if(changed) {
            System.out.println(" -> MASTER_CLIENT_FORCED_NICKCHANGE ("+nick+")");
            net.send(adr, ProtocolCmd.MASTER_CLIENT_FORCED_NICKCHANGE, argStr(nick));
        } else {
            System.out.println(" -> MASTER_CLIENT_NICKCHANGE_OK");
            net.send(adr, ProtocolCmd.MASTER_CLIENT_NICKCHANGE_OK);
        }
        Main.getClient(adr).getPlayer().setName(nick);
        Main.broadcastClientlist();
        return nick;
    }
}