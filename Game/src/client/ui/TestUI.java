package client.ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Julian Sanio
 */
public class TestUI extends JFrame {

    public TestUI(){
        super("UserInterface - Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        JPanel p = new JPanel();
        p.add(new MainMenu());
        getContentPane().add(p, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new TestUI();
    }
}
