/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import common.CVector2;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author miracle
 */
public class CEntity {
    /**
     *
     */
    public static final CVector2 VEC_DOWN = new CVector2(0, 1);
    /**
     *
     */
    public static final CVector2 VEC_UP = new CVector2(0, -1);
    /**
     *
     */
    public static final CVector2 VEC_LEFT = new CVector2(-1, 0);
    /**
     *
     */
    public static final CVector2 VEC_RIGHT = new CVector2(1, 0);

    /**
     *
     */
    public static final CVector2 VEC_UP_RIGHT = new CVector2(Math.sqrt(2)/2,
            -Math.sqrt(2)/2);
    /**
     *
     */
    public static final CVector2 VEC_UP_LEFT = new CVector2(-Math.sqrt(2)/2,
            -Math.sqrt(2)/2);
    /**
     *
     */
    public static final CVector2 VEC_DOWN_RIGHT = new CVector2(Math.sqrt(2)/2,
            Math.sqrt(2)/2);
    /**
     *
     */
    public static final CVector2 VEC_DOWN_LEFT = new CVector2(-Math.sqrt(2)/2,
            Math.sqrt(2)/2);

    /**
     *
     */
    protected CVector2 position;
    /**
     *
     */
    protected CVector2 direction;
    /**
     *
     */
    protected double speed;
    /**
     *
     */
    protected Dimension size;
    /**
     *
     */
    protected String name;
    /**
     *
     */
    protected int id;

    /**
     *
     */
    public CEntity() {
        position = null;
        direction = null;
        speed = 1.0d;
        size = null;
        name = null;
    }

    /**
     *
     * @param vec2
     */
    public void setPosition(CVector2 vec2) {
        if(vec2 == null)
            return;
        position = vec2;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setPosition(double x, double y) {
        if(position == null)
            position = new CVector2(x, y);
        else
            position.set(x, y);
    }

    /**
     *
     * @return
     */
    public CVector2 getPosition() {
        return position;
    }

    /**
     *
     * @param vec2
     */
    public void setDirection(CVector2 vec2) {
        if(vec2 == null)
            return;
        direction = vec2;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setDirection(double x, double y) {
        if(direction == null)
            direction = new CVector2(x, y);
        else
            direction.set(x, y);
    }

    /**
     *
     * @return
     */
    public CVector2 getDirection() {
        return direction;
    }

    /**
     *
     * @param s
     */
    public void setSpeed(double s) {
        speed = s;
    }

    /**
     *
     * @return
     */
    public double getSpeed() {
        return speed;
    }

    /**
     *
     * @param s
     */
    public void setSize(Dimension s) {
        if(s == null)
            return;
        size = s;
    }

    /**
     *
     * @param w
     * @param h
     */
    public void setSize(int w, int h) {
        size.setSize(w, h);
    }

    /**
     *
     * @return
     */
    public Dimension getSize() {
        return size;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param i
     */
    public void setId(int i) {
        id = i;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param g
     */
    public void render(Graphics2D g) {
        
    }

    private int collidePlayer(CPlayer[] player, CVector2 hit) {
        CPlayer owner = player[getId()];

        for(int i=0; i<player.length; i++){
            if(player[i] == null)
                continue;

            CVector2 p = player[i].getPosition();
            double distance = hit.getDistanceTo(p);
            if(!player[i].isDead() && player[i].getId() != getId()
                    && distance <= (player[i].getSize().width/2)
                    && player[i].getTeam() != owner.getTeam()){
                return i;
            }
        }
        return -2;
    }

    /**
     *
     * @param map
     * @param p
     * @return
     */
    public int collideWall(CMap map, CVector2 p){
        Point g = p.get();
        Point t;

        if(map == null)
            return -1;

        int sX = size.width/2;
        int sY = size.height/2;

        Point around[] = {
            new Point(g.x, g.y + sY),
            new Point(g.x, g.y - sY),
            new Point(g.x + sX, g.y),
            new Point(g.x - sX, g.y),
            new Point(g.x + sX, g.y + sY),
            new Point(g.x - sX, g.y + sY),
            new Point(g.x + sX, g.y - sY),
            new Point(g.x - sX, g.y - sY)
        };

        int colId = -1;

        for (int i = 0; i < around.length; i++) {

            t = map.getTileByCoords(around[i]);
            Tile hindrance = map.getTile(t.x, t.y);
            if (hindrance == null || hindrance.getType() == Tile.TYPE_OBSTACLE){
                colId = i;
                break;
            }
        }

        return colId;
    }

    /**
     *
     * @param map
     * @param mov
     * @param speedfactor
     * @param pc
     * @param p
     * @return
     */
    public int move(CMap map, CVector2 mov, double speedfactor, boolean pc, CPlayer[] p){
        if(map == null || mov == null)
            return -2;

        CVector2 next = position;
        CVector2 last = next;

        double m = speed * speedfactor;
        CVector2 end = position.add_cpy(mov.resize_cpy(m));
        CVector2 step = mov.resize_cpy(m);
        step.div(m);

        int ret = -2;
        int colId = -1;

        for(int i = 1; i <= (int)m + 1; i++) {
            last = next;
            next = (i == (int)m + 1) ? end :
                position.add_cpy(step.mul_cpy(i));

            colId = collideWall(map, next);

            if(colId != -1){
                ret = -1;
                break;
            } else if(pc && p != null) {
                ret = collidePlayer(p, next);
                if(ret > -1)
                    break;
            } else if(i == (int)m + 1) {
                last = next;
            }
        }

        setPosition(last);

        if(colId == -1 || pc)
            return ret;

        double newSpeed;

        if (mov.equals(VEC_UP_RIGHT)) {
            if (colId != 2 && colId != 6 && colId != 4) {
                newSpeed = 0.5 * speedfactor;
                return move(map, VEC_RIGHT, newSpeed, pc, p);
            } else {
                newSpeed = 0.5 * speedfactor;
                return move(map, VEC_UP, newSpeed, pc, p);
            }
        } else if (mov.equals(VEC_UP_LEFT)) {
            if (colId != 3 && colId != 5 && colId != 7) {
                newSpeed = 0.5 * speedfactor;
                return move(map, VEC_LEFT, newSpeed, pc, p);
            } else {
                newSpeed = 0.5 * speedfactor;
                return move(map, VEC_UP, newSpeed, pc, p);
            }
        } else if (mov.equals(VEC_DOWN_LEFT)) {
            if (colId != 3 && colId != 5 && colId != 7) {
                newSpeed = 0.5 * speedfactor;
                return move(map, VEC_LEFT, newSpeed, pc, p);
            } else {
                newSpeed = 0.5 * speedfactor;
                return move(map, VEC_DOWN, newSpeed, pc, p);
            }
        } else if (mov.equals(VEC_DOWN_RIGHT)) {
            if (colId != 2 && colId != 6 && colId != 4) {
                newSpeed = 0.5 * speedfactor;
                return move(map, VEC_RIGHT, newSpeed, pc, p);
            } else {
                newSpeed = 0.5 * speedfactor;
                return move(map, VEC_DOWN, newSpeed, pc, p);
            }
        }

        return ret;
    }
}
