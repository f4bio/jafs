package common.net;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public abstract class ProtocolHandler implements Runnable {
    /**
     *
     */
    public static final int MODE_CLIENT = 0;
    /**
     *
     */
    public static final int MODE_SERVER = 1;
    /**
     *
     */
    public static final int MODE_MASTER = 2;

    /**
     *
     */
    protected Network net;
    /**
     *
     */
    protected Thread thread;
    /**
     *
     */
    protected boolean ready;
    /**
     *
     */
    protected int mode;

    /**
     *
     * @param net
     * @param mode
     */
    public ProtocolHandler(Network net, int mode) {
        this.mode = mode;
        this.net = net;
        ready = false;
        thread = new Thread(this);
        thread.start();
    }

    private void waiting() {
        synchronized(thread) {
            try {
                while(!ready)
                    thread.wait();
            } catch(Exception e) {

            }
        }
    }

    /**
     *
     */
    public void wakeUp() {
        if(!ready) {
            ready = true;

            synchronized(thread) {
                thread.notify();
            }
        }
    }

    public void run() {
        DatagramPacket packet;
        InetSocketAddress adr;

        while(true) {
            packet = net.getPacket();

            if(packet == null) {
                ready = false;
                waiting();
                continue;
            }

            byte[] data = new byte[packet.getLength()];
            byte[] recv = packet.getData();
            adr = (InetSocketAddress)packet.getSocketAddress();
            ProtocolCmd cmd = Protocol.getCmdById(packet.getData()[0]);
            byte[] param = Protocol.getArgType(cmd);
            int[] idx = new int[param.length];

            for(int i=packet.getOffset(); i<packet.getLength(); ++i) {
                data[i] = recv[i];
            }
            
            int index = 1;
            
            for(byte i=0; i<param.length; ++i) {
                switch(param[i]) {
                    case Protocol.ARG_STRING:
                        idx[i] = index;
                        index = ProtocolCmdArgument.terminatorIndex(data, index) + 1;
                        break;
                        
                    case Protocol.ARG_NONE:
                        break;
                    
                    default:
                        idx[i] = index;
                        index += Protocol.ARG_SIZE[param[i]];
                }
            }

            if (mode == MODE_MASTER) {
                switch (cmd) {
                    case CLIENT_MASTER_JOINSERVER:
                        c_m_joinserver(toStr(data, idx[0]), toInt(data, idx[1]), adr);
                        break;
                    case CLIENT_MASTER_LISTREQUEST:
                        c_m_listrequest(toShort(data, idx[0]), adr);
                        break;
                    case CLIENT_MASTER_CHAT_LOBBY:
                        c_m_chat_lobby(toStr(data, idx[0]), adr);
                        break;
                    case CLIENT_MASTER_CHAT_PRIVATE:
                        c_m_chat_private(toInt(data, idx[0]), toStr(data, idx[1]), adr);
                        break;
                    case CLIENT_MASTER_CHAT_PRIVATE_OK:
                        c_m_chat_private_ok(adr);
                        break;
                    case CLIENT_MASTER_AUTH:
                        c_m_auth(toStr(data, idx[0]), adr);
                        break;
                    case CLIENT_MASTER_PONG:
                        c_m_pong(adr);
                        break;
                    case CLIENT_MASTER_LOGOFF:
                        c_m_logoff(adr);
                        break;
                    case CLIENT_MASTER_NICKCHANGE:
                        c_m_nickchange(toStr(data, idx[0]), adr);
                        break;
                    case CLIENT_MASTER_FORCED_NICKCHANGE_OK:
                        c_m_forced_nickchange_ok(adr);
                        break;

                    case SERVER_MASTER_PONG:
                        s_m_pong(adr);
                        break;
                    case SERVER_MASTER_AUTH:
                        s_m_auth(adr);
                        break;
                    case SERVER_MASTER_SERVERCOUNT:
                        s_m_servercount(adr);
                }
            } else if (mode == MODE_SERVER) {
                switch (cmd) {
                    case MASTER_SERVER_AUTH_REPLY:
                        m_s_auth_reply(toInt(data, idx[0]), adr);
                        break;
                    case MASTER_SERVER_PING:
                        m_s_ping(adr);
                        break;
                    case MASTER_SERVER_SERVERCOUNT:
                        m_s_servercount(toInt(data, idx[0]), adr);
                        break;

                    case CLIENT_SERVER_AUTH:
                        c_s_auth(adr);
                        break;
                    case CLIENT_SERVER_LATENCY:
                        c_s_latency(adr);
                        break;
                    case CLIENT_SERVER_ALL_PLAYER_DATA:
                        c_s_all_player_data(adr);
                        break;
                    case CLIENT_SERVER_PLAYER_DATA_OK:
                        c_s_player_data_ok(adr);
                        break;
                    case CLIENT_SERVER_REQUEST_SERVER_INFO:
                        c_s_request_server_info(toStr(data, idx[0]), adr);
                        break;
                    case CLIENT_SERVER_PONG:
                        c_s_pong(adr);
                        break;
                    case CLIENT_SERVER_CLIENTCOUNT:
                        c_s_clientcount(adr);
                        break;
                    case CLIENT_SERVER_CLIENTID:
                        c_s_clientid(adr);
                        break;
                    case CLIENT_SERVER_LOGOFF:
                        c_s_logoff(adr);
                        break;
                    case CLIENT_SERVER_INIT_REPLY:
                        c_s_init_reply(toInt(data, idx[0]), adr);
                        break;
                    case CLIENT_SERVER_PLAYER_INFO:
                        c_s_player_info(toInt(data, idx[0]), toInt(data, idx[1]),
                                toDouble(data, idx[2]), toDouble(data, idx[3]),
                                toDouble(data, idx[4]), toDouble(data, idx[5]), adr);
                        break;
                    case CLIENT_SERVER_JOINTEAM:
                        c_s_jointeam(toInt(data, idx[0]), adr);
                        break;
                    case CLIENT_SERVER_FORCED_NICKCHANGE_OK:
                        c_s_forced_nickchange_ok(adr);
                        break;
                    case CLIENT_SERVER_REQUEST_NAME_REPLY:
                        c_s_request_name_reply(toStr(data, idx[0]), adr);
                        break;
                    case CLIENT_SERVER_CONNECTION_TERMINATED_OK:
                        c_s_connection_terminated_ok(adr);
                        break;
                    case CLIENT_SERVER_CONNECTION_ESTABLISHED_OK:
                        c_s_connection_established_ok(adr);
                        break;

                    case CLIENT_SERVER_CHAT_ALL:
                        c_s_chat_all(toStr(data, idx[0]), adr);
                        break;
                    case CLIENT_SERVER_CHAT_TEAM:
                        c_s_chat_team(toStr(data, idx[0]), adr);
                        break;
                    case CLIENT_SERVER_CHAT_PRIVATE:
                        c_s_chat_private(toInt(data, idx[0]), toStr(data, idx[1]), adr);
                        break;
                    case CLIENT_SERVER_CHAT_ALL_OK:
                        c_s_chat_all_ok(adr);
                        break;
                    case CLIENT_SERVER_CHAT_TEAM_OK:
                        c_s_chat_team_ok(adr);
                        break;
                    case CLIENT_SERVER_CHAT_PRIVATE_OK:
                        c_s_chat_private_ok(adr);
                        break;
                    case CLIENT_SERVER_EVENT_PLAYER_JOINED_OK:
                        c_s_event_player_joined_ok(adr);
                        break;
                    case CLIENT_SERVER_SHOOT:
                        c_s_shoot(toInt(data, idx[0]), toInt(data, idx[1]), toDouble(data, idx[2]),
                                toDouble(data, idx[3]), toDouble(data, idx[4]), toDouble(data, idx[5]), adr);
                        break;
                    case CLIENT_SERVER_EVENT_PLAYER_LEFT_OK:
                        break;
                    case CLIENT_SERVER_EVENT_ITEM_SPAWNED_OK:
                        break;
                    case CLIENT_SERVER_EVENT_ITEM_PICKED_OK:
                        break;
                    case CLIENT_SERVER_EVENT_PLAYER_SHOT_OK:
                        break;
                    case CLIENT_SERVER_EVENT_PLAYER_KILLED_OK:
                        break;
                    case CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK:
                        break;
                    case CLIENT_SERVER_EVENT_PLAYER_NICK_CHANGED_OK:
                        break;
                    case CLIENT_SERVER_EVENT_PLAYER_TEAM_CHANGED_OK:
                        break;
                    case CLIENT_SERVER_EVENT_TEAM_WON_OK:
                }
            } else if (mode == MODE_CLIENT) {
                switch (cmd) {
                    case MASTER_CLIENT_PING:
                        m_c_ping(adr);
                        break;
                    case MASTER_CLIENT_JOINSERVER_REPLY:
                        m_c_joinserver_reply(toStr(data, idx[0]), adr);
                        break;
                    case MASTER_CLIENT_AUTH_REPLY:
                        m_c_auth_reply(toInt(data, idx[0]), toInt(data, idx[1]), adr);
                        break;
                    case MASTER_CLIENT_NEWLIST:
                        m_c_newlist(toShort(data, idx[0]), adr);
                        break;
                    case MASTER_CLIENT_LISTENTRY_SERVER:
                        m_c_listentry_server(toStr(data, idx[0]), adr);
                        break;
                    case MASTER_CLIENT_LISTENTRY_CLIENT:
                        m_c_listentry_client(toStr(data, idx[0]), toInt(data, idx[1]), toStr(data, idx[2]), adr);
                        break;
                    case MASTER_CLIENT_ENDLIST:
                        m_c_endlist(toShort(data, idx[0]), adr);
                        break;
                    case MASTER_CLIENT_FORCED_NICKCHANGE:
                        m_c_forced_nickchange(toStr(data, idx[0]), adr);
                        break;
                    case MASTER_CLIENT_CHAT:
                        m_c_chat(toInt(data, idx[0]), toStr(data, idx[1]), adr);
                        break;
                    case MASTER_CLIENT_CHAT_PRIVATE:
                        m_c_chat_private(toInt(data, idx[0]), toInt(data, idx[1]), toStr(data, idx[2]), adr);
                        break;
                    case MASTER_CLIENT_CHAT_OK:
                        m_c_chat_ok(adr);
                        break;
                    case MASTER_CLIENT_NICKCHANGE_OK:
                        m_c_nickchange_ok(adr);
                        break;
                    case SERVER_CLIENT_INIT:
                        s_c_init(toStr(data, idx[0]), toInt(data, idx[1]), adr);
                        break;
                    case SERVER_CLIENT_PING:
                        s_c_ping(adr);
                        break;
                    case SERVER_CLIENT_AUTH_REPLY:
                        s_c_auth_reply(toInt(data, idx[0]), adr);
                        break;
                    case SERVER_CLIENT_REQUEST_NAME:
                        s_c_request_name(adr);
                        break;
                    case SERVER_CLIENT_CONNECTION_ESTABLISHED:
                        s_c_connection_established(adr);
                        break;
                    case SERVER_CLIENT_CONNECTION_TERMINATED:
                        s_c_connection_terminated(adr);
                        break;
                    case SERVER_CLIENT_PLAYER_DATA:
                        s_c_player_data(toStr(data, idx[0]), toInt(data, idx[1]), toInt(data, idx[2]), adr);
                        break;
                    case SERVER_CLIENT_PLAYER_INFO:
                        s_c_player_info(toInt(data, idx[0]), toInt(data, idx[1]), toInt(data, idx[2]), 
                                toInt(data, idx[3]), toInt(data, idx[4]), toInt(data, idx[5]),
                                toDouble(data, idx[6]), toDouble(data, idx[7]), toDouble(data, idx[8]),
                                toDouble(data, idx[9]), adr);
                        break;
                    case SERVER_CLIENT_GAME_INFO:
                        s_c_game_info(toLong(data, idx[0]), toLong(data, idx[1]), toInt(data, idx[2]),
                                toInt(data, idx[3]), adr);
                        break;
                    case SERVER_CLIENT_REQUEST_SERVER_INFO_REPLY:
                        s_c_request_server_info_reply(toStr(data, idx[0]), toStr(data, idx[1]), toInt(data, idx[2]), toInt(data, idx[3]), toInt(data, idx[4]), adr);
                        break;
                    case SERVER_CLIENT_ALL_PLAYER_DATA_OK:
                        s_c_all_player_data_ok(adr);
                        break;
                    case SERVER_CLIENT_CHAT_ALL_OK:
                        s_c_chat_all_ok(adr);
                        break;
                    case SERVER_CLIENT_EVENT_PLAYER_JOINED:
                        s_c_event_player_joined(toStr(data, idx[0]), toInt(data, idx[1]), adr);
                        break;
                    case SERVER_CLIENT_EVENT_PLAYER_LEFT:
                        s_c_event_player_left(toInt(data, idx[0]), adr);
                        break;
                    case SERVER_CLIENT_CHAT_ALL:
                        s_c_chat_all(toInt(data, idx[0]), toStr(data, idx[1]), adr);
                        break;
                    case SERVER_CLIENT_CHAT_TEAM:
                        s_c_chat_team(toInt(data, idx[0]), toStr(data, idx[1]), adr);
                        break;
                    case SERVER_CLIENT_CHAT_PRIVATE:
                        s_c_chat_private(toInt(data, idx[0]), toStr(data, idx[1]), adr);
                        break;
                    case SERVER_CLIENT_CHAT_PRIVATE_OK:
                        s_c_chat_private_ok(adr);
                        break;
                    case SERVER_CLIENT_LOGOFF_REPLY:
                        s_c_logoff_reply(toInt(data, idx[0]), adr);
                        break;
                    case SERVER_CLIENT_JOINTEAM_REPLY:
                        s_c_jointeam_reply(toInt(data, idx[0]), toInt(data, idx[1]), adr);
                        break;
                    case SERVER_CLIENT_EVENT_PLAYER_RESPAWN:
                        s_c_event_player_respawned(adr);
                        break;
                    case SERVER_CLIENT_EVENT_PLAYER_TEAM_CHANGED:
                        s_c_event_player_team_changed(toInt(data, idx[0]), toInt(data, idx[1]), adr);
                        break;
                    case SERVER_CLIENT_EVENT_PLAYER_SHOT:
                        s_c_event_player_shot(toInt(data, idx[0]), toInt(data, idx[1]), toDouble(data, idx[2]),
                                toDouble(data, idx[3]), toDouble(data, idx[4]), toDouble(data, idx[5]), adr);
                        break;
                    case SERVER_CLIENT_EVENT_PLAYER_KILLED:
                        s_c_event_player_killed(toInt(data, idx[0]), toInt(data, idx[1]), adr);
                        break;
                    case SERVER_CLIENT_EVENT_TEAM_WON:
                        s_c_event_team_won(toInt(data, idx[0]), toInt(data, idx[1]), adr);
                        break;
                    case SERVER_CLIENT_FORCED_NICKCHANGE:
                        s_c_forced_nickchange(toStr(data, idx[0]), adr);
                        break;
                    case SERVER_CLIENT_CLIENTID_REPLY:
                        s_c_clientid_reply(toInt(data, idx[0]), adr);
                        break;
                    case SERVER_CLIENT_CLIENTCOUNT:
                        s_c_clientcount(toInt(data, idx[0]), adr);
                        break;
                    case SERVER_CLIENT_LATENCY_REPLY:
                        s_c_latency_reply(adr);
                }
            }
        }
    }

    /**
     *
     * @param p
     */
    public abstract void noReplyReceived(Packet p);
    
    //Masterserver
    /**
     *
     * @param adr
     */
    public void s_m_auth(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_m_pong(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_m_servercount(InetSocketAddress adr) { }
    /**
     *
     * @param host
     * @param port
     * @param adr
     */
    public void c_m_joinserver(String host, int port, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_m_pong(InetSocketAddress adr) { }
    /**
     *
     * @param type
     * @param adr
     */
    public void c_m_listrequest(short type, InetSocketAddress adr) { }
    /**
     *
     * @param name
     * @param adr
     */
    public void c_m_auth(String name, InetSocketAddress adr) { }
    /**
     *
     * @param msg
     * @param adr
     */
    public void c_m_chat_lobby(String msg, InetSocketAddress adr) { }
    /**
     *
     * @param receiverID
     * @param msg
     * @param adr
     */
    public void c_m_chat_private(int receiverID, String msg, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_m_chat_private_ok(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_m_logoff(InetSocketAddress adr) { }
    /**
     *
     * @param newNick
     * @param adr
     */
    public void c_m_nickchange(String newNick, InetSocketAddress adr) {  }
    /**
     *
     * @param adr
     */
    public void c_m_forced_nickchange_ok(InetSocketAddress adr) {  }

    //Server
    /**
     *
     * @param adr
     */
    public void m_s_ping(InetSocketAddress adr) { }
    /**
     *
     * @param i
     * @param adr
     */
    public void m_s_servercount(int i, InetSocketAddress adr) { }
    /**
     *
     * @param i
     * @param adr
     */
    public void m_s_auth_reply(int i, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_auth(InetSocketAddress adr) { }
    /**
     *
     * @param i
     * @param adr
     */
    public void c_s_init_reply(int i, InetSocketAddress adr) { }
    /**
     *
     * @param name
     * @param adr
     */
    public void c_s_request_name_reply(String name, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_connection_established_ok(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_connection_terminated_ok(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_forced_nickchange_ok(InetSocketAddress adr) { }
    /**
     *
     * @param playerName
     * @param adr
     */
    public void c_s_request_server_info(String playerName, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_all_player_data(InetSocketAddress adr) { }
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
    public void c_s_player_info(int id, int wep, double posX, double posY,
                                double dirX, double dirY, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_player_data_ok(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_pong(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_clientcount(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_clientid(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_logoff(InetSocketAddress adr) { }
    /**
     *
     * @param msg
     * @param adr
     */
    public void c_s_chat_all(String msg, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_chat_all_ok(InetSocketAddress adr) { }
    /**
     *
     * @param msg
     * @param adr
     */
    public void c_s_chat_team(String msg, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_chat_team_ok(InetSocketAddress adr) { }
    /**
     *
     * @param to
     * @param msg
     * @param adr
     */
    public void c_s_chat_private(int to, String msg, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_chat_private_ok(InetSocketAddress adr) { }
    /**
     *
     * @param teamId
     * @param adr
     */
    public void c_s_jointeam(int teamId, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_event_player_joined_ok(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void c_s_latency(InetSocketAddress adr) { }
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
    public void c_s_shoot(int id, int wepId, double dirX, double dirY, double orgX,
            double orgY, InetSocketAddress adr) { }

    //Client
    /**
     *
     * @param type
     * @param adr
     */
    public void m_c_newlist(short type, InetSocketAddress adr) { }
    /**
     *
     * @param serverAdr
     * @param adr
     */
    public void m_c_listentry_server(String serverAdr, InetSocketAddress adr) { }
    /**
     *
     * @param clientAdr
     * @param id
     * @param name
     * @param adr
     */
    public void m_c_listentry_client(String clientAdr, int id, String name, InetSocketAddress adr) { }
    /**
     *
     * @param type
     * @param adr
     */
    public void m_c_endlist(short type, InetSocketAddress adr) { }
    /**
     *
     * @param i
     * @param id
     * @param adr
     */
    public void m_c_auth_reply(int i, int id, InetSocketAddress adr) { }
    /**
     *
     * @param s
     * @param adr
     */
    public void m_c_joinserver_reply(String s, InetSocketAddress adr) { }
    /**
     *
     * @param senderID
     * @param msg
     * @param adr
     */
    public void m_c_chat(int senderID, String msg, InetSocketAddress adr) { }
    /**
     *
     * @param senderID
     * @param recieverID
     * @param msg
     * @param adr
     */
    public void m_c_chat_private(int senderID, int recieverID, String msg, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void m_c_ping(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void m_c_nickchange_ok(InetSocketAddress adr) { }
    /**
     *
     * @param forcedNick
     * @param adr
     */
    public void m_c_forced_nickchange(String forcedNick, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_c_ping(InetSocketAddress adr) { }
    /**
     *
     * @param i
     * @param adr
     */
    public void s_c_clientcount(int i, InetSocketAddress adr) { }
    /**
     *
     * @param id
     * @param adr
     */
    public void s_c_clientid_reply(int id, InetSocketAddress adr) { }
    /**
     *
     * @param i
     * @param adr
     */
    public void s_c_auth_reply(int i, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_c_request_name(InetSocketAddress adr) { }
    /**
     *
     * @param m
     * @param t
     * @param adr
     */
    public void s_c_init(String m, int t, InetSocketAddress adr) { }
    /**
     *
     * @param n
     * @param adr
     */
    public void s_c_forced_nickchange(String n, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_c_connection_established(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_c_connection_terminated(InetSocketAddress adr) { }
    /**
     *
     * @param name
     * @param id
     * @param team
     * @param adr
     */
    public void s_c_player_data(String name, int id, int team, InetSocketAddress adr) { }
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
    public void s_c_player_info(int id, int health, int kills, int deaths, int team,
            int wep, double posX, double posY, double dirX, double dirY, InetSocketAddress adr) { }

    public void s_c_game_info(long roundTime, long respawnTime, int scoreRed, int scoreBlue,
            InetSocketAddress adr) { }
    
    /**
     *
     * @param adr
     */
    public void s_c_all_player_data_ok(InetSocketAddress adr) { }
    /**
     *
     * @param n
     * @param i
     * @param adr
     */
    public void s_c_event_player_joined(String n, int i, InetSocketAddress adr) { }

    public void s_c_event_player_left(int i, InetSocketAddress adr) { }
    /**
     *
     * @param p
     * @param t
     * @param adr
     */
    public void s_c_event_player_team_changed(int p, int t, InetSocketAddress adr) { }
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
    public void s_c_event_player_shot(int id, int wepId, double dirX, double dirY, double orgX,
            double orgY, InetSocketAddress adr) { }

    public void s_c_event_team_won(int teamId, int bestPlayerId, InetSocketAddress adr) { }

    /**
     *
     * @param name
     * @param map
     * @param curPlayers
     * @param maxPlayers
     * @param highscore
     * @param adr
     */
    public void s_c_request_server_info_reply(String name, String map, int curPlayers, int maxPlayers, int highscore, InetSocketAddress adr){ }


    /**
     *
     * @param who
     * @param by
     * @param adr
     */
    public void s_c_event_player_killed(int who, int by, InetSocketAddress adr) { }
    /**
     *
     * @param id
     * @param msg
     * @param adr
     */
    public void s_c_chat_all(int id, String msg, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_c_chat_all_ok(InetSocketAddress adr) { }
    /**
     *
     * @param id
     * @param msg
     * @param adr
     */
    public void s_c_chat_team(int id, String msg, InetSocketAddress adr) { }
    /**
     *
     * @param id
     * @param msg
     * @param adr
     */
    public void s_c_chat_private(int id, String msg, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_c_chat_private_ok(InetSocketAddress adr) { }
    /**
     *
     * @param reply
     * @param adr
     */
    public void s_c_logoff_reply(int reply, InetSocketAddress adr) { }
    /**
     *
     * @param reply
     * @param team
     * @param adr
     */
    public void s_c_jointeam_reply(int reply, int team, InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_c_event_player_respawned(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void s_c_latency_reply(InetSocketAddress adr) { }
    /**
     *
     * @param adr
     */
    public void m_c_chat_ok(InetSocketAddress adr) { }
}
