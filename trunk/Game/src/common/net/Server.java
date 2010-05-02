package common.net;

import java.net.InetSocketAddress;

/**
 *
 * @author miracle
 */
public class Server {
    private String host;
    private String name = "J.A.F.S. Server";
    private String map;
    private int port;
    private int limitPlayers;
    private String curPlayers = "";
    private int latency;
    private int clienthighscore;
    private InetSocketAddress address;
    private int serverId;
    private long c_s_latency = 0;

    /**
     *
     * @param host
     * @param port
     */
    public Server(String host, int port) {
        this.host = host;
        this.port = port;
        address = new InetSocketAddress(host, port);
    }

    /**
     *
     * @param adr
     */
    public Server(InetSocketAddress adr) {
        this.host = adr.getHostName();
        this.port = adr.getPort();
        address = adr;
    }

    /**
     *
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @param n
     */
    public void setName(String n){
        name = n;
    }

    /**
     *
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     *
     * @return
     */
    public String getHostPort() {
        return host+":"+port;
    }

    /**
     *
     * @param id
     */
    public void setServerId(int id){
        this.serverId = id;
    }

    /**
     *
     * @return
     */
    public int getServerId(){
        return serverId;
    }

    /**
     *
     * @param map
     */
    public void setMap(String map){
       this.map = map;
    }

    /**
     *
     * @return
     */
    public String getMap(){
        return map;
    }

    /**
     *
     * @param latency
     */
    public void setClientServerLatency(long latency){
        c_s_latency = latency - c_s_latency;
    }

    /**
     *
     * @return
     */
    public long getClientSserverLatency(){
        return c_s_latency;
    }

    /**
     *
     * @param n
     */
    public void setCurPlayers(String n){
        curPlayers = n;
    }

    /**
     *
     * @return
     */
    public String getCurPlayers(){
        return curPlayers;
    }

    /**
     *
     * @return
     */
    public int getClientHighscore(){
        return clienthighscore;
    }

    /**
     *
     * @param h
     */
    public void setClientHighscore(int h){
        clienthighscore = h;
    }
}
