/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

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
    private int id;
    private int health;

    public CPlayer() {
        team = TEAM_NONE;
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

    public void move() {

    }
}
