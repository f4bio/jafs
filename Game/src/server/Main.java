package server;

import client.anim.UpdateLoop;
import common.engine.CPlayer;
import common.engine.ProjectileManager;
import common.net.Client;
import common.net.Network;
import common.net.Protocol;
import common.net.ProtocolCmd;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public class Main {
    /**
     *
     */
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

    private static String pathHighscore = System.getProperty("user.dir")+"\\highscores.ini";
    private static Properties highscores = new Properties();

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        if(args.length == 2) {
            name = args[0];
            map = args[1];
        } else {
            name = "Server";
            map = "rhein2";
        }

        game = new Game(map);
        boolean loaded = game.load();
        ProjectileManager.setGame(game);

        if(!loaded)
            System.exit(0);

        Protocol.init();
        net = new Network();
        ProtocolHandler protocol = new ProtocolHandler(net);
        net.setProtocolHandler(protocol);
        net.listen(net.getFreePort(40000, 50000));
        net.send(Network.MASTERHOST, Network.MASTERPORT, ProtocolCmd.SERVER_MASTER_AUTH);

        System.out.println("### SERVER STARTET ###\n");
        pingTimer = new Timer();
        pingTimer.scheduleAtFixedRate(pinger, PING_INTERVAL, PING_INTERVAL);

        respawnTimer = new Timer();
        respawnTimer.scheduleAtFixedRate(respawner, respawntime, respawntime);

        roundTimer = new Timer();
        roundTimer.scheduleAtFixedRate(gameTimer, gameTime, gameTime);

        update = new UpdateLoop(60);
        update.addUpdateObject(game);

        try {
            Thread.sleep(1000);
        } catch(Exception e) { }
    }

    /**
     *
     */
    public static void reset() {
        net.clear();
        game.setScoreBlue(0);
        game.setScoreRed(0);
        broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_RESPAWN);
    }

    public static void setPlayerHighscore(String playerName, int highscore){
        readHighscores();
        highscores.setProperty(playerName, ""+highscore);
        writeHighscores();
    }

    public static int getPlayerHighscore(String playerName){
        readHighscores();
        return Integer.parseInt(highscores.getProperty(playerName, "0"));
    }

    private static void readHighscores() {
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(pathHighscore));
            highscores.load(fis);
            fis.close();
        }
        catch (FileNotFoundException ex) { }
        catch (IOException ex){ }
    }

    private static void writeHighscores() {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(pathHighscore));
            highscores.store(fos, "");
            fos.close();
        }
        catch (FileNotFoundException ex) { }
        catch (IOException ex){ }
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
    private static Timer roundTimer;

    /**
     *
     * @param adr
     * @return
     */
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

    /**
     *
     * @param c
     */
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

    /**
     *
     * @param adr
     */
    public synchronized static void removeClient(InetSocketAddress adr) {
        removeClient(getClient(adr));
    }

    /**
     *
     * @param i
     * @return
     */
    public static Client getClient(int i) {
        if(i < 0 || i > client.length - 1)
            return null;
        return client[i];
    }

    /**
     *
     * @return
     */
    public static Client[] getClients() {
        return client;
    }

    /**
     *
     * @return
     */
    public static int getServerId(){
        return serverId;
    }

    /**
     *
     * @param id
     */
    public static void setServerId(int id){
        serverId = id;
    }

    /**
     *
     * @param adr
     * @return
     */
    public static Client getClient(InetSocketAddress adr) {
        for(Client cur : client) {
            if(cur != null && cur.getAddress().equals(adr))
                return cur;
        }

        return null;
    }

    /**
     *
     * @return
     */
    public static int clientCount() {
        int cnt = 0;
        for(Client c : client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING)
                cnt++;
        }
        return cnt;
    }

    /**
     *
     * @param adr
     * @return
     */
    public static int getClientId(InetSocketAddress adr){
        for(Client c: client)
            if(c != null && c.getAddress().equals(adr))
                return c.getId();
        return -1;
    }
    
    /**
     *
     * @param cmd
     * @param d
     */
    public synchronized static void broadcast(ProtocolCmd cmd, byte[]... d) {
        for(Client c : client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING &&
                    c.getPlayer().getTeam() != CPlayer.TEAM_NONE)
                net.send(c.getAddress(), cmd, d);
        }
    }

    /**
     *
     * @param msg
     * @param adr
     */
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

    /**
     *
     * @param msg
     * @param adr
     */
    public synchronized static void broadcast_chat_team(String msg, InetSocketAddress adr) {
        Client from = getClient(adr);

        for(Client c: client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING && c.getTeamId() == from.getTeamId())
                net.send(c.getAddress(), ProtocolCmd.SERVER_CLIENT_CHAT_TEAM,
                        argInt(from.getId()), argStr(msg));
        }
    }
    
    //well, not really a bradcast, is it?
    /**
     *
     * @param msg
     * @param to
     * @param adr
     */
    public synchronized static void broadcast_chat_private(String msg, int to, InetSocketAddress adr) {
        Client from = getClient(adr);
        Client recv = client[to];
        System.out.println("SERVER_CLIENT_CHAT_PRIVATE (to id="+to+", msg="+msg+")");
        // to receiver
        net.send(recv.getAddress(),
                 ProtocolCmd.SERVER_CLIENT_CHAT_PRIVATE,
                 argInt(from.getId()),
                 argStr(msg));
        if(!recv.getAddress().equals(from.getAddress())) {
            // to sender
            net.send(from.getAddress(),
                     ProtocolCmd.SERVER_CLIENT_CHAT_PRIVATE,
                     argInt(from.getId()),
                     argStr(msg));
        }
    }

    /**
     *
     * @param adr
     * @return
     */
    public static int getClientTeamId(InetSocketAddress adr) {
        Client c = getClient(adr);

        if(c != null)
            return c.getTeamId();

        return -1;
    }

    // wenn teamid schon gesetzt nicht nehr setzten?!  xD
    /**
     *
     * @param adr
     * @param teamId
     * @return
     */
    public static int setClientTeamId(InetSocketAddress adr, int teamId) {
        Client c = getClient(adr);

        if(c != null) {
            c.setTeamId(teamId);
            return 0;
        } else
            return -1;
    }

    /**
     *
     * @param name
     * @return
     */
    public static boolean nameExists(String name) {
        for(Client c : client) {
            if(c != null) {
                if(name.equalsIgnoreCase(c.getPlayer().getName()))
                    return true;
            }
        }

        return false;
    }

    /**
     *
     * @return
     */
    public static String getServerName() {
        return name;
    }

    /**
     *
     * @return
     */
    public static String getMapName() {
        return map;
    }

    /**
     *
     * @return
     */
    public static int getMaxPlayers() {
        return maxClients;
    }

    /**
     *
     * @return
     */
    public static int getCurPlayers() {
        return clientCount();
    }

    /**
     *
     * @return
     */
    public static Network getNetwork() {
        return net;
    }

    /**
     *
     * @return
     */
    public static Game getGame() {
        return game;
    }
}
