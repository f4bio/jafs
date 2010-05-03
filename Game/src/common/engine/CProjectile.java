/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import common.CVector2;
import java.awt.Dimension;

/**
 *
 * @author miracle
 */
public class CProjectile extends CEntity {
    private int weapon;
    private CWeapon weaponObj;
    private CVector2 origin;
    private int owner;
    private boolean collided;

    /**
     *
     * @param owner
     * @param speed
     * @param weapon
     * @param direction
     * @param org
     */
    public CProjectile(int owner, int speed, CWeapon weapon, CVector2 direction, CVector2 org) {
         size = new Dimension(1,1);
         collided = false;
         this.speed = speed;
         this.origin = org;

         this.direction = direction;
         this.setPosition(org);
         this.weaponObj = weapon;
         this.weapon = weapon.getId();
         this.id = owner;
    }

    /**
     *
     * @param owner
     * @param speed
     * @param weapon
     * @param direction
     * @param org
     */
    public CProjectile(int owner, int speed, int weapon, CVector2 direction, CVector2 org) {
         size = new Dimension(1,1);
         collided = false;
         this.speed = speed;
         this.origin = org;

         this.direction = direction;
         this.setPosition(org);
         this.weapon = weapon;
         this.id = owner;
    }

    /**
     *
     * @param v
     */
    public void setOrigin(CVector2 v) {
        origin = v;
    }

    /**
     *
     * @return
     */
    public CVector2 getOrigin() {
        return origin;
    }

    /**
     *
     * @return
     */
    public int getWeaponId() {
        return weapon;
    }

    public void setCollided(boolean col) {
        collided = col;
    }

    public boolean isCollided() {
        return collided;
    }
}
