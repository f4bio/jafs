package client.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Julian Sanio
 */
public class TestUI extends JFrame {

    private UiWindow uiTest;

    public TestUI(){
        super("UserInterface - Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);


        uiTest = new UiWindow();
        UiManager.init();
        UiManager.addComponent("Test", uiTest);


        JPanel p = new JPanel();
        p.add(new UiWindow());
        getContentPane().add(p, BorderLayout.CENTER);
      
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
