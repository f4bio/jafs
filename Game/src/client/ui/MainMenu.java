/*
 * MainMenu.java
 *
 * Created on 10.03.2010, 14:05:21
 */

package client.ui;

import java.awt.event.ActionListener;

/**
 *
 * @author Julian Sanio
 */
public class MainMenu extends UiWindow {

    /** Creates new form MainMenu */
    public MainMenu() {
        initComponents();
        setSize(getPreferredSize().width, getPreferredSize().height); // !!! bessere Implementierung?
        setUndecorated(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setName("MainMenu"); // NOI18N
        setPreferredSize(new java.awt.Dimension(150, 190));

        jButton1.setText("Server suchen");
        jButton1.setMaximumSize(new java.awt.Dimension(111, 23));
        jButton1.setMinimumSize(new java.awt.Dimension(111, 23));
        jButton1.setPreferredSize(new java.awt.Dimension(111, 28));

        jButton2.setText("Server erstellen");
        jButton2.setPreferredSize(new java.awt.Dimension(109, 28));

        jButton3.setText("Optionen");
        jButton3.setMaximumSize(new java.awt.Dimension(111, 23));
        jButton3.setMinimumSize(new java.awt.Dimension(111, 23));
        jButton3.setPreferredSize(new java.awt.Dimension(111, 23));

        jButton4.setText("Credits");
        jButton4.setMaximumSize(new java.awt.Dimension(111, 23));
        jButton4.setMinimumSize(new java.awt.Dimension(111, 23));
        jButton4.setPreferredSize(new java.awt.Dimension(111, 23));

        jButton5.setText("Beenden");
        jButton5.setMaximumSize(new java.awt.Dimension(111, 23));
        jButton5.setMinimumSize(new java.awt.Dimension(111, 23));
        jButton5.setPreferredSize(new java.awt.Dimension(111, 23));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addActionListener(ActionListener a) {
        jButton1.setActionCommand(UiWindow.ALCMD_SERVERBROWSER);
        jButton1.addActionListener(a);
        jButton2.addActionListener(a);
        jButton3.setActionCommand(UiWindow.ALCMD_OPTIONS);
        jButton3.addActionListener(a);
        jButton4.addActionListener(a);
        jButton5.addActionListener(a);
    }

}
