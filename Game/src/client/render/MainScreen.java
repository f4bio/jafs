/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.render;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import client.ui.UiManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JWindow;

/**
 *
 * @author miracle
 */
public  class MainScreen extends JWindow implements UpdateObject {
    private VolatileImage buffer;
    private Dimension screenSize;
    private Viewport gamescene;
    private boolean init = true;
    private JLayeredPane pane;

    public MainScreen(JFrame owner) {
        super(owner);
        this.setIgnoreRepaint(true);
        this.screenSize = this.getToolkit().getScreenSize();
        this.setSize(screenSize);
        this.pane = new JLayeredPane();
        this.setContentPane(pane);
        this.getContentPane().setLayout(null);
        
        createBuffer();
        createGameScene();

        setVisible(true);
    }

    public void update(UpdateLoop a) {
        renderOffscreen(a);
        render(a);
    }

    public void render(UpdateLoop u) {
        Graphics2D g = null;
        try {
            g = (Graphics2D)getGraphics();
            do {
                int returnCode = buffer.validate(getGraphicsConfiguration());
                if (returnCode == VolatileImage.IMAGE_RESTORED) {
                    renderOffscreen(u);
                } else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                    createBuffer();
                    renderOffscreen(u);
                }
                g.drawImage(buffer, 0, 0, this);
            } while (buffer.contentsLost());
        } catch(Exception e) {

        } finally {
            if(g != null)
                g.dispose();
        }
    }

    public void renderOffscreen(UpdateLoop u) {
        Graphics2D g = buffer.createGraphics();

        clear(g);

        gamescene.renderScene(g);

        g.setColor(Color.white);
        g.drawString("speedfactor: " + u.getSpeedfactor(), 50, 50);
        g.drawString("current up/s: " + u.getCurrentUPS(), 50, 75);
        g.drawString("buffer accelerated: " + buffer.getCapabilities().isAccelerated(), 50, 100);

        UiManager.renderAll(g);
    }

    public void createBuffer() {
        buffer = getGraphicsConfiguration().createCompatibleVolatileImage(screenSize.width, screenSize.height);
    }

    public void clear(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, screenSize.width, screenSize.height);
    }

    public void createGameScene() {
        gamescene = new Viewport(getGraphicsConfiguration());
    }
}
