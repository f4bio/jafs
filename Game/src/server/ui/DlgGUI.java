/*
 * DlgGUI.java
 *
 * Created on 02.05.2010, 15:15:52
 */

package server.ui;

import common.net.Network;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author Julian Sanio
 */
public class DlgGUI extends javax.swing.JDialog  implements ActionListener, WindowListener, Runnable {

    /** Creates new form DlgGUI */
    public DlgGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        if(!parent.equals(client.Main.getMainMenu()))
            standalone = true;
        jTextField2.setText(Network.MASTERHOST+":"+Network.MASTERPORT);
        jButton1.addActionListener(this);
        addWindowListener(this);
        t = new Thread(this);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Server erstellen");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/common/resource/Logo.png"))); // NOI18N

        jPanel1.setLayout(new java.awt.GridLayout(3, 2));

        jLabel3.setText("Servername");
        jPanel1.add(jLabel3);

        jTextField1.setText("Server");
        jPanel1.add(jTextField1);

        jLabel4.setText("Map");
        jPanel1.add(jLabel4);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "map", "map1", "rhein1", "rhein2", "labyrinth", "labyrinth2" }));
        jPanel1.add(jComboBox1);

        jLabel5.setText("Masterserver");
        jPanel1.add(jLabel5);
        jPanel1.add(jTextField2);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton1.setText("Starten");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
    private boolean started = false;
    private boolean standalone = false;
    private Thread t;

    @Override
    public void setVisible(boolean b){
        if(!standalone){
            jTextField2.setText(client.Main.getNetwork().MASTERHOST+":"+client.Main.getNetwork().MASTERPORT);
        }
        super.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
//        if(e.getSource() == jButton1) {
        server.Main.getNetwork().MASTERHOST = jTextField2.getText().split(":")[0];
        server.Main.getNetwork().MASTERPORT = Integer.parseInt(jTextField2.getText().split(":")[1]);
        if(!started){
            t.start();
            jButton1.setEnabled(false);
        } else {
            started = false;
            jButton1.setText("Starten");
        }
//        }
    }

    public void windowOpened(WindowEvent e) { }

    public void windowClosing(WindowEvent e) {
        dispose();
    }

    public void windowClosed(WindowEvent e) { }

    public void windowIconified(WindowEvent e) { }

    public void windowDeiconified(WindowEvent e) { }

    public void windowActivated(WindowEvent e) { }

    public void windowDeactivated(WindowEvent e) { }

    public void run() {
        String[] args = new String[2];
        args[0] = jTextField1.getText();
        args[1] = (String)jComboBox1.getSelectedItem();
        server.Main.main(args);
        started = true;
        jButton1.setText("Beenden");
        jButton1.setEnabled(true);
    }

}
