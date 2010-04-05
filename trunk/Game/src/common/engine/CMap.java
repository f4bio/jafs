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

    public CMap() {
        textures = new ArrayList<CImage>();
    }

    public void setSize(int x, int y) {
        sizeX = x;
        sizeY = y;

        tile = new Tile[x][y];
    }

    public void setTile(int x, int y, Tile t) {
        try {
            tile[x][y] = t;
        } catch(Exception e) {

        }
    }

    public Dimension getSize() {
        return new Dimension(sizeX, sizeY);
    }

    public Dimension getRealSize() {
        return new Dimension(sizeX * tileSizeX, sizeY * tileSizeY);
    }

    public void setTileSize(int x, int y) {
        tileSizeX = x;
        tileSizeY = y;
    }

    public Dimension getTileSize() {
        return new Dimension(tileSizeX, tileSizeY);
    }

    public void addTexture(CImage texture) {
        textures.add(texture);
    }

    public CImage getTexture(int i) {
        return textures.get(i);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSpawnRed(int x, int y) {
        spawnRed = new Point(x, y);
    }

    public Point getSpawnRed() {
        return spawnRed;
    }

    public void setSpawnBlue(int x, int y) {
        spawnBlue = new Point(x, y);
    }

    public Point getSpawnBlue() {
        return spawnBlue;
    }

    public Point getTileByCoords(int x, int y) {
        int xx, yy;

        if(x < 0)
            xx = 0;
        else if(x > sizeX * tileSizeX)
            xx = sizeX - 1;
        else
            xx = x / tileSizeX;

        if(y < 0)
            yy = 0;
        else if(y > sizeY * tileSizeY)
            yy = sizeY - 1;
        else
            yy = y / tileSizeY;

        return new Point(xx, yy);
    }

    public Tile getTile(int x, int y) {
        return tile[x][y];
    }

    public Point getTileByCoords(Point p) {
        return getTileByCoords(p.x, p.y);
    }

    public void render(Graphics2D g) {
        for (int x = 0; x < tile.length; ++x) {
            for (int y = 0; y < tile[x].length; ++y) {
                g.drawImage(tile[x][y].getTexture(), x * tileSizeX, y * tileSizeY,
                        tileSizeX, tileSizeY, null);
            }
        }
    }

    public void renderEntity(Graphics2D g, CEntity entity) {
        
    }
}
