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
            this.thread.start();
        }

        public void run() {
            while(socket != null) {
                DatagramPacket p = outQueue.poll();
                if(p != null) {
                    try {
                        socket.send(p);
                    } catch(Exception e) {

                    }
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

    public void send(String packet) {
        DatagramPacket p = null;
        try {
            p = new DatagramPacket(packet.getBytes(), packet.length(), dest);
            outQueue.add(p);
        } catch(SocketException e) {

        }
    }

    public void send(String packet, InetSocketAddress destination) {
        DatagramPacket p = null;
        try {
            p = new DatagramPacket(packet.getBytes(), packet.length(), destination);
            outQueue.add(p);
        } catch(SocketException e) {

        }
    }
    
    public String getPacket() {
        DatagramPacket p = inQueue.poll();
        if(p != null)
            return new String(p.getData());
        return null;
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

    public void listen() {
        try {
            socket = new DatagramSocket(masterPort);
            nIn = new NetworkReader();
            nOut = new NetworkWriter();
        } catch(SocketException e) {
            e.printStackTrace();
            disconnect();
        }
    }
}
