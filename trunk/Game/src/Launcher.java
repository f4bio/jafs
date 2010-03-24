
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
        Launcher dialog = new Launcher(new javax.swing.JFrame(), false);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(screenSize.width/2-dialog.getWidth()/2, screenSize.height/2-dialog.getHeight()/2);
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String launch = System.getProperty("user.dir") + "\\";
        if(e.getSource()==jButton1)
            launch += "client\\Main";
        else
            launch += "server\\Main";
        System.out.println("Main-path: "+launch);
        // Checking Java version >= 6
        if (Double.parseDouble(System.getProperty("java.version").substring(0, 3)) >= 1.6) {
            System.out.println("Java 6 installed > Launching Game with OpenGL support.");
            try {
                Runtime.getRuntime().exec("java -Dsun.java2d.opengl=True \"" + launch + "\"");
            } catch (IOException ex) {
                System.out.println("Could not launch game. " + ex);
            }
            setVisible(false);
        } else {
            System.out.println("The Game requires Java 6, please install the latest JRE!");
        }
    }

}
