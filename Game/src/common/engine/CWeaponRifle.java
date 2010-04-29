/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

/**
 *
 * @author miracle
 */
public class CWeaponRifle extends CWeapon {
    /**
     *
     */
    public CWeaponRifle() {
        id = 1;
        setDamage(11);
        setCriticalRate(0.25);
        setCoolDown(100);
        setSpread(15);
        setSpeed(200);
        setMaxBullets(200);
        setBullets(0);
    }
}
