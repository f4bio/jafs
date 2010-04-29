package server;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import client.resource.MapLoader;
import common.engine.CMap;
import common.engine.CPlayer;
import common.engine.CProjectile;
import common.engine.CWeapon;
import common.engine.CWeaponPistol;
import common.engine.CWeaponRifle;
import common.engine.ProjectileManager;
import common.net.ProtocolCmd;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public class Game implements UpdateObject {
    private MapLoader loader;
    private String map;
    private CPlayer[] player = new CPlayer[Main.getMaxPlayers()];
    private int scoreRed;
    private int scoreBlue;
    private CWeapon weapon[];

    /**
     *
     * @param map
     */
    public Game(String map) {
        weapon = new CWeapon[2];
        weapon[0] = new CWeaponPistol();
        weapon[1] = new CWeaponRifle();
        loader = new MapLoader(null, null);
        this.map = map;
        scoreRed = 0;
        scoreBlue = 0;
    }

    /**
     *
     * @param u
     */
    public void update(UpdateLoop u) {
        /*
         * Do magic. Collision detection for instance :3
         * Just broadcasting for now...
        */

        for(int i=0; i<player.length; ++i) {
            if(player[i] != null) {
                int id = player[i].getId();
            int weapon = player[i].getCurrentWeapon();
            int t = player[i].getTeam();
            double posX = player[i].getPosition().getX();
            double posY = player[i].getPosition().getY();
            double dirX = player[i].getDirection().getX();
            double dirY = player[i].getDirection().getY();

            Main.broadcast(ProtocolCmd.SERVER_CLIENT_PLAYER_INFO, argInt(id), argInt(t),
                    argInt(weapon), argDouble(posX), argDouble(posY),
                    argDouble(dirX), argDouble(dirY));
            }
        }

        ProjectileManager.checkProjectiles(u, player, getMap());
    }

    /**
     *
     * @return
     */
    public boolean load() {
        loader.setMap(map);

        return loader.load(map);
    }

    /**
     *
     * @return
     */
    public CMap getMap() {
        return loader.getMap();
    }

    /**
     *
     * @param p
     */
    public void addPlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = p;
    }

    /**
     *
     * @param p
     */
    public void removePlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = null;
    }


    /**
     *
     * @param i
     * @return
     */
    public CPlayer getPlayer(int i) {
        if(player != null && i < player.length && i > -1)
            return player[i];
        return null;
    }

    /**
     *
     * @param score
     */
    public void setScoreBlue(int score) {
        scoreBlue = score;
    }

    /**
     *
     * @param score
     */
    public void setScoreRed(int score) {
        scoreRed = score;
    }

    /**
     *
     * @return
     */
    public int getScoreBlue() {
        return scoreBlue;
    }

    /**
     *
     * @return
     */
    public int getScoreRed() {
        return scoreRed;
    }

    /**
     *
     * @return
     */
    public final int getWinnerTeam() {
        if(scoreBlue < scoreRed)
            return CPlayer.TEAM_RED;
        else if(scoreBlue > scoreRed)
            return CPlayer.TEAM_BLUE;
        else
            return -1; // DRAW
    }

    /**
     *
     * @param id
     * @return
     */
    public CWeapon getWeapon(int id) {
        if(id > -1 && id < weapon.length)
            return weapon[id];
        return null;
    }

    /**
     *
     * @param pId
     * @param c
     */
    public void hitPlayer(int pId, CProjectile c) {
        CPlayer p = player[pId];
        CWeapon w = getWeapon(c.getWeaponId());
        if(p != null && w != null && !p.isDead()) {
            p.setHealth(p.getHealth() - w.getDamage());
            if(p.isDead()) {
                Main.broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_KILLED, argInt(pId),
                        argInt(c.getOwner()));
            }
        }
    }
}
