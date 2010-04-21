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

    public CVector2 add_cpy(CVector2 vec) {
        if(vec == null)
            return null;

        return new CVector2(x + vec.x, y + vec.y);
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

    public void rotate(double a) {
        x = x*Math.cos(a) - y*Math.sin(a);
        y = x*Math.sin(a) + y*Math.cos(a);
    }

    public CVector2 rotate_cpy(double a) {
        return new CVector2(x*Math.cos(a) - y*Math.sin(a),
                x*Math.sin(a) + y*Math.cos(a));
    }

    public void invert() {
        x = -x;
        y = -y;
    }

    public CVector2 invert_cpy() {
        return new CVector2(-x, -y);
    }

    public void resize(double l) {
        if(l <= 0)
            return;

        double n = norm();

        x /= n / l;
        y /= n / l;
    }

    public CVector2 resize_cpy(double l) {
        if(l <= 0)
            return null;

        double n = norm();

        return new CVector2(x/(n/l), y/(n/l));
    }

    public CVector2 cpy() {
        return new CVector2(x, y);
    }

    public double getDistanceTo(CVector2 vec) {
        double diffX = x - vec.x, diffY = y - vec.y;
        return Math.sqrt((diffX * diffX) + (diffY * diffY));
    }

    public boolean equals(CVector2 c) {
        if(x == c.getX() && y == c.getY())
            return true;
        return false;
    }
}
