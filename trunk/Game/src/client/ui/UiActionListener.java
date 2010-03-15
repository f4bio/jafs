package client.ui;

import client.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Julia Sanio
 */
public class UiActionListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
//        System.out.println(e.getActionCommand() + " (actionPerformed by " + e.getSource().getClass().getSimpleName() + ")");
        // Server erstellen
        if(e.getActionCommand().equals("Server erstellen")) {
            Main.getUiCreateServer().setVisible(Main.getUiCreateServer().isVisible()?false:true);
        }
        // Serverbrowser
        else if(e.getActionCommand().equals(UiWindow.ALCMD_SERVERBROWSER)) {
            Main.getUiServerbrowser().setVisible(Main.getUiServerbrowser().isVisible()?false:true);
        }
        // Options
        else if(e.getActionCommand().equals(UiWindow.ALCMD_OPTIONS)) {
            Main.getUiOptions().setVisible(Main.getUiOptions().isVisible()?false:true);
        }
        // Serverliste aktualisieren
        else if(e.getActionCommand().equals("Aktualisieren")) {
            String[][] s = {{"127.0.0.1", "dust", "0/16", "500"},   // !!! SERVERLIST INPUT?
                            {"192.0.0.1", "italy", "2/16", "80"},
                            {"162.0.0.1", "aztec", "5/16", "63"}};
            Main.getUiServerbrowser().setServerlist(s);
        }
        // Mit Server verbinden
        else if(e.getActionCommand().equals("Verbinden")) {
            System.out.println(Main.getUiServerbrowser().getSelectedServer());
        }
        // Credits
        else if(e.getActionCommand().equals("Credits")) {
            Main.getUiCredits().setVisible(Main.getUiCredits().isVisible()?false:true);
        }
        // Exit
        else if(e.getActionCommand().equals("Beenden")) {
            System.exit(0);
        }
    }
}
