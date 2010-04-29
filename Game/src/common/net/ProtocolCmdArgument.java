package common.net;

import java.nio.ByteBuffer;

/**
 *
 * @author miracle
 */
public class ProtocolCmdArgument {
    /**
     *
     */
    public static final String charset = "US-ASCII";

    /**
     *
     * @param b
     * @param from
     * @return
     */
    public static int terminatorIndex(byte[] b, int from) {
        for(int i=from; i<b.length; ++i) {
            if(b[i] == Protocol.STRING_TERMINATOR)
                return i;
        }
        return -1;
    }

    /**
     *
     * @param b
     * @param from
     * @return
     */
    public static String toStr(byte[] b, int from) {
        String str = null;
        try {
            str = new String(b, from, terminatorIndex(b, from) - from, charset);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param str
     * @return
     */
    public static byte[] argStr(String str) {
        if(str == null) {
            return new byte[] { Protocol.STRING_TERMINATOR };
        }
        
        byte[] ret = new byte[str.length() + 1];

        for(int i=0; i<str.length(); ++i) {
            char c = str.charAt(i);
            if(c < 32 || c > 127)
                ret[i] = 42;
            else
                ret[i] = (byte)c;
        }

        ret[str.length()] = Protocol.STRING_TERMINATOR;

        return ret;
    }

    /**
     *
     * @param b
     * @param from
     * @return
     */
    public static double toDouble(byte[] b, int from) {
        if(b.length - from < 8)
            throw new IllegalArgumentException("Range *has* to be 8 bytes");

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(b, from, 8);
        return buffer.getDouble(0);
    }

    /**
     *
     * @param d
     * @return
     */
    public static byte[] argDouble(double d) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putDouble(d);
        byte[] ret = buffer.array();
        buffer.clear();

        return ret;
    }

    /**
     *
     * @param b
     * @param from
     * @return
     */
    public static float toFloat(byte[] b, int from) {
        if(b.length - from < 4)
            throw new IllegalArgumentException("Range *has* to be 4 bytes");

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(b, from, 4);

        return buffer.getFloat(0);
    }

    /**
     *
     * @param f
     * @return
     */
    public static byte[] argFloat(float f) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(f);
        byte[] ret = buffer.array();
        buffer.clear();

        return ret;
    }

    /**
     *
     * @param b
     * @param from
     * @return
     */
    public static long toLong(byte[] b, int from) {
        if(b.length - from < 8)
            throw new IllegalArgumentException("Range *has* to be 8 bytes");

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(b, from, 8);

        return buffer.getLong(0);
    }

    /**
     *
     * @param l
     * @return
     */
    public static byte[] argLong(long l) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(l);
        byte[] ret = buffer.array();
        buffer.clear();

        return ret;
    }

    /**
     *
     * @param b
     * @param from
     * @return
     */
    public static int toInt(byte[] b, int from) {
        if(b.length - from < 4)
            throw new IllegalArgumentException("Range *has* to be 4 bytes");

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(b, from, 4);

        return buffer.getInt(0);
    }

     /**
      *
      * @param i
      * @return
      */
     public static byte[] argInt(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(i);
        byte[] ret = buffer.array();
        buffer.clear();

        return ret;
    }

    /**
     *
     * @param b
     * @param from
     * @return
     */
    public static short toShort(byte[] b, int from) {
        if(b.length - from < 2)
            throw new IllegalArgumentException("Range *has* to be 2 bytes");

        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put(b, from, 2);

        return buffer.getShort(0);
    }

    /**
     *
     * @param s
     * @return
     */
    public static byte[] argShort(short s) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(s);
        byte[] ret = buffer.array();
        buffer.clear();

        return ret;
    }
}
