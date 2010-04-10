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
    public static final byte CHECK_COUNTER = 5;

    private DatagramPacket p;
    private byte cmd;
    private byte ttl;
    private byte resent;

    public Packet(DatagramPacket packet) {
        if(packet != null) {
            p = packet;
            cmd = p.getData()[0];
        }
        resent = Network.RESEND_COUNT;
        ttl = CHECK_COUNTER;
    }

    public byte getCmd() {
        return cmd;
    }

    public DatagramPacket getDatagram() {
        return p;
    }
    
    public void resetTimeToLive() {
        ttl = CHECK_COUNTER;
    }

    public void decreaseTimeToLive() {
        ttl--;
    }

    public boolean hasTimeToLive() {
        if(ttl < 0)
            return false;
        return true;
    }

    public byte decreaseResentCounter() {
        return --resent;
    }

    public InetSocketAddress getAddress() {
        return (InetSocketAddress)p.getSocketAddress();
    }
}
