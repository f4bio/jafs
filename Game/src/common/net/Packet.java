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
    private String[] sPacket;
    private InetSocketAddress adr;
    private int ttl;

    public Packet(DatagramPacket packet) {
        if(packet != null) {
            sPacket = new String(packet.getData(), 0, packet.getLength()).split(Protocol.ARG_SEPERATOR);
            adr = (InetSocketAddress)packet.getSocketAddress();
        }

        ttl = Network.RESEND_COUNT;
    }

    public String[] getPacket() {
        return sPacket;
    }

    public String getCmd() {
        return sPacket[0];
    }

    public InetSocketAddress getAddress() {
        return adr;
    }
    
    public void resetTimeToLive() {
        ttl = Network.RESEND_COUNT;
    }

    public void decreaseTimeToLive() {
        ttl--;
    }

    public boolean hasTimeToLive() {
        if(ttl < 0)
            return false;
        return true;
    }
}
