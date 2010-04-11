/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.net;

/**
 *
 * @author miracle
 */
public enum ProtocolCmd {
    /*
     * Client commands
     */

    //----- Client -> Master commands
    CLIENT_MASTER_JOINSERVER,
    CLIENT_MASTER_LISTREQUEST,
    CLIENT_MASTER_CHAT_LOBBY,
    CLIENT_MASTER_CHAT_PRIVATE,
    CLIENT_MASTER_CHAT_PRIVATE_OK,
    CLIENT_MASTER_AUTH,
    CLIENT_MASTER_PONG,
    CLIENT_MASTER_LOGOFF,

    //----- Client -> Server commands
    CLIENT_SERVER_AUTH,
    CLIENT_SERVER_LATENCY,
    CLIENT_SERVER_CURRENT_MAP,
    CLIENT_SERVER_PLAYERS,
    CLIENT_SERVER_ALL_PLAYER_DATA,
    CLIENT_SERVER_PLAYER_DATA_OK,
    CLIENT_SERVER_REQUEST_SERVER_INFO,
    CLIENT_SERVER_PONG,
    CLIENT_SERVER_CLIENTCOUNT,
    CLIENT_SERVER_CLIENTID,
    CLIENT_SERVER_LOGOFF,
    CLIENT_SERVER_INIT_REPLY,
    CLIENT_SERVER_PLAYER_INFO,
    CLIENT_SERVER_JOINTEAM,
    CLIENT_SERVER_FORCED_NICKCHANGE_OK,
    CLIENT_SERVER_REQUEST_NAME_REPLY,
    CLIENT_SERVER_CONNECTION_TERMINATED_OK,
    CLIENT_SERVER_CONNECTION_ESTABLISHED_OK,

    CLIENT_SERVER_CHAT_ALL,
    CLIENT_SERVER_CHAT_TEAM,
    CLIENT_SERVER_CHAT_PRIVATE,
    CLIENT_SERVER_CHAT_ALL_OK,
    CLIENT_SERVER_CHAT_TEAM_OK,
    CLIENT_SERVER_CHAT_PRIVATE_OK,

    CLIENT_SERVER_EVENT_PLAYER_JOINED_OK,
    CLIENT_SERVER_EVENT_PLAYER_LEFT_OK,
    CLIENT_SERVER_EVENT_ITEM_SPAWNED_OK,
    CLIENT_SERVER_EVENT_ITEM_PICKED_OK,
    CLIENT_SERVER_EVENT_PLAYER_SHOT_OK,
    CLIENT_SERVER_EVENT_PLAYER_KILLED_OK,
    CLIENT_SERVER_EVENT_PLAYER_RESPAWN_OK,
    CLIENT_SERVER_EVENT_PLAYER_NICK_CHANGED_OK,
    CLIENT_SERVER_EVENT_PLAYER_TEAM_CHANGED_OK,
    CLIENT_SERVER_EVENT_TEAM_WON_OK,

    /*
     * Server commands
     */

     //----- Server -> Master commands
    SERVER_MASTER_PONG,
    SERVER_MASTER_AUTH,
    SERVER_MASTER_SERVERCOUNT,

    //----- Server -> Client commands
    SERVER_CLIENT_PING,
    SERVER_CLIENT_CLIENTCOUNT,
    SERVER_CLIENT_CLIENTID_REPLY,
    SERVER_CLIENT_LATENCY_REPLY,
    SERVER_CLIENT_CURRENT_MAP_REPLY,
    SERVER_CLIENT_PLAYERS_REPLY,

    SERVER_CLIENT_CHAT_ALL,
    SERVER_CLIENT_CHAT_TEAM,
    SERVER_CLIENT_CHAT_PRIVATE,
    SERVER_CLIENT_CHAT_ALL_OK,
    SERVER_CLIENT_CHAT_TEAM_OK,
    SERVER_CLIENT_CHAT_PRIVATE_OK,
    SERVER_CLIENT_LOGOFF_REPLY,
    SERVER_CLIENT_AUTH_REPLY,
    SERVER_CLIENT_INIT,
    SERVER_CLIENT_REQUEST_SERVER_INFO_REPLY,
    SERVER_CLIENT_JOINTEAM_REPLY,
    SERVER_CLIENT_CONNECTION_TERMINATED,
    SERVER_CLIENT_CONNECTION_ESTABLISHED,
    SERVER_CLIENT_ALL_PLAYER_DATA_OK,
    SERVER_CLIENT_PLAYER_DATA,
    SERVER_CLIENT_REQUEST_NAME,
    SERVER_CLIENT_FORCED_NICKCHANGE,
    SERVER_CLIENT_PLAYER_INFO,

    SERVER_CLIENT_EVENT_PLAYER_JOINED,
    SERVER_CLIENT_EVENT_PLAYER_LEFT,
    SERVER_CLIENT_EVENT_ITEM_SPAWNED,
    SERVER_CLIENT_EVENT_ITEM_PICKED,
    SERVER_CLIENT_EVENT_PLAYER_SHOT,
    SERVER_CLIENT_EVENT_PLAYER_KILLED,
    SERVER_CLIENT_EVENT_PLAYER_RESPAWN,
    SERVER_CLIENT_EVENT_PLAYER_NICK_CHANGED,
    SERVER_CLIENT_EVENT_PLAYER_TEAM_CHANGED,
    SERVER_CLIENT_EVENT_TEAM_WON,

    /*
     * Masterserver commands
     */

    //----- Master -> Server commands
    MASTER_SERVER_AUTH_REPLY,
    MASTER_SERVER_PING,
    MASTER_SERVER_SERVERCOUNT,

    //----- Master -> Client commands
    MASTER_CLIENT_PING,
    MASTER_CLIENT_JOINSERVER_REPLY,
    MASTER_CLIENT_AUTH_REPLY,
    MASTER_CLIENT_NEWLIST,
    MASTER_CLIENT_LISTENTRY,
    MASTER_CLIENT_ENDLIST,
    MASTER_CLIENT_CHAT,
    MASTER_CLIENT_CHAT_OK
}