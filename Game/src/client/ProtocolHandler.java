package client;

import common.engine.CMap;
import common.engine.CPlayer;
import common.net.Network;
import common.net.Packet;
import common.net.Protocol;
import common.net.ProtocolCmd;
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

    public void m_c_newlist(InetSocketAddress adr)
    {
        Main.serverlist.clear();
        System.out.println("NewServerList:");
    }

    public void m_c_listentry(String server, InetSocketAddress adr)
    {
        Main.serverlist.add(server);
        System.out.println(server);
    }

    public void m_c_endlist(InetSocketAddress adr)
    {
        Main.completeServerlist(Main.serverlist);
        System.out.println(":NewServerList");
    }

    public void m_c_auth_reply(int i, InetSocketAddress adr)
    {
        if(i == Protocol.REPLY_SUCCESS)
            System.out.println("client succesfully listed. (master)");
        else
            System.out.println("client failed to be listed. (master)");
    }

    public void m_c_joinserver_reply(String s, InetSocketAddress adr)
    {
        
    }

    public void m_c_chat(int id, String msg, InetSocketAddress adr)
    {
        if(msg == null)
            System.out.println("NULL!");
        if(id != -1){
            Main.getUiLobbyChat().appendMSG("Player-"+id+": "+msg.replace("vXv", ";"));
            Main.getUiLobbyChat().clearMsgField();
//            System.out.println("Player-"+id+": "+msg.replace("vXv", ";"));
        }
    }

    public void m_c_chat_ok (InetSocketAddress adr)
    {

    }

    public void s_c_ping(InetSocketAddress adr)
    {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_PONG);
//        System.out.println("Client ponged");
    }

    public void s_c_clientcount(Integer i, InetSocketAddress adr)
    {
        System.out.println(i+ "clients connected on this server");
    }

    public void s_c_clientid_reply(Integer id, InetSocketAddress adr)
    {
        CPlayer self = new CPlayer();
        self.setId(id);
        self.setName(Main.getGameData().getName());
        Main.getGameData().addPlayer(self);
        Main.getGameData().setSelfId(id);

        net.send(adr, ProtocolCmd.CLIENT_SERVER_ALL_PLAYER_DATA);
    }

    public void s_c_auth_reply(Integer i, InetSocketAddress adr)
    {
        if(i == Protocol.REPLY_SUCCESS)
            System.out.println("client succesfully listed. (master)");
        else
            System.out.println("client failed to be listed. (master)");
    }

    public void s_c_request_name(InetSocketAddress adr)
    {
        String n = Main.getGameData().getName();
        net.send(adr, ProtocolCmd.CLIENT_SERVER_REQUEST_NAME_REPLY, argStr(n));
    }

    public void s_c_init(String m, Integer t, InetSocketAddress adr) {
        Main.getGameData().setMaxPlayers(t);
        boolean result = Main.getGameData().loadMap(m);

        int rep;

        if(result)
            rep = Protocol.REPLY_SUCCESS;
        else
            rep = Protocol.REPLY_FAILURE;

        net.send(adr, ProtocolCmd.CLIENT_SERVER_INIT_REPLY, argInt(rep));
    }

    public void s_c_forced_nickchange(String n, InetSocketAddress adr) {
        Main.getGameData().setName(n);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_FORCED_NICKCHANGE_OK);
    }

    public void s_c_connection_established(InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CONNECTION_ESTABLISHED_OK);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CLIENTID);
    }

    public void s_c_connection_terminated(InetSocketAddress adr) {
        net.setReallyConnected(false);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_CONNECTION_TERMINATED_OK);
    }

    public void s_c_player_data(String name, int id, Integer team, InetSocketAddress adr) {
        if(Main.getGameData().getPlayer(id) == null) {
            CPlayer player = new CPlayer();
            player.setId(id);
            player.setTeam(team);
            player.setName(name);
            Main.getGameData().addPlayer(player);
        }

        net.send(adr, ProtocolCmd.CLIENT_SERVER_PLAYER_DATA_OK);
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
        }
    }

    public void s_c_all_player_data_ok(InetSocketAddress adr) {
        net.setReallyConnected(true);
        net.setServer(adr);
        net.send(adr, ProtocolCmd.CLIENT_SERVER_JOINTEAM, argInt(CPlayer.TEAM_BLUE));
    }

    public void s_c_event_player_joined(String n, InetSocketAddress adr) {
        System.out.println("Player " + n + " joined.");
        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_JOINED_OK);
    }

    // --- chat fkt
    public void s_c_chat_all(int id, String msg, InetSocketAddress adr)
    {
        Main.getUiLobbyChat().appendMSG(msg); // LOBBY
        System.out.println("CHAT: "+msg);
    }

    public void s_c_chat_team(int id, String msg, InetSocketAddress adr)
    {
        Main.getUiLobbyChat().appendMSG(msg); // LOBBY
        System.out.println("CHAT: "+msg);
    }

    public void s_c_chat_private(int id, String msg, InetSocketAddress adr)
    {
        Main.getUiLobbyChat().appendMSG(msg); // LOBBY
        System.out.println("CHAT: "+msg);
    }

    // --- end chat
    public void s_c_logoff_reply(int reply, InetSocketAddress adr)
    {
        if(reply == Protocol.REPLY_SUCCESS)
            System.out.println("you have been succesfully logged off.");
        else
            System.out.println("log off failed");
    }

    public void s_c_jointeam_reply(int reply, int team, InetSocketAddress adr)
    {
        if(reply == Protocol.REPLY_SUCCESS) {
            Main.getGameData().getSelf().setTeam(team);
            System.out.println("joined team " + team);
        } else
            System.out.println("join team failed");
    }

    public void s_c_event_player_respawned(InetSocketAddress adr) {
        CMap map = Main.getGameData().getMap();
        CPlayer self = Main.getGameData().getSelf();

        net.send(adr, ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK);

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
    }

    public void noReplyReceived(Packet p)
    {

    }
}
