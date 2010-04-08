/*
 * Serverbrowser.java
 *
 * Created on 10.03.2010, 21:23:18
 */

package client.ui;

import client.render.MainScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Julian Sanio
 */
public class Serverbrowser extends UiWindow implements ActionListener {

    /** Creates new form Serverbrowser */
    public Serverbrowser(MainScreen scr) {
        super(scr);
        sModel = new ServerbrowserTableModel();
        initComponents();
        jCheckBox1.setBackground(UiWindow.UI_COLOR_TRANSPARENT);
        jTextField1.setBackground(UiWindow.UI_COLOR_TRANSPARENT);
        jTextField2.setBackground(UiWindow.UI_COLOR_TRANSPARENT);
        jCheckBox1.addActionListener(this);
        jButton1.addActionListener(this);
        setSize(getPreferredSize().width, getPreferredSize().height);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField2 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setName("Serverbrowser"); // NOI18N
        setPreferredSize(new java.awt.Dimension(520, 324));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(java.awt.SystemColor.controlHighlight);
        jTable1.setFont(new java.awt.Font("Consolas", 0, 11));
        jTable1.setModel(sModel);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setMaximumSize(new java.awt.Dimension(300, 256));
        jTable1.setName(""); // NOI18N
        jTable1.setRowMargin(0);
        jTable1.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setShowVerticalLines(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jTextField2.setBackground(new java.awt.Color(240, 240, 240));
        jTextField2.setEnabled(false);
        jTextField2.setSelectionColor(new java.awt.Color(204, 204, 204));

        jCheckBox1.setText("Filter");
        jCheckBox1.setEnabled(false);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton2.setText("Verbinden");
        jButton2.setMargin(new java.awt.Insets(1, 14, 1, 14));
        jButton2.setMaximumSize(new java.awt.Dimension(79, 21));
        jButton2.setMinimumSize(new java.awt.Dimension(79, 21));
        jButton2.setPreferredSize(new java.awt.Dimension(79, 21));

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton1.setText("Aktualisieren");
        jButton1.setMargin(new java.awt.Insets(1, 14, 1, 14));
        jButton1.setMaximumSize(new java.awt.Dimension(89, 21));
        jButton1.setMinimumSize(new java.awt.Dimension(89, 21));
        jButton1.setPreferredSize(new java.awt.Dimension(89, 21));

        jTextField1.setBackground(new java.awt.Color(240, 240, 240));
        jTextField1.setEnabled(false);
        jTextField1.setSelectionColor(new java.awt.Color(204, 204, 204));

        jLabel1.setText("Max Ping");
        jLabel1.setEnabled(false);

        jLabel2.setText("Map");
        jLabel2.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                                        .addGap(27, 27, 27))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                .addGap(75, 75, 75)))
                        .addGap(238, 238, 238)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
    private ServerbrowserTableModel sModel;

    @Override
    public void addActionListener(ActionListener a) {
        jButton1.setActionCommand(UiActionListener.CMD_REFRESH_SERVERBROWSER);
        jButton1.addActionListener(a);
        jButton2.setActionCommand(UiActionListener.CMD_CONNECT);
        jButton2.addActionListener(a);
    }

    public void actionPerformed(ActionEvent e) {
        if(jCheckBox1.isSelected()){
            jLabel1.setEnabled(true);
            jLabel2.setEnabled(true);
            jTextField1.setEnabled(true);
            jTextField2.setEnabled(true);
        } else {
            jLabel1.setEnabled(false);
            jLabel2.setEnabled(false);
            jTextField1.setEnabled(false);
            jTextField2.setEnabled(false);
        }
    }

    public void setServerlist(String[][] list) {
        sModel.setServerlist(list);
        jTable1.setModel(sModel);
        jScrollPane1.setViewportView(jTable1);
    }

    public String getSelectedServer(){
        if (jTable1.getSelectedRow() >= 0)
            return (String) sModel.getValueAt(jTable1.getSelectedRow(), 0);
        else
            return null;
    }

    public void refreshValue(String v, int row, int col){
        System.out.println("refresh @" + row + "," + col + " :" + v);
        sModel.setValueAt(v, row, col);
        jTable1.setModel(sModel);
        jScrollPane1.setViewportView(jTable1);
    }


    /**
     *
     * @author Julian Sanio
     */
    public class ServerbrowserTableModel extends AbstractTableModel {

        private String[] columnNames = {"Server", "Map", "Spieler", "Ping"};
        private String[][] serverList = new String [][] {  };

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return serverList.length;
        }

        public void setValueAt(String value, int row, int col) {
            serverList[row][col] = value;
        }

        public String getValueAt(int row, int col) {
            return serverList[row][col];
        }

        public void setServerlist(String[][] list) {
            this.serverList = list;
        }

        public String[][] getServerlist() {
            return serverList;
        }
    }
}
