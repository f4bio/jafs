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
public class CWeaponRailgun extends CWeapon {

    public CWeaponRailgun() {
        id = 2;
        setColor(Color.MAGENTA);
        setDamage(25);
        setCriticalRate(0.15);
        setCoolDown(1000000000L);
        setSpread(3);
        setSpeed(150);
        setMaxBullets(-1);
        setBullets(1);
    }
}
