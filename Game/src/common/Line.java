/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

/**
 *
 * @author miracle
 */
public class Line {
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    /**
     *
     * @param sX
     * @param sY
     * @param eX
     * @param eY
     */
    public Line(double sX, double sY, double eX, double eY) {
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;
    }

    /**
     *
     */
    public Line() {
        startX = 0;
        startY = 0;
        endX = 0;
        endY = 0;
    }

    /**
     *
     * @param sX
     * @param sY
     */
    public void setStart(double sX, double sY) {
        startX = sX;
        startY = sY;
    }

    /**
     *
     * @param p
     */
    public void setStart(CVector2 p) {
        startX = p.getX();
        startY = p.getY();
    }

    /**
     *
     * @param eX
     * @param eY
     */
    public void setEnd(double eX, double eY) {
        endX = eX;
        endY = eY;
    }

    /**
     *
     * @param p
     */
    public void setEnd(CVector2 p) {
        endX = p.getX();
        endY = p.getY();
    }
}
