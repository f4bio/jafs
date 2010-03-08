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
public class CEntity {
    protected CVector2 position;
    protected CVector2 direction;
    protected double speed;
    protected Dimension size;

    public CEntity() {
        position = null;
        direction = null;
        speed = 1.0d;
        size = null;
    }

    public void setPosition(CVector2 vec2) {
        if(vec2 == null)
            return;
        position = vec2;
    }

    public void setPosition(double x, double y) {
        position.set(x, y);
    }

    public CVector2 getPosition() {
        return position;
    }

    public void setDirection(CVector2 vec2) {
        if(vec2 == null)
            return;
        direction = vec2;
    }

    public void setDirection(double x, double y) {
        direction.set(x, y);
    }

    public CVector2 getDirection() {
        return direction;
    }

    public void setSpeed(double s) {
        speed = s;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSize(Dimension s) {
        if(s == null)
            return;
        size = s;
    }

    public void setSize(int w, int h) {
        size.setSize(w, h);
    }

    public Dimension getSize() {
        return size;
    }
}
