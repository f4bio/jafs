/*
 * DlgMasterserver.java
 *
 * Created on 02.05.2010, 14:21:48
 */

package client.ui;

import client.Main;
import common.net.Network;
import common.net.ProtocolCmd;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author Administrator
 */
public class DlgMasterserver extends javax.swing.JDialog implements WindowListener {

    /** Creates new form DlgMasterserver */
    public DlgMasterserver(java.awt.Frame parent, boolean modal, UiActionListener aListener) {
        super(parent, modal);
        initComponents();
        jTextField1.setText(Network.MASTERHOST + ":" +Network.MASTERPORT);
        jButton1.setActionCommand(UiActionListener.CMD_AUTH_MASTERSERVER);
        jButton1.addActionListener(aListener);
        addWindowListener(this);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
    }

    /**
     *
     * @return Host adress
     */
    public String getHost(){
        return jTextField1.getText().split(":")[0];
    }

    /**
     *
     * @return Port
     */
    public int getPort(){
        return Integer.parseInt(jTextField1.getText().split(":")[1]);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Masterserver IP:Port");
        setResizable(false);

        jTextField1.setAlignmentX(0.0F);
        jTextField1.setAlignmentY(0.0F);

        jButton1.setText("OK");
        jButton1.setAlignmentY(0.0F);
        jButton1.setMaximumSize(new java.awt.Dimension(47, 20));
        jButton1.setMinimumSize(new java.awt.Dimension(47, 20));
        jButton1.setPreferredSize(new java.awt.Dimension(47, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    public void windowOpened(WindowEvent e) { }

    public void windowClosing(WindowEvent e) {
        Main.getNetwork().send(Network.MASTERHOST,
                               Network.MASTERPORT,
                               ProtocolCmd.CLIENT_MASTER_AUTH,
                               argStr(Main.getGameData().getName()));
        dispose();
    }

    public void windowClosed(WindowEvent e) { }

    public void windowIconified(WindowEvent e) { }

    public void windowDeiconified(WindowEvent e) { }

    public void windowActivated(WindowEvent e) { }

    public void windowDeactivated(WindowEvent e) { }
}
