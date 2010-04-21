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

    public CProjectile(int owner, int speed, CWeapon weapon, CVector2 direction, CVector2 org) {
         size = new Dimension(1,1);
         this.speed = speed;
         this.origin = org;

         this.direction = direction;
         this.setPosition(org);
         this.weaponObj = weapon;
         this.owner = owner;
    }

    public CProjectile(int owner, int speed, int weapon, CVector2 direction, CVector2 org) {
         size = new Dimension(1,1);
         this.speed = speed;
         this.origin = org;

         this.direction = direction;
         this.setPosition(org);
         this.weapon = weapon;
         this.owner = owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getOwner() {
        return owner;
    }

    public void setOrigin(CVector2 v) {
        origin = v;
    }

    public CVector2 getOrigin() {
        return origin;
    }

    public int getWeaponId() {
        return weapon;
    }
}
