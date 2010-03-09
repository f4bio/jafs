/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
    private int curPlayers;
    private int latency;
    private InetSocketAddress address;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
        address = new InetSocketAddress(host, port);
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
}
