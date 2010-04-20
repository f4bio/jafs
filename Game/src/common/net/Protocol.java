package common.net;

import java.util.EnumMap;
import java.util.HashMap;


/**
 *
 * @author miracle
 */
public class Protocol {
    
    private static EnumMap<ProtocolCmd, byte[]> cmdTable =
            new EnumMap<ProtocolCmd, byte[]>(ProtocolCmd.class);

    private static EnumMap<ProtocolCmd, Byte> cmdIndexTable =
            new EnumMap<ProtocolCmd, Byte>(ProtocolCmd.class);

    private static HashMap<Byte, ProtocolCmd> indexCmdTable =
            new HashMap<Byte, ProtocolCmd>();

    private static EnumMap<ProtocolCmd, ProtocolCmd> cmdReplyTable =
            new EnumMap<ProtocolCmd, ProtocolCmd>(ProtocolCmd.class);

    private static HashMap<Byte, Byte> cmdReplyTableId =
            new HashMap<Byte, Byte>();
    
    //private static

    //----- Argument definitions
    public static final byte ARG_BYTE   = 0;
    public static final byte ARG_SHORT  = 1;
    public static final byte ARG_INT    = 2;
    public static final byte ARG_LONG   = 3;
    public static final byte ARG_FLOAT  = 4;
    public static final byte ARG_DOUBLE = 5;
    public static final byte ARG_STRING = 6;
    public static final byte ARG_NONE   = 7;

    public static final byte[] ARG_SIZE = {
        1, 2, 4, 8, 4, 8, 0, 0
    };

    public static final byte REPLY_SUCCESS = 0;
    public static final byte REPLY_FAILURE = 1;
    
    public static final String ARG_SEPERATOR = ";";
    public static final char STRING_TERMINATOR = 3;

    public static final short LIST_TYPE_SERVERLIST = 1;
    public static final short LIST_TYPE_CLIENTLIST = 2;

    public static void registerCmd(ProtocolCmd command, byte... arg) {
        cmdTable.put(command, arg);
    }

    public static void registerCmdReply(ProtocolCmd command, ProtocolCmd reply) {
        cmdReplyTable.put(command, reply);
    }

    public static boolean hasReply(ProtocolCmd command) {
        if(cmdReplyTable.containsKey(command))
            return true;
        return false;
    }
    
    public static boolean hasReplyById(byte command) {
        if(cmdReplyTableId.containsKey(command))
            return true;
        return false;
    }

    public static boolean isReply(ProtocolCmd command) {
        if(cmdReplyTable.containsValue(command))
            return true;
        return false;
    }

    public static boolean isReplyById(byte id) {
        if(cmdReplyTableId.containsValue(id))
            return true;
        return false;
    }

    public static ProtocolCmd getReplyOfCmd(ProtocolCmd command) {
        return cmdReplyTable.get(command);
    }

    public static byte getReplyOfCmdById(byte id) {
        return cmdReplyTableId.get(id);
    }

    public static byte[] buildPacket(ProtocolCmd command, byte[]... c) {
        byte[] args = getArgType(command);

        if(cmdTable.containsKey(command) && args[0] == ARG_NONE)
            return new byte[] { (byte) command.ordinal() };

        if(!cmdTable.containsKey(command) || cmdTable.get(command).length != c.length)
            return null;

        int size = 1;

        for(byte i=0; i<args.length; ++i) {
            if(args[i] == ARG_STRING) {
                size += c[i].length;
            } else
                size += ARG_SIZE[args[i]];
        }

        byte[] packet = new byte[size];
        packet[0] = (byte)command.ordinal();
        int idx = 1;

        for(byte i=0; i<args.length; ++i) {
            for(int j=0; j<c[i].length; ++j) {
                packet[idx] = c[i][j];
                idx++;
            }
        }

        return packet;
    }

    public static byte[] buildPacket(ProtocolCmd command) {
        return buildPacket(command, new byte[0]);
    }

    public static int getArgSize(ProtocolCmd command) {
        byte[] args = cmdTable.get(command);
        if(args != null) {
            if(args[0] != ARG_NONE)
                return args.length;
            else
                return 0;
        }
        return -1;
    }

    public static byte[] getArgType(ProtocolCmd command) {
        return cmdTable.get(command);
    }

    public static boolean containsCmd(ProtocolCmd command) {
        return cmdTable.containsKey(command);
    }

    public static boolean containsCmdId(byte id) {
        return indexCmdTable.containsKey(id);
    }

    public static byte getIdByCmd(ProtocolCmd command) {
        return cmdIndexTable.get(command);
    }
    
    public static ProtocolCmd getCmdById(byte id) {
        return indexCmdTable.get(id);
    }

    public static boolean containsId(byte id) {
        return cmdIndexTable.containsValue(id);
    }

    public static void init() {
        //----- Client commands
        registerCmd(ProtocolCmd.CLIENT_MASTER_PONG, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_MASTER_LISTREQUEST, ARG_SHORT);
        registerCmd(ProtocolCmd.CLIENT_MASTER_NICKCHANGE, ARG_STRING);
        registerCmd(ProtocolCmd.CLIENT_MASTER_FORCED_NICKCHANGE_OK, ARG_NONE);


        // chat
        registerCmd(ProtocolCmd.CLIENT_MASTER_CHAT_LOBBY, ARG_STRING);
        registerCmd(ProtocolCmd.CLIENT_MASTER_CHAT_PRIVATE, ARG_INT, ARG_STRING);
        registerCmd(ProtocolCmd.CLIENT_MASTER_CHAT_PRIVATE_OK, ARG_NONE);
        // ----
        registerCmd(ProtocolCmd.CLIENT_MASTER_AUTH, ARG_STRING);
        registerCmd(ProtocolCmd.CLIENT_MASTER_JOINSERVER, ARG_STRING, ARG_INT);
        registerCmd(ProtocolCmd.CLIENT_MASTER_LOGOFF, ARG_NONE);

        registerCmd(ProtocolCmd.CLIENT_SERVER_AUTH, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_PONG, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_CLIENTCOUNT,ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_CLIENTID,ARG_NONE);
        // chat
        registerCmd(ProtocolCmd.CLIENT_SERVER_CHAT_ALL, ARG_STRING);
        registerCmd(ProtocolCmd.CLIENT_SERVER_CHAT_TEAM, ARG_STRING);
        registerCmd(ProtocolCmd.CLIENT_SERVER_CHAT_PRIVATE, ARG_INT, ARG_STRING);
        // ----
        registerCmd(ProtocolCmd.CLIENT_SERVER_LOGOFF, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_JOINTEAM, ARG_INT);

        registerCmd(ProtocolCmd.CLIENT_SERVER_ALL_PLAYER_DATA, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_PLAYER_DATA_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_REQUEST_SERVER_INFO, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_INIT_REPLY, ARG_INT);
        registerCmd(ProtocolCmd.CLIENT_SERVER_PLAYER_INFO, ARG_INT, ARG_INT, ARG_DOUBLE,
                    ARG_DOUBLE, ARG_DOUBLE, ARG_DOUBLE);

        registerCmd(ProtocolCmd.CLIENT_SERVER_FORCED_NICKCHANGE_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_REQUEST_NAME_REPLY, ARG_STRING);
        registerCmd(ProtocolCmd.CLIENT_SERVER_CONNECTION_TERMINATED_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_CONNECTION_ESTABLISHED_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_LATENCY, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_CURRENT_MAP, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_PLAYERS, ARG_NONE);

        registerCmd(ProtocolCmd.CLIENT_SERVER_CHAT_ALL_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_CHAT_TEAM_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_CHAT_PRIVATE_OK, ARG_NONE);

        registerCmd(ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_JOINED_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_LEFT_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_EVENT_ITEM_SPAWNED_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_EVENT_ITEM_PICKED_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_KILLED_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_NICK_CHANGED_OK, ARG_NONE);
        registerCmd(ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_TEAM_CHANGED_OK, ARG_NONE);

        //----- Server commands
        registerCmd(ProtocolCmd.SERVER_MASTER_PONG, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_MASTER_AUTH, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_MASTER_SERVERCOUNT, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CLIENTID_REPLY, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CLIENTCOUNT, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_PING, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_AUTH_REPLY, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CHAT_ALL, ARG_STRING);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CHAT_TEAM, ARG_STRING);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CHAT_PRIVATE, ARG_STRING, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CHAT_ALL_OK, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CHAT_TEAM_OK, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CHAT_PRIVATE_OK, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_LOGOFF_REPLY, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_JOINTEAM_REPLY, ARG_INT, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_INIT, ARG_STRING, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_REQUEST_SERVER_INFO_REPLY, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_LATENCY_REPLY, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CURRENT_MAP_REPLY, ARG_STRING);
        registerCmd(ProtocolCmd.SERVER_CLIENT_PLAYERS_REPLY, ARG_STRING);

        registerCmd(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_JOINED, ARG_STRING, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_LEFT, ARG_STRING);
        registerCmd(ProtocolCmd.SERVER_CLIENT_EVENT_ITEM_SPAWNED, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_EVENT_ITEM_PICKED, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_SHOT, ARG_INT, ARG_DOUBLE, ARG_DOUBLE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_KILLED, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_RESPAWN, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_NICK_CHANGED, ARG_INT, ARG_STRING);
        registerCmd(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_TEAM_CHANGED, ARG_INT, ARG_INT);

        registerCmd(ProtocolCmd.SERVER_CLIENT_CONNECTION_TERMINATED, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_CONNECTION_ESTABLISHED, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_ALL_PLAYER_DATA_OK, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_PLAYER_DATA, ARG_STRING, ARG_INT, ARG_INT);
        registerCmd(ProtocolCmd.SERVER_CLIENT_REQUEST_NAME, ARG_NONE);
        registerCmd(ProtocolCmd.SERVER_CLIENT_FORCED_NICKCHANGE, ARG_STRING);
        registerCmd(ProtocolCmd.SERVER_CLIENT_PLAYER_INFO, ARG_INT, ARG_INT, ARG_DOUBLE,
                    ARG_DOUBLE, ARG_DOUBLE, ARG_DOUBLE);

        //----- Master Server commands
        registerCmd(ProtocolCmd.MASTER_SERVER_AUTH_REPLY, ARG_INT);
        registerCmd(ProtocolCmd.MASTER_SERVER_PING, ARG_NONE);
        registerCmd(ProtocolCmd.MASTER_SERVER_SERVERCOUNT, ARG_INT);

        registerCmd(ProtocolCmd.MASTER_CLIENT_JOINSERVER_REPLY, ARG_STRING);
        registerCmd(ProtocolCmd.MASTER_CLIENT_AUTH_REPLY, ARG_INT, ARG_INT);
        registerCmd(ProtocolCmd.MASTER_CLIENT_NEWLIST, ARG_SHORT);
        registerCmd(ProtocolCmd.MASTER_CLIENT_LISTENTRY_SERVER, ARG_STRING);
        registerCmd(ProtocolCmd.MASTER_CLIENT_LISTENTRY_CLIENT, ARG_STRING, ARG_INT, ARG_STRING);
        registerCmd(ProtocolCmd.MASTER_CLIENT_ENDLIST, ARG_SHORT);
        registerCmd(ProtocolCmd.MASTER_CLIENT_CHAT, ARG_INT, ARG_STRING);
        registerCmd(ProtocolCmd.MASTER_CLIENT_CHAT_PRIVATE, ARG_INT, ARG_INT, ARG_STRING);
        registerCmd(ProtocolCmd.MASTER_CLIENT_CHAT_OK, ARG_NONE);
        registerCmd(ProtocolCmd.MASTER_CLIENT_PING, ARG_NONE);
        registerCmd(ProtocolCmd.MASTER_CLIENT_NICKCHANGE_OK, ARG_NONE);
        registerCmd(ProtocolCmd.MASTER_CLIENT_FORCED_NICKCHANGE, ARG_STRING);


        //----- Associate commands with replies
        registerCmdReply(ProtocolCmd.SERVER_MASTER_AUTH,
                ProtocolCmd.MASTER_SERVER_AUTH_REPLY);
        registerCmdReply(ProtocolCmd.CLIENT_MASTER_AUTH,
                ProtocolCmd.MASTER_CLIENT_AUTH_REPLY);
        registerCmdReply(ProtocolCmd.CLIENT_SERVER_AUTH,
                ProtocolCmd.SERVER_CLIENT_AUTH_REPLY);
        registerCmdReply(ProtocolCmd.MASTER_CLIENT_PING,
                ProtocolCmd.CLIENT_MASTER_PONG);
        registerCmdReply(ProtocolCmd.CLIENT_MASTER_CHAT_LOBBY,
                ProtocolCmd.MASTER_CLIENT_CHAT_OK);
        registerCmdReply(ProtocolCmd.CLIENT_MASTER_CHAT_PRIVATE,
                ProtocolCmd.MASTER_CLIENT_CHAT_OK);

        // Ping <-> Pong
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_PING,
                ProtocolCmd.CLIENT_SERVER_PONG);
        registerCmdReply(ProtocolCmd.MASTER_CLIENT_PING,
                ProtocolCmd.CLIENT_MASTER_PONG);
        registerCmdReply(ProtocolCmd.MASTER_SERVER_PING,
                ProtocolCmd.SERVER_MASTER_PONG);
        registerCmdReply(ProtocolCmd.MASTER_CLIENT_FORCED_NICKCHANGE,
                ProtocolCmd.CLIENT_MASTER_FORCED_NICKCHANGE_OK);
        // Ping <-> Pong

        registerCmdReply(ProtocolCmd.CLIENT_MASTER_JOINSERVER,
                ProtocolCmd.MASTER_CLIENT_JOINSERVER_REPLY);

        registerCmdReply(ProtocolCmd.CLIENT_MASTER_NICKCHANGE,
                ProtocolCmd.MASTER_CLIENT_NICKCHANGE_OK);


        registerCmdReply(ProtocolCmd.SERVER_CLIENT_INIT,
                ProtocolCmd.CLIENT_SERVER_INIT_REPLY);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_REQUEST_NAME,
                ProtocolCmd.CLIENT_SERVER_REQUEST_NAME_REPLY);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_FORCED_NICKCHANGE,
                ProtocolCmd.CLIENT_SERVER_FORCED_NICKCHANGE_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_CONNECTION_ESTABLISHED,
                ProtocolCmd.CLIENT_SERVER_CONNECTION_ESTABLISHED_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_CONNECTION_TERMINATED,
                ProtocolCmd.CLIENT_SERVER_CONNECTION_TERMINATED_OK);
        registerCmdReply(ProtocolCmd.CLIENT_SERVER_ALL_PLAYER_DATA,
                ProtocolCmd.SERVER_CLIENT_ALL_PLAYER_DATA_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_PLAYER_DATA,
                ProtocolCmd.CLIENT_SERVER_PLAYER_DATA_OK);
        registerCmdReply(ProtocolCmd.CLIENT_SERVER_CHAT_ALL,
                ProtocolCmd.SERVER_CLIENT_CHAT_ALL_OK);
        registerCmdReply(ProtocolCmd.CLIENT_SERVER_CHAT_TEAM,
                ProtocolCmd.SERVER_CLIENT_CHAT_TEAM_OK);
        registerCmdReply(ProtocolCmd.CLIENT_SERVER_CHAT_PRIVATE,
                ProtocolCmd.SERVER_CLIENT_CHAT_PRIVATE_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_CHAT_ALL,
                ProtocolCmd.CLIENT_SERVER_CHAT_ALL_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_CHAT_TEAM,
                ProtocolCmd.CLIENT_SERVER_CHAT_TEAM_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_CHAT_PRIVATE,
                ProtocolCmd.CLIENT_SERVER_CHAT_PRIVATE_OK);

        registerCmdReply(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_JOINED,
                ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_JOINED_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_LEFT,
                ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_LEFT_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_EVENT_ITEM_SPAWNED,
                ProtocolCmd.CLIENT_SERVER_EVENT_ITEM_SPAWNED_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_EVENT_ITEM_PICKED,
                ProtocolCmd.CLIENT_SERVER_EVENT_ITEM_PICKED_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_KILLED,
                ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_KILLED_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_RESPAWN,
                ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_NICK_CHANGED,
                ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_NICK_CHANGED_OK);
        registerCmdReply(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_TEAM_CHANGED,
                ProtocolCmd.CLIENT_SERVER_EVENT_PLAYER_TEAM_CHANGED_OK);

        //Create enum index table
        for(ProtocolCmd c : ProtocolCmd.values()) {
            cmdIndexTable.put(c, (byte)c.ordinal());
            indexCmdTable.put((byte)c.ordinal(), c);
        }

        //Creates association tables based on IDs
        for(ProtocolCmd c : ProtocolCmd.values()) {
            if(hasReply(c)) {
                cmdReplyTableId.put((byte)c.ordinal(), (byte)getReplyOfCmd(c).ordinal());
            }
        }
    }
}
