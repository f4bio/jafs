/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.net;

import java.util.Hashtable;

/**
 *
 * @author miracle
 */
public class Protocol {
    private static Hashtable<String, int[]> cmd = new Hashtable<String, int[]>();

    public static final int argByte = 0;
    public static final int argShort = 1;
    public static final int argInt = 2;
    public static final int argLong = 3;
    public static final int argFloat = 4;
    public static final int argDouble = 5;
    public static final int argString = 6;
    public static final int argNull = 7;

    public static final String argSeperator = ";";

    public static int[] registerCmd(String command, int... arg) {
        return cmd.put(command, arg);
    }

    public static String buildPacket(String command, Object... o) {
        if(!cmd.containsKey(command) || cmd.get(command).length != o.length)
            return null;

        String packet = command;

        for(Object t : o) {
            packet += argSeperator + t;
        }

        return packet;
    }

    public static int getArgSize(String command) {
        int[] args = cmd.get(command);
        if(args != null)
            return args.length;
        return -1;
    }

    public static int[] getArgType(String command) {
        return cmd.get(command);
    }

    public static boolean containsCmd(String command) {
        return cmd.containsKey(command);
    }

    public static void init() {
        //----- Client commands
        registerCmd("test", argString, argInt, argDouble);

        //----- Server commands

        //----- Master Server commands
    }
}
