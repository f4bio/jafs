/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.net;

import java.net.InetSocketAddress;

/**
 *
 * @author adm1n
 */
public class Client {

    private String host;
    private String name;
    private String map;
    private int port;
    private int limitPlayers;
    private int curPlayers;
    private int latency;
    private boolean inGame;
    private InetSocketAddress address;
    private int pingFailureCnt;
    private int id;
    private int teamId;
    
    public Client(String host, int port){
        this.host = host;
        this.port = port;
        pingFailureCnt = 0;
        address = new InetSocketAddress(host, port);
    }
    public Client(InetSocketAddress adr) {
        this.host = adr.getHostName();
        this.port = adr.getPort();
        pingFailureCnt = 0;
        address = adr;
        this.inGame = false;
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
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void changeInGame() {
        this.inGame = !this.inGame;
    }
    public boolean isInGame() {
        return this.inGame;
    }
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
    public int getTeamId() {
        return teamId;
    }
}
