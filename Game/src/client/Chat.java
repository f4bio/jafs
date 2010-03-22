/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

    public void run(){
        in = new BufferedReader(new InputStreamReader(System.in));
        while(running){
            try{
                System.out.print("Type in your msg: ");
                String input = in.readLine();
                if(input.startsWith("/")){
                    if(input.startsWith("/logoff")){
                        net.send("localhost", 40000, Protocol.client_server_logoff);
                        running = false;
                    }else if(input.startsWith("/serverlist")){
                        net.send("localhost", 30000, Protocol.client_master_listrequest);
                    }
                }else if(input.equals(";")){
                    System.out.println("illegal character");
                }else
                    net.send("localhost", 40000, Protocol.client_server_chat_all, input);
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }

    public Chat(common.net.Network net) {
        this.net = net;
    }

}
