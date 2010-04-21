/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import client.anim.UpdateLoop;
import common.CVector2;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author miracle
 */
public class CPlayer extends CEntity {
    public static final int TEAM_NONE = 0;
    public static final int TEAM_RED = 1;
    public static final int TEAM_BLUE = 2;

    private int team;
    private CWeapon[] weapon;
    private int currentWeapon;
    private int health;

    public CPlayer() {
        team = TEAM_NONE;
        weapon = new CWeapon[2];
        weapon[0] = new CWeaponPistol();
        weapon[1] = new CWeaponRifle();
        currentWeapon = 0;
        size = new Dimension(50, 50);
        speed = 3.0d;
        health = 100;
        setPosition(50, 50);
        setDirection(1, 0);
    }

    public void setTeam(final int team) {
        this.team = team;
    }

    public int getTeam() {
        return team;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public boolean isDead() {
        if(health <= 0)
            return true;
        return false;
    }

    public void setCurrentWeapon(int i) {
        if(i < 0 || i > weapon.length - 1)
            return;
        currentWeapon = i;
    }

    public int getCurrentWeapon() {
        return currentWeapon;
    }

    public CWeapon getWeapon(int idx) {
        if(idx > -1 && idx < weapon.length)
            return weapon[idx];
        return null;
    }

    @Override
    public void render(Graphics2D g) {

    }

    public void hit(CEntity ent) {
        CProjectile c = (CProjectile)ent;
    }

    public void move(CVector2 mov, double factor) {
        position.add(mov.mul_cpy(factor*speed));
    }

    public CProjectile shoot(UpdateLoop u) {
        return weapon[currentWeapon].shoot(this, u);
    }
}
