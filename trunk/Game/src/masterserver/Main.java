/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package masterserver;

import common.net.Network;
import common.net.Protocol;
import common.net.Server;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 *
 * @author miracle
 */
public class Main {
    public static int maxPingFailures = 2;
    public static final int pingRefreshInterval = 5000;

    private static Network net;
    private static ProtocolHandler handler;
    private static Vector<Server> serverlist = new Vector<Server>();

    private static TimerTask pinger = new TimerTask() {
        public void run() {
            int failures;

            for(int i=0; i<serverlist.size(); ++i) {
                Server cur = serverlist.get(i);
                cur.increasePingFailureCnt();
                failures = cur.getPingFailureCnt();

                if(failures >= maxPingFailures) {
                    removeServer(cur);
                    continue;
                }
                
                net.send(Protocol.buildPacket("master_server_ping", new Object[0]), cur.getAddress());
            }
        }
    };
    private static Timer pingTimer;

    public static void main(String[] args) {
        net = new Network();
        net.listen();
        handler = new ProtocolHandler(net);
        Protocol.init();

        pingTimer = new Timer();
        pingTimer.schedule(pinger, pingRefreshInterval, pingRefreshInterval);
    }

    public static Server addServer(String host, Integer port) {
        for(int i=0; i<serverlist.size(); ++i) {
            Server cur = serverlist.get(i);
            if(cur.getHost().equals(host) && cur.getPort() == port)
                return null;
        }

        Server serv = new Server(host, port);
        serverlist.add(serv);

        System.out.println("Server " + serv.getHost() + ":" + serv.getPort() + " listed." );
        
        return serv;
    }

    public static void removeServer(Server server) {
        serverlist.remove(server);

        System.out.println("Server " + server.getHost() + ":" + server.getPort() + " dropped." );
    }

    public static Server getServer(String host, int port) {
        for(int i=0; i<serverlist.size(); ++i) {
            Server cur = serverlist.get(i);

            if(cur.getHost().equals(host) && cur.getPort() == port)
                return cur;
        }

        return null;
    }

    public static String getServerlist() {
        String list = "";
        String seperator = " ";
        Server current = null;

        for(int i=0; i<serverlist.size(); ++i) {
            current = serverlist.get(i);
            list += current.getHost() + ":" + current.getPort();
            if(i < serverlist.size() - 1)
                list += seperator;
        }

        return list;
    }

    public static void decreasePingFailures(String server, int port) {
        Server serv = getServer(server, port);

        if(serv != null) {
            serv.decreasePingFailureCnt();
        }
    }
}