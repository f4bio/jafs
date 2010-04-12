package common.net;

import java.net.InetSocketAddress;

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
    private String curPlayers = "";
    private int latency;
    private InetSocketAddress address;
    private int serverId;
    private long c_s_latency = 0;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
        address = new InetSocketAddress(host, port);
    }

    public Server(InetSocketAddress adr) {
        this.host = adr.getHostName();
        this.port = adr.getPort();
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

    public String getHostPort() {
        return host+":"+port;
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

    public void setClientServerLatency(long latency){
        c_s_latency = latency - c_s_latency;
    }

    public long getClientSserverLatency(){
        return c_s_latency;
    }

    public void setCurPlayers(String n){
        curPlayers = n;
    }

    public String getCurPlayers(){
        return curPlayers;
    }
}
