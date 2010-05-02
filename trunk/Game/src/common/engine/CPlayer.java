/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author miracle
 */
public class CPlayer extends CEntity {
    /**
     *
     */
    public static final int TEAM_NONE = 0;
    /**
     *
     */
    public static final int TEAM_RED = 1;
    /**
     *
     */
    public static final int TEAM_BLUE = 2;

    private int team;
    private CWeapon[] weapon;
    private int currentWeapon;
    private int health;
    private int kills;
    private int deaths;

    /**
     *
     */
    public CPlayer() {
        team = TEAM_NONE;
        weapon = new CWeapon[2];
        weapon[0] = new CWeaponPistol();
        weapon[1] = new CWeaponRifle();
        currentWeapon = 0;
        size = new Dimension(50, 50);
        speed = 3.0d;
        health = 0;
        setPosition(50, 50);
        setDirection(1, 0);
        kills = 0;
        deaths = 0;
    }

    /**
     *
     * @param team
     */
    public void setTeam(final int team) {
        this.team = team;
    }

    /**
     *
     * @return
     */
    public int getTeam() {
        return team;
    }

    /**
     *
     * @param health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     *
     * @return
     */
    public int getHealth() {
        return health;
    }

    /**
     *
     * @return
     */
    public boolean isDead() {
        if(health <= 0)
            return true;
        return false;
    }

    /**
     *
     * @param i
     */
    public void setCurrentWeapon(int i) {
        if(i < 0 || i > weapon.length - 1)
            return;
        currentWeapon = i;
    }

    /**
     *
     * @return
     */
    public int getCurrentWeapon() {
        return currentWeapon;
    }

    /**
     *
     * @param idx
     * @return
     */
    public CWeapon getWeapon(int idx) {
        if(idx > -1 && idx < weapon.length)
            return weapon[idx];
        return null;
    }

    /**
     *
     * @param g
     */
    @Override
    public void render(Graphics2D g) {

    }

    /**
     *
     * @param ent
     */
    public void hit(CEntity ent) {
        System.out.println("hit player " + id);
    }
    
    /**
     *
     * @param u
     * @return
     */
    public CProjectile shoot(UpdateLoop u) {
        return weapon[currentWeapon].shoot(this, u);
    }

    public void setKills(int k) {
        kills = k;
    }

    public int getKills() {
        return kills;
    }

    public void setDeaths(int d) {
        deaths = d;
    }

    public int getDeaths() {
        return deaths;
    }

    public void moveToSpawn(CMap map) {
        Point p = (team == CPlayer.TEAM_BLUE) ? map.getSpawnBlue() :
            map.getSpawnRed();
        int x = p.x * map.getTileSize().width + map.getTileSize().width/2;
        int y = p.y * map.getTileSize().height + map.getTileSize().height/2;
        setPosition(x, y);
    }
}
