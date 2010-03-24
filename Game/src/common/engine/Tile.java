/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import common.resource.CImage;
import java.awt.image.BufferedImage;

/**
 *
 * @author miracle
 */
public class Tile {
    public static final int TYPE_GROUND = 0;
    public static final int TYPE_OBSTACLE = 1;
    
    private int type;
    private int item;
    private CImage texture;

    public Tile() {
        texture = null;
        item = 0;
        type = 0;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getItem() {
        return item;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setTexture(CImage texture) {
        this.texture = texture;
    }

    public BufferedImage getTexture() {
        return texture.getImage();
    }
}
