/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
        net.listen(40000);
        net.send("localhost", 30000, Protocol.server_master_servercount);
        net.send("localhost", 30000, Protocol.server_master_auth, new Object[0]);
        System.out.println("Connected on MasterServer "+net.getHost()+":"+net.getPort()+"! Server-iD:"+serverId+"");

        pingTimer = new Timer();
        pingTimer.schedule(pinger, pingRefreshInterval, pingRefreshInterval);
    }
    private static TimerTask pinger = new TimerTask() {
        public void run() {
            int failures;
            for(Client cur : clientlist) {
                cur.increasePingFailureCnt();
                failures = cur.getPingFailureCnt();

                if(failures >= maxPingFailures) {
                    removeClient(cur);
                    continue;
                }
                net.send(cur.getAddress(), Protocol.server_client_ping, new Object[0]);
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
        removeClient(new Client(adr));
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
}
