package client;

import common.net.Network;
import common.net.Protocol;
import common.net.ProtocolCmd;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import static common.net.ProtocolCmdArgument.*;


/**
 *
 * @author adm1n
 */
public class Chat extends Thread{
    Network net;
    BufferedReader in;
    private static boolean running = true;

    @Override
    public void run() {
        in = new BufferedReader(new InputStreamReader(System.in));
        while(running) {
            try{
                System.out.print("Type in your msg/cmd: ");
                String input = in.readLine();
                if(input.contains(";"))
                    input = input.replace(";", "vXv");

                if(input.startsWith("/")){
                    // Log Off
                    if(input.startsWith("/logoff")) {
                        net.send("localhost", 40000, ProtocolCmd.CLIENT_SERVER_LOGOFF);
                        running = false;
                    }

                    // Show Serverlist
                    else if(input.startsWith("/serverlist") || input.startsWith("/sl")) {
                        net.send(Network.MASTERHOST, Network.MASTERPORT, ProtocolCmd.CLIENT_MASTER_LISTREQUEST, argShort(Protocol.LIST_TYPE_SERVERLIST));
                    }
                    // Show Clientlist
                    else if(input.startsWith("/clientlist") || input.startsWith("/cl")) {
                        net.send(Network.MASTERHOST, Network.MASTERPORT, ProtocolCmd.CLIENT_MASTER_LISTREQUEST, argShort(Protocol.LIST_TYPE_CLIENTLIST));
                    }
                    // join server
                    else if(input.startsWith("/joinserver") || input.startsWith("/js")) {
//                        new InetSocketAdress(input.substring(input.indexOf(" ")+1,input.indexOf(":")), argInt(Integer.parseInt(input.substring(input.indexOf(":")+1))));
                        net.send(new InetSocketAddress(input.substring(input.indexOf(" ")+1,input.indexOf(":")),
                                Integer.parseInt(input.substring(input.indexOf(":")+1))),
                                ProtocolCmd.CLIENT_SERVER_AUTH);
//                        net.send(input.substring(input.indexOf(" ")+1,input.indexOf(":")), argInt(Integer.parseInt(input.substring(input.indexOf(":")+1))), ProtocolCmd.CLIENT_SERVER_AUTH);
                        net.send(Network.MASTERHOST, Network.MASTERPORT,
                                 ProtocolCmd.CLIENT_MASTER_JOINSERVER, argStr(input.substring(input.indexOf(" "))), argInt(Integer.parseInt(input.substring(input.indexOf(":")+1))));
                    }
                     // lobby chat
                    else if(input.startsWith("/lobbychat") || input.startsWith("/lc")) {
                        net.send(Network.MASTERHOST, Network.MASTERPORT, ProtocolCmd.CLIENT_MASTER_CHAT_LOBBY, argStr(input.substring(input.indexOf(" "))));
                    }
                    // Join team
                    else if(input.startsWith("/jointeam") || input.startsWith("/jt")) {
                        net.send("localhost", 40000, 
                                ProtocolCmd.CLIENT_SERVER_JOINTEAM, argInt(Integer.parseInt(input.split(" ")[1])));
                    }
                    // Team Chat
                    else if(input.startsWith("/teamchat") || input.startsWith("/tc")) {
                        net.send("localhost", 40000,
                                ProtocolCmd.CLIENT_SERVER_CHAT_TEAM, argStr(input.substring(input.indexOf(" "))));
                    }
                    // private Chat
                    else if(input.startsWith("/private") || input.startsWith("/p")) {
                        String[] zwi = input.split(" ");
                        System.out.println("private id:"+Integer.parseInt(zwi[1])+",msg:"+input.substring(input.indexOf(" ", input.indexOf(zwi[1]))));                        
                        net.send(Network.MASTERHOST,
                                 Network.MASTERPORT,
                                 ProtocolCmd.CLIENT_MASTER_CHAT_PRIVATE,
                                 argInt(Integer.parseInt(zwi[1])), argStr(input.substring(input.indexOf(" ", input.indexOf(zwi[1])))));
                    }

                }               
//                else
//                    net.send("localhost", 40000, Protocol.CLIENT_SERVER_CHAT_ALL, input);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     *
     * @param net
     */
    public Chat(common.net.Network net) {
        this.net = net;
    }

}
