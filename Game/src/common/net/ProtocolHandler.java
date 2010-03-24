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
        //thread.setDaemon(true);
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

            String[] sPacket = new String(dPacket.getData(), 0, dPacket.getLength()).split(Protocol.ARG_SEPERATOR);

            dAddress = (InetSocketAddress)dPacket.getSocketAddress();

            if(!Protocol.containsCmd(sPacket[0])) {
                sleep();
                continue;
            }

            Object[] param = new Object[Protocol.getArgSize(sPacket[0]) + 1];
            Class[] sig = new Class[param.length];
            int[] type = Protocol.getArgType(sPacket[0]);

            for(int i=0; i<type.length; ++i) {
                try {
                    switch(type[i]) {
                        case Protocol.ARG_BYTE:
                            param[i] = Byte.parseByte(sPacket[i+1]);
                            sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_SHORT:
                            param[i] = Short.parseShort(sPacket[i+1]);
                            sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_INT:
                            param[i] = Integer.parseInt(sPacket[i+1]);
                            sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_LONG:
                            param[i] = Long.parseLong(sPacket[i+1]);
                            sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_FLOAT:
                            param[i] = Float.parseFloat(sPacket[i+1]);
                            sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_DOUBLE:
                            param[i] = Double.parseDouble(sPacket[i+1]);
                            sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_STRING:
                            param[i] = sPacket[i+1];
                            sig[i] = param[i].getClass();
                            break;
                        case Protocol.ARG_NONE:
                            sig = new Class[1];
                            param = new Object[1];
                            break;

                        default:
                    }

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
