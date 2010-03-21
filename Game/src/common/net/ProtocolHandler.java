/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.net;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

/**
 *
 * @author miracle
 */
public abstract class ProtocolHandler implements Runnable {
    protected Network net;
    protected Thread thread;

    public ProtocolHandler(Network net) {
        this.net = net;
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void sleep() {
        try {
            Thread.sleep(1);
        } catch(Exception e) {

        }
    }

    public void run() {
        DatagramPacket dPacket;
        InetSocketAddress dAddress;
        while(true) {
            dPacket = net.getPacket();

            if(dPacket == null) {
                sleep();
                continue;
            }

            String[] sPacket = new String(dPacket.getData(), 0, dPacket.getLength()).split(Protocol.argSeperator);
            dAddress = (InetSocketAddress)dPacket.getSocketAddress();

            if(!Protocol.containsCmd(sPacket[0])) {
                sleep();
                continue;
            }

            Object[] param = new Object[Protocol.getArgSize(sPacket[0]) + 1];
            Class[] sig = new Class[param.length + 1];
            int[] type = Protocol.getArgType(sPacket[0]);

            for(int i=0; i<type.length; ++i) {
                try {
                    sPacket[i+1] = sPacket[i+1].trim();
                    switch(type[i]) {
                        case Protocol.argByte:
                            param[i] = Byte.parseByte(sPacket[i+1]);
                            break;
                        case Protocol.argShort:
                            param[i] = Short.parseShort(sPacket[i+1]);
                            break;
                        case Protocol.argInt:
                            param[i] = Integer.parseInt(sPacket[i+1]);
                            break;
                        case Protocol.argLong:
                            param[i] = Long.parseLong(sPacket[i+1]);
                            break;
                        case Protocol.argFloat:
                            param[i] = Float.parseFloat(sPacket[i+1]);
                            break;
                        case Protocol.argDouble:
                            param[i] = Double.parseDouble(sPacket[i+1]);
                            break;
                        case Protocol.argString:
                            param[i] = sPacket[i+1];
                            break;
                        case Protocol.argNone:
                            sig = new Class[1];
                            param = new Object[1];
                            break;

                        default:
                    }

                    sig[i] = param[i].getClass();

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            param[param.length - 1] = dAddress;
            sig[sig.length - 1] = dAddress.getClass();

            Method method = null;
            try {
                method = this.getClass().getDeclaredMethod(sPacket[0], sig);

                if(Modifier.isPrivate(method.getModifiers())) {
                    method.setAccessible(true);
                }

                method.invoke(this, param);
            } catch(NoSuchMethodException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
