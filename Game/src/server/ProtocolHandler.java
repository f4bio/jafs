package server;

import common.net.Client;
import common.net.Network;
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
    public void m_s_auth_success(InetSocketAddress adr) {
        System.out.println("server succesfully listed.");
    }

    public void m_s_auth_failure(InetSocketAddress adr) {
        System.out.println("server failed to be listed.");
    }

    public void c_s_auth(InetSocketAddress adr){
        Client added = Main.addClient(adr);
        if(added != null)
            net.send(adr, Protocol.SERVER_CLIENT_AUTH_SUCCESS, new Object[0]);
        else
            net.send(adr, Protocol.SERVER_CLIENT_AUTH_FAILURE, new Object[0]);
    }
    public void c_s_pong(InetSocketAddress adr) {
        Main.decreasePingFailures(adr);
    }
    public void c_s_clientcount(InetSocketAddress adr){
        net.send(adr, Protocol.SERVER_CLIENT_CLIENTCOUNT, Main.clientCount());
    }
    public void c_s_clientid(InetSocketAddress adr){
        net.send(adr, Protocol.SERVER_CLIENT_CLIENTID, Main.getClient(adr));
    }
    public void c_s_logoff(InetSocketAddress adr){
        Main.removeClient(adr);
        net.send(adr, Protocol.SERVER_CLIENT_LOGOFF_SUCCESS, new Object[0]);
    }
    // --- chat fkt
    public void c_s_chat_all(String msg, InetSocketAddress adr){
        Main.broadcast(msg,adr);
//        net.send(adr, Protocol.server_client_chat, "(PUBLiC-CHAT) Player-"+Main.getClientId(adr)+" ("+adr.getHostName()+":"+adr.getPort()+"): "+msg);
    }
    // team wird aus der clientlist geholt, muss nicht übergeben werden
    public void c_s_chat_team(String msg, InetSocketAddress adr){
        Main.broadcast_team(msg, adr);
    }
    // --- chat end
    public void c_s_jointeam(Integer teamId, InetSocketAddress adr){
        if(Main.setClientTeamId(adr, teamId)==-1)
            net.send(adr, Protocol.SERVER_CLIENT_JOINTEAM_FAILURE);
        else
            net.send(adr, Protocol.SERVER_CLIENT_JOINTEAM_SUCCESS);
    }
}
