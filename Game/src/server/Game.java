package server;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import client.resource.MapLoader;
import common.engine.CMap;
import common.engine.CPlayer;
import common.net.Protocol;

/**
 *
 * @author miracle
 */
public class Game implements UpdateObject {
    private MapLoader loader;
    private String map;
    private CPlayer[] player = new CPlayer[Main.getMaxPlayers()];

    public Game(String map) {
        this.map = map;
    }

    public void update(UpdateLoop u) {
        /*
         * Do magic. Collision detection for instance :3
         * Just broadcasting for now...
        */

        for(int i=0; i<player.length; ++i) {
            if(player[i] != null) {
                int id = player[i].getId();
            int weapon = player[i].getCurrentWeapon();
            double posX = player[i].getPosition().getX();
            double posY = player[i].getPosition().getY();
            double dirX = player[i].getDirection().getX();
            double dirY = player[i].getDirection().getY();

            Main.broadcast(Protocol.SERVER_CLIENT_PLAYER_INFO, id, weapon,
                posX, posY, dirX, dirY);
            }
        }
    }

    public boolean load() {
        loader.setMap(map);

        return loader.load(map);
    }

    public CMap getMap() {
        return loader.getMap();
    }

    public void addPlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = p;
    }

    public void removePlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = null;
    }


    public CPlayer getPlayer(int i) {
        if(player != null && i < player.length && i > -1)
            return player[i];
        return null;
    }
}
