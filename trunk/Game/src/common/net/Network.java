/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author miracle
 */
public class Network {
    public static final String masterHost = "x.org";
    public static final int masterPort = 31338;

    private InetSocketAddress dest;

    private static final int packetLength = 256;

    private class NetworkReader implements Runnable {
        private Thread thread;

        private NetworkReader() {
            this.thread = new Thread(this);
            this.thread.setDaemon(true);
            this.thread.start();
        }

        public void run() {
            try {
                while(socket != null) {
                    DatagramPacket packet = new DatagramPacket(new byte[packetLength], packetLength);
                    socket.receive(packet);
                    inQueue.add(packet);
                }
            } catch(Exception e) {

            }
        }
    }

    private class NetworkWriter implements Runnable {
        private Thread thread;

        private NetworkWriter() {
            this.thread = new Thread(this);
            this.thread.setDaemon(true);
            this.thread.start();
        }

        public void run() {
            DatagramPacket curPacket = null;
            
            while(socket != null) {
                for(int i=0; i<outQueue.size(); i++) {
                    curPacket = outQueue.poll();
                    if(curPacket != null) {
                        try {
                            socket.send(curPacket);
                        } catch(Exception e) {

                        }
                    }
                }

                try {
                    Thread.sleep(1);
                } catch(Exception e) {

                }
            }
        }
    }

    private ConcurrentLinkedQueue<DatagramPacket> inQueue =
            new ConcurrentLinkedQueue<DatagramPacket>();
    private ConcurrentLinkedQueue<DatagramPacket> outQueue =
            new ConcurrentLinkedQueue<DatagramPacket>();

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
    }

    public boolean send(final String cmd, Object... o) {
        String packet = Protocol.buildPacket(cmd, o);

        if(packet == null)
            return false;

        DatagramPacket p = null;
        try {
            p = new DatagramPacket(packet.getBytes(), packet.length(), dest);
            outQueue.add(p);
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
            outQueue.add(p);
        } catch(SocketException e) {
            return false;
        }

        return true;
    }

    public boolean send(String host, int port, final String cmd, Object... o) {
        InetSocketAddress destination = new InetSocketAddress(host, port);
        return send(destination, cmd, o);
    }
   
    public DatagramPacket getPacket() {
        DatagramPacket p = inQueue.poll();
        return p;
    }
   
    public void addTestPacket(String packet) {
        DatagramPacket p = null;
        try {
            p = new DatagramPacket(packet.getBytes(), packet.length());
            inQueue.add(p);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if(connected) {
            inQueue.clear();
            outQueue.clear();

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
        connect(masterHost, masterPort);
    }

    public void listen(int port) {
        try {
            socket = new DatagramSocket(port);
            nIn = new NetworkReader();
            nOut = new NetworkWriter();
        } catch(SocketException e) {
            e.printStackTrace();
            disconnect();
        }
    }
}

