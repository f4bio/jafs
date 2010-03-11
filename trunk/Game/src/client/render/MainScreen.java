/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.render;

import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import client.anim.UpdateObject;
import client.anim.UpdateLoop;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author miracle
 */
public class MainScreen extends JFrame implements UpdateObject {
    private BufferStrategy buffer;
    private Dimension screenSize;
    private Viewport gamescene;

    public MainScreen() {
        this.screenSize = this.getToolkit().getScreenSize();
        this.setSize(screenSize);
        this.setResizable(false);
        this.setUndecorated(true);
        this.createBufferStrategy(2);
        this.buffer = this.getBufferStrategy();

        this.setVisible(true);
    }

    public void update(UpdateLoop u) {
        do {
            Graphics g = buffer.getDrawGraphics();

            if(gamescene != null) {
                gamescene.renderScene();

            } else {

            }

            buffer.show();
        } while(buffer.contentsLost());
    }

    public void clear() {
        buffer.getDrawGraphics().fillRect(0, 0, screenSize.width, screenSize.height);
    }

    public void createGameScene() {
        gamescene = new Viewport(this.getGraphicsConfiguration());
    }
}
