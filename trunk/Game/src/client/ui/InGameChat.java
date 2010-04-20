/*
 * LobbyChat.java
 *
 * Created on 24.03.2010, 21:38:14
 */

package client.ui;

import client.render.MainScreen;
import java.awt.event.ActionListener;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Julian Sanio
 */
public class InGameChat extends UiWindow {

    /** Creates new form LobbyChat */
    public InGameChat(MainScreen scr) {
        super(scr);
        initComponents();
        DefaultCaret caret = (DefaultCaret)jTextArea1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        setSize(getPreferredSize().width, getPreferredSize().height);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setName("LobbyChat"); // NOI18N
        setPreferredSize(new java.awt.Dimension(400, 312));
        setLayout(null);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jTextArea1.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setSelectionColor(new java.awt.Color(204, 204, 204));
        jScrollPane1.setViewportView(jTextArea1);

        add(jScrollPane1);
        jScrollPane1.setBounds(10, 11, 380, 263);

        jTextField1.setBackground(new java.awt.Color(240, 240, 240));
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jTextField1.setMinimumSize(new java.awt.Dimension(2, 21));
        jTextField1.setPreferredSize(new java.awt.Dimension(2, 21));
        jTextField1.setSelectionColor(new java.awt.Color(204, 204, 204));
        add(jTextField1);
        jTextField1.setBounds(10, 280, 293, 21);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jButton1.setText("Senden");
        jButton1.setMaximumSize(new java.awt.Dimension(73, 21));
        jButton1.setMinimumSize(new java.awt.Dimension(73, 21));
        jButton1.setPreferredSize(new java.awt.Dimension(73, 21));
        add(jButton1);
        jButton1.setBounds(309, 280, 81, 21);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addActionListener(ActionListener a) {
        jButton1.setActionCommand(UiActionListener.CMD_LOBBYCHAT_SEND_MSG);
        jButton1.addActionListener(a);
        jTextField1.setActionCommand(UiActionListener.CMD_LOBBYCHAT_SEND_MSG);
        jTextField1.addActionListener(a);
    }

    public String getMSG() {
        String str = jTextField1.getText();
        clearMsgField();
        return str;
    }

    public void appendMSG(String msg) {
        jTextArea1.append(msg + "\n");
    }

    public void clearMsgField() {
        jTextField1.setText("");
    }
}
