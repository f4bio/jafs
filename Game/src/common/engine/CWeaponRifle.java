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
public class CWeaponRifle extends CWeapon {
    /**
     *
     */
    public CWeaponRifle() {
        id = 1;
        setColor(Color.CYAN);
        setDamage(11);
        setCriticalRate(0.25);
        setCoolDown(250000000L);
        setSpread(15);
        setSpeed(150);
        setMaxBullets(-1);
        setBullets(1);
    }
}
