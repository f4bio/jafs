package common.net;

import common.engine.CPlayer;
import java.net.InetSocketAddress;

/**
 *
 * @author adm1n
 */
public class Client {
    public static int STATUS_PENDING = 0;
    public static int STATUS_CONNECTED = 1;

    private String host;
    private int port;
    private boolean inGame;
    private InetSocketAddress address;
    private int status;
    private CPlayer player;
    
    public Client(String host, int port){
        this.host = host;
        this.port = port;
        address = new InetSocketAddress(host, port);
        status = STATUS_PENDING;
        player = new CPlayer();
    }

    public Client(InetSocketAddress adr) {
        this.host = adr.getHostName();
        this.port = adr.getPort();
        status = STATUS_PENDING;
        address = adr;
        this.inGame = false;
        player = new CPlayer();
    }

    public CPlayer getPlayer() {
        return player;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setId(int id) {
        player.setId(id);
    }

    public int getId() {
        return player.getId();
    }

    public void changeInGame() {
        this.inGame = !this.inGame;
    }

    public boolean isInGame() {
        return this.inGame;
    }

    public void setTeamId(int teamId) {
        player.setTeam(teamId);
    }

    public int getTeamId() {
        return player.getTeam();
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
