
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * GUI.java
 *
 * Created on 20.03.2010, 00:24:35
 */

/**
 *
 * @author Julian Sanio
 */
public class Launcher extends javax.swing.JDialog implements ActionListener {

    /** Creates new form GUI */
    public Launcher(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setSize(400, 300);
        initComponents();
        jButton1.addActionListener(this);
        jButton2.addActionListener(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("J.A.F.S. - Just Another Furious Shooter");
        setResizable(false);

        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jButton1.setText("Client");
        jPanel1.add(jButton1);

        jButton2.setText("Server");
        jPanel1.add(jButton2);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/common/resource/jafs_logo.png"))); // NOI18N
        getContentPane().add(jLabel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        // Checking Java version >= 6
        if ( Double.parseDouble(System.getProperty("java.version").substring(0, 3)) >= 1.6 ) {
            if( args.length >= 2 ) {
                if( args[0].equals("master") ) {
                    // launch masterserver
                    masterserver.Main.main(new String[0]);
                }
                else if( args[0].equals("server") ) {
                    // launch server -> args[1] = ip+port
                    server.Main.main(args);
                }
                else if( args[0].equals("client") ) {
                    // launch client -> args[1] = ip+port
                    client.Main.main(args);
                }
            }
            else {
                Launcher uiLauncher = new Launcher(new javax.swing.JFrame(), false);
                uiLauncher.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                uiLauncher.setLocation(screenSize.width/2-uiLauncher.getWidth()/2, screenSize.height/2-uiLauncher.getHeight()/2);
                uiLauncher.setVisible(true);
            }
        }
        else {
            // fehlermeldung java 6 re
            javax.swing.JOptionPane.showMessageDialog(null, "The Game requires Java 6, please install the latest JRE!", "Error", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jButton1) {
            // launch client
            dispose();
            client.Main.main(new String[0]);
        }
        else {
            // launch server
            dispose();
            server.Main.main(new String[0]);
        }
    }

}
