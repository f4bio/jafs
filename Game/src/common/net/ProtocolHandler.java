package common.net;

import common.CLog;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;

/**
 *
 * @author miracle
 */
public abstract class ProtocolHandler implements Runnable {
    protected Network net;
    protected Thread thread;
    protected boolean ready;

    public ProtocolHandler(Network net) {
        this.net = net;
        ready = false;
        thread = new Thread(this);
        thread.start();
    }

    private void waiting() {
        synchronized(thread) {
            try {
                while(!ready)
                    thread.wait();
            } catch(Exception e) {

            }
        }
    }

    public void wakeUp() {
        if(!ready) {
            ready = true;

            synchronized(thread) {
                thread.notify();
            }
        }
    }

    public void run() {
        Packet dPacket;
        InetSocketAddress dAddress;

        while(true) {
            dPacket = net.getPacket();

            if(dPacket == null) {
                ready = false;
                waiting();
                continue;
            }

            String[] sPacket = dPacket.getPacket();
            dAddress = dPacket.getAddress();

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
                    CLog.log("Invalid packet sent by " + dAddress.getHostName()
                            + ":" + dAddress.getPort());
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

    public abstract void noReplyReceived(Packet p);
}
