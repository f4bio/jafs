/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import common.net.Network;
import common.net.Protocol;
import common.net.Server;
import common.utils.CUtils;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author adm1n
 */
public class Main {
    public static final String PATH = CUtils.getApplicationPath("Game");

    private static MainScreen screen;
    private static UpdateLoop loop;
    private static Input input;
    private static Network net;
    private static GameData data;

    public static ArrayList<String> serverlist = new ArrayList<String>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*Protocol.init();
        Network net = new Network();
        ProtocolHandler protocol = new ProtocolHandler(net);
        net.listen(50000);
//        net.send("localhost", 40000, Protocol.client_server_clientcount);
        net.send("localhost", 40000, Protocol.client_server_auth, new Object[0]);
        new Chat(net).start();*/
        Protocol.init();
        net = new Network();
        net.listen(31330);

        JFrame frame = new JFrame();
        frame.setVisible(true);

        screen = new MainScreen(frame);
        input = new Input();
        screen.addKeyListener(input);
        screen.addMouseMotionListener(input);

        data = new GameData(input);
        data.loadMap("map");

        loop = new UpdateLoop(60);
        loop.addUpdateObject(data);
        loop.addUpdateObject(screen);
    }

    public static GameData getGameData() {
        return data;
    }
}
