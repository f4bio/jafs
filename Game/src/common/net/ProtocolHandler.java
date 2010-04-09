package common.net;

import common.CLog;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;

/**
 *
 * @author miracle
 */
public abstract class ProtocolHandler implements Runnable {
    protected Network net;
    protected Thread thread;
    protected boolean ready;

    public ProtocolHandler(Network net) {
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
        Packet dPacket;
        InetSocketAddress dAddress;

        while(true) {
            dPacket = net.getPacket();

            if(dPacket == null) {
                ready = false;
                waiting();
                continue;
            }

            String[] sPacket = dPacket.getPacket();
            dAddress = dPacket.getAddress();
            String cmd = sPacket[0];

            Object[] param = new Object[Protocol.getArgSize(sPacket[0])];
            //Class[] sig = new Class[param.length];
            int[] type = Protocol.getArgType(sPacket[0]);

            for(int i=0; i<type.length; ++i) {
                try {
                    switch(type[i]) {
                        case Protocol.ARG_BYTE:
                            param[i] = Byte.parseByte(sPacket[i+1]);
                            //sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_SHORT:
                            param[i] = Short.parseShort(sPacket[i+1]);
                            //sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_INT:
                            param[i] = Integer.parseInt(sPacket[i+1]);
                            //sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_LONG:
                            param[i] = Long.parseLong(sPacket[i+1]);
                            //sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_FLOAT:
                            param[i] = Float.parseFloat(sPacket[i+1]);
                            //sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_DOUBLE:
                            param[i] = Double.parseDouble(sPacket[i+1]);
                            //sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_STRING:
                            param[i] = sPacket[i+1];
                            //sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_NONE:
                            //sig = new Class[1];
                            param = new Object[1];
                            break;

                        default:
                    }

                } catch(Exception e) {
                    CLog.log("Invalid packet sent by " + dAddress.getHostName()
                            + ":" + dAddress.getPort());
                }
            }

            //param[param.length - 1] = dAddress;
//            sig[sigsig.length - 1] = dAddress.getClass();

            /*Method method = null;
            try {
                method = this.getClass().getDeclaredMethod(sPacket[0], sig);

                if(Modifier.isPrivate(method.getModifiers())) {
                    method.setAccessible(true);
                }

                method.invoke(this, param);
            } catch(NoSuchMethodException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }*/

            //Masterserver
            if(cmd.equals(Protocol.CLIENT_MASTER_AUTH))
                c_m_auth(dAddress);
            else if(cmd.equals(Protocol.SERVER_MASTER_AUTH))
                s_m_auth(dAddress);
            else if(cmd.equals(Protocol.CLIENT_MASTER_CHAT_LOBBY))
                c_m_chat_lobby((String)param[0], dAddress);
            
        }
    }

    public abstract void noReplyReceived(Packet p);
    
    //Masterserver
    public void s_m_auth(InetSocketAddress adr) { }
    public void s_m_pong(InetSocketAddress adr) { }
    public void s_m_servercount(InetSocketAddress adr) { }
    public void c_m_joinserver(String host, Integer port, InetSocketAddress adr) { }
    public void c_m_listrequest(InetSocketAddress adr) { }
    public void c_m_auth(InetSocketAddress adr) { }
    public void c_m_chat_lobby(String msg, InetSocketAddress adr) { }
    public void c_m_chat_private(Integer id, String msg, InetSocketAddress adr) { }
    public void c_m_logoff(InetSocketAddress adr) { }

    //Server
    public void m_s_ping(InetSocketAddress adr) { }
    public void m_s_servercount(Integer i, InetSocketAddress adr) { }
    public void m_s_auth_reply(Integer i, InetSocketAddress adr) { }
    public void c_s_auth(InetSocketAddress adr) { }
    public void c_s_init_reply(Integer i, InetSocketAddress adr) { }
    public void c_s_request_name_reply(String name, InetSocketAddress adr) { }
    public void c_s_connection_established_ok(InetSocketAddress adr) { }
    public void c_s_forced_nickchange_ok(InetSocketAddress adr) { }
    public void c_s_request_server_info(InetSocketAddress adr) { }
    public void c_s_all_player_data(InetSocketAddress adr) { }
    public void c_s_player_info(Integer id, Integer wep, Double posX, Double posY,
                                Double dirX, Double dirY, InetSocketAddress adr) { }
    public void c_s_player_data_ok(InetSocketAddress adr) { }
    public void c_s_pong(InetSocketAddress adr) { }
    public void c_s_clientcount(InetSocketAddress adr) { }
    public void c_s_clientid(InetSocketAddress adr) { }
    public void c_s_logoff(InetSocketAddress adr) { }
    public void c_s_chat_all(String msg, InetSocketAddress adr) { }
    public void c_s_chat_team(String msg, InetSocketAddress adr) { }
    public void c_s_chat_private(String msg, Integer to, InetSocketAddress adr) { }
    public void c_s_jointeam(Integer teamId, InetSocketAddress adr) { }
    public void c_s_event_player_joined_ok(InetSocketAddress adr) { }
    public void c_s_latency(InetSocketAddress adr) { }

    //Client
    public void m_c_newlist(InetSocketAddress adr) { }
    public void m_c_listentry(String server, InetSocketAddress adr) { }
    public void m_c_endlist(InetSocketAddress adr) { }
    public void m_c_auth_reply(Integer i, InetSocketAddress adr) { }
    public void m_c_joinserver_reply(String s, InetSocketAddress adr) { }
    public void m_c_chat(Integer id, String msg, InetSocketAddress adr) { }
    public void s_c_ping(InetSocketAddress adr) { }
    public void s_c_clientcount(Integer i, InetSocketAddress adr) { }
    public void s_c_clientid_reply(Integer id, InetSocketAddress adr) { }
    public void s_c_auth_reply(Integer i, InetSocketAddress adr) { }
    public void s_c_request_name(InetSocketAddress adr) { }
    public void s_c_init(String m, Integer t, InetSocketAddress adr) { }
    public void s_c_forced_nickchange(String n, InetSocketAddress adr) { }
    public void s_c_connection_established(InetSocketAddress adr) { }
    public void s_c_connection_terminated(InetSocketAddress adr) { }
    public void s_c_player_data(String name, Integer id, Integer team, InetSocketAddress adr) { }
    public void s_c_player_info(Integer id, Integer wep, Double posX, Double posY,
            Double dirX, Double dirY, InetSocketAddress adr) { }
    public void s_c_all_player_data_ok(InetSocketAddress adr) { }
    public void s_c_event_player_joined(String n, InetSocketAddress adr) { }
    public void s_c_chat_all(Integer id, String msg, InetSocketAddress adr) { }
    public void s_c_chat_team(Integer id, String msg, InetSocketAddress adr) { }
    public void s_c_chat_private(Integer id, String msg, InetSocketAddress adr) { }
    public void s_c_logoff_reply(Integer reply, InetSocketAddress adr) { }
    public void s_c_jointeam_reply(Integer reply, Integer team, InetSocketAddress adr) { }
    public void s_c_event_player_respawned(InetSocketAddress adr) { }
    public void s_c_latency_reply(InetSocketAddress adr) { }
    public void m_c_chat_ok (InetSocketAddress adr) { }
}
