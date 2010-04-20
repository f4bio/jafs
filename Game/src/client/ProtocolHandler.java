package client;

import common.engine.CMap;
import common.engine.CPlayer;
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
 * @author miracle
 */
public class ProtocolHandler extends common.net.ProtocolHandler {

    public ProtocolHandler(Network net) {
        super(net, ProtocolHandler.MODE_CLIENT);
    }

    public void m_c_ping(InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.CLIENT_MASTER_PONG);
//        System.out.println("MASTER_CLIENT_PING -> CLIENT_MASTER_PONG");
    }

    public void m_c_nickchange_ok(InetSocketAddress adr)
    {
        System.out.println("MASTER_CLIENT_NICKCHANGE_OK");
        Main.getGameData().setName(Main.getMainMenu().getPlayerName());
        Main.getMainMenu().enableOptions(true);
    }

    public void m_c_forced_nickchange(String forcedNick, InetSocketAddress adr)
    {
        System.out.print("MASTER_CLIENT_FORCED_NICKCHANGE");
        Main.getGameData().setName(forcedNick);
        Main.getMainMenu().setPlayerName(forcedNick);
        Main.getMainMenu().enableOptions(true);
        System.out.println(" -> CLIENT_MASTER_FORCED_NICKCHANGE_OK");
        net.send(adr, ProtocolCmd.CLIENT_MASTER_FORCED_NICKCHANGE_OK);
    }

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

    public void m_c_listentry_server(String serverAdr, InetSocketAddress adr)
    {
        System.out.print("MASTER_CLIENT_LISTENTRY_SERVER");
        Server server = new Server(serverAdr.split(":")[0], Integer.parseInt(serverAdr.split(":")[1]));
        if(server != null){
            Main.addServerToServerlist(server);
            System.out.println(" ("+server.getHostPort()+")");
        }
    }
    
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

    public void m_c_endlist(short type, InetSocketAddress adr)
    {
        if(type == Protocol.LIST_TYPE_SERVERLIST){
            Main.completeServerlist();
            System.out.println("MASTER_CLIENT_ENDLIST (server)");
        } else {
            // complete clientlist
            System.out.println("MASTER_CLIENT_ENDLIST (client)");
        }
        
    }

    public void m_c_auth_reply(int i, int id, InetSocketAddress adr)
    {
        if(i == Protocol.REPLY_SUCCESS){
//            net.send(Network.MASTERHOST,
//                     Network.MASTERPORT,
//                     ProtocolCmd.CLIENT_MASTER_LISTREQUEST,
//                     argShort(Protocol.LIST_TYPE_CLIENTLIST));
            Main.getGameData().setSelfId(id);
            Main.getMainMenu().enableLobby(true);
//            Main.getMainMenu().appendIncommingMSG(false, -1, id, "Connection established!\n");
            System.out.println("MASTER_CLIENT_AUTH_REPLY success (id="+id+")");
        } else
            System.out.println("MASTER_CLIENT_AUTH_REPLY failure");
    }

    public void m_c_joinserver_reply(String s, InetSocketAddress adr)
    {
        System.out.println("MASTER_CLIENT_JOINSERVER_REPLY ("+s+")");
    }

    public void m_c_chat(int senderID, String msg, InetSocketAddress adr)
    {
        System.out.println("MASTER_CLIENT_CHAT senderID="+senderID+",msg="+msg);
        if(msg == null)
            System.out.println(" msg=null");
        if(senderID != -1){
            Main.getMainMenu().appendIncommingMSG(false, senderID, -1, msg);
        }
    }

    public void m_c_chat_private(int senderID, int recieverID, String msg, InetSocketAddress adr) {
        System.out.println("MASTER_CLIENT_CHAT senderID="+senderID+",receiverID="+recieverID+"msg="+msg);
        Main.getMainMenu().appendIncommingMSG(true, senderID, recieverID, msg);
    }

    public void m_c_chat_ok (InetSocketAddress adr)
    {
        System.out.println("MASTER_CLIENT_CHAT_OK");
    }

    public void s_c_ping(InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_PONG);
        System.out.println("SERVER_CLIENT_PING -> CLIENT_SERVER_PONG");
    }

    public void s_c_clientcount(int i, InetSocketAddress adr)
    {
        System.out.println("SERVER_CLIENT_CLIENTCOUNT "+i+" (clients connected)");
    }

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

    public void s_c_auth_reply(int i, InetSocketAddress adr)
    {
        if(i == Protocol.REPLY_SUCCESS) {
//            System.out.println("client succesfully listed. (server)");
            System.out.println("SERVER_CLIENT_AUTH_REPLY success (client listed)");
        }
        else {
//            System.out.println("client failed to be listed. (server)");
            System.out.println("SERVER_CLIENT_AUTH_REPLY failure");
        }
    }

    public void s_c_request_name(InetSocketAddress adr)
    {
        String n = Main.getGameData().getName();
        net.send(adr, ProtocolCmd.CLIENT_SERVER_REQUEST_NAME_REPLY, argStr(n));
        System.out.println("SERVER_CLIENT_REQUEST_NAME -> CLIENT_SERVER_REQUEST_NAME_REPLY ("+n+")");
    }

    public void s_c_init(String m, int t, InetSocketAddress adr) {
        Main.getGameData().setMaxPlayers(t);
        boolean result = Main.getGameData().loadMap(m);

        int rep;

        if(result)
            rep = Protocol.REPLY_SUCCESS;
        else
            rep = Protocol.REPLY_FAILURE;

        net.send(adr, ProtocolCmd.CLIENT_SERVER_INIT_REPLY, argInt(rep));
        System.out.println("SERVER_CLIENT_INIT map="+m+",maxPlayer="+t+" -> CLIENT_SERVER_INIT_REPLY ("+rep+")");
    }

    public void s_c_forced_nickchange(String n, InetSocketAddress adr) {
        Main.getGameData().setName(n);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_FORCED_NICKCHANGE_OK);
        System.out.println("SERVER_CLIENT_FORCED_NICKCHANGE name="+n+" -> CLIENT_SERVER_FORCED_NICKCHANGE_OK");
    }

    public void s_c_connection_established(InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CONNECTION_ESTABLISHED_OK);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CLIENTID);
        System.out.println("SERVER_CLIENT_CONNECTION_ESTABLISHED -> CLIENT_SERVER_CONNECTION_ESTABLISHED_OK, CLIENT_SERVER_CLIENTID");
    }

    public void s_c_connection_terminated(InetSocketAddress adr) {
        net.setReallyConnected(false);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CONNECTION_TERMINATED_OK);
        System.out.println("SERVER_CLIENT_CONNECTION_TERMINATED -> CLIENT_SERVER_CONNECTION_TERMINATED_OK");
    }

    public void s_c_player_data(String name, int id, int team, InetSocketAddress adr) {
        if(Main.getGameData().getPlayer(id) == null) {
            CPlayer player = new CPlayer();
            player.setId(id);
            player.setTeam(team);
            player.setName(name);
            Main.getGameData().addPlayer(player);
        }

        net.send(adr, ProtocolCmd.CLIENT_SERVER_PLAYER_DATA_OK);
        System.out.println("SERVER_CLIENT_PLAYER_DATA name="+name+",id="+id+",teamid="+team+" -> CLIENT_SERVER_PLAYER_DATA_OK");
    }

    public void s_c_player_info(int id, int wep, double posX, double posY,
                                double dirX, double dirY, InetSocketAddress adr) {
        if(!net.isReallyConnected())
            return;

        CPlayer c = Main.getGameData().getPlayer(id);

        if(c != null && c.getId() != Main.getGameData().getSelfId()) {
            c.setCurrentWeapon(wep);
            c.setPosition(posX, posY);
            c.setDirection(dirX, dirY);
//            System.out.println("SERVER_CLIENT_PLAYER_INFO id="+id+",weapon="+wep+",posX="+posX+",posY="+posY+",dirX="+dirX+"dirY="+dirY);
        }
    }

    public void s_c_all_player_data_ok(InetSocketAddress adr) {
        net.setReallyConnected(true);
        net.setServer(adr);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_JOINTEAM, argInt(CPlayer.TEAM_BLUE));
        System.out.println("SERVER_CLIENT_ALL_PLAYER_DATA_OK -> CLIENT_SERVER_JOINTEAM (TEAM_BLUE)");
    }

    public void s_c_event_player_joined(String n, int id, InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_JOINED_OK);

        CPlayer player = new CPlayer();
        player.setId(id);
        Main.getGameData().addPlayer(player);

        System.out.println("SERVER_CLIENT_EVENT_PLAYER_JOINED name="+n+" -> CLIENT_SERVER_EVENT_PLAYER_JOINED_OK");
    }

    public void s_c_event_player_team_changed(int p, int t, InetSocketAddress adr) {
        CPlayer pl = Main.getGameData().getPlayer(p);
        if(pl != null)
            pl.setTeam(t);

        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_TEAM_CHANGED_OK);
    }

    // --- chat fkt
    public void s_c_chat_all(int id, String msg, InetSocketAddress adr)
    {
        System.out.println("SERVER_CLIENT_CHAT_ALL id="+id+",msg="+msg);
        Main.getUiInGameChat().appendMSG(Main.getClientName(id)+": "+msg);
    }

    public void s_c_chat_all_ok(InetSocketAddress adr) {
        System.out.println("SERVER_CLIENT_CHAT_ALL_OK");
    }

    public void s_c_chat_team(int id, String msg, InetSocketAddress adr)
    {
        Main.getUiInGameChat().appendMSG("(TEAM)"+Main.getClientName(id)+": "+msg);
        System.out.println("SERVER_CLIENT_CHAT_TEAM id="+id+",msg="+msg);
    }

    public void s_c_chat_private(int id, String msg, InetSocketAddress adr)
    {
        Main.getUiInGameChat().appendMSG(msg); // LOBBY
        System.out.println("SERVER_CLIENT_CHAT_PRIVATE id="+id+",msg="+msg);
    }

    // --- end chat
    public void s_c_logoff_reply(int reply, InetSocketAddress adr)
    {
        if(reply == Protocol.REPLY_SUCCESS)
            System.out.println("SERVER_CLIENT_LOGOFF_REPLY success");
        else
            System.out.println("SERVER_CLIENT_LOGOFF_REPLY failure");
    }

    public void s_c_jointeam_reply(int reply, int team, InetSocketAddress adr)
    {
        if(reply == Protocol.REPLY_SUCCESS) {
            Main.getGameData().getSelf().setTeam(team);
            System.out.println("SERVER_CLIENT_JOINTEAM_REPLY success (team="+team+")");
        } else
            System.out.println("SERVER_CLIENT_JOINTEAM_REPLY failure");
    }

    public void s_c_event_player_respawned(InetSocketAddress adr) {
        CMap map = Main.getGameData().getMap();
        CPlayer self = Main.getGameData().getSelf();

        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK);
        System.out.println("SERVER_CLIENT_PLAYER_RESPAWNED -> CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK");

        if(!self.isDead())
            return;

        Point p = (self.getTeam() == CPlayer.TEAM_BLUE) ? map.getSpawnBlue() :
            map.getSpawnRed();
        int x = p.x * map.getTileSize().width + map.getTileSize().width/2;
        int y = p.y * map.getTileSize().height + map.getTileSize().height/2;

        Main.getGameData().getSelf().setPosition(x, y);
    }

    public void s_c_latency_reply(InetSocketAddress adr)
    {
        Main.refreshLatency(adr, System.nanoTime());
        System.out.println("SERVER_CLIENT_LATENCY_REPLY");
    }

    public void s_c_current_map_reply(String map, InetSocketAddress adr)
    {
        Main.refreshCurrentMap(adr, map);
        System.out.println("SERVER_CLIENT_CURRENT_MAP_REPLY");
    }

    public void s_c_players_reply(String players, InetSocketAddress adr)
    {
        Main.refreshPlayers(adr, players);
        System.out.println("SERVER_CLIENT_PLAYERS_REPLY");
    }

    public void noReplyReceived(Packet p)
    {

    }
}
