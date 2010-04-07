package client;

import common.engine.CPlayer;
import common.net.Network;
import common.net.Packet;
import common.net.Protocol;
import java.net.InetSocketAddress;

/**
 *
 * @author miracle
 */
public class ProtocolHandler extends common.net.ProtocolHandler {
    public ProtocolHandler(Network net) {
        super(net);
    }

    public void s_c_ping(InetSocketAddress adr)
    {
        net.send(adr, Protocol.CLIENT_SERVER_PONG);
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

        net.send(adr, Protocol.CLIENT_SERVER_ALL_PLAYER_DATA);
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

    public void m_c_auth_reply(Integer i, InetSocketAddress adr)
    {
        if(i == Protocol.REPLY_SUCCESS)
            System.out.println("client succesfully listed. (master)");
        else
            System.out.println("client failed to be listed. (master)");
    }

    public void m_c_joinserver_reply(String s, InetSocketAddress adr)
    {
        System.out.println(s);
    }

    public void m_c_chat(String msg, InetSocketAddress adr)
    {
        Main.getUiLobbyChat().appendMSG(msg.replace("vXv", ";"));
        Main.getUiLobbyChat().clearMsgField();
        System.out.println(msg.replace("vXv", ";"));
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
        net.send(adr, Protocol.CLIENT_SERVER_REQUEST_NAME_REPLY, n);
    }

    public void c_s_init(String m, Integer t, InetSocketAddress adr) {
        Main.getGameData().setMaxPlayers(t);
        boolean result = Main.getGameData().loadMap(m);

        int rep;

        if(result)
            rep = Protocol.REPLY_SUCCESS;
        else
            rep = Protocol.REPLY_FAILURE;

        net.send(adr, Protocol.CLIENT_SERVER_INIT_REPLY, rep);
    }

    public void s_c_forced_nickchange(String n, InetSocketAddress adr) {
        Main.getGameData().setName(n);
    }

    public void s_c_connection_established(InetSocketAddress adr) {
        net.setReallyConnected(true);
        net.send(adr, Protocol.CLIENT_SERVER_CONNECTION_ESTABLISHED_OK);
        net.send(adr, Protocol.CLIENT_SERVER_CLIENTID);
    }

    public void s_c_connection_terminated(InetSocketAddress adr) {
        net.setReallyConnected(false);
        net.send(adr, Protocol.CLIENT_SERVER_CONNECTION_TERMINATED_OK);
    }

    public void s_c_player_data(String name, Integer id, Integer team, InetSocketAddress adr) {
        if(Main.getGameData().getPlayer(id) == null) {
            CPlayer player = new CPlayer();
            player.setId(id);
            player.setTeam(team);
            player.setName(name);
            Main.getGameData().addPlayer(player);
        }
    }

    public void s_c_player_info(Integer id, Integer wep, Double posX, Double posY,
            Double dirX, Double dirY) {
        CPlayer c = Main.getGameData().getPlayer(id);

        if(c != null && c.getId() == Main.getGameData().getSelfId()) {
            c.setCurrentWeapon(wep);
            c.setPosition(posX, posY);
            c.setDirection(dirX, dirY);
        }
    }

    public void s_c_all_player_data_ok(InetSocketAddress adr) {
        
    }

    // --- chat fkt
    public void s_c_chat_all(Integer id, String msg, InetSocketAddress adr)
    {
        Main.getUiLobbyChat().appendMSG(msg); // LOBBY
        System.out.println("CHAT: "+msg);
    }

    public void s_c_chat_team(Integer id, String msg, InetSocketAddress adr)
    {
        Main.getUiLobbyChat().appendMSG(msg); // LOBBY
        System.out.println("CHAT: "+msg);
    }

    public void s_c_chat_private(Integer id, String msg, InetSocketAddress adr)
    {
        Main.getUiLobbyChat().appendMSG(msg); // LOBBY
        System.out.println("CHAT: "+msg);
    }

    // --- end chat
    public void s_c_logoff_reply(Integer reply, InetSocketAddress adr)
    {
        if(reply == Protocol.REPLY_SUCCESS)
            System.out.println("you have been succesfully logged off.");
        else
            System.out.println("log off failed");
    }

    public void s_c_jointeam_reply(Integer reply, InetSocketAddress adr)
    {
        if(reply == Protocol.REPLY_SUCCESS)
            System.out.println("you have been succesfully join team");
        else
            System.out.println("join team failed");
    }

    public void noReplyReceived(Packet p)
    {

    }
}
