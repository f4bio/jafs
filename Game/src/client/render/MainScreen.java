/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.render;

import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import client.anim.UpdateObject;
import client.anim.UpdateLoop;
import client.ui.UiManager;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.BufferCapabilities.FlipContents;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.ImageCapabilities;

/**
 *
 * @author miracle
 */
public class MainScreen extends JFrame implements UpdateObject {
    private BufferStrategy buffer;
    private Dimension screenSize;
    private Viewport gamescene;
    private boolean init = true;

    public MainScreen() {
        super();
        this.screenSize = this.getToolkit().getScreenSize();
        this.setLayout(null);
        this.setSize(screenSize);
        this.setResizable(false);
        this.setUndecorated(true);


        this.setVisible(true);

        ImageCapabilities img = new ImageCapabilities(true);
        try {
            this.createBufferStrategy(2, new BufferCapabilities(img, img, FlipContents.UNDEFINED));
        } catch(AWTException e) {

        }
        
        this.buffer = this.getBufferStrategy();
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
