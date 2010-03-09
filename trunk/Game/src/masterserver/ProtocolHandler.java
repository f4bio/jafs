/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package masterserver;

import common.net.Network;
import common.net.Protocol;
import common.net.Server;

/**
 *
 * @author miracle
 */
public class ProtocolHandler extends common.net.ProtocolHandler {
    public ProtocolHandler(Network net) {
        super(net);
    }

    public void server_master_auth(String host, Integer port) {
        Server added = Main.addServer(host, port);
        String result;

        if(added != null)
            result = "success";
        else
            result = "failure";

        net.send(Protocol.buildPacket("master_server_auth_" + result, new Object[0]), added.getAddress());
    }

    public void server_master_pong(String host, Integer port) {
        Main.decreasePingFailures(host, port);
    }

    public void client_master_listrequest(String host, Integer port) {
        net.send(Protocol.buildPacket("master_client_list", Main.getServerlist()), host, port);
    }
}
