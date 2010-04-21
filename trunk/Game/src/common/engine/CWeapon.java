/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import client.anim.UpdateLoop;
import common.CVector2;
import common.resource.CImage;
import java.awt.image.BufferedImage;

/**
 *
 * @author miracle
 */
public class CWeapon {
    protected CImage texture;
    protected int damage;
    protected double critRate;
    protected long coolDown;
    protected long shotLast;
    protected double spread;
    protected int speed;
    protected int bullets;
    protected int maxBullets;
    protected int id;

    public void setDamage(int dmg) {
        this.damage = dmg;
    }

    public int getDamage() {
        return damage;
    }

    public void setCriticalRate(double rate) {
        critRate = rate;
    }

    public double getCriticalRate() {
        return critRate;
    }

    public void setCoolDown(long cd) {
        coolDown = cd;
    }

    public long getCoolDown() {
        return coolDown;
    }

    public long getShotLast() {
        return shotLast;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

    public double getSpread() {
        return spread;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setTexture(CImage img) {
        texture = img;
    }

    public BufferedImage getTexture() {
        return texture.getImage();
    }

    public void setMaxBullets(int b) {
        maxBullets = b;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public void setBullets(int b) {
        bullets = b;
    }

    public int getBullets() {
        return bullets;
    }

    public int getId() {
        return id;
    }

    public CProjectile shoot(CPlayer player, UpdateLoop u) {
        if(Math.abs(shotLast - u.getCurrentTime()) > coolDown && bullets > 0) {
            if(maxBullets != -1)
                bullets--;
            shotLast = u.getCurrentTime();
            CVector2 offset = player.getDirection().resize_cpy(25);
            CVector2 pos = player.getPosition().add_cpy(offset);
            return new CProjectile(player.getId(), speed, this, offset, pos);
        }
        return null;
    }
}
