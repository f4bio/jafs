/*
 * LobbyChat.java
 *
 * Created on 24.03.2010, 21:38:14
 */

package client.ui;

import client.render.MainScreen;
import java.awt.event.ActionListener;

/**
 *
 * @author Julian Sanio
 */
public class LobbyChat extends UiWindow {

    /** Creates new form LobbyChat */
    public LobbyChat(MainScreen scr) {
        super(scr);
        initComponents();
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

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jTextArea1.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setSelectionColor(new java.awt.Color(204, 204, 204));
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.setBackground(new java.awt.Color(240, 240, 240));
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jTextField1.setMinimumSize(new java.awt.Dimension(2, 21));
        jTextField1.setPreferredSize(new java.awt.Dimension(2, 21));
        jTextField1.setSelectionColor(new java.awt.Color(204, 204, 204));

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton1.setText("Senden");
        jButton1.setMaximumSize(new java.awt.Dimension(73, 21));
        jButton1.setMinimumSize(new java.awt.Dimension(73, 21));
        jButton1.setPreferredSize(new java.awt.Dimension(73, 21));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
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
    }

    public String getMSG() {
        return jTextField1.getText();
    }

    public void appendMSG(String msg) {
        jTextArea1.append(msg + "\n");
    }

    public void clearMsgField() {
        jTextField1.setText("");
    }
}