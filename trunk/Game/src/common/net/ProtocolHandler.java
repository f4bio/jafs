package common.net;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public abstract class ProtocolHandler implements Runnable {
    public static final int MODE_CLIENT = 0;
    public static final int MODE_SERVER = 1;
    public static final int MODE_MASTER = 2;

    protected Network net;
    protected Thread thread;
    protected boolean ready;
    protected int mode;

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
                //System.out.print(recv[i] + " ");
            }
            //System.out.println(packet.getLength());
            
            int index = 1;
            
            for(byte i=0; i<param.length; ++i) {
                switch(param[i]) {
                    case Protocol.ARG_STRING:
                        idx[i] = index;
                        ///System.out.println(index);
                        index = ProtocolCmdArgument.terminatorIndex(data, index) + 1;
                        //System.out.println(index);
                        break;
                        
                    case Protocol.ARG_NONE:
                        break;
                    
                    default:
                        idx[i] = index;
                        //System.out.println(index);
                        index += Protocol.ARG_SIZE[param[i]];
                }
            }

            if (mode == MODE_MASTER) {
                switch (cmd) {
                    case CLIENT_MASTER_JOINSERVER:
                        c_m_joinserver(toStr(data, idx[0]), toInt(data, idx[1]), adr);
                        break;
                    case CLIENT_MASTER_LISTREQUEST:
                        c_m_listrequest(adr);
                        break;
                    case CLIENT_MASTER_CHAT_LOBBY:
                        c_m_chat_lobby(toStr(data, idx[0]), adr);
                        break;
                    case CLIENT_MASTER_CHAT_PRIVATE:
//                        c_m_chat_private(toInt(data, idx[0]), "test", adr);
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
                    case CLIENT_SERVER_CURRENT_MAP:
                        c_s_current_map(adr);
                        break;
                    case CLIENT_SERVER_PLAYERS:
                        c_s_players(adr);
                        break;
                    case CLIENT_SERVER_ALL_PLAYER_DATA:
                        c_s_all_player_data(adr);
                        break;
                    case CLIENT_SERVER_PLAYER_DATA_OK:
                        c_s_player_data_ok(adr);
                        break;
                    case CLIENT_SERVER_REQUEST_SERVER_INFO:
                        c_s_request_server_info(adr);
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
                        c_s_chat_private(toStr(data, idx[0]), toInt(data, idx[1]), adr);
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
                        m_c_auth_reply(toInt(data, idx[0]), adr);
                        break;
                    case MASTER_CLIENT_NEWLIST:
                        m_c_newlist(adr);
                        break;
                    case MASTER_CLIENT_LISTENTRY:
                        m_c_listentry(toStr(data, idx[0]), adr);
                        break;
                    case MASTER_CLIENT_ENDLIST:
                        m_c_endlist(adr);
                        break;
                    case MASTER_CLIENT_CHAT:
                        m_c_chat(toInt(data, idx[0]), toStr(data, idx[1]), toStr(data, idx[2]), adr);
                        break;
                    case MASTER_CLIENT_CHAT_OK:
                        m_c_chat_ok(adr);
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
                        s_c_player_info(toInt(data, idx[0]), toInt(data, idx[1]), toDouble(data, idx[2]), toDouble(data, idx[3]), toDouble(data, idx[4]), toDouble(data, idx[5]), adr);
                        break;
                    case SERVER_CLIENT_ALL_PLAYER_DATA_OK:
                        s_c_all_player_data_ok(adr);
                        break;
                    case SERVER_CLIENT_EVENT_PLAYER_JOINED:
                        s_c_event_player_joined(toStr(data, idx[0]), toInt(data, idx[1]), adr);
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
                        break;
                    case SERVER_CLIENT_CURRENT_MAP_REPLY:
                        s_c_current_map_reply(toStr(data, idx[0]), adr);
                        break;
                    case SERVER_CLIENT_PLAYERS_REPLY:
                        s_c_players_reply(toStr(data, idx[0]), adr);
                }
            }
        }
    }

    public abstract void noReplyReceived(Packet p);
    
    //Masterserver
    public void s_m_auth(InetSocketAddress adr) { }
    public void s_m_pong(InetSocketAddress adr) { }
    public void s_m_servercount(InetSocketAddress adr) { }
    public void c_m_joinserver(String host, int port, InetSocketAddress adr) { }
    public void c_m_pong(InetSocketAddress adr) { }
    public void c_m_listrequest(InetSocketAddress adr) { }
    public void c_m_auth(String name, InetSocketAddress adr) { }
    public void c_m_chat_lobby(String msg, InetSocketAddress adr) { }
    public void c_m_chat_private(int id, String msg, InetSocketAddress adr) { }
    public void c_m_chat_private_ok(InetSocketAddress adr) { }
    public void c_m_logoff(InetSocketAddress adr) { }

    //Server
    public void m_s_ping(InetSocketAddress adr) { }
    public void m_s_servercount(int i, InetSocketAddress adr) { }
    public void m_s_auth_reply(int i, InetSocketAddress adr) { }
    public void c_s_auth(InetSocketAddress adr) { }
    public void c_s_init_reply(int i, InetSocketAddress adr) { }
    public void c_s_request_name_reply(String name, InetSocketAddress adr) { }
    public void c_s_connection_established_ok(InetSocketAddress adr) { }
    public void c_s_connection_terminated_ok(InetSocketAddress adr) { }
    public void c_s_forced_nickchange_ok(InetSocketAddress adr) { }
    public void c_s_request_server_info(InetSocketAddress adr) { }
    public void c_s_all_player_data(InetSocketAddress adr) { }
    public void c_s_player_info(int id, int wep, double posX, double posY,
                                double dirX, double dirY, InetSocketAddress adr) { }
    public void c_s_player_data_ok(InetSocketAddress adr) { }
    public void c_s_pong(InetSocketAddress adr) { }
    public void c_s_clientcount(InetSocketAddress adr) { }
    public void c_s_clientid(InetSocketAddress adr) { }
    public void c_s_logoff(InetSocketAddress adr) { }
    public void c_s_chat_all(String msg, InetSocketAddress adr) { }
    public void c_s_chat_all_ok(InetSocketAddress adr) { }
    public void c_s_chat_team(String msg, InetSocketAddress adr) { }
    public void c_s_chat_team_ok(InetSocketAddress adr) { }
    public void c_s_chat_private(String msg, int to, InetSocketAddress adr) { }
    public void c_s_chat_private_ok(InetSocketAddress adr) { }
    public void c_s_jointeam(int teamId, InetSocketAddress adr) { }
    public void c_s_event_player_joined_ok(InetSocketAddress adr) { }
    public void c_s_latency(InetSocketAddress adr) { }
    public void c_s_current_map(InetSocketAddress adr) { }
    public void c_s_players(InetSocketAddress adr) { }

    //Client
    public void m_c_newlist(InetSocketAddress adr) { }
    public void m_c_listentry(String server, InetSocketAddress adr) { }
    public void m_c_endlist(InetSocketAddress adr) { }
    public void m_c_auth_reply(int i, InetSocketAddress adr) { }
    public void m_c_joinserver_reply(String s, InetSocketAddress adr) { }
    public void m_c_chat(int id, String sender, String msg, InetSocketAddress adr) { }
    public void m_c_ping(InetSocketAddress adr) { }
    public void s_c_ping(InetSocketAddress adr) { }
    public void s_c_clientcount(int i, InetSocketAddress adr) { }
    public void s_c_clientid_reply(int id, InetSocketAddress adr) { }
    public void s_c_auth_reply(int i, InetSocketAddress adr) { }
    public void s_c_request_name(InetSocketAddress adr) { }
    public void s_c_init(String m, int t, InetSocketAddress adr) { }
    public void s_c_forced_nickchange(String n, InetSocketAddress adr) { }
    public void s_c_connection_established(InetSocketAddress adr) { }
    public void s_c_connection_terminated(InetSocketAddress adr) { }
    public void s_c_player_data(String name, int id, int team, InetSocketAddress adr) { }
    public void s_c_player_info(int id, int wep, double posX, double posY,
                                double dirX, double dirY, InetSocketAddress adr) { }
    public void s_c_all_player_data_ok(InetSocketAddress adr) { }
    public void s_c_event_player_joined(String n, int i, InetSocketAddress adr) { }
    public void s_c_event_player_team_changed(int p, int t, InetSocketAddress adr) { }
    public void s_c_chat_all(int id, String msg, InetSocketAddress adr) { }
    public void s_c_chat_team(int id, String msg, InetSocketAddress adr) { }
    public void s_c_chat_private(int id, String msg, InetSocketAddress adr) { }
    public void s_c_logoff_reply(int reply, InetSocketAddress adr) { }
    public void s_c_jointeam_reply(int reply, int team, InetSocketAddress adr) { }
    public void s_c_event_player_respawned(InetSocketAddress adr) { }
    public void s_c_latency_reply(InetSocketAddress adr) { }
    public void s_c_current_map_reply(String map, InetSocketAddress adr) { }
    public void s_c_players_reply(String nPlayers, InetSocketAddress adr) { }
    public void m_c_chat_ok (InetSocketAddress adr) { }
}
