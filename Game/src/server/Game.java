package server;

import common.engine.UpdateLoop;
import common.engine.UpdateObject;
import client.resource.MapLoader;
import common.engine.CMap;
import common.engine.CPlayer;
import common.engine.CProjectile;
import common.engine.CWeapon;
import common.engine.CWeaponPistol;
import common.engine.CWeaponRailgun;
import common.engine.CWeaponRifle;
import common.engine.ProjectileManager;
import common.engine.UpdateCountdown;
import common.engine.UpdateCountdownObject;
import common.net.ProtocolCmd;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public class Game implements UpdateObject, UpdateCountdownObject {
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
        weapon = new CWeapon[3];
        weapon[0] = new CWeaponPistol();
        weapon[1] = new CWeaponRifle();
        weapon[2] = new CWeaponRailgun();
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

        Main.getProjectileManager().checkProjectiles(u, player, getMap());

        for(int i=0; i<player.length; ++i) {
            if(player[i] == null)
                continue;
            
            int id = player[i].getId();
            String name = player[i].getName();
            int weapon = player[i].getCurrentWeapon();
            int t = player[i].getTeam();
            int h = player[i].getHealth();
            int k = player[i].getKills();
            int d = player[i].getDeaths();
            double posX = player[i].getPosition().getX();
            double posY = player[i].getPosition().getY();
            double dirX = player[i].getDirection().getX();
            double dirY = player[i].getDirection().getY();

            Main.broadcast(ProtocolCmd.SERVER_CLIENT_PLAYER_INFO, argInt(id), argStr(name), argInt(h),
                    argInt(k), argInt(d), argInt(t), argInt(weapon), argDouble(posX),
                    argDouble(posY), argDouble(dirX), argDouble(dirY));
        }

        long round = Main.getRoundTimeLeft();
        long res = Main.getRespawnTimeLeft();
        Main.broadcast(ProtocolCmd.SERVER_CLIENT_GAME_INFO, argLong(round), argLong(res),
                argInt(scoreRed), argInt(scoreBlue));
    }

    public void countdown(UpdateCountdown c) {
        String name = c.getName();
        if(name.equals("ping"))
            Main.ping();
        else if(name.equals("respawn"))
            Main.respawn();
        else if(name.equals("round"))
            Main.end();
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

    public CPlayer[] getPlayer() {
        return player;
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

    public int playerCount(int team) {
        if(team == CPlayer.TEAM_NONE)
            return 0;

        int n = 0;
        for(int i=0; i<player.length; ++i) {
            if(player[i] != null && player[i].getTeam() == team)
                n++;
        }

        return n;
    }

    public int getBestPlayer() {
        int kills = 0;
        int id = 0;
        for(int i=0; i<player.length; ++i) {
            if(player[i] == null)
                continue;

            if(player[i].getKills() > kills) {
                kills = player[i].getKills();
                id = i;
            }
        }
        return id;
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
            return 0; // DRAW
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
        CPlayer a = player[c.getId()];
        CWeapon w = getWeapon(c.getWeaponId());
        if(p != null && w != null && !p.isDead()) {
            p.setHealth(p.getHealth() - w.getDamage());
            if(p.isDead()) {
                Main.broadcast(ProtocolCmd.SERVER_CLIENT_EVENT_PLAYER_KILLED, argInt(pId),
                        argInt(c.getId()));
                if(a.getTeam() == CPlayer.TEAM_BLUE)
                    scoreBlue++;
                else
                    scoreRed++;
                p.setDeaths(p.getDeaths() + 1);
                a.setKills(a.getKills() + 1);
            }
        }
    }
}
