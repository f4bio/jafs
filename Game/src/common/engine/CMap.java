/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import common.resource.CImage;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author miracle
 */
public class CMap {
    private String name;
    private int sizeX;
    private int sizeY;
    private int tileSizeX;
    private int tileSizeY;

    private ArrayList<CImage> textures;
    private Tile[][] tile;

    private Point spawnRed;
    private Point spawnBlue;

    /**
     *
     */
    public CMap() {
        textures = new ArrayList<CImage>();
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setSize(int x, int y) {
        sizeX = x;
        sizeY = y;

        tile = new Tile[x][y];
    }

    /**
     *
     * @param x
     * @param y
     * @param t
     */
    public void setTile(int x, int y, Tile t) {
        try {
            tile[x][y] = t;
        } catch(Exception e) {

        }
    }

    /**
     *
     * @return
     */
    public Dimension getSize() {
        return new Dimension(sizeX, sizeY);
    }

    /**
     *
     * @return
     */
    public Dimension getRealSize() {
        return new Dimension(sizeX * tileSizeX, sizeY * tileSizeY);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setTileSize(int x, int y) {
        tileSizeX = x;
        tileSizeY = y;
    }

    /**
     *
     * @return
     */
    public Dimension getTileSize() {
        return new Dimension(tileSizeX, tileSizeY);
    }

    /**
     *
     * @param texture
     */
    public void addTexture(CImage texture) {
        textures.add(texture);
    }

    /**
     *
     * @param i
     * @return
     */
    public CImage getTexture(int i) {
        return textures.get(i);
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
     * @param x
     * @param y
     */
    public void setSpawnRed(int x, int y) {
        spawnRed = new Point(x, y);
    }

    /**
     *
     * @return
     */
    public Point getSpawnRed() {
        return spawnRed;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setSpawnBlue(int x, int y) {
        spawnBlue = new Point(x, y);
    }

    /**
     *
     * @return
     */
    public Point getSpawnBlue() {
        return spawnBlue;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Point getTileByCoords(int x, int y) {
        int xx, yy;

        if(x < 0)
            xx = x-1;
        else if(x > sizeX * tileSizeX)
            xx = sizeX - 1;
        else
            xx = x / tileSizeX;

        if(y < 0)
            yy = y-1;
        else if(y > sizeY * tileSizeY)
            yy = sizeY - 1;
        else
            yy = y / tileSizeY;

        return new Point(xx, yy);
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Tile getTile(int x, int y) {
        if(x >= 0 && x < sizeX &&
                y >= 0 && y < sizeY)
            return tile[x][y];
        return null;
    }

    /**
     *
     * @param p
     * @return
     */
    public Point getTileByCoords(Point p) {
        return getTileByCoords(p.x, p.y);
    }

    /**
     *
     * @param g
     */
    public void render(Graphics2D g) {
        for (int x = 0; x < tile.length; ++x) {
            for (int y = 0; y < tile[x].length; ++y) {
                g.drawImage(tile[x][y].getTexture(), x * tileSizeX, y * tileSizeY,
                        tileSizeX, tileSizeY, null);
            }
        }
    }

    /**
     *
     * @param g
     * @param entity
     */
    public void renderEntity(Graphics2D g, CEntity entity) {
        
    }
}
