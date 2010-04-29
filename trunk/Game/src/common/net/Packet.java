/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.net;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

/**
 *
 * @author miracle
 */
public class Packet {
    /**
     *
     */
    public static final byte CHECK_COUNTER = 5;

    private DatagramPacket p;
    private byte cmd;
    private byte ttl;
    private byte resent;

    /**
     *
     * @param packet
     */
    public Packet(DatagramPacket packet) {
        if(packet != null) {
            p = packet;
            cmd = p.getData()[0];
        }
        resent = Network.RESEND_COUNT;
        ttl = CHECK_COUNTER;
    }

    /**
     *
     * @return
     */
    public byte getCmd() {
        return cmd;
    }

    /**
     *
     * @return
     */
    public DatagramPacket getDatagram() {
        return p;
    }
    
    /**
     *
     */
    public void resetTimeToLive() {
        ttl = CHECK_COUNTER;
    }

    /**
     *
     */
    public void decreaseTimeToLive() {
        ttl--;
    }

    /**
     *
     * @return
     */
    public boolean hasTimeToLive() {
        if(ttl < 0)
            return false;
        return true;
    }

    /**
     *
     * @return
     */
    public byte decreaseResentCounter() {
        return --resent;
    }

    /**
     *
     * @return
     */
    public InetSocketAddress getAddress() {
        return (InetSocketAddress)p.getSocketAddress();
    }

    /**
     *
     * @param packet
     * @return
     */
    public boolean equals(DatagramPacket packet) {
        byte[] ipOwn = p.getAddress().getAddress();
        byte[] ipOther = packet.getAddress().getAddress();

        for(int i=0; i<ipOwn.length; ++i) {
            if(ipOwn[i] != ipOther[i])
                return false;
        }

        if(p.getData()[0] != packet.getData()[0])
            return false;

        if(p.getPort() != packet.getPort())
            return false;

        return true;
    }
}
