package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import common.CLog;
import common.net.Network;
import common.net.Protocol;
import common.utils.CUtils;
import java.util.ArrayList;

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
        CLog.init("debug.txt");
        Protocol.init();
        net = new Network();
        ProtocolHandler protocol = new ProtocolHandler(net);
        net.setProtocolHandler(protocol);
        net.listen(50001);
//        net.send("localhost", 40000, Protocol.client_server_clientcount);
//        net.send("localhost", 40000, Protocol.CLIENT_SERVER_AUTH);
        net.send("localhost", Network.MASTERPORT, Protocol.CLIENT_MASTER_AUTH);
        new Chat(net).start();

        /*Protocol.init();
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
        loop.addUpdateObject(screen);*/
    }

    public static GameData getGameData() {
        return data;
    }

    public static void completeServerlist(ArrayList<String> list) {
        System.out.println("not yet implemented");
    }
}