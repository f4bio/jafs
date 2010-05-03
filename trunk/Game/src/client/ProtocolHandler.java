package client;

import common.CVector2;
import common.engine.CMap;
import common.engine.CPlayer;
import common.engine.CProjectile;
import common.engine.CWeapon;
import common.engine.ProjectileManager;
import common.net.Client;
import common.net.Network;
import common.net.Packet;
import common.net.Protocol;
import common.net.ProtocolCmd;
import common.net.Server;
import java.awt.Point;
import java.net.InetSocketAddress;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author J.A.F.S
 */
public class ProtocolHandler extends common.net.ProtocolHandler {
    private boolean dataRecv = false;

    /**
     * Constructs an ProtocolHandler object.
     * Overrides every needed method of ProtocolHandler
     * @param net Network
     */
    public ProtocolHandler(Network net) {
        super(net, ProtocolHandler.MODE_CLIENT);
    }

    /**
     * 
     * @param adr
     */
    @Override
    public void m_c_ping(InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.CLIENT_MASTER_PONG);
//        System.out.println("MASTER_CLIENT_PING -> CLIENT_MASTER_PONG");
    }

    /**
     *
     * @param adr
     */
    @Override
    public void m_c_nickchange_ok(InetSocketAddress adr)
    {
        System.out.println("MASTER_CLIENT_NICKCHANGE_OK");
        Main.getGameData().setName(Main.getMainMenu().getSelfName());
        Main.getMainMenu().setSelfName(Main.getMainMenu().getSelfName());
        Main.getMainMenu().enableOptions(true);
    }

    /**
     *
     * @param forcedNick
     * @param adr
     */
    @Override
    public void m_c_forced_nickchange(String forcedNick, InetSocketAddress adr)
    {
        System.out.print("MASTER_CLIENT_FORCED_NICKCHANGE");
        Main.getGameData().setName(forcedNick);
        Main.getMainMenu().setSelfName(forcedNick);
        Main.getMainMenu().enableOptions(true);
        System.out.println(" -> CLIENT_MASTER_FORCED_NICKCHANGE_OK");
        net.send(adr, ProtocolCmd.CLIENT_MASTER_FORCED_NICKCHANGE_OK);
    }

    /**
     *
     * @param type
     * @param adr
     */
    @Override
    public void m_c_newlist(short type, InetSocketAddress adr)
    {
        if(type == Protocol.LIST_TYPE_SERVERLIST){
            Main.clearServerlist();
            System.out.println("MASTER_CLIENT_NEWLIST (server)");
        } else {
            Main.clearClientlist();
            System.out.println("MASTER_CLIENT_NEWLIST (client)");
        }
    }

    /**
     *
     * @param serverAdr
     * @param adr
     */
    @Override
    public void m_c_listentry_server(String serverAdr, InetSocketAddress adr)
    {
        System.out.print("MASTER_CLIENT_LISTENTRY_SERVER");
        Server server = new Server(serverAdr.split(":")[0], Integer.parseInt(serverAdr.split(":")[1]));
        if(server != null){
            Main.addServerToServerlist(server);
            System.out.println(" ("+server.getHostPort()+")");
        }
    }
    
    /**
     *
     * @param clientAdr
     * @param id
     * @param name
     * @param adr
     */
    @Override
    public void m_c_listentry_client(String clientAdr, int id, String name, InetSocketAddress adr)
    {
        System.out.print("MASTER_CLIENT_LISTENTRY_CLIENT");
        Client client = new Client(clientAdr.split(":")[0], Integer.parseInt(clientAdr.split(":")[1]));
        client.setId(id);
        client.getPlayer().setName(name);
        if(client != null){
            Main.addClientToClientlist(client);
            System.out.println(" ("+client.getHost()+":"+ client.getPort() +")");
        }
    }

    /**
     *
     * @param type
     * @param adr
     */
    @Override
    public void m_c_endlist(short type, InetSocketAddress adr)
    {
        if(type == Protocol.LIST_TYPE_SERVERLIST){
            Main.completeServerlist();
            System.out.println("MASTER_CLIENT_ENDLIST (server)");
        } else {
            System.out.println("MASTER_CLIENT_ENDLIST (client)");
            Main.getMainMenu().completeClientlist();
        }
    }

    /**
     *
     * @param i
     * @param id
     * @param adr
     */
    @Override
    public void m_c_auth_reply(int i, int id, InetSocketAddress adr)
    {
        if(i == Protocol.REPLY_SUCCESS){
            Main.getGameData().setSelfId(id);
            Main.getMainMenu().setSelfName(Main.getGameData().getName());
            Main.getMainMenu().enableLobby(true);
//            Main.getMainMenu().appendIncommingMSG(false, -1, id, "Connection established!\n");
            System.out.println("MASTER_CLIENT_AUTH_REPLY success (id="+id+")");
        } else
            System.out.println("MASTER_CLIENT_AUTH_REPLY failure");
    }

    /**
     *
     * @param s
     * @param adr
     */
    @Override
    public void m_c_joinserver_reply(String s, InetSocketAddress adr)
    {
        System.out.println("MASTER_CLIENT_JOINSERVER_REPLY ("+s+")");
    }

    /**
     *
     * @param senderID
     * @param msg
     * @param adr
     */
    @Override
    public void m_c_chat(int senderID, String msg, InetSocketAddress adr)
    {
        System.out.println("MASTER_CLIENT_CHAT senderID="+senderID+",msg="+msg);
        if(msg == null)
            System.out.println(" msg=null");
        if(senderID != -1){
            Main.getMainMenu().appendIncommingMSG(false, senderID, -1, msg);
        }
    }

    /**
     *
     * @param senderID
     * @param recieverID
     * @param msg
     * @param adr
     */
    @Override
    public void m_c_chat_private(int senderID, int recieverID, String msg, InetSocketAddress adr) {
        System.out.println("MASTER_CLIENT_CHAT senderID="+senderID+",receiverID="+recieverID+"msg="+msg);
        Main.getMainMenu().appendIncommingMSG(true, senderID, recieverID, msg);
    }

    /**
     *
     * @param adr
     */
    @Override
    public void m_c_chat_ok(InetSocketAddress adr)
    {
        System.out.println("MASTER_CLIENT_CHAT_OK");
    }

    /**
     *
     * @param adr
     */
    @Override
    public void s_c_ping(InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_PONG);
        System.out.println("SERVER_CLIENT_PING -> CLIENT_SERVER_PONG");
    }

    /**
     *
     * @param i
     * @param adr
     */
    @Override
    public void s_c_clientcount_reply(int i, InetSocketAddress adr)
    {
        System.out.println("SERVER_CLIENT_CLIENTCOUNT "+i+" (clients connected)");
    }

    /**
     *
     * @param id
     * @param adr
     */
    @Override
    public void s_c_clientid_reply(int id, InetSocketAddress adr)
    {
        CPlayer self = new CPlayer();
        self.setId(id);
        self.setName(Main.getGameData().getName());
        Main.getGameData().addPlayer(self);
        Main.getGameData().setSelfId(id);

        net.send(adr, ProtocolCmd.CLIENT_SERVER_ALL_PLAYER_DATA);
        System.out.println("SERVER_CLIENT_CLIENTID_REPLY id="+id+" -> CLIENT_SERVER_ALL_PLAYER_DATA");
    }

    /**
     *
     * @param i
     * @param adr
     */
    @Override
    public void s_c_auth_reply(int i, InetSocketAddress adr)
    {
        if(i == Protocol.REPLY_SUCCESS) {
//            System.out.println("client succesfully listed. (server)");
            System.out.println("SERVER_CLIENT_AUTH_REPLY success (client listed)");
            Main.getMainMenu().enableLobby(false);
            Main.getMainMenu().setVisible(false);
            Main.getScreen().setVisible(true);
            Main.getFrame().setVisible(true);
        }
        else {
//            System.out.println("client failed to be listed. (server)");
            System.out.println("SERVER_CLIENT_AUTH_REPLY failure");
        }
    }

    /**
     *
     * @param adr
     */
    @Override
    public void s_c_request_name(InetSocketAddress adr)
    {
        String n = Main.getGameData().getName();
        net.send(adr, ProtocolCmd.CLIENT_SERVER_REQUEST_NAME_REPLY, argStr(n));
        System.out.println("SERVER_CLIENT_REQUEST_NAME -> CLIENT_SERVER_REQUEST_NAME_REPLY ("+n+")");
    }

    /**
     *
     * @param m
     * @param t
     * @param adr
     */
    @Override
    public void s_c_init(String m, int t, InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_INIT_REPLY, argInt(Protocol.REPLY_SUCCESS));

        Main.getGameData().setMaxPlayers(t);
        boolean result = Main.getGameData().loadMap(m);

        int rep;

        if(result)
            rep = Protocol.REPLY_SUCCESS;
        else
            rep = Protocol.REPLY_FAILURE;

        System.out.println("SERVER_CLIENT_INIT map="+m+",maxPlayer="+t+" -> CLIENT_SERVER_INIT_REPLY ("+rep+")");
    }

    /**
     *
     * @param n
     * @param adr
     */
    @Override
    public void s_c_forced_nickchange(String n, InetSocketAddress adr) {
        Main.getGameData().setName(n);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_FORCED_NICKCHANGE_OK);
        System.out.println("SERVER_CLIENT_FORCED_NICKCHANGE name="+n+" -> CLIENT_SERVER_FORCED_NICKCHANGE_OK");
    }

    /**
     *
     * @param adr
     */
    @Override
    public void s_c_connection_established(InetSocketAddress adr) {
        net.send(Network.MASTERHOST,
                 Network.MASTERPORT,
                 ProtocolCmd.CLIENT_MASTER_LOGOFF);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CONNECTION_ESTABLISHED_OK);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CLIENTID);
        Main.refInGameClientlist();
        System.out.println("SERVER_CLIENT_CONNECTION_ESTABLISHED -> CLIENT_SERVER_CONNECTION_ESTABLISHED_OK, CLIENT_SERVER_CLIENTID");
    }

    /**
     *
     * @param adr
     */
    @Override
    public void s_c_connection_terminated(InetSocketAddress adr) {
        net.setReallyConnected(false);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CONNECTION_TERMINATED_OK);
        System.out.println("SERVER_CLIENT_CONNECTION_TERMINATED -> CLIENT_SERVER_CONNECTION_TERMINATED_OK");
    }

    /**
     *
     * @param name
     * @param id
     * @param team
     * @param adr
     */
    @Override
    public void s_c_player_data(String name, int id, int team, InetSocketAddress adr) {
        CPlayer player = Main.getGameData().getPlayer(id);
        if(player == null) {
            player = new CPlayer();
            player.setId(id);
            player.setTeam(team);
            player.setName(name);
            Main.getGameData().addPlayer(player);
        } else {
            player.setId(id);
            player.setTeam(team);
            player.setName(name);
        }

        net.send(adr, ProtocolCmd.CLIENT_SERVER_PLAYER_DATA_OK);
        System.out.println("SERVER_CLIENT_PLAYER_DATA name="+name+",id="+id+",teamid="+team+" -> CLIENT_SERVER_PLAYER_DATA_OK");
    }

    /**
     *
     * @param id
     * @param team
     * @param wep
     * @param posX
     * @param posY
     * @param dirX
     * @param dirY
     * @param adr
     */
    @Override
    public void s_c_player_info(int id, String name, int health, int kills, int deaths, int team,
            int wep, double posX, double posY, double dirX, double dirY, InetSocketAddress adr) {
        if(!net.isReallyConnected())
            return;

        System.out.println(id + " " + name + " " + health);

        CPlayer c = Main.getGameData().getPlayer(id);

        if(c != null && c.getId() != Main.getGameData().getSelfId()) {
            c.setName(name);
            c.setCurrentWeapon(wep);
            c.setPosition(posX, posY);
            c.setDirection(dirX, dirY);
            c.setTeam(team);
            c.setHealth(health);
            c.setKills(kills);
            c.setDeaths(deaths);
        }
        if(c != null && c.getId() == Main.getGameData().getSelfId()) {
            c.setName(name);
            c.setTeam(team);
            c.setHealth(health);
            c.setKills(kills);
            c.setDeaths(deaths);
        }
        if(c == null && dataRecv) {
            c = new CPlayer();
            c.setName(name);
            c.setCurrentWeapon(wep);
            c.setPosition(posX, posY);
            c.setDirection(dirX, dirY);
            c.setTeam(team);
            c.setHealth(health);
            c.setKills(kills);
            c.setDeaths(deaths);
            Main.getGameData().addPlayer(c);
        }
    }

    @Override
    public void s_c_game_info(long roundTime, long respawnTime, int scoreRed, int scoreBlue,
            InetSocketAddress adr) {
        GameData data = Main.getGameData();
        data.setRoundTime(roundTime);
        data.setRespawnTime(respawnTime);
        data.setScoreRed(scoreRed);
        data.setScoreBlue(scoreBlue);
    }

    /**
     *
     * @param adr
     */
    @Override
    public void s_c_all_player_data_ok(InetSocketAddress adr) {
        net.setReallyConnected(true);
        net.setServer(adr);
        //net.send(adr, ProtocolCmd.CLIENT_SERVER_JOINTEAM, argInt(CPlayer.TEAM_BLUE));
        Main.getScreen().getGameScene().setTeamSelectionVisible(true);
        dataRecv = true;
        Main.refInGameClientlist();
        System.out.println("SERVER_CLIENT_ALL_PLAYER_DATA_OK -> CLIENT_SERVER_JOINTEAM (TEAM_BLUE)");
    }

    /**
     *
     * @param n
     * @param id
     * @param adr
     */
    @Override
    public void s_c_event_player_joined(String n, int id, InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_JOINED_OK);

        CPlayer player = new CPlayer();
        player.setId(id);
        player.setName(n);
        Main.getGameData().addPlayer(player);

        Main.refInGameClientlist();

        Main.getGameData().addGameEvent(new Event(Event.EVENT_SYSTEM, "Player " + n
                + " joined the server."));
    }

    /**
     *
     * @param p
     * @param t
     * @param adr
     */
    @Override
    public void s_c_event_player_team_changed(int p, int t, InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_TEAM_CHANGED_OK);
        GameData g = Main.getGameData();

        CPlayer pl = g.getPlayer(p);
        if(pl != null)
            pl.setTeam(t);
        
        String team = "";
        
        if(t == CPlayer.TEAM_BLUE)
            team = "Blue";
        else
            team = "Red";

        g.addGameEvent(new Event(t, "Player " + g.getPlayer(p).getName() + " joined team " + team));
    }

    /**
     *
     * @param id
     * @param wepId
     * @param dirX
     * @param dirY
     * @param orgX
     * @param orgY
     * @param adr
     */
    @Override
    public void s_c_event_player_shot(int id, int wepId, double dirX, double dirY,
            double orgX, double orgY, InetSocketAddress adr) {
        
        CWeapon wep = Main.getGameData().getSelf().getWeapon(wepId);
        if(wep != null && id != Main.getGameData().getSelfId()) {
            CVector2 dir = new CVector2(dirX, dirY);
            CVector2 org = new CVector2(orgX, orgY);
            CProjectile c = new CProjectile(id, wep.getSpeed(), wepId, dir, org);
            ProjectileManager.addProjectile(c);
        }
    }

    /**
     *
     * @param who
     * @param by
     * @param adr
     */
    @Override
    public void s_c_event_player_killed(int who, int by, InetSocketAddress adr) {
        net.send(ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_KILLED_OK);

        GameData g = Main.getGameData();

        CPlayer p = g.getPlayer(by);

        if(p != null) {
            int t = g.getPlayer(by).getTeam();

            g.addGameEvent(new Event(t, "" + g.getPlayer(who).getName() + " got marked by " +
                    g.getPlayer(by).getName()));
        }
    }

    // --- chat fkt
    /**
     *
     * @param id
     * @param msg
     * @param adr
     */
    @Override
    public void s_c_chat_all(int id, String msg, InetSocketAddress adr)
    {
        //Main.getUiInGameChat().appendMSG(Main.getUiInGameChat().getClientName(id)+": "+msg);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CHAT_ALL_OK);

        GameData g = Main.getGameData();

        g.addChatEvent(new Event(Event.EVENT_SYSTEM, "" + g.getPlayer(id).getName()
                + ": " + msg));
        System.out.println(msg);
    }

    /**
     *
     * @param adr
     */
    @Override
    public void s_c_chat_all_ok(InetSocketAddress adr) {
        System.out.println("SERVER_CLIENT_CHAT_ALL_OK");
    }

    /**
     *
     * @param id
     * @param msg
     * @param adr
     */
    @Override
    public void s_c_chat_team(int id, String msg, InetSocketAddress adr)
    {
        //Main.getUiInGameChat().appendMSG("(TEAM) "+Main.getClientName(id)+": "+msg);
        //System.out.println("SERVER_CLIENT_CHAT_TEAM id="+id+",msg="+msg);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CHAT_TEAM_OK);

        GameData g = Main.getGameData();

        g.addChatEvent(new Event(g.getPlayer(id).getTeam(), "" + g.getPlayer(id).getName()
                +": " + msg));
    }

    /**
     *
     * @param id
     * @param msg
     * @param adr
     */
    @Override
    public void s_c_chat_private(int id, String msg, InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CHAT_PRIVATE_OK);

        GameData g = Main.getGameData();

        g.addChatEvent(new Event(g.getPlayer(id).getTeam(), "(Private) " + g.getPlayer(id).getName()
                +": " + msg));
    }

    /**
     *
     * @param adr
     */
    @Override
    public void s_c_chat_private_ok(InetSocketAddress adr) {
        System.out.println("SERVER_CLIENT_CHAT_PRIVATE_OK");
    }

    // --- end chat
    /**
     *
     * @param reply
     * @param adr
     */
    @Override
    public void s_c_logoff_reply(int reply, InetSocketAddress adr)
    {
        if(reply == Protocol.REPLY_SUCCESS)
            System.out.println("SERVER_CLIENT_LOGOFF_REPLY success");
        else
            System.out.println("SERVER_CLIENT_LOGOFF_REPLY failure");
    }

    /**
     *
     * @param reply
     * @param team
     * @param adr
     */
    @Override
    public void s_c_jointeam_reply(int reply, int team, InetSocketAddress adr)
    {
        if(reply == Protocol.REPLY_SUCCESS) {
            Main.getGameData().getSelf().setTeam(team);
            System.out.println("SERVER_CLIENT_JOINTEAM_REPLY success (team="+team+")");
        } else {
            System.out.println("SERVER_CLIENT_JOINTEAM_REPLY failure");
            Main.getGameData().addGameEvent(new Event(Event.EVENT_SYSTEM, "Unable to change team."));
        }
    }

    /**
     *
     * @param adr
     */
    @Override
    public void s_c_event_player_respawned(InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK);

        CMap map = Main.getGameData().getMap();
        CPlayer self = Main.getGameData().getSelf();

        System.out.println("SERVER_CLIENT_PLAYER_RESPAWNED -> CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK");

        if(!self.isDead())
            return;

        self.moveToSpawn(map);
        Main.getScreen().getGameScene().setLockStatsVisible(false);
    }

    @Override
    public void s_c_event_team_won(int teamId, int bestPlayerId, InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_TEAM_WON_OK);

        String team = "";
        teamId++;

        if(teamId == CPlayer.TEAM_BLUE)
            team = "Blue team is victorious.";
        else if(teamId == CPlayer.TEAM_RED)
            team = "Red team is victorious.";
        else
            team = "Rounds ends in a draw.";

        Main.getGameData().addGameEvent(new Event(Event.EVENT_SYSTEM, team));
        Main.getScreen().getGameScene().setLockStatsVisible(true);
    }

    @Override
    public void s_c_event_player_left(int id, InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_LEFT_OK);

        GameData g = Main.getGameData();
        g.addGameEvent(new Event(Event.EVENT_SYSTEM, "" + g.getPlayer(id).getName()
                + " left the server."));
        g.removePlayer(g.getPlayer(id));
    }

    /**
     *
     * @param adr
     */
    @Override
    public void s_c_latency_reply(InetSocketAddress adr)
    {
        Main.refreshLatency(adr, System.nanoTime());
        System.out.println("SERVER_CLIENT_LATENCY_REPLY");
    }


    /**
     *
     * @param name
     * @param map
     * @param curPlayers
     * @param maxPlayers
     * @param highscore
     * @param adr
     */
    @Override
    public void s_c_request_server_info_reply(String name, String map, int curPlayers, int maxPlayers, int highscore, InetSocketAddress adr){
        System.out.println("SERVER_CLIENT_REQUEST_SERVER_INFO_REPLY");
        Main.refreshServerInfo(name, map, curPlayers+"/"+maxPlayers, highscore, adr);
    }
    /**
     *
     * @param p
     */
    public void noReplyReceived(Packet p)
    {
        System.out.println("noReplyReceived -> " + Protocol.getCmdById(p.getCmd()));
        if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.CLIENT_MASTER_AUTH) {
            Main.getMainMenu().appendIncommingMSG(false, -2, -1, "Verbindung konnte nicht hergestellt werden!");
        }
        else if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.CLIENT_MASTER_CHAT_LOBBY ||
                Protocol.getCmdById(p.getCmd()) == ProtocolCmd.CLIENT_MASTER_CHAT_PRIVATE) {
            Main.getMainMenu().appendIncommingMSG(false, -2, -1, "Verbindung zum Masterserver verloren!");
        }
    }
}
