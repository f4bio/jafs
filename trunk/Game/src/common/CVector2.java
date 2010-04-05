/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.awt.Point;

/**
 *
 * @author miracle
 */
public class CVector2 {
    private double x;
    private double y;

    public CVector2() {
        x = 0.0d;
        y = 0.0d;
    }

    public CVector2(double x, double y) {
        set(x, y);
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(CVector2 vec) {
        if(vec == null)
            return;

        x += vec.x;
        y += vec.y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void sub(CVector2 vec) {
        if(vec == null)
            return;

        x -= vec.x;
        y -= vec.y;
    }

    public void sub(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public void mul(double a) {
        x *= a;
        y *= a;
    }

    public CVector2 mul_cpy(double a) {
        return new CVector2(x*a, y*a);
    }

    public double norm() {
        return Math.sqrt((x*x) + (y*y));
    }

    public Point get() {
        return new Point((int)x, (int)y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
