package server;

import client.anim.UpdateLoop;
import common.net.Client;
import common.net.Network;
import common.net.Protocol;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author miracle
 */
public class Main {
    public static final int PING_INTERVAL = 10000;

    private static int maxClients = 16;
    private static String map;
    private static String name;
    private static Client[] client = new Client[maxClients];

    private static int serverId;
    private static Network net;
    private static Game game;
    private static UpdateLoop update;

    public static void main(String[] args) {
        name = "Test";
        map = "map";
        game = new Game(map);

        boolean loaded = game.load();

        if(!loaded)
            System.exit(0);

        Protocol.init();
        net = new Network();
        ProtocolHandler protocol = new ProtocolHandler(net);
        net.setProtocolHandler(protocol);
        //net.listen(net.getFreePort(40000, 50000));
        net.listen(40000);
        //net.send(Network.MASTERHOST, Network.MASTERPORT, Protocol.SERVER_MASTER_AUTH);

        pingTimer = new Timer();
        pingTimer.schedule(pinger, PING_INTERVAL, PING_INTERVAL);

        update = new UpdateLoop(60);
        update.addUpdateObject(game);

        try {
            Thread.sleep(1000);
        } catch(Exception e) {

        }
    }

    private static TimerTask pinger = new TimerTask() {
        public void run() {
            for(Client cur : client) {
                if(cur != null)
                    net.send(cur.getAddress(), Protocol.SERVER_CLIENT_PING);
            }
        }
    };

    private static Timer pingTimer;

    public synchronized static Client addClient(InetSocketAddress adr) {
        Client c = null;

        for(int i=0; i<client.length; ++i) {
            if(client[i] == null) {
                c = new Client(adr);
                c.setId(i);
                client[i] = c;
                game.addPlayer(c.getPlayer());
            }
        }

        return c;
    }

    public synchronized static void removeClient(Client c) {
        try {
            if(client[c.getId()] != null) {
                game.removePlayer(c.getPlayer());
                client[c.getId()] = null;
            }
            System.out.println("Client " + c.getHost() + ":" + c.getPort() + " dropped from Server-"+serverId+"." );
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public synchronized static void removeClient(InetSocketAddress adr) {
        removeClient(getClient(adr));
    }

    public static Client getClient(int i) {
        if(i < 0 || i > client.length - 1)
            return null;
        return client[i];
    }

    public static Client[] getClients() {
        return client;
    }

    public static int getServerId(){
        return serverId;
    }

    public static void setServerId(int id){
        serverId = id;
    }

    public static Client getClient(InetSocketAddress adr) {
        for(Client cur : client) {
            if(cur != null && cur.getAddress().equals(adr))
                return cur;
        }

        return null;
    }

    public static int clientCount() {
        int cnt = 0;
        for(Client c : client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING)
                cnt++;
        }
        return cnt;
    }

    public static int getClientId(InetSocketAddress adr){
        for(Client c: client)
            if(c != null && c.getAddress().equals(adr))
                return c.getId();
        return -1;
    }
    
    public synchronized static void broadcast(String cmd, Object... o) {
        for(Client c : client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING)
                net.send(c.getAddress(), cmd, o);
        }
    }

    public synchronized static void broadcast_chat(String msg, InetSocketAddress adr){
        Client from = getClient(adr);

        if(from == null)
            return;

        for(Client c: client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING)
                net.send(c.getAddress(), Protocol.SERVER_CLIENT_CHAT_ALL, from.getId(), msg);
        }
    }

    public synchronized static void broadcast_chat_team(String msg, InetSocketAddress adr) {
        Client from = getClient(adr);

        for(Client c: client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING && c.getTeamId() == from.getTeamId())
                net.send(c.getAddress(), Protocol.SERVER_CLIENT_CHAT_TEAM, from.getId(), msg);
        }
    }
    
    //well, not really a bradcast, is it?
    public synchronized static void broadcast_chat_private(String msg, Integer to, InetSocketAddress adr) {
        Client from = getClient(adr);
        Client recv = client[to];
        
        net.send(recv.getAddress(), Protocol.SERVER_CLIENT_CHAT_PRIVATE, from.getId(), msg);
    }

    public static int getClientTeamId(InetSocketAddress adr) {
        Client c = getClient(adr);

        if(c != null)
            return c.getTeamId();

        return -1;
    }

    // wenn teamid schon gesetzt nicht nehr setzten?!  xD
    public static int setClientTeamId(InetSocketAddress adr, int teamId) {
        Client c = getClient(adr);

        if(c != null) {
            c.setTeamId(teamId);
            return 0;
        } else
            return -1;
    }

    public static boolean nameExists(String name) {
        for(Client c : client) {
            if(c != null) {
                if(name.equalsIgnoreCase(c.getPlayer().getName()))
                    return true;
            }
        }

        return false;
    }

    public static String getServerName() {
        return name;
    }

    public static String getMapName() {
        return map;
    }

    public static int getMaxPlayers() {
        return maxClients;
    }

    public static int getCurPlayers() {
        return clientCount();
    }

    public static Network getNetwork() {
        return net;
    }
}
