/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.net;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
        while(true) {
            String packet = net.getPacket();

            if(packet == null) {
                sleep();
                continue;
            }

            String[] sPacket = packet.split(Protocol.argSeperator);

            if(!Protocol.containsCmd(sPacket[0])) {
                sleep();
                continue;
            }

            Object[] param = new Object[Protocol.getArgSize(sPacket[0])];
            Class[] sig = new Class[param.length];
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
                        case Protocol.argNull:
                            sig = new Class[0];
                            param = new Object[0];
                            break;

                        default:
                    }

                    sig[i] = param[i].getClass();

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

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
