package client;

import common.net.Network;
import common.net.Protocol;
import java.io.BufferedReader;
import java.io.InputStreamReader;

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
                if(input.startsWith("/")){
                    // Log Off
                    if(input.startsWith("/logoff")) {
                        net.send("localhost", 40000, Protocol.CLIENT_SERVER_LOGOFF);
                        running = false;
                    }

                    // Show Serverlist
                    else if(input.startsWith("/serverlist") || input.startsWith("/sl")) {
                        net.send("localhost", 30000, Protocol.CLIENT_MASTER_LISTREQUEST);
                    }
                    // join server
                    else if(input.startsWith("/joinserver") || input.startsWith("/js")) {
                        net.send(input.substring(input.indexOf(" ")+1,input.indexOf(":")), Integer.parseInt(input.substring(input.indexOf(":")+1)), Protocol.CLIENT_SERVER_AUTH);
                        net.send("localhost", 30000,
                                Protocol.CLIENT_MASTER_JOINSERVER, input.substring(input.indexOf(" ")), Integer.parseInt(input.substring(input.indexOf(":")+1)));
                    }
                     // lobby chat
                    else if(input.startsWith("/lobbychat") || input.startsWith("/lc")) {
                        net.send("localhost", 30000, Protocol.CLIENT_MASTER_CHAT_LOBBY, input.substring(input.indexOf(" ")));
                    }
                    // Join team
                    else if(input.startsWith("/jointeam") || input.startsWith("/jt")) {
                        net.send("localhost", 40000, 
                                Protocol.CLIENT_SERVER_JOINTEAM, Integer.parseInt(input.split(" ")[1]));
                    }
                    // Team Chat
                    else if(input.startsWith("/teamchat") || input.startsWith("/tc")) {
                        net.send("localhost", 40000,
                                Protocol.CLIENT_SERVER_CHAT_TEAM, input.substring(input.indexOf(" ")));
                    }
                    // Lobby Chat
                    else if(input.startsWith("/lobbychat") || input.startsWith("/lc")) {
                        net.send("localhost", 30000,
                                Protocol.CLIENT_MASTER_CHAT_LOBBY, input.substring(input.indexOf(" ")));
                    }

                }
                else if(input.contains(";")) {
                    System.out.println("illegal character");
                }
//                else
//                    net.send("localhost", 40000, Protocol.CLIENT_SERVER_CHAT_ALL, input);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    public Chat(common.net.Network net) {
        this.net = net;
    }

}
