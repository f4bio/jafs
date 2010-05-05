package common.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author miracle
 */
public class Network {
    /**
     *
     */
    public static String MASTERHOST = "localhost";
    /**
     *
     */
    public static int MASTERPORT = 30000;

    /**
     *
     */
    public static final int RESEND_COUNT = 5;
    /**
     *
     */
    public static final int RESEND_INTERVAL = 500;

    private HashMap<InetSocketAddress, Byte> counterTable;

    private InetSocketAddress dest;
    private ProtocolHandler handler;

    private static final int packetLength = 512;
    private int port;

    private TimerTask failCheck = new TimerTask() {
        Packet p;
        //Iterator<Packet> t;

        public void run() {
            synchronized(replyLock) {
                //t = replyQueue.iterator();

                //while (t.hasNext()) {
                for(int i=0; i<replyQueue.size(); ++i) {
                    p = replyQueue.get(i);

                    if (!p.hasTimeToLive()) {
                        send(p.getDatagram(), false);

                        if (p.decreaseResentCounter() == 0) {
                            handler.noReplyReceived(p);
                            replyQueue.remove(i);
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
            //Iterator<Packet> i;

            try {
                while(socket != null) {
                    packet = new DatagramPacket(new byte[packetLength], packetLength);
                    socket.receive(packet);

                    if(handler != null) {
                        byte cmd = packet.getData()[0];

                        if(Protocol.isReplyById(cmd)) {
                            synchronized(replyLock) {
                                //i = replyQueue.iterator();

                                //while(i.hasNext()) {
                                for(int i=0; i<replyQueue.size(); ++i) {
                                    rPacket = replyQueue.get(i);
                                    if(rPacket.checkReply(packet))
                                        replyQueue.remove(i);
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
        private final Object threadLock = new Object();
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

    private LinkedList<Packet> replyQueue =
            new LinkedList<Packet>();
    private final Object replyLock = new Object();

    private DatagramSocket socket;
    private NetworkReader nIn;
    private NetworkWriter nOut;
    private boolean sConnected;
    private boolean connected;

    /**
     *
     */
    public Network() {
        dest =  null;
        nIn = null;
        nOut = null;
        socket = null;
        connected = false;
        sConnected = false;
        counterTable = new HashMap<InetSocketAddress, Byte>();

        checker = new Timer();
        checker.scheduleAtFixedRate(failCheck, RESEND_INTERVAL, RESEND_INTERVAL);
    }

    private synchronized void handleCounterTable(InetSocketAddress adr) {
        if(!counterTable.containsKey(adr)) {
            counterTable.put(adr, Byte.MIN_VALUE);
        } else {
            Byte val = counterTable.get(adr);
            if(val == Byte.MAX_VALUE) {
                counterTable.put(adr, Byte.MIN_VALUE);
            } else {
                counterTable.put(adr, (byte)(val + 1));
            }
        }
    }

    public synchronized void removeCounter(InetSocketAddress adr) {
        counterTable.remove(adr);
    }

    private synchronized void send(DatagramPacket packet, boolean check) {
        if(check && Protocol.hasReplyById(packet.getData()[0])) {
            synchronized(replyLock) {
                replyQueue.add(new Packet(packet));
            }
        }

        outQueue.add(packet);
        nOut.wakeUp();
    }

    /**
     *
     * @param cmd
     * @param c
     * @return
     */
    public synchronized boolean send(ProtocolCmd cmd, byte[]... c) {
        handleCounterTable(dest);
        return send(cmd, counterTable.get(dest), c);
    }

    /**
     *
     * @param cmd
     * @param count
     * @param c
     * @return
     */
    public synchronized boolean send(ProtocolCmd cmd, byte count, byte[]... c) {
        byte[] packet = Protocol.buildPacket(cmd, count, c);

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

    /**
     *
     * @param destination
     * @param cmd
     * @param c
     * @return
     */
    public synchronized boolean send(InetSocketAddress destination, ProtocolCmd cmd,
            byte[]... c) {
        handleCounterTable(destination);
        return send(destination, cmd, counterTable.get(destination), c);
    }

    /**
     *
     * @param destination
     * @param cmd
     * @param c
     * @return
     */
    public synchronized boolean send(InetSocketAddress destination, ProtocolCmd cmd,
            byte count, byte[]... c) {
        byte[] packet = Protocol.buildPacket(cmd, count, c);

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

    /**
     *
     * @param host
     * @param port
     * @param cmd
     * @param c
     * @return
     */
    public synchronized boolean send(String host, int port, ProtocolCmd cmd,
            byte[]... c) {
        InetSocketAddress destination = new InetSocketAddress(host, port);
        return send(destination, cmd, c);
    }

    /**
     *
     * @param host
     * @param port
     * @param cmd
     * @param c
     * @return
     */
    public synchronized boolean send(String host, int port, ProtocolCmd cmd,
            byte count, byte[]... c) {
        InetSocketAddress destination = new InetSocketAddress(host, port);
        return send(destination, cmd, count, c);
    }
   
    /**
     *
     * @return
     */
    public DatagramPacket getPacket() {
        return inQueue.poll();
    }

    /**
     *
     */
    public void clear() {
        inQueue.clear();
        outQueue.clear();
        replyQueue.clear();
    }

    /**
     *
     */
    public void clearReplyQueue() {
        synchronized(replyLock) {
            replyQueue.clear();
        }
    }

    /**
     *
     * @param con
     */
    public void setReallyConnected(boolean con) {
        sConnected = con;
    }

    /**
     *
     * @return
     */
    public boolean isReallyConnected() {
        return sConnected;
    }

    /**
     *
     */
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

    /**
     *
     * @return
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     *
     * @param server
     * @param port
     */
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

    /**
     *
     * @param adr
     */
    public void setServer(InetSocketAddress adr) {
        dest = adr;
    }

    /**
     *
     * @return
     */
    public InetSocketAddress getServer() {
        return dest;
    }

    /**
     *
     */
    public void connect() {
        connect(MASTERHOST, MASTERPORT);
    }

    /**
     *
     * @return
     */
    public int getPort(){
        return port;
    }

    /**
     *
     * @return
     */
    public String getHost(){
        return MASTERHOST;
    }

    /**
     *
     * @param port
     */
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

    /**
     *
     * @param handler
     */
    public void setProtocolHandler(ProtocolHandler handler) {
        this.handler = handler;
    }

    /**
     *
     * @param from
     * @param to
     * @return
     */
    public static int getFreePort(int from, int to){
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

