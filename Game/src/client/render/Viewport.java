/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.image.VolatileImage;

/**
 *
 * @author miracle
 */
public class Viewport {
    public static final Dimension size = new Dimension(800, 600);

    private VolatileImage buffer;
    private GraphicsConfiguration gc;

    public Viewport(GraphicsConfiguration gc) {
        this.gc = gc;
        createBuffer();
    }

    public void clear(Graphics2D g) {
        if(buffer != null) {
            g.setColor(Color.black);
            g.fillRect(0, 0, size.width, size.height);
        }
    }

    private void createBuffer() {
        buffer = gc.createCompatibleVolatileImage(size.width, size.height);
    }

    public VolatileImage getBuffer() {
        return buffer;
    }

    public void renderScene() {
        do {
            int result = buffer.validate(gc);

            if(result == VolatileImage.IMAGE_INCOMPATIBLE)
                createBuffer();

            Graphics2D g = buffer.createGraphics();

            if(g != null) {
                clear(g);
                renderWorld(g);
                renderProjectiles(g);
                renderPlayers(g);
            }
        } while(buffer.contentsLost());
    }

    public void renderWorld(Graphics2D g) {

    }

    public void renderProjectiles(Graphics2D g) {

    }

    public void renderPlayers(Graphics2D g) {

    }
}
