package client.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;

/**
 *
 * @author Julian Sanio
 */
public class TestUI extends JFrame {

    private UiWindow uiTest;

    public TestUI(){
        super("UserInterface - Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        uiTest = new MainMenu();
        uiTest.setVisible(true);
        UiManager.init();
        UiManager.addComponent("Test", uiTest);
      
        setVisible(true);
    }

    @Override
    public void paint(Graphics g){
        UiManager.renderAll((Graphics2D) g);
    }

    public static void main(String[] args) {
        new TestUI();
    }
}
