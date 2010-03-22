package client.ui;

import client.Main_OLD;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Julia Sanio
 */
public class UiActionListener implements ActionListener {

    // Command list
    public static final String CMD_TOGGLE_SERVERBROWSER =   "1";
    public static final String CMD_TOGGLE_OPTIONS =         "2";
    public static final String CMD_TOGGLE_CREDITS =         "3";
    public static final String CMD_TOGGLE_CREATESERVER =    "4";
    public static final String CMD_EXIT =                   "5";
    public static final String CMD_CONNECT =                "6";
    public static final String CMD_REFRESH_SERVERBROWSER =  "7";

    public void actionPerformed(ActionEvent e) {
//        System.out.println(e.getActionCommand() + " (actionPerformed by " + e.getSource().getClass().getSimpleName() + ")");
        // Server erstellen
        if(e.getActionCommand().equals(CMD_TOGGLE_CREATESERVER)) {
            Main_OLD.getUiCreateServer().setVisible(Main_OLD.getUiCreateServer().isVisible()?false:true);
        }
        // Serverbrowser
        else if(e.getActionCommand().equals(CMD_TOGGLE_SERVERBROWSER)) {
            Main_OLD.getUiServerbrowser().setVisible(Main_OLD.getUiServerbrowser().isVisible()?false:true);
        }
        // Options
        else if(e.getActionCommand().equals(CMD_TOGGLE_OPTIONS)) {
            Main_OLD.getUiOptions().setVisible(Main_OLD.getUiOptions().isVisible()?false:true);
        }
        // Serverliste aktualisieren
        else if(e.getActionCommand().equals(CMD_REFRESH_SERVERBROWSER)) {
            String[][] s = {{"127.0.0.1:40000", "dust", "0/16", "500"},   // !!! SERVERLIST INPUT?
                            {"192.0.0.1:40001", "italy", "2/16", "80"},
                            {"162.0.0.1:40002", "aztec", "5/16", "63"}};
            Main_OLD.getUiServerbrowser().setServerlist(s);
        }
        // Mit Server verbinden
        else if(e.getActionCommand().equals(CMD_CONNECT)) {
            if(Main_OLD.getUiServerbrowser().getSelectedServer() != null)
                System.out.println(Main_OLD.getUiServerbrowser().getSelectedServer());
        }
        // Credits
        else if(e.getActionCommand().equals(CMD_TOGGLE_CREDITS)) {
            Main_OLD.getUiCredits().setVisible(Main_OLD.getUiCredits().isVisible()?false:true);
        }
        // Exit
        else if(e.getActionCommand().equals(CMD_EXIT)) {
            System.exit(0);
        }
    }
}
