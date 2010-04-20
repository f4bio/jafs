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
    public static final String MASTERHOST = "localhost";
    public static final int MASTERPORT = 30000;

    public static final int RESEND_COUNT = 5;
    public static final int RESEND_INTERVAL = 500;

    private InetSocketAddress dest;
    private ProtocolHandler handler;

    private static final int packetLength = 512;
    private int port;

    private TimerTask failCheck = new TimerTask() {
        Packet p;
        Iterator<Packet> t;

        public void run() {
            synchronized(replyQueue) {
                t = replyQueue.iterator();

                while(t.hasNext()) {
                    p = t.next();

                    if(!p.hasTimeToLive()) {
                        send(p.getDatagram(), false);

                        if(p.decreaseResentCounter() == 0) {
                            t.remove();
                            handler.noReplyReceived(p);
                        } else {
                            p.resetTimeToLive();
                        }
                    } else {
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
            Packet rPacket;
            Iterator<Packet> i;

            try {
                while(socket != null) {
                    packet = new DatagramPacket(new byte[packetLength], packetLength);
                    socket.receive(packet);

                    if(handler != null) {
                        byte cmd = packet.getData()[0];

                        if(Protocol.isReplyById(cmd)) {
                            synchronized(replyQueue) {
                                i = replyQueue.iterator();

                                while(i.hasNext()) {
                                    rPacket = i.next();
                                    if(Protocol.getReplyOfCmdById(rPacket.getCmd()) == cmd) {
                                        i.remove();
                                    }
                                }
                            }
                        }

                        inQueue.add(packet);
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

    private ConcurrentLinkedQueue<DatagramPacket> inQueue =
            new ConcurrentLinkedQueue<DatagramPacket>();

    private ConcurrentLinkedQueue<DatagramPacket> outQueue =
            new ConcurrentLinkedQueue<DatagramPacket>();

    private final ArrayList<Packet> replyQueue =
            new ArrayList<Packet>();

    private DatagramSocket socket;
    private NetworkReader nIn;
    private NetworkWriter nOut;
    private boolean sConnected;
    private boolean connected;

    public Network() {
        dest =  null;
        nIn = null;
        nOut = null;
        socket = null;
        connected = false;
        sConnected = false;

        checker = new Timer();
        checker.schedule(failCheck, RESEND_INTERVAL, RESEND_INTERVAL);
    }

    private synchronized void send(DatagramPacket packet, boolean check) {
        if(check && Protocol.hasReplyById(packet.getData()[0])) {
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

//        System.out.println("Sending: " + Protocol.getCmdById(packet.getData()[0]));
        outQueue.add(packet);
        nOut.wakeUp();
    }

    public synchronized boolean send(ProtocolCmd cmd, byte[]... c) {
        byte[] packet = Protocol.buildPacket(cmd, c);

        if(packet == null)
            return false;

        DatagramPacket p = null;
        try {
            p = new DatagramPacket(packet, packet.length, dest);

            send(p, true);
        } catch(SocketException e) {
            return false;
        }

        return true;
    }

    public synchronized boolean send(InetSocketAddress destination, ProtocolCmd cmd,
            byte[]... c) {
        byte[] packet = Protocol.buildPacket(cmd, c);

        if(destination == null || packet == null)
             return false;

        DatagramPacket p = null;
        try {
            p = new DatagramPacket(packet, packet.length, destination);

            send(p, true);
        } catch(SocketException e) {
            return false;
        }

        return true;
    }

    public synchronized boolean send(String host, int port, ProtocolCmd cmd,
            byte[]... c) {
        InetSocketAddress destination = new InetSocketAddress(host, port);
        return send(destination, cmd, c);
    }
   
    public DatagramPacket getPacket() {
        return inQueue.poll();
    }

    public void clear() {
        inQueue.clear();
        outQueue.clear();
        replyQueue.clear();
    }

    public void clearReplyQueue() {
        synchronized(replyQueue) {
            replyQueue.clear();
        }
    }

    public void setReallyConnected(boolean con) {
        sConnected = con;
    }

    public boolean isReallyConnected() {
        return sConnected;
    }

    public void disconnect() {
        if(connected) {
            inQueue.clear();
            outQueue.clear();
            replyQueue.clear();

            socket.disconnect();

            nIn = null;
            nOut = null;

            connected = false;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void connect(String server, int port) {
        dest = new InetSocketAddress(server, port);
//
//        disconnect();
//
//        try {
//            socket.connect(dest);
//            connected = true;
//        } catch(SocketException e) {
//            connected = false;
//        }
    }

    public void setServer(InetSocketAddress adr) {
        dest = adr;
    }

    public InetSocketAddress getServer() {
        return dest;
    }

    public void connect() {
        connect(MASTERHOST, MASTERPORT);
    }

    public int getPort(){
        return port;
    }

    public String getHost(){
        return MASTERHOST;
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

    public int getFreePort(int from, int to){
        DatagramSocket testSocket;
            for(int i = from; i<to;i++)
                try{
                    testSocket = new DatagramSocket(i);
                    testSocket.close();
                    return i;
                }catch(java.net.SocketException se){}
        return -1;
    }
}

