/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.resource;

import client.Main;
import common.engine.CMap;
import common.engine.Tile;
import common.resource.CArchive;
import common.resource.CConfig;
import common.resource.CImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author miracle
 */
public class MapLoader extends Thread {
    public static final String mapFolder = "maps";

    private CMap map;
    private CArchive archive;
    private String mapName;
    private ActionListener al;

    public MapLoader(String map, ActionListener al) {
        this.map = null;
        this.mapName = map;
        this.archive = null;
        this.al = al;
    }

    public boolean load(String m) {
        String path = Main.PATH + mapFolder + "\\" + m + ".map";

        archive = new CArchive(path);
        CConfig config = new CConfig(archive.getFileData("about.ini"));
        map = new CMap();

        try {
            int sizeX = config.getValueI("Main", "tilesX");
            int sizeY = config.getValueI("Main", "tilesY");
            map.setSize(sizeX, sizeY);
            map.setTileSize(config.getValueI("Main", "tileSizeX"),
                    config.getValueI("Main", "tileSizeY"));
            map.setName(config.getValue("Main", "name"));

            int i = 0;
            String tex = null;

            while((tex = config.getValue("Main", "texture" + i)) != null) {
                map.addTexture(new CImage(archive, tex));
                i++;
            }

            for(int x=0; x<sizeX; ++x) {
                for(int y=0; y<sizeY; ++y) {
                    int type = config.getValueI("Tile " + x + " " + y, "typeId");
                    int texture = config.getValueI("Tile " + x + " " + y, "textureId");
                    int item = config.getValueI("Tile " + x + " " + y, "itemId");

                    Tile tile = new Tile();
                    tile.setItem(item);
                    tile.setTexture(map.getTexture(texture));
                    tile.setType(type);
                    map.setTile(x, y, tile);
                }
            }

            map.setSpawnRed(config.getValueI("SpawnRed", "tileX"),
                    config.getValueI("SpawnRed", "tileY"));
            map.setSpawnBlue(config.getValueI("SpawnBlue", "tileX"),
                    config.getValueI("SpawnBlue", "tileY"));
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        archive = null;
        
        return true;
    }

    public void setActionListener(ActionListener al) {
        this.al = al;
    }
    
    public void setMap(String map) {
        map = null;
        mapName = map;
        archive = null;
    }

    public CMap getMap() {
        return map;
    }

    public final void run() {
        boolean loaded = load(mapName);
        ActionEvent ae = new ActionEvent(this, 0x7A69, "maploaded" + loaded);

        if(al != null) {
            synchronized(al) {
                al.actionPerformed(ae);
            }
        }
    }
}
