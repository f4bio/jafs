package server;

import common.engine.UpdateLoop;
import common.engine.CPlayer;
import common.engine.ProjectileManager;
import common.engine.UpdateCountdown;
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
    private static int gameTime = 60*10000;
    /*private static int respawntime = 5000;
    private static int gameTime = 10*10000;*/

    private static String map;
    private static String name;
    private static Client[] client = new Client[maxClients];

    private static int serverId;
    private static Network net;
    private static Game game;
    private static UpdateLoop update;
    private static boolean reset;
    private static ProjectileManager projectile;

    private static UpdateCountdown pingTimer;
    private static UpdateCountdown respawnTimer;
    private static UpdateCountdown roundTimer;

    private static String pathHighscore = System.getProperty("user.dir")+"\\highscores.ini";
    private static Properties highscores;

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length == 2) {
            name = args[0];
            map = args[1];
        } else {
            name = "Server";
            map = "labyrinth2";
        }

        highscores = new Properties();
        readHighscores();

        game = new Game(map);
        boolean loaded = game.load();
        projectile = new ProjectileManager();
        projectile.setGame(game);

        if(!loaded)
            System.exit(0);

        reset = false;
        Protocol.init();
        net = new Network();
        ProtocolHandler protocol = new ProtocolHandler(net);
        net.setProtocolHandler(protocol);
        net.listen(Network.getFreePort(40000, 50000));
        net.send(Network.MASTERHOST, Network.MASTERPORT, ProtocolCmd.SERVER_MASTER_AUTH);

        System.out.println("### SERVER STARTET ###\n");
        pingTimer = new UpdateCountdown("ping", PING_INTERVAL);

        respawnTimer = new UpdateCountdown("respawn", respawntime);

        roundTimer = new UpdateCountdown("round", gameTime);

        update = new UpdateLoop(60);
        update.addUpdateObject(game);
        update.addUpdateCountdownObject(game);
        update.addUpdateCountdown(pingTimer);
        update.addUpdateCountdown(respawnTimer);
        update.addUpdateCountdown(roundTimer);

        try {
            Thread.sleep(1000);
        } catch(Exception e) { }
    }

    /**
     * Reset the whole server settings and stats
     */
    public static void reset() {
        //net.clear();
        game.setScoreBlue(0);
        game.setScoreRed(0);

        CPlayer[] p = game.getPlayer();
        for(int i=0; i<p.length; ++i) {
            if(p[i] == null)
                continue;

            p[i].setDeaths(0);
            p[i].setKills(0);
            p[i].setHealth(0);
        }
    }

    /**
     *
     * @param playerName Name of the player you want to set the highscore
     * @param highscore The highscore of the player
     */
    public static void setPlayerHighscore(String playerName, int highscore){
        if(playerName == null)
            return;
        
        try {
            int current = Integer.parseInt(highscores.getProperty(playerName, "0"));
            
            if(highscore > current)
                highscores.setProperty(playerName, ""+highscore);
        } catch(Exception e) {
            highscores.setProperty(playerName, ""+highscore);
        }
    }

    /**
     *
     * @param playerName Name of the player you want to get the highscore
     * @return The highscore of the player
     */
    public static int getPlayerHighscore(String playerName){
        return Integer.parseInt(highscores.getProperty(playerName, "0"));
    }

    /**
     * To read the highscores saved in ini file
     */
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

    /**
     * To write the highscores into ini file
     */
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

    public static void ping() {
        for(Client cur : client) {
                if(cur != null){
                    net.send(cur.getAddress(), ProtocolCmd.SERVER_CLIENT_PING);
                    System.out.println("SERVER_CLIENT_PING -> " + cur.getAddress().getHostName());
                }
            }
    }

    public static void respawn() {
        if(reset) {
            reset();
            reset = false;
        }

        CPlayer[] p = game.getPlayer();
        for(CPlayer c : p) {
            if(c != null && c.isDead())
                c.setHealth(100);
        }

        broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_RESPAWN);
    }

    public static void end() {
        broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_TEAM_WON,
                    argInt(game.getWinnerTeam()), argInt(game.getBestPlayer()));
        for(Client c : client) {
            if(c != null) {
                CPlayer p = c.getPlayer();
                
                if(p != null) {
                    setPlayerHighscore(p.getName(), p.getKills());
                }
            }
        }
        writeHighscores();

        CPlayer[] p = game.getPlayer();
        for(CPlayer c : p) {
            if(c != null)
                c.setHealth(0);
        }
        
        update.resetCountdown(respawnTimer);
        update.resetCountdown(roundTimer);
        reset = true;
    }

    /**
     *
     * @param adr InetSocketAddress of the client you wat to get
     * @return The client you want to get
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
     * @param c The client you want to remove
     */
    public synchronized static void removeClient(Client c) {
        try {
            if(c != null) {
                System.out.println("Client " + c.getHost() + ":" + c.getPort() + " dropped from Server-"+serverId+"." );
                game.removePlayer(c.getPlayer());
                client[c.getId()] = null;
                broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_LEFT, argInt(c.getId()));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     *
     * @param adr InetSocketAddress of the player you want to remove
     */
    public synchronized static void removeClient(InetSocketAddress adr) {
        removeClient(getClient(adr));
    }

    /**
     *
     * @param i Index of the listed client you want to get
     * @return Client you want to get
     */
    public static Client getClient(int i) {
        if(i < 0 || i > client.length - 1)
            return null;
        return client[i];
    }

    /**
     *
     * @return The client array
     */
    public static Client[] getClients() {
        return client;
    }

    /**
     *
     * @return The server id
     */
    public static int getServerId(){
        return serverId;
    }

    /**
     *
     * @param id To set the server id
     */
    public static void setServerId(int id){
        serverId = id;
    }

    /**
     *
     * @param adr InetSocketAddress of the client you want to get
     * @return The client you want to get
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
     * @return Number of clients, which are connected
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
     * @param adr InetSocketAddress of the client you want to get the id
     * @return ID of the client
     */
    public static int getClientId(InetSocketAddress adr){
        for(Client c: client)
            if(c != null && c.getAddress().equals(adr))
                return c.getId();
        return -1;
    }
    
    /**
     *
     * @param cmd Network command
     * @param d Parameters of network command
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
     * @param msg Message of the client you want to broadcast as chat
     * @param adr InetSocketAddress of the client, which sent the message
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
     * @param msg Message of the client you want to broadcast as team chat
     * @param adr InetSocketAddress of the client, who sent the message
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
     * @param msg Message of the client you want to broadcast as private chat
     * @param to Index in clientlist of the receiver client
     * @param adr InetSocketAddress of the client, who sent the message
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
     * @param adr InetSocketAddress of the client you want to get the team id
     * @return Team ID of client
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
     * @param adr InetSocketAddress of the client you want to set the Team ID
     * @param teamId Team ID
     * @return 0: success, -1: failture
     */
    public static int setClientTeamId(InetSocketAddress adr, int teamId) {
        Client c = getClient(adr);

        if(c != null) {
            if(teamId == c.getTeamId())
                return -1;

            if(game.playerCount(teamId) - game.playerCount(c.getTeamId()) > 1)
                return -1;

            c.setTeamId(teamId);
            c.getPlayer().setHealth(0);

            if(getCurPlayers() < 2)
                end();

            return 0;
        } else
            return -1;
    }

    /**
     *
     * @param name Name to check if it exists
     * @return true: name exists, false: name does not exist
     */
    public static boolean nameExists(String name) {
        for(Client c : client) {
            if(c != null && c.getStatus() != Client.STATUS_PENDING) {
                if(name.equalsIgnoreCase(c.getPlayer().getName()))
                    return true;
            }
        }
        return false;
    }

    /**
     *
     * @return Name of server
     */
    public static String getServerName() {
        return name;
    }

    /**
     *
     * @return Name of map
     */
    public static String getMapName() {
        return map;
    }

    /**
     *
     * @return Number of max players
     */
    public static int getMaxPlayers() {
        return maxClients;
    }

    /**
     *
     * @return Number of current players
     */
    public static int getCurPlayers() {
        return clientCount();
    }

    /**
     *
     * @return Network object of server
     */
    public static Network getNetwork() {
        return net;
    }

    /**
     *
     * @return Gamedata of server
     */
    public static Game getGame() {
        return game;
    }

    public static ProjectileManager getProjectileManager() {
        return projectile;
    }

    /**
     *
     * @return Left round time
     */
    public static long getRoundTimeLeft() {
        return roundTimer.getTimeLeft();
    }

    /**
     *
     * @return Left respawn time
     */
    public static long getRespawnTimeLeft() {
        return respawnTimer.getTimeLeft();
    }
}
