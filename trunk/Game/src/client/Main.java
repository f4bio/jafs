package client;

import client.anim.UpdateLoop;
import client.render.MainScreen;
import client.ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 *
 * @author miracle
 */
public class Main implements ActionListener, KeyListener {

    /**
     * @param args the command line arguments
     */
    private static MainScreen screen;
    private UiWindow main;
    private UiWindow serverbowser;
    private UiWindow options;
    private WeaponSidebar wSidebar;

    public Main(){
        JFrame frm = new JFrame();
        frm.addKeyListener(this);
        frm.setVisible(true);
        screen = new MainScreen(frm);

        // Ui
        main = new MainMenu();
        main.setLocation(10, 200);
        main.addActionListener(this);
        main.setVisible(true);
        serverbowser = new Serverbrowser();
        serverbowser.setLocation(60, 300);
        serverbowser.addActionListener(this);
        options = new Options();
        options.setLocation(600, 400);
        options.addActionListener(this);
        wSidebar = new WeaponSidebar(300, 100);
        wSidebar.setLocation(screen.getWidth()-wSidebar.getWidth(), 100);
        wSidebar.setVisible(true);

        // UiManager
        UiManager.init();
        UiManager.addComponent(main);
        UiManager.addComponent(serverbowser);
        UiManager.addComponent(options);
        UiManager.addComponent(wSidebar);

        screen.getContentPane().add(main);
        screen.getContentPane().add(serverbowser);
        screen.getContentPane().add(options);
        screen.getContentPane().add(wSidebar);

        UpdateLoop loop = new UpdateLoop(60);
        loop.addUpdateObject(screen);

        /*Protocol.init();
        Network net = new Network();
        ProtocolHandler handler = new ProtocolHandler(net);*/
    }

    public static MainScreen getScreen() {
        return screen;
    }

    // ActionListener
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand() + " (actionPerformed by " + e.getSource().getClass().getSimpleName() + ")");
        // Serverbrowser
        if(e.getActionCommand().equals(UiWindow.ALCMD_SERVERBROWSER)) {
            serverbowser.setVisible(true);
        }
        // Options
        else if(e.getActionCommand().equals(UiWindow.ALCMD_OPTIONS)) {
            options.setVisible(true);
        }
        // Exit
        else if(e.getActionCommand().equals("Beenden")) {
            System.exit(0);
        }
    }

    // KeyListener
    public void keyTyped(KeyEvent e) {  }

    public void keyPressed(KeyEvent e) {
        System.out.println("Pressed "+e.getKeyChar());
        if(e.getKeyCode() == KeyEvent.VK_A){
            wSidebar.setAktiveWeapon(wSidebar.getAktiveWeapon()+1);
        } else if(e.getKeyCode() == KeyEvent.VK_Q){
            wSidebar.setAktiveWeapon(wSidebar.getAktiveWeapon()-1);
        }
    }

    public void keyReleased(KeyEvent e) {  }

    public static void main(String[] args) {
        new Main();
    }
}
