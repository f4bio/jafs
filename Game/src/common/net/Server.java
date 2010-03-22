/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 *
 * @author miracle
 */
public class Server {
    private String host;
    private String name;
    private String map;
    private int port;
    private int limitPlayers;
    private int curPlayers;
    private int latency;
    private InetSocketAddress address;
    private int pingFailureCnt;
    private int serverId;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
        pingFailureCnt = 0;
        address = new InetSocketAddress(host, port);
    }

    public Server(InetSocketAddress adr) {
        this.host = adr.getHostName();
        this.port = adr.getPort();
        pingFailureCnt = 0;
        address = adr;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void increasePingFailureCnt() {
        pingFailureCnt++;
    }

    public void decreasePingFailureCnt() {
        pingFailureCnt--;
    }

    public void resetPingFailureCnt() {
        pingFailureCnt = 0;
    }

    public int getPingFailureCnt() {
        return pingFailureCnt;
    }
    public void setServerId(int id){
        this.serverId = id;
    }
    public int getServerId(){
        return serverId;
    }
    public void setMap(String map){
       this.map = map;
    }
    public String getMap(){
        return map;
    }

}
