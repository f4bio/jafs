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
    /**
     *
     */
    public static final int TYPE_GROUND = 0;
    /**
     *
     */
    public static final int TYPE_OBSTACLE = 1;
    
    private int type;
    private int item;
    private CImage texture;

    /**
     *
     */
    public Tile() {
        texture = null;
        item = 0;
        type = 0;
    }

    /**
     *
     * @param item
     */
    public void setItem(int item) {
        this.item = item;
    }

    /**
     *
     * @return
     */
    public int getItem() {
        return item;
    }

    /**
     *
     * @param type
     */
    public void setType(final int type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @param texture
     */
    public void setTexture(CImage texture) {
        this.texture = texture;
    }

    /**
     *
     * @return
     */
    public BufferedImage getTexture() {
        return texture.getImage();
    }
}
