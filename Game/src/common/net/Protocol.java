package common.net;

import java.util.Hashtable;

/**
 *
 * @author miracle
 */
public class Protocol {
    
    private static Hashtable<String, int[]> cmd = new Hashtable<String, int[]>();
    private static Hashtable<String, String> cmdReply = new Hashtable<String, String>();

    //----- Argument definitions
    public static final int ARG_BYTE = 0;
    public static final int ARG_SHORT = 1;
    public static final int ARG_INT = 2;
    public static final int ARG_LONG = 3;
    public static final int ARG_FLOAT = 4;
    public static final int ARG_DOUBLE = 5;
    public static final int ARG_STRING = 6;
    public static final int ARG_NONE = 7;

    public static final int REPLY_SUCCESS = 0;
    public static final int REPLY_FAILURE = 1;
    
    public static final String ARG_SEPERATOR = ";";

    //----- Client commands
    public static final String CLIENT_MASTER_JOINSERVER = "c_m_joinserver";
    public static final String CLIENT_MASTER_LISTREQUEST = "c_m_listrequest";
    public static final String CLIENT_MASTER_CHAT_LOBBY = "c_m_chat_lobby";
    public static final String CLIENT_MASTER_CHAT_PRIVATE = "c_m_chat_private";
    public static final String CLIENT_MASTER_AUTH = "c_m_auth";

    public static final String CLIENT_SERVER_AUTH = "c_s_auth";
    public static final String CLIENT_SERVER_PONG = "c_s_pong";
    public static final String CLIENT_SERVER_CLIENTCOUNT = "c_s_clientcount";
    public static final String CLIENT_SERVER_CLIENTID = "c_s_clientid";
    public static final String CLIENT_SERVER_CHAT_ALL = "c_s_chat_all";
    public static final String CLIENT_SERVER_CHAT_TEAM = "c_s_chat_team";
    public static final String CLIENT_SERVER_CHAT_PRIVATE = "c_s_chat_private";
    public static final String CLIENT_SERVER_LOGOFF = "c_s_logoff";
    public static final String CLIENT_SERVER_JOINTEAM = "c_s_jointeam";

    //----- Server commands
    public static final String SERVER_MASTER_PONG = "s_m_pong";
    public static final String SERVER_MASTER_AUTH = "s_m_auth";
    public static final String SERVER_MASTER_SERVERCOUNT = "s_m_servercount";
    
    public static final String SERVER_CLIENT_PING = "s_c_ping";
    public static final String SERVER_CLIENT_AUTH_REPLY = "s_c_auth_reply";
    public static final String SERVER_CLIENT_CLIENTCOUNT = "s_c_clientcount";
    public static final String SERVER_CLIENT_CLIENTID = "s_c_clientid";
    public static final String SERVER_CLIENT_CHAT = "s_c_chat";
    public static final String SERVER_CLIENT_LOGOFF_REPLY = "s_c_logoff_reply";
    public static final String SERVER_CLIENT_JOINTEAM_REPLY = "s_c_jointeam_reply";

    //----- Master Server commands
    public static final String MASTER_SERVER_AUTH_REPLY = "m_s_auth_reply";
    public static final String MASTER_SERVER_PING = "m_s_ping";
    public static final String MASTER_SERVER_SERVERCOUNT = "m_s_servercount";

    public static final String MASTER_CLIENT_JOINSERVER_REPLY = "m_c_joinserver_reply";
    public static final String MASTER_CLIENT_AUTH_REPLY = "m_c_auth_reply";
    public static final String MASTER_CLIENT_NEWLIST = "m_c_newlist";
    public static final String MASTER_CLIENT_LISTENTRY = "m_c_listentry";
    public static final String MASTER_CLIENT_ENDLIST = "m_c_endlist";
    public static final String MASTER_CLIENT_CHAT = "m_c_chat";

    public static int[] registerCmd(final String command, int... arg) {
        return cmd.put(command, arg);
    }

    public static String registerCmdReply(final String command, final String reply) {
        return cmdReply.put(command, reply);
    }

    public static boolean hasReply(final String command) {
        if(cmdReply.containsKey(command))
            return true;
        return false;
    }

    public static final String getReplyOfCmd(final String command) {
        return cmdReply.get(command);
    }

    public static String buildPacket(final String command, Object... o) {
        if(cmd.containsKey(command) && getArgType(command)[0] == ARG_NONE)
            return command;

        if(!cmd.containsKey(command) || cmd.get(command).length != o.length)
            return null;

        String packet = command;

        for(Object t : o) {
            packet += ARG_SEPERATOR + t;
        }

        return packet;
    }

    public static String buildPacket(final String command) {
        return buildPacket(command, new Object[0]);
    }

    public static int getArgSize(final String command) {
        int[] args = cmd.get(command);
        if(args != null) {
            if(args[0] != ARG_NONE)
                return args.length;
            else
                return 0;
        }
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
        registerCmd(CLIENT_MASTER_LISTREQUEST, ARG_NONE);
        // chat
        registerCmd(CLIENT_MASTER_CHAT_LOBBY, ARG_STRING);
        registerCmd(CLIENT_MASTER_CHAT_PRIVATE, ARG_INT, ARG_STRING);
        // ----
        registerCmd(CLIENT_MASTER_AUTH, ARG_NONE);
        registerCmd(CLIENT_MASTER_JOINSERVER, ARG_STRING, ARG_INT);

        registerCmd(CLIENT_SERVER_AUTH, ARG_NONE);
        registerCmd(CLIENT_SERVER_PONG, ARG_NONE);
        registerCmd(CLIENT_SERVER_CLIENTCOUNT,ARG_NONE);
        registerCmd(CLIENT_SERVER_CLIENTID,ARG_NONE);
        // chat
        registerCmd(CLIENT_SERVER_CHAT_ALL, ARG_STRING);
        registerCmd(CLIENT_SERVER_CHAT_TEAM, ARG_STRING);
        registerCmd(CLIENT_SERVER_CHAT_PRIVATE, ARG_INT, ARG_STRING);
        // ----
        registerCmd(CLIENT_SERVER_LOGOFF, ARG_NONE);
        registerCmd(CLIENT_SERVER_JOINTEAM, ARG_INT);

        //----- Server commands
        registerCmd(SERVER_MASTER_PONG, ARG_NONE);
        registerCmd(SERVER_MASTER_AUTH, ARG_NONE);
        registerCmd(SERVER_MASTER_SERVERCOUNT, ARG_NONE);
        registerCmd(SERVER_CLIENT_CLIENTID, ARG_INT);
        registerCmd(SERVER_CLIENT_CLIENTCOUNT, ARG_INT);
        registerCmd(SERVER_CLIENT_PING, ARG_NONE);
        registerCmd(SERVER_CLIENT_AUTH_REPLY, ARG_INT);
        registerCmd(SERVER_CLIENT_CHAT, ARG_STRING);
        registerCmd(SERVER_CLIENT_LOGOFF_REPLY, ARG_INT);
        registerCmd(SERVER_CLIENT_JOINTEAM_REPLY, ARG_INT);

        //----- Master Server commands
        registerCmd(MASTER_SERVER_AUTH_REPLY, ARG_INT);
        registerCmd(MASTER_SERVER_PING, ARG_NONE);
        registerCmd(MASTER_SERVER_SERVERCOUNT, ARG_INT);

        registerCmd(MASTER_CLIENT_JOINSERVER_REPLY, ARG_STRING);
        registerCmd(MASTER_CLIENT_AUTH_REPLY, ARG_INT);
        registerCmd(MASTER_CLIENT_NEWLIST, ARG_NONE);
        registerCmd(MASTER_CLIENT_LISTENTRY, ARG_STRING);
        registerCmd(MASTER_CLIENT_ENDLIST, ARG_NONE);
        registerCmd(MASTER_CLIENT_CHAT, ARG_STRING);

        //----- Associate commands with replies
        registerCmdReply(SERVER_MASTER_AUTH, MASTER_SERVER_AUTH_REPLY);
        registerCmdReply(CLIENT_MASTER_AUTH, MASTER_CLIENT_AUTH_REPLY);
        registerCmdReply(CLIENT_SERVER_AUTH, SERVER_CLIENT_AUTH_REPLY);
        registerCmdReply(SERVER_CLIENT_PING, CLIENT_SERVER_PONG);
        registerCmdReply(MASTER_SERVER_PING, SERVER_MASTER_PONG);
        registerCmdReply(CLIENT_MASTER_JOINSERVER, MASTER_CLIENT_JOINSERVER_REPLY);
    }
}
