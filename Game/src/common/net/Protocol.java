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

    public static final String argSeperator = ";";

    public static final int argByte = 0;
    public static final int argShort = 1;
    public static final int argInt = 2;
    public static final int argLong = 3;
    public static final int argFloat = 4;
    public static final int argDouble = 5;
    public static final int argString = 6;
    public static final int argNone = 7;

    //----- Client commands
    public static final String client_master_listrequest = "c_m_listrequest";

    //----- Server commands
    public static final String server_master_pong = "s_m_pong";
    public static final String server_master_auth = "s_m_auth";

    //----- Master Server commands
    public static final String master_server_auth_success = "m_s_auth_success";
    public static final String master_server_auth_failure = "m_s_auth_failure";
    public static final String master_server_ping = "m_s_ping";
    public static final String master_client_newlist = "m_c_newlist";
    public static final String master_client_listentry = "m_c_listentry";
    public static final String master_client_endlist = "m_c_endlist";

    public static int[] registerCmd(final String command, int... arg) {
        return cmd.put(command, arg);
    }

    public static String buildPacket(final String command, Object... o) {
        if(cmd.containsKey(command) && getArgType(command)[0] == argNone)
            return command;

        if(!cmd.containsKey(command) || cmd.get(command).length != o.length)
            return null;

        String packet = command;

        for(Object t : o) {
            packet += argSeperator + t;
        }

        return packet;
    }

    public static int getArgSize(final String command) {
        int[] args = cmd.get(command);
        if(args != null)
            return args.length;
        return -1;
    }

    public static int[] getArgType(final String command) {
        return cmd.get(command);
    }

    public static boolean containsCmd(final String command) {
        return cmd.containsKey(command);
    }

    public static void init() {
        //----- Client commands
        registerCmd(client_master_listrequest, argNone);

        //----- Server commands
        registerCmd(server_master_pong, argNone);
        registerCmd(server_master_auth, argNone);

        //----- Master Server commands
        registerCmd(master_server_auth_success, argNone);
        registerCmd(master_server_auth_failure, argNone);
        registerCmd(master_server_ping, argNone);
        registerCmd(master_client_newlist, argNone);
        registerCmd(master_client_listentry, argString);
        registerCmd(master_client_endlist, argNone);
    }
}
