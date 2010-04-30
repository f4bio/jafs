package server;

import common.CVector2;
import common.engine.CProjectile;
import common.engine.CWeapon;
import common.engine.ProjectileManager;
import common.net.Client;
import common.net.Network;
import common.net.Packet;
import common.net.Protocol;
import common.net.ProtocolCmd;
import java.net.InetSocketAddress;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public class ProtocolHandler extends common.net.ProtocolHandler {
    /**
     *
     * @param net
     */
    public ProtocolHandler(Network net) {
        super(net, ProtocolHandler.MODE_SERVER);
    }

    /**
     *
     * @param adr
     */
    @Override
    public void m_s_ping(InetSocketAddress adr) {
        net.send(adr, ProtocolCmd.SERVER_MASTER_PONG);
    }

    /**
     *
     * @param i
     * @param adr
     */
    @Override
    public void m_s_servercount(int i, InetSocketAddress adr){
        Main.setServerId(i);
    }

    /**
     *
     * @param i
     * @param adr
     */
    @Override
    public void m_s_auth_reply(int i, InetSocketAddress adr) {
        if(i == Protocol.REPLY_SUCCESS)
            System.out.println("MASTER_SERVER_AUTH_REPLY success, server listed");
//            System.out.println("server succesfully listed.");
        else
            System.out.println("MASTER_SERVER_AUTH_REPLY failure");
//            System.out.println("server failed to be listed.");
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_auth(InetSocketAddress adr){
        Client added = Main.addClient(adr);

        if(added != null) {
            net.send(adr, ProtocolCmd.SERVER_CLIENT_AUTH_REPLY, argInt(Protocol.REPLY_SUCCESS));
            net.send(adr, ProtocolCmd.SERVER_CLIENT_INIT, argStr(Main.getMapName()),
                     argInt(Main.getMaxPlayers()));
//            System.out.println("Client "+added.getHost()+":"+added.getPort()+" joined server");
            System.out.println("CLIENT_SERVER_AUTH success -> SERVER_CLIENT_AUTH_REPLY (REPLY_SUCCESS), SERVER_CLIENT_INIT");
        } else {
            net.send(adr, ProtocolCmd.SERVER_CLIENT_AUTH_REPLY, argInt(Protocol.REPLY_FAILURE));
//            System.out.println("Client "+added.getHost()+":"+added.getPort()+" not able joined server");
            System.out.println("CLIENT_SERVER_AUTH failure -> SERVER_CLIENT_AUTH_REPLY (REPLY_FAILURE)");
        }
    }

    /**
     *
     * @param i
     * @param adr
     */
    @Override
    public void c_s_init_reply(int i, InetSocketAddress adr) {
        if(i == Protocol.REPLY_SUCCESS) {
            net.send(adr, ProtocolCmd.SERVER_CLIENT_REQUEST_NAME);
            System.out.println("CLIENT_SERVER_INIT_REPLY success -> SERVER_CLIENT_REQUEST_NAME");
        } else {
            Main.removeClient(adr);
            System.out.println("CLIENT_SERVER_INIT_REPLY failure -> client removed");
        }
    }

    /**
     *
     * @param name
     * @param adr
     */
    @Override
    public void c_s_request_name_reply(String name, InetSocketAddress adr) {
        boolean changed = false;

        while(Main.nameExists(name)) {
            name = name.concat("*");
            changed = true;
        }

        if(changed)     
            net.send(adr, ProtocolCmd.SERVER_CLIENT_FORCED_NICKCHANGE, argStr(name));

        Client c = Main.getClient(adr);

        c.getPlayer().setName(name);

        c.setStatus(Client.STATUS_CONNECTED);

        int team = c.getTeamId();
        int id = c.getId();

        net.send(adr, ProtocolCmd.SERVER_CLIENT_PLAYER_DATA,
                argStr(name), argInt(id), argInt(team));

        net.send(adr, ProtocolCmd.SERVER_CLIENT_CONNECTION_ESTABLISHED);

        Main.broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_JOINED,
                        argStr(c.getPlayer().getName()), argInt(c.getId()));

        System.out.println("CLIENT_SERVER_REQUEST_NAME_REPLY "+name+"(no SERVER_CLIENT_FORCED_NICKCHANGE) -> SERVER_CLIENT_CONNECTION_ESTABLISHED");
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_connection_established_ok(InetSocketAddress adr) {

    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_forced_nickchange_ok(InetSocketAddress adr) {

    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_request_server_info(String playerName, InetSocketAddress adr) {
        System.out.println("CLIENT_SERVER_REQUEST_SERVER_INFO -> SERVER_CLIENT_REQUEST_SERVER_INFO_REPLY");
        String name = Main.getServerName();
        String map = Main.getMapName();
        int cur = Main.getCurPlayers();
        int max = Main.getMaxPlayers();
        int high = Main.getPlayerHighscore(playerName);
        net.send(adr,
                 ProtocolCmd.SERVER_CLIENT_REQUEST_SERVER_INFO_REPLY,
                 argStr(name), argStr(map), argInt(cur), argInt(max), argInt(high));
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_all_player_data(InetSocketAddress adr) {
        Client[] c = Main.getClients();

        for(Client client : c) {
            if(client != null && !client.getAddress().equals(adr)) {
                String name = client.getPlayer().getName();
                int team = client.getTeamId();
                int id = client.getId();

                net.send(adr,
                         ProtocolCmd.SERVER_CLIENT_PLAYER_DATA,
                         argStr(name), argInt(id), argInt(team));
            }
        }

        net.send(adr, ProtocolCmd.SERVER_CLIENT_ALL_PLAYER_DATA_OK);
    }

    /**
     *
     * @param id
     * @param wep
     * @param posX
     * @param posY
     * @param dirX
     * @param dirY
     * @param adr
     */
    @Override
    public void c_s_player_info(int id, int wep, double posX, double posY,
                                double dirX, double dirY, InetSocketAddress adr) {
        Client c = Main.getClient(id);

        if(c != null) {
            c.getPlayer().setCurrentWeapon(wep);
            c.getPlayer().setPosition(posX, posY);
            c.getPlayer().setDirection(dirX, dirY);
        }
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_player_data_ok(InetSocketAddress adr) {

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
    public void c_s_shoot(int id, int wepId, int dirX, int dirY, double orgX, double orgY,
                          InetSocketAddress adr) {
        CWeapon wep = Main.getGame().getWeapon(id);
        if(wep != null) {
            CVector2 dir = new CVector2(dirX, dirY);
            CVector2 org = new CVector2(orgX, orgY);
            CProjectile c = new CProjectile(id, wep.getSpeed(), wepId, dir, org);
            ProjectileManager.addProjectile(c);
            Main.broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_SHOT,
                           argInt(id), argInt(wepId), argInt(dirX),
                           argInt(dirY), argDouble(orgX), argDouble(orgY));
        }
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_pong(InetSocketAddress adr) {
//        System.out.println("CLIENT_SERVER_PONG");
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_clientcount(InetSocketAddress adr){
        net.send(adr,
                 ProtocolCmd.SERVER_CLIENT_CLIENTCOUNT,
                 argInt(Main.clientCount()));
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_clientid(InetSocketAddress adr){
        net.send(adr,
                 ProtocolCmd.SERVER_CLIENT_CLIENTID_REPLY,
                 argInt(Main.getClient(adr).getId()));
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_logoff(InetSocketAddress adr){
        System.out.println("CLIENT_SERVER_LOGOFF");
        Main.removeClient(adr);
        net.send(adr,
                 ProtocolCmd.SERVER_CLIENT_LOGOFF_REPLY,
                 argInt(Protocol.REPLY_SUCCESS));
    }

    // --- chat fkt
    /**
     *
     * @param msg
     * @param adr
     */
    @Override
    public void c_s_chat_all(String msg, InetSocketAddress adr){
        System.out.println("CLIENT_SERVER_CHAT_ALL (msg="+msg+")-> SERVER_CLIENT_CHAT_ALL_OK");
        net.send(adr, ProtocolCmd.SERVER_CLIENT_CHAT_ALL_OK);
        Main.broadcast_chat(msg, adr);
//        net.send(adr, Protocol.server_client_chat, "(PUBLiC-CHAT) Player-"+Main.getClientId(adr)+" ("+adr.getHostName()+":"+adr.getPort()+"): "+msg);
    }
    
    /**
     *
     * @param adr
     */
    @Override
    public void c_s_chat_all_ok(InetSocketAddress adr) {
        System.out.println("CLIENT_SERVER_CHAT_ALL_OK");
    }

    // team wird aus der clientlist geholt, muss nicht übergeben werden
    /**
     *
     * @param msg
     * @param adr
     */
    @Override
    public void c_s_chat_team(String msg, InetSocketAddress adr){
        Main.broadcast_chat_team(msg, adr);
    }
    
    /**
     *
     * @param to
     * @param msg
     * @param adr
     */
    @Override
    public void c_s_chat_private(int to, String msg, InetSocketAddress adr) {
        System.out.println("CLIENT_SERVER_CHAT_PRIVATE -> SERVER_CLIENT_CHAT_PRIVATE_OK");
        Main.broadcast_chat_private(msg, to, adr);
        net.send(adr, ProtocolCmd.SERVER_CLIENT_CHAT_PRIVATE_OK);
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_chat_private_ok(InetSocketAddress adr) {
        System.out.println("CLIENT_SERVER_CHAT_PRIVATE_OK");
    }

    // --- chat end
    /**
     *
     * @param teamId
     * @param adr
     */
    @Override
    public void c_s_jointeam(int teamId, InetSocketAddress adr){
        if(Main.setClientTeamId(adr, teamId) == -1) {
            net.send(adr,
                     ProtocolCmd.SERVER_CLIENT_JOINTEAM_REPLY,
                     argInt(Protocol.REPLY_FAILURE));
            System.out.println("CLIENT_SERVER_JOINTEAM failure -> SERVER_CLIENT_JOINTEAM_REPLY (failure)");
        }
        else {
            Client c = Main.getClient(adr);

            if(c != null) {

                if((c.getId()+1) % 2 == 0)
                    teamId = 1;
                else
                    teamId = 2;

//                c.setTeamId(teamId);
                net.send(adr,
                         ProtocolCmd.SERVER_CLIENT_JOINTEAM_REPLY,
                         argInt(Protocol.REPLY_SUCCESS), argInt(teamId));

                Main.broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_TEAM_CHANGED,
                               argInt(c.getId()) , argInt(teamId));
                c.setTeamId(teamId);
                System.out.println("CLIENT_SERVER_JOINTEAM success id="+teamId+" -> SERVER_CLIENT_JOINTEAM_REPLY (success)");
            }
        }
    }

    /**
     *
     * @param adr
     */
    @Override
    public void c_s_latency(InetSocketAddress adr){
        net.send(adr, ProtocolCmd.SERVER_CLIENT_LATENCY_REPLY);
    }

    /**
     *
     * @param p
     */
    public void noReplyReceived(Packet p) {
        if(Protocol.getCmdById(p.getCmd()) == ProtocolCmd.SERVER_CLIENT_PING) {
            Main.removeClient(p.getAddress());
        }
    }   
}
