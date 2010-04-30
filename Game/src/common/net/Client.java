package common.net;

import common.engine.CPlayer;
import java.net.InetSocketAddress;

/**
 *
 * @author adm1n
 */
public class Client {
    /**
     *
     */
    public static int STATUS_PENDING = 0;
    /**
     *
     */
    public static int STATUS_CONNECTED = 1;

    private String host;
    private int port;
    private boolean inGame;
    private InetSocketAddress address;
    private int status;
    private int highscore = 0;
    private CPlayer player;
    
    /**
     *
     * @param host
     * @param port
     */
    public Client(String host, int port){
        this.host = host;
        this.port = port;
        address = new InetSocketAddress(host, port);
        status = STATUS_PENDING;
        player = new CPlayer();
    }

    /**
     *
     * @param adr
     */
    public Client(InetSocketAddress adr) {
        this.host = adr.getHostName();
        this.port = adr.getPort();
        status = STATUS_PENDING;
        address = adr;
        this.inGame = false;
        player = new CPlayer();
    }

    /**
     *
     * @return
     */
    public CPlayer getPlayer() {
        return player;
    }

    /**
     *
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @return
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        player.setId(id);
    }

    /**
     *
     * @return
     */
    public int getId() {
        return player.getId();
    }

    /**
     *
     */
    public void changeInGame() {
        this.inGame = !this.inGame;
    }

    /**
     *
     * @return
     */
    public boolean isInGame() {
        return this.inGame;
    }

    /**
     *
     * @param teamId
     */
    public void setTeamId(int teamId) {
        player.setTeam(teamId);
    }

    /**
     *
     * @return
     */
    public int getTeamId() {
        return player.getTeam();
    }

    /**
     *
     * @param status
     */
    public void setStatus(final int status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    public void setHighscore(int n){
        highscore = n;
    }

    public int getHighscore(){
        return highscore;
    }
}
