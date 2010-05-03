/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import java.awt.Color;

/**
 *
 * @author miracle
 */
public class CWeaponPistol extends CWeapon {
    /**
     *
     */
    public CWeaponPistol() {
        id = 0;
        setColor(Color.YELLOW);
        setDamage(10);
        setCriticalRate(0.15);
        setCoolDown(500000000L);
        setSpread(10);
        setSpeed(150);
        setMaxBullets(-1);
        setBullets(1);
    }
}
