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
    public static final int ARG_BYTE   = 0;
    public static final int ARG_SHORT  = 1;
    public static final int ARG_INT    = 2;
    public static final int ARG_LONG   = 3;
    public static final int ARG_FLOAT  = 4;
    public static final int ARG_DOUBLE = 5;
    public static final int ARG_STRING = 6;
    public static final int ARG_NONE   = 7;

    public static final int REPLY_SUCCESS = 0;
    public static final int REPLY_FAILURE = 1;
    
    public static final String ARG_SEPERATOR = ";";

    //----- Client -> Master commands
    public static final String CLIENT_MASTER_JOINSERVER = "c_m_joinserver";
    public static final String CLIENT_MASTER_LISTREQUEST = "c_m_listrequest";
    public static final String CLIENT_MASTER_CHAT_LOBBY = "c_m_chat_lobby";
    public static final String CLIENT_MASTER_CHAT_PRIVATE = "c_m_chat_private";
    public static final String CLIENT_MASTER_CHAT_PRIVATE_OK = "c_m_chat_private_ok";
    public static final String CLIENT_MASTER_AUTH = "c_m_auth";
    public static final String CLIENT_MASTER_PONG = "c_m_pong";
    public static final String CLIENT_MASTER_LOGOFF = "c_m_logoff";

    //----- Client -> Server commands
    public static final String CLIENT_SERVER_AUTH = "c_s_auth";
    public static final String CLIENT_SERVER_LATENCY = "c_s_latency";
    public static final String CLIENT_SERVER_ALL_PLAYER_DATA = "c_s_all_player_data";
    public static final String CLIENT_SERVER_PLAYER_DATA_OK = "c_s_player_data_ok";
    public static final String CLIENT_SERVER_REQUEST_SERVER_INFO = "c_s_request_server_info";
    public static final String CLIENT_SERVER_PONG = "c_s_pong";
    public static final String CLIENT_SERVER_CLIENTCOUNT = "c_s_clientcount";
    public static final String CLIENT_SERVER_CLIENTID = "c_s_clientid";
    public static final String CLIENT_SERVER_LOGOFF = "c_s_logoff";
    public static final String CLIENT_SERVER_INIT_REPLY = "c_s_init_reply";
    public static final String CLIENT_SERVER_PLAYER_INFO = "c_s_player_info";
    public static final String CLIENT_SERVER_JOINTEAM = "c_s_jointeam";

    public static final String CLIENT_SERVER_FORCED_NICKCHANGE_OK = "c_s_forced_nickchange_ok";
    public static final String CLIENT_SERVER_REQUEST_NAME_REPLY = "c_s_request_name_reply";
    public static final String CLIENT_SERVER_CONNECTION_TERMINATED_OK = "c_s_connection_terminated_ok";
    public static final String CLIENT_SERVER_CONNECTION_ESTABLISHED_OK = "c_s_connection_established_ok";
    
    public static final String CLIENT_SERVER_CHAT_ALL = "c_s_chat_all";
    public static final String CLIENT_SERVER_CHAT_TEAM = "c_s_chat_team";
    public static final String CLIENT_SERVER_CHAT_PRIVATE = "c_s_chat_private";
    
    public static final String CLIENT_SERVER_CHAT_ALL_OK = "c_s_chat_all_ok";
    public static final String CLIENT_SERVER_CHAT_TEAM_OK = "c_s_chat_team_ok";
    public static final String CLIENT_SERVER_CHAT_PRIVATE_OK = "c_s_chat_private_ok";

    public static final String CLIENT_SERVER_EVENT_PLAYER_JOINED_OK = "c_s_event_player_joined_ok";
    public static final String CLIENT_SERVER_EVENT_PLAYER_LEFT_OK = "c_s_event_player_left_ok";
    public static final String CLIENT_SERVER_EVENT_ITEM_SPAWNED_OK = "c_s_event_item_spawned_ok";
    public static final String CLIENT_SERVER_EVENT_ITEM_PICKED_OK = "c_s_event_item_picked_ok";
    public static final String CLIENT_SERVER_EVENT_PLAYER_SHOT_OK = "c_s_event_player_shot_ok";
    public static final String CLIENT_SERVER_EVENT_PLAYER_KILLED_OK = "c_s_event_player_killed_ok";
    public static final String CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK = "c_s_event_player_respawn_ok";
    public static final String CLIENT_SERVER_EVENT_PLAYER_NICK_CHANGED_OK = "c_s_event_player_nick_changed_ok";
    public static final String CLIENT_SERVER_EVENT_PLAYER_TEAM_CHANGED_OK = "c_s_event_player_team_changed_ok";
    public static final String CLIENT_SERVER_EVENT_TEAM_WON_OK = "c_s_event_team_won_ok";

    //----- Server -> Master commands
    public static final String SERVER_MASTER_PONG = "s_m_pong";
    public static final String SERVER_MASTER_AUTH = "s_m_auth";
    public static final String SERVER_MASTER_SERVERCOUNT = "s_m_servercount";

    //----- Server -> Client commands
    public static final String SERVER_CLIENT_PING = "s_c_ping";
    public static final String SERVER_CLIENT_CLIENTCOUNT = "s_c_clientcount";
    public static final String SERVER_CLIENT_CLIENTID_REPLY = "s_c_clientid_reply";

    public static final String SERVER_CLIENT_CHAT_ALL = "s_c_chat_all";
    public static final String SERVER_CLIENT_CHAT_TEAM = "s_c_chat_team";
    public static final String SERVER_CLIENT_CHAT_PRIVATE = "s_c_chat_private";
    public static final String SERVER_CLIENT_CHAT_ALL_OK = "s_c_chat_all_ok";
    public static final String SERVER_CLIENT_CHAT_TEAM_OK = "s_c_chat_team_ok";
    public static final String SERVER_CLIENT_CHAT_PRIVATE_OK = "s_c_chat_private_ok";

    public static final String SERVER_CLIENT_LOGOFF_REPLY = "s_c_logoff_reply";
    public static final String SERVER_CLIENT_AUTH_REPLY = "s_c_auth_reply";
    public static final String SERVER_CLIENT_INIT = "s_c_init";
    public static final String SERVER_CLIENT_REQUEST_SERVER_INFO_REPLY = "c_s_request_server_info_reply";
    public static final String SERVER_CLIENT_JOINTEAM_REPLY = "s_c_jointeam_reply";
    public static final String SERVER_CLIENT_CONNECTION_TERMINATED = "s_c_connection_terminated";
    public static final String SERVER_CLIENT_CONNECTION_ESTABLISHED = "s_c_connection_established";
    public static final String SERVER_CLIENT_ALL_PLAYER_DATA_OK = "s_c_all_player_data_ok";
    public static final String SERVER_CLIENT_PLAYER_DATA = "s_c_player_data";
    public static final String SERVER_CLIENT_REQUEST_NAME = "s_c_request_name";
    public static final String SERVER_CLIENT_FORCED_NICKCHANGE = "s_c_forced_nickchange";
    public static final String SERVER_CLIENT_PLAYER_INFO = "s_c_player_info";

    public static final String SERVER_CLIENT_EVENT_PLAYER_JOINED = "s_c_event_player_joined";
    public static final String SERVER_CLIENT_EVENT_PLAYER_LEFT = "s_c_event_player_left";
    public static final String SERVER_CLIENT_EVENT_ITEM_SPAWNED = "s_c_event_item_spawned";
    public static final String SERVER_CLIENT_EVENT_ITEM_PICKED = "s_c_event_item_picked";
    public static final String SERVER_CLIENT_EVENT_PLAYER_SHOT = "s_c_event_player_shot";
    public static final String SERVER_CLIENT_EVENT_PLAYER_KILLED = "s_c_event_player_killed";
    public static final String SERVER_CLIENT_EVENT_PLAYER_RESPAWN = "s_c_event_player_respawn";
    public static final String SERVER_CLIENT_EVENT_PLAYER_NICK_CHANGED = "s_c_event_player_nick_changed";
    public static final String SERVER_CLIENT_EVENT_PLAYER_TEAM_CHANGED = "s_c_event_player_team_changed";
    public static final String SERVER_CLIENT_EVENT_TEAM_WON = "s_c_event_team_won";
    public static final String SERVER_CLIENT_LATENCY_REPLY = "s_c_latency_reply";

    //----- Master -> Server commands
    public static final String MASTER_SERVER_AUTH_REPLY = "m_s_auth_reply";
    public static final String MASTER_SERVER_PING = "m_s_ping";
    public static final String MASTER_SERVER_SERVERCOUNT = "m_s_servercount";

    //----- Master -> Client commands
    public static final String MASTER_CLIENT_PING = "m_c_ping";
    public static final String MASTER_CLIENT_JOINSERVER_REPLY = "m_c_joinserver_reply";
    public static final String MASTER_CLIENT_AUTH_REPLY = "m_c_auth_reply";
    public static final String MASTER_CLIENT_NEWLIST = "m_c_newlist";
    public static final String MASTER_CLIENT_LISTENTRY = "m_c_listentry";
    public static final String MASTER_CLIENT_ENDLIST = "m_c_endlist";
    public static final String MASTER_CLIENT_CHAT = "m_c_chat";
    public static final String MASTER_CLIENT_CHAT_OK = "m_c_chat_ok";
//    public static final String MASTER_CLIENT_CHAT_PRIVATE_OK = "m_c_chat_private_ok";

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
        registerCmd(CLIENT_MASTER_CHAT_PRIVATE_OK, ARG_NONE);
        // ----
        registerCmd(CLIENT_MASTER_AUTH, ARG_NONE);
        registerCmd(CLIENT_MASTER_JOINSERVER, ARG_STRING, ARG_INT);
        registerCmd(CLIENT_MASTER_LOGOFF, ARG_NONE);

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

        registerCmd(CLIENT_SERVER_ALL_PLAYER_DATA, ARG_NONE);
        registerCmd(CLIENT_SERVER_PLAYER_DATA_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_REQUEST_SERVER_INFO, ARG_NONE);
        registerCmd(CLIENT_SERVER_INIT_REPLY, ARG_INT);
        registerCmd(CLIENT_SERVER_PLAYER_INFO, ARG_INT, ARG_INT, ARG_DOUBLE,
                    ARG_DOUBLE, ARG_DOUBLE, ARG_DOUBLE);

        registerCmd(CLIENT_SERVER_FORCED_NICKCHANGE_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_REQUEST_NAME_REPLY, ARG_STRING);
        registerCmd(CLIENT_SERVER_CONNECTION_TERMINATED_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_CONNECTION_ESTABLISHED_OK, ARG_NONE);

        registerCmd(CLIENT_SERVER_CHAT_ALL_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_CHAT_TEAM_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_CHAT_PRIVATE_OK, ARG_NONE);

        registerCmd(CLIENT_SERVER_EVENT_PLAYER_JOINED_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_EVENT_PLAYER_LEFT_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_EVENT_ITEM_SPAWNED_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_EVENT_ITEM_PICKED_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_EVENT_PLAYER_KILLED_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_EVENT_PLAYER_NICK_CHANGED_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_EVENT_PLAYER_TEAM_CHANGED_OK, ARG_NONE);
        registerCmd(CLIENT_SERVER_LATENCY, ARG_NONE);

        //----- Server commands
        registerCmd(SERVER_MASTER_PONG, ARG_NONE);
        registerCmd(SERVER_MASTER_AUTH, ARG_NONE);
        registerCmd(SERVER_MASTER_SERVERCOUNT, ARG_NONE);
        registerCmd(SERVER_CLIENT_CLIENTID_REPLY, ARG_INT);
        registerCmd(SERVER_CLIENT_CLIENTCOUNT, ARG_INT);
        registerCmd(SERVER_CLIENT_PING, ARG_NONE);
        registerCmd(SERVER_CLIENT_AUTH_REPLY, ARG_INT);
        registerCmd(SERVER_CLIENT_CHAT_ALL, ARG_STRING);
        registerCmd(SERVER_CLIENT_CHAT_TEAM, ARG_STRING);
        registerCmd(SERVER_CLIENT_CHAT_PRIVATE, ARG_STRING, ARG_INT);
        registerCmd(SERVER_CLIENT_CHAT_ALL_OK, ARG_NONE);
        registerCmd(SERVER_CLIENT_CHAT_TEAM_OK, ARG_NONE);
        registerCmd(SERVER_CLIENT_CHAT_PRIVATE_OK, ARG_NONE);
        registerCmd(SERVER_CLIENT_LOGOFF_REPLY, ARG_INT);
        registerCmd(SERVER_CLIENT_JOINTEAM_REPLY, ARG_INT, ARG_INT);
        registerCmd(SERVER_CLIENT_INIT, ARG_STRING, ARG_INT);
        registerCmd(SERVER_CLIENT_REQUEST_SERVER_INFO_REPLY, ARG_NONE);
        registerCmd(SERVER_CLIENT_LATENCY_REPLY, ARG_NONE);

        registerCmd(SERVER_CLIENT_EVENT_PLAYER_JOINED, ARG_STRING);
        registerCmd(SERVER_CLIENT_EVENT_PLAYER_LEFT, ARG_STRING);
        registerCmd(SERVER_CLIENT_EVENT_ITEM_SPAWNED, ARG_NONE);
        registerCmd(SERVER_CLIENT_EVENT_ITEM_PICKED, ARG_INT);
        registerCmd(SERVER_CLIENT_EVENT_PLAYER_SHOT, ARG_INT, ARG_DOUBLE, ARG_DOUBLE);
        registerCmd(SERVER_CLIENT_EVENT_PLAYER_KILLED, ARG_INT);
        registerCmd(SERVER_CLIENT_EVENT_PLAYER_RESPAWN, ARG_NONE);
        registerCmd(SERVER_CLIENT_EVENT_PLAYER_NICK_CHANGED, ARG_INT, ARG_STRING);
        registerCmd(SERVER_CLIENT_EVENT_PLAYER_TEAM_CHANGED, ARG_INT, ARG_INT);

        registerCmd(SERVER_CLIENT_CONNECTION_TERMINATED, ARG_NONE);
        registerCmd(SERVER_CLIENT_CONNECTION_ESTABLISHED, ARG_NONE);
        registerCmd(SERVER_CLIENT_ALL_PLAYER_DATA_OK, ARG_NONE);
        registerCmd(SERVER_CLIENT_PLAYER_DATA, ARG_STRING, ARG_INT, ARG_INT);
        registerCmd(SERVER_CLIENT_REQUEST_NAME, ARG_NONE);
        registerCmd(SERVER_CLIENT_FORCED_NICKCHANGE, ARG_STRING);
        registerCmd(SERVER_CLIENT_PLAYER_INFO, ARG_INT, ARG_INT, ARG_DOUBLE,
                    ARG_DOUBLE, ARG_DOUBLE, ARG_DOUBLE);

        //----- Master Server commands
        registerCmd(MASTER_SERVER_AUTH_REPLY, ARG_INT);
        registerCmd(MASTER_SERVER_PING, ARG_NONE);
        registerCmd(MASTER_SERVER_SERVERCOUNT, ARG_INT);

        registerCmd(MASTER_CLIENT_JOINSERVER_REPLY, ARG_STRING);
        registerCmd(MASTER_CLIENT_AUTH_REPLY, ARG_INT);
        registerCmd(MASTER_CLIENT_NEWLIST, ARG_NONE);
        registerCmd(MASTER_CLIENT_LISTENTRY, ARG_STRING);
        registerCmd(MASTER_CLIENT_ENDLIST, ARG_NONE);
        registerCmd(MASTER_CLIENT_CHAT, ARG_INT, ARG_STRING);
        registerCmd(MASTER_CLIENT_CHAT_OK, ARG_NONE);

        //----- Associate commands with replies
        registerCmdReply(SERVER_MASTER_AUTH, MASTER_SERVER_AUTH_REPLY);
        registerCmdReply(CLIENT_MASTER_AUTH, MASTER_CLIENT_AUTH_REPLY);
        registerCmdReply(CLIENT_SERVER_AUTH, SERVER_CLIENT_AUTH_REPLY);
        registerCmdReply(MASTER_CLIENT_PING, CLIENT_MASTER_PONG);
        registerCmdReply(CLIENT_MASTER_CHAT_LOBBY, MASTER_CLIENT_CHAT_OK);
        registerCmdReply(CLIENT_MASTER_CHAT_PRIVATE, MASTER_CLIENT_CHAT_OK);

        registerCmdReply(SERVER_CLIENT_PING, CLIENT_SERVER_PONG);
        registerCmdReply(MASTER_SERVER_PING, SERVER_MASTER_PONG);
        registerCmdReply(CLIENT_MASTER_JOINSERVER, MASTER_CLIENT_JOINSERVER_REPLY);

        registerCmdReply(SERVER_CLIENT_INIT, CLIENT_SERVER_INIT_REPLY);
        registerCmdReply(SERVER_CLIENT_REQUEST_NAME, CLIENT_SERVER_REQUEST_NAME_REPLY);
        registerCmdReply(SERVER_CLIENT_FORCED_NICKCHANGE, CLIENT_SERVER_FORCED_NICKCHANGE_OK);
        registerCmdReply(SERVER_CLIENT_CONNECTION_ESTABLISHED, CLIENT_SERVER_CONNECTION_ESTABLISHED_OK);
        registerCmdReply(SERVER_CLIENT_CONNECTION_TERMINATED, CLIENT_SERVER_CONNECTION_TERMINATED_OK);
        registerCmdReply(CLIENT_SERVER_ALL_PLAYER_DATA, SERVER_CLIENT_ALL_PLAYER_DATA_OK);
        registerCmdReply(SERVER_CLIENT_PLAYER_DATA, CLIENT_SERVER_PLAYER_DATA_OK);
        registerCmdReply(CLIENT_SERVER_CHAT_ALL, SERVER_CLIENT_CHAT_ALL_OK);
        registerCmdReply(CLIENT_SERVER_CHAT_TEAM, SERVER_CLIENT_CHAT_TEAM_OK);
        registerCmdReply(CLIENT_SERVER_CHAT_PRIVATE, SERVER_CLIENT_CHAT_PRIVATE_OK);
        registerCmdReply(SERVER_CLIENT_CHAT_ALL, CLIENT_SERVER_CHAT_ALL_OK);
        registerCmdReply(SERVER_CLIENT_CHAT_TEAM, CLIENT_SERVER_CHAT_TEAM_OK);
        registerCmdReply(SERVER_CLIENT_CHAT_PRIVATE, CLIENT_SERVER_CHAT_PRIVATE_OK);

        registerCmdReply(SERVER_CLIENT_EVENT_PLAYER_JOINED, CLIENT_SERVER_EVENT_PLAYER_JOINED_OK);
        registerCmdReply(SERVER_CLIENT_EVENT_PLAYER_LEFT, CLIENT_SERVER_EVENT_PLAYER_LEFT_OK);
        registerCmdReply(SERVER_CLIENT_EVENT_ITEM_SPAWNED, CLIENT_SERVER_EVENT_ITEM_SPAWNED_OK);
        registerCmdReply(SERVER_CLIENT_EVENT_ITEM_PICKED, CLIENT_SERVER_EVENT_ITEM_PICKED_OK);
        registerCmdReply(SERVER_CLIENT_EVENT_PLAYER_KILLED, CLIENT_SERVER_EVENT_PLAYER_KILLED_OK);
        registerCmdReply(SERVER_CLIENT_EVENT_PLAYER_RESPAWN, CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK);
        registerCmdReply(SERVER_CLIENT_EVENT_PLAYER_NICK_CHANGED, CLIENT_SERVER_EVENT_PLAYER_NICK_CHANGED_OK);
        registerCmdReply(SERVER_CLIENT_EVENT_PLAYER_TEAM_CHANGED, CLIENT_SERVER_EVENT_PLAYER_TEAM_CHANGED_OK);
    }
}
