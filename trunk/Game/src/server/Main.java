package server;

import common.net.Client;
import common.net.Network;
import common.net.Protocol;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author miracle
 */
public class Main {

    private static int serverId;
    public static int maxPingFailures = 2;
    public static final int pingRefreshInterval = 10000;
    private static ArrayList<Client> clientlist = new ArrayList<Client>();
    private static Network net;

    public static void main(String[] args) {
        Protocol.init();
        net = new Network();
        ProtocolHandler protocol = new ProtocolHandler(net);
        net.setProtocolHandler(protocol);
        net.listen(net.getFreePort(40000, 50000));
        net.send(Network.MASTERHOST, Network.MASTERPORT, Protocol.SERVER_MASTER_SERVERCOUNT);
        net.send(Network.MASTERHOST, Network.MASTERPORT, Protocol.SERVER_MASTER_AUTH);


        pingTimer = new Timer();
        pingTimer.schedule(pinger, pingRefreshInterval, pingRefreshInterval);
        
        try {
            Thread.sleep(1000);
        } catch(Exception e) {

        }
    }
    private static TimerTask pinger = new TimerTask() {
        public void run() {
            for(Client cur : clientlist) {
                /*cur.increasePingFailureCnt();
                failures = cur.getPingFailureCnt();

                if(failures >= maxPingFailures) {
                    removeClient(cur);
                    continue;
                }*/
                net.send(cur.getAddress(), Protocol.SERVER_CLIENT_PING);
            }
        }
    };
    private static Timer pingTimer;

    static Client addClient(InetSocketAddress adr) {
        for(Client client: clientlist)
            if(client.getAddress().equals(adr))
                return null;

        Client client = new Client(adr);
        client.setId(clientlist.size());
        clientlist.add(client);
        System.out.println("Client "+client.getHost()+":"+client.getPort()+" on Server-"+serverId+" listed.");
        return client;
    }

    public static void removeClient(Client client) {
        clientlist.remove(client);
        System.out.println("Client " + client.getHost() + ":" + client.getPort() + " dropped from Server-"+serverId+"." );
    }
    public static void removeClient(InetSocketAddress adr) {
        removeClient(getClient(adr));
    }
    public static int getServerId(){
        return serverId;
    }
    public static void setServerId(int id){
        serverId = id;
    }
    public static Client getClient(InetSocketAddress adr) {
        for(Client cur : clientlist) {
            if(cur.getAddress().equals(adr))
                return cur;
        }

        return null;
    }
    public static void decreasePingFailures(InetSocketAddress adr) {
        Client client = getClient(adr);

        if(client != null)
            client.decreasePingFailureCnt();
    }
    public static int clientCount(){
        return clientlist.size();
    }
    public static int getClientId(InetSocketAddress adr){
        for(Client client: clientlist)
            if(client.getAddress().equals(adr))
                return client.getId();
        return -1;
    }
    public static void broadcast(String msg, InetSocketAddress adr){
        for(Client client: clientlist)
            net.send(client.getAddress(), Protocol.SERVER_CLIENT_CHAT, "(PUBLiC-CHAT) Player-"+Main.getClientId(adr)+" ("+client.getHost()+":"+client.getPort()+"): "+msg);
    }
    public static void broadcast_team(String msg, InetSocketAddress adr){
        int teamId = getClient(adr).getTeamId();
        for(Client client: clientlist)
            if(client.getTeamId()==teamId)
                net.send(client.getAddress(), Protocol.SERVER_CLIENT_CHAT, "(TEAM-CHAT) Player-"+Main.getClientId(adr)+" ("+client.getHost()+":"+client.getPort()+") @ Team-"+teamId+": "+msg);
    }
    public static int getClientTeamId(InetSocketAddress adr){
        for(Client client: clientlist)
            if(client.getAddress().equals(adr))
                return client.getTeamId();
        return -1;
    }
    // wenn teamid schon gesetzt nicht nehr setzten?!  xD
    public static int setClientTeamId(InetSocketAddress adr, int teamId){
        for(Client client: clientlist)
            if(client.getAddress().equals(adr)){
                client.setTeamId(teamId);
                System.out.println("Player-"+client.getId()+" joined Team: "+client.getTeamId());
                return 0;
            }
        return -1;
    }
}
