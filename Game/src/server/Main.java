package server;

import client.anim.UpdateLoop;
import common.engine.CPlayer;
import common.net.Client;
import common.net.Network;
import common.net.Protocol;
import common.net.ProtocolCmd;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public class Main {
    public static final int PING_INTERVAL = 10000;

    private static int maxClients = 16;
    private static int respawntime = 10000;
    private static int respawntimeItems = 5000;
    private static int gameTime = 60*10000;

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
        net.listen(net.getFreePort(40000, 50000));
//        net.listen(40000);
        net.send(Network.MASTERHOST, Network.MASTERPORT, ProtocolCmd.SERVER_MASTER_AUTH);

        System.out.println("### SERVER STARTET ###\n");
        pingTimer = new Timer();
        //pingTimer.schedule(pinger, PING_INTERVAL, PING_INTERVAL);
        pingTimer.scheduleAtFixedRate(pinger, PING_INTERVAL, PING_INTERVAL);

        update = new UpdateLoop(60);
        update.addUpdateObject(game);

        try {
            Thread.sleep(1000);
        } catch(Exception e) {

        }
    }

    public static void reset() {
        net.clear();
        game.setScoreBlue(0);
        game.setScoreRed(0);
        broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_RESPAWN);
    }

    private static TimerTask pinger = new TimerTask() {
        public void run() {
            for(Client cur : client) {
                if(cur != null){
                    net.send(cur.getAddress(), ProtocolCmd.SERVER_CLIENT_PING);
                    System.out.println("SERVER_CLIENT_PING -> " + cur.getAddress().getHostName());
                }
            }
        }
    };

    private static TimerTask respawner = new TimerTask() {
        public void run() {
            broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_RESPAWN);
        }
    };

    private static TimerTask itemRespawner = new TimerTask() {
        public void run() {
            broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_ITEM_SPAWNED);
        }
    };

    private static TimerTask gameTimer = new TimerTask() {
        public void run() {
            broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_TEAM_WON,
                    argInt(game.getWinnerTeam()));
            reset();
        }
    };

    private static Timer pingTimer;
    private static Timer respawnTimer;
    private static Timer itemRespawnTimer;

    public synchronized static Client addClient(InetSocketAddress adr) {
        Client c = null;

        for(int i=0; i<client.length; ++i) {
            if(client[i] == null) {
                c = new Client(adr);
                c.setId(i);
                client[i] = c;
                game.addPlayer(c.getPlayer());
                break;
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
    
    public synchronized static void broadcast(ProtocolCmd cmd, byte[]... d) {
        for(Client c : client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING &&
                    c.getPlayer().getTeam() != CPlayer.TEAM_NONE)
                net.send(c.getAddress(), cmd, d);
        }
    }

    public synchronized static void broadcast_chat(String msg, InetSocketAddress adr){
        Client from = getClient(adr);

        if(from == null)
            return;

        for(Client c: client) {
            System.out.print(" broadcast_chat(...)");
            if(c != null && c.getStatus() != Client.STATUS_PENDING) {
                net.send(c.getAddress(),
                         ProtocolCmd.SERVER_CLIENT_CHAT_ALL,
                         argInt(from.getId()),
                         argStr(msg));
                System.out.println(" !Client.STATUS_PENDING ("+c.getAddress()+") -> SERVER_CLIENT_CHAT_ALL (id="+from.getId()+", msg="+msg+")");
            } else
                System.out.println(" Client.STATUS_PENDING -> no broadcast.");
        }
    }

    public synchronized static void broadcast_chat_team(String msg, InetSocketAddress adr) {
        Client from = getClient(adr);

        for(Client c: client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING && c.getTeamId() == from.getTeamId())
                net.send(c.getAddress(), ProtocolCmd.SERVER_CLIENT_CHAT_TEAM,
                        argInt(from.getId()), argStr(msg));
        }
    }
    
    //well, not really a bradcast, is it?
    public synchronized static void broadcast_chat_private(String msg, Integer to, InetSocketAddress adr) {
        Client from = getClient(adr);
        Client recv = client[to];
        
        net.send(recv.getAddress(), ProtocolCmd.SERVER_CLIENT_CHAT_PRIVATE,
                argInt(from.getId()), argStr(msg));
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
