/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author miracle
 */
public class Network {
    public static final String HOST = "x.org";
    public static final int MASTERPORT = 30000;

    public static final int RESEND_COUNT = 5;
    public static final int RESEND_INTERVAL = 1000;

    private InetSocketAddress dest;
    private ProtocolHandler handler;

    private static final int packetLength = 256;
    private int port;

    private TimerTask failCheck = new TimerTask() {

        public void run() {
            Packet p;
            Iterator<Packet> t;

            synchronized(replyQueue) {
                t = replyQueue.iterator();

                while(t.hasNext()) {
                    p = t.next();

                    if(!p.hasTimeToLive()) {
                        handler.noReplyReceived(p);
                        t.remove();
                    } else {
                        send(p.getCmd(), p.getDatagram(), false);
                        p.decreaseTimeToLive();
                    }
                }
            }
        }
    };

    private static Timer checker;

    private class NetworkReader implements Runnable {
        private Thread thread;

        private NetworkReader() {
            this.thread = new Thread(this);
            this.thread.setDaemon(true);
            this.thread.start();
        }

        public void run() {
            DatagramPacket packet;
            Packet pPacket;
            Packet rPacket;
            Iterator<Packet> i;

            try {
                while(socket != null) {
                    packet = new DatagramPacket(new byte[packetLength], packetLength);
                    socket.receive(packet);

                    pPacket = new Packet(packet);

                    if(isValid(pPacket) && handler != null) {
                        synchronized(replyQueue) {
                            i = replyQueue.iterator();

                            while(i.hasNext()) {
                                rPacket = i.next();
                                if(pPacket.getCmd().equals(Protocol.getReplyOfCmd(rPacket.getCmd()))) {
                                    i.remove();
                                }
                            }
                        }

                        inQueue.add(pPacket);
                        handler.wakeUp();
                    }
                }
            } catch(Exception e) {

            }
        }
    }

    private class NetworkWriter implements Runnable {
        private Thread thread;
        private boolean ready;

        private NetworkWriter() {
            this.thread = new Thread(this);
            this.thread.setDaemon(true);
            this.thread.start();
        }

        private void waiting() {
            synchronized(thread) {
                try {
                    while(!ready) {
                        thread.wait();
                    }
                } catch (Exception e) {

                }
            }
        }

        public void wakeUp() {
            if (!ready) {
                ready = true;

                synchronized(thread) {
                    thread.notify();
                }
            }
        }

        public void run() {
            DatagramPacket curPacket = null;
            
            while(socket != null) {
                curPacket = outQueue.poll();

                if(curPacket == null) {
                    ready = false;
                    waiting();
                    continue;
                }

                try {
                    socket.send(curPacket);
                } catch (Exception e) {

                }
            }
        }
    }

    private ConcurrentLinkedQueue<Packet> inQueue =
            new ConcurrentLinkedQueue<Packet>();

    private ConcurrentLinkedQueue<DatagramPacket> outQueue =
            new ConcurrentLinkedQueue<DatagramPacket>();

    private final ArrayList<Packet> replyQueue =
            new ArrayList<Packet>();

    private DatagramSocket socket;
    private NetworkReader nIn;
    private NetworkWriter nOut;
    private boolean connected;

    public Network() {
        dest =  null;
        nIn = null;
        nOut = null;
        socket = null;
        connected = false;

        checker = new Timer();
        checker.schedule(failCheck, RESEND_INTERVAL, RESEND_INTERVAL);
    }

    private boolean isValid(Packet packet) {
        String[] p = packet.getPacket();

        if(Protocol.containsCmd(p[0]) && Protocol.getArgSize(p[0]) == p.length - 1) {
            return true;
        }

        return false;
    }

    private void send(String cmd, DatagramPacket packet, boolean check) {
        if(check && Protocol.hasReply(cmd)) {
            Iterator<Packet> i;
            Packet p;

            synchronized(replyQueue) {
                i = replyQueue.iterator();

                while(i.hasNext()) {
                    p = i.next();

                    if(p.equals(packet)) {
                        return;
                    }
                }

                replyQueue.add(new Packet(packet));
            }
        }

        outQueue.add(packet);
        nOut.wakeUp();
    }

    public boolean send(final String cmd, Object... o) {
        String packet = Protocol.buildPacket(cmd, o);

        if(packet == null)
            return false;

        DatagramPacket p = null;
        try {
            p = new DatagramPacket(packet.getBytes(), packet.length(), dest);

            send(cmd, p, true);
        } catch(SocketException e) {
            return false;
        }

        return true;
    }

    public boolean send(InetSocketAddress destination, final String cmd, Object... o) {
        String packet = Protocol.buildPacket(cmd, o);

        if(destination == null || packet == null)
             return false;

        DatagramPacket p = null;
        try {
            p = new DatagramPacket(packet.getBytes(), packet.length(), destination);

            send(cmd, p, true);
        } catch(SocketException e) {
            return false;
        }

        return true;
    }

    public boolean send(String host, int port, final String cmd, Object... o) {
        InetSocketAddress destination = new InetSocketAddress(host, port);
        return send(destination, cmd, o);
    }
   
    public Packet getPacket() {
        return inQueue.poll();
    }

    public void disconnect() {
        if(connected) {
            inQueue.clear();
            outQueue.clear();
            replyQueue.clear();

            socket.close();
            socket = null;

            nIn = null;
            nOut = null;

            connected = false;
        }
    }

    public void connect(String server, int port) {
        dest = new InetSocketAddress(server, port);

        disconnect();

        try {
            socket.connect(dest);
            connected = true;
        } catch(SocketException e) {
            connected = false;
        }

        if(connected) {
            nIn = new NetworkReader();
            nOut = new NetworkWriter();
        }
    }

    public void connect() {
        connect(HOST, MASTERPORT);
    }

    public int getPort(){
        return port;
    }
    public String getHost(){
        return HOST;
    }
    public void listen(int port) {
        try {
            this.port = port;
            socket = new DatagramSocket(port);
            nIn = new NetworkReader();
            nOut = new NetworkWriter();
        } catch(SocketException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    public void setProtocolHandler(ProtocolHandler handler) {
        this.handler = handler;
    }
}

