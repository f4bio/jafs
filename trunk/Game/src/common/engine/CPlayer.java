/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

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

    public static final CVector2 VEC_DOWN = new CVector2(0, 1);
    public static final CVector2 VEC_UP = new CVector2(0, -1);
    public static final CVector2 VEC_LEFT = new CVector2(-1, 0);
    public static final CVector2 VEC_RIGHT = new CVector2(1, 0);

    public static final CVector2 VEC_UP_RIGHT = new CVector2(Math.sqrt(2)/2,
            -Math.sqrt(2)/2);
    public static final CVector2 VEC_UP_LEFT = new CVector2(-Math.sqrt(2)/2,
            -Math.sqrt(2)/2);
    public static final CVector2 VEC_DOWN_RIGHT = new CVector2(Math.sqrt(2)/2,
            Math.sqrt(2)/2);
    public static final CVector2 VEC_DOWN_LEFT = new CVector2(-Math.sqrt(2)/2,
            Math.sqrt(2)/2);

    private int team;
    private CWeapon[] weapon;
    private int currentWeapon;
    private int id;
    private int health;

    public CPlayer() {
        team = TEAM_NONE;
        weapon = new CWeapon[3];
        currentWeapon = 0;
        size = new Dimension(50, 50);
        speed = 3.0d;
        health = 0;
        setPosition(0, 0);
        setDirection(1, 0);
    }

    public void setId(int i) {
        id = i;
    }

    public int getId() {
        return id;
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

    @Override
    public void render(Graphics2D g) {

    }

    public void move(CVector2 mov, double factor) {
        position.add(mov.mul_cpy(factor*speed));
    }
}
