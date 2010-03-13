/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.render;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import client.ui.UiManager;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.BufferCapabilities.FlipContents;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.ImageCapabilities;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JWindow;

/**
 *
 * @author miracle
 */
public class MainScreen extends JWindow implements UpdateObject {
    private BufferStrategy buffer;
    private Dimension screenSize;
    private Viewport gamescene;
    private boolean init = true;

    public MainScreen(JFrame owner) {
        super(owner);
        this.setIgnoreRepaint(true);
        this.screenSize = this.getToolkit().getScreenSize();
        this.setLayout(null);
        this.setSize(screenSize);
        setVisible(true);
    }

    public void update(UpdateLoop u) {
        do {
            if(init) {
                ImageCapabilities img = new ImageCapabilities(true);

                try {
                    this.createBufferStrategy(2, new BufferCapabilities(img, img, FlipContents.UNDEFINED));
                } catch(AWTException e) {
                    System.out.println("Unable to create buffer strategy!");
                }

                this.buffer = this.getBufferStrategy();

                if(buffer != null)
                    init = false;
            }

            Graphics2D g = (Graphics2D)buffer.getDrawGraphics();

            clear(g);

            g.setColor(Color.white);
            g.drawString("speedfactor: " + u.getSpeedfactor(), 50, 50);
            g.drawString("current up/s: " + u.getCurrentUPS(), 50, 75);
            g.drawString("backbuffer accelerated: " + buffer.getCapabilities().getBackBufferCapabilities().isAccelerated(), 50, 100);
            g.drawString("frontbuffer accelerated: " + buffer.getCapabilities().getFrontBufferCapabilities().isAccelerated(), 50, 125);

            if(gamescene != null) {
                gamescene.renderScene();

            } else {

            }

            UiManager.renderAll(g);

            buffer.show();
            
            g.dispose();
        } while(buffer.contentsLost());
    }

    public void clear(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, screenSize.width, screenSize.height);
    }

    public void createGameScene() {
        gamescene = new Viewport(this.getGraphicsConfiguration());
    }
}
