package server;

import common.net.Client;
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

    public void m_s_ping(InetSocketAddress adr) {
        net.send(adr, Protocol.SERVER_MASTER_PONG, new Object[0]);
    }

    public void m_s_servercount(Integer i, InetSocketAddress adr){
        Main.setServerId(i);
    }

    public void m_s_auth_reply(Integer i, InetSocketAddress adr) {
        if(i == Protocol.REPLY_SUCCESS)
            System.out.println("server succesfully listed.");
        else
            System.out.println("server failed to be listed.");
    }

    public void c_s_auth(InetSocketAddress adr){
        Client added = Main.addClient(adr);

        if(added != null) {
            net.send(adr, Protocol.SERVER_CLIENT_AUTH_REPLY, Protocol.REPLY_SUCCESS);
            net.send(adr, Protocol.SERVER_CLIENT_INIT, Main.getMapName(), Main.getMaxPlayers());
        } else {
            net.send(adr, Protocol.SERVER_CLIENT_AUTH_REPLY, Protocol.REPLY_FAILURE);
        }
    }

    public void c_s_init_reply(Integer i, InetSocketAddress adr) {
        if(i == Protocol.REPLY_SUCCESS) {
            net.send(adr, Protocol.SERVER_CLIENT_REQUEST_NAME);
        } else {
            Main.removeClient(adr);
        }
    }

    public void c_s_request_name_reply(String name, InetSocketAddress adr) {
        boolean changed = false;

        while(Main.nameExists(name)) {
            name = name.concat("*");
            changed = true;
        }

        if(changed) {
            Main.getClient(adr).getPlayer().setName(name);
            net.send(adr, Protocol.SERVER_CLIENT_FORCED_NICKCHANGE, name);
        }

        Main.getClient(adr).setStatus(Client.STATUS_CONNECTED);
        net.send(adr, Protocol.SERVER_CLIENT_CONNECTION_ESTABLISHED);
    }

    public void c_s_connection_established_ok(InetSocketAddress adr) {

    }

    public void c_s_forced_nickchange_ok(InetSocketAddress adr) {

    }

    public void c_s_request_server_info(InetSocketAddress adr) {
        String name = Main.getServerName();
        String map = Main.getMapName();
        int cur = Main.getCurPlayers();
        int max = Main.getMaxPlayers();

        net.send(adr, Protocol.SERVER_CLIENT_REQUEST_SERVER_INFO_REPLY, name, map, cur, max);
    }

    public void c_s_all_player_data(InetSocketAddress adr) {
        Client[] c = Main.getClients();

        for(Client client : c) {
            if(!client.getAddress().equals(adr)) {
                String name = client.getPlayer().getName();
                int team = client.getTeamId();
                int id = client.getId();

                net.send(adr, Protocol.SERVER_CLIENT_PLAYER_DATA, name, id, team);
            }
        }

        net.send(adr, Protocol.SERVER_CLIENT_ALL_PLAYER_DATA_OK);
    }

    public void c_s_player_info(Integer id, Integer wep, Double posX, Double posY,
            Double dirX, Double dirY, InetSocketAddress adr) {
        Client c = Main.getClient(id);

        if(c != null) {
            c.getPlayer().setCurrentWeapon(wep);
            c.getPlayer().setPosition(posX, posY);
            c.getPlayer().setDirection(dirX, dirY);
        }
    }

    public void c_s_player_data_ok(InetSocketAddress adr) {
        
    }

    public void c_s_pong(InetSocketAddress adr) {
;
    }

    public void c_s_clientcount(InetSocketAddress adr){
        net.send(adr, Protocol.SERVER_CLIENT_CLIENTCOUNT, Main.clientCount());
    }

    public void c_s_clientid(InetSocketAddress adr){
        net.send(adr, Protocol.SERVER_CLIENT_CLIENTID_REPLY, Main.getClient(adr).getId());
    }

    public void c_s_logoff(InetSocketAddress adr){
        Main.removeClient(adr);
        net.send(adr, Protocol.SERVER_CLIENT_LOGOFF_REPLY, Protocol.REPLY_SUCCESS);
    }

    // --- chat fkt
    public void c_s_chat_all(String msg, InetSocketAddress adr){
        Main.broadcast_chat(msg, adr);
//        net.send(adr, Protocol.server_client_chat, "(PUBLiC-CHAT) Player-"+Main.getClientId(adr)+" ("+adr.getHostName()+":"+adr.getPort()+"): "+msg);
    }

    // team wird aus der clientlist geholt, muss nicht übergeben werden
    public void c_s_chat_team(String msg, InetSocketAddress adr){
        Main.broadcast_chat_team(msg, adr);
    }
    
    public void c_s_chat_private(String msg, Integer to, InetSocketAddress adr){
        Main.broadcast_chat_private(msg, to, adr);
    }

    // --- chat end
    public void c_s_jointeam(Integer teamId, InetSocketAddress adr){
        if(Main.setClientTeamId(adr, teamId) == -1)
            net.send(adr, Protocol.SERVER_CLIENT_JOINTEAM_REPLY, Protocol.REPLY_FAILURE);
        else {
            Client c = Main.getClient(adr);

            if(c != null) {
                c.setTeamId(teamId);
                net.send(adr, Protocol.SERVER_CLIENT_JOINTEAM_REPLY, Protocol.REPLY_SUCCESS, teamId);
                Main.broadcast(Protocol.SERVER_CLIENT_EVENT_PLAYER_JOINED, c.getId());
            }
        }
    }

    public void c_s_event_player_joined_ok(InetSocketAddress adr) {
        
    }

    public void noReplyReceived(Packet p) {
        if(p.getCmd().equals(Protocol.SERVER_CLIENT_PING)) {
            Main.removeClient(p.getAddress());
        }
    }
}
