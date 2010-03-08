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

    private String name;
    private int team;
    private CWeapon primary;
    private CWeapon secondary;
    private int id;
}
