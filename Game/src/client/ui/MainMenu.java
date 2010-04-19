/*
 * Gui.java
 *
 * Created on 16.04.2010, 16:40:19
 */

package client.ui;

import client.Main;
import common.net.Client;
import common.net.Network;
import common.net.ProtocolCmd;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.DefaultCaret;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author Julian Sanio
 */
public class MainMenu extends javax.swing.JFrame implements ActionListener, MouseListener, KeyListener {

    private ArrayList<PrivateChatTab> clientlist = new ArrayList<PrivateChatTab>();
    private Network net;
    
    /** Creates new form Gui */
    public MainMenu(UiActionListener uiaListener, Network net) {
        this.net = net;
        sModel = new ServerbrowserTableModel();
        listModel = new DefaultListModel();
        initComponents();
        jButton1.setBackground(btn_bg_aktive);
        jButton1.addActionListener(this);
        jButton2.setBackground(btn_bg_normal);
        jButton2.addActionListener(this);
        jButton4.setBackground(btn_bg_normal);
        jButton4.addActionListener(this);

        jButton4.setActionCommand(UiActionListener.CMD_EXIT);
        jButton4.addActionListener(uiaListener);

        jButton7.setBackground(btn_bg_normal);
        jButton7.addActionListener(this);
        jButton7.setActionCommand(UiActionListener.CMD_TOGGLE_SERVERBROWSER);
        jButton7.addActionListener(uiaListener);

        jButton5.setActionCommand(UiActionListener.CMD_REFRESH_SERVERBROWSER);
        jButton5.addActionListener(uiaListener);

        jButton6.setActionCommand(UiActionListener.CMD_CONNECT);
        jButton6.addActionListener(uiaListener);

        jButton3.setActionCommand(UiActionListener.CMD_LOBBYCHAT_SEND_MSG);
        jButton3.addActionListener(uiaListener);
        jList1.addMouseListener(this);
        jTabbedPane1.addMouseListener(this);

        jTextField1.addKeyListener(this);
                
        DefaultCaret caret = (DefaultCaret)jTextArea1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
    }

    public void clearClientlist(){
        clientlist.clear();
        listModel.clear();
    }

    public void addClientToList(Client client){
        clientlist.add(new PrivateChatTab(client));
        listModel.addElement(client.getPlayer().getName());
        repaint();
        pack();
    }

    private void openPrivateChatTab(int id) {
        jTabbedPane1.addTab(clientlist.get(id).getName(), clientlist.get(id).getTextArea());
        jTabbedPane1.setToolTipTextAt(id, "Doubleclick to close private chat tab.");
        jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount()-1);
    }

    public void setServerlist(String[][] list) {
        sModel.setServerlist(list);
    }

    public int getSelectedServerlistIndex(){
        return jTable1.getSelectedRow();
    }

    public void refreshValue(String v, int row, int col){
        sModel.setValueAt(v, row, col);
        repaint();
        pack();
    }

    private int getPrivateChatTabId(int clientID){
        if(Main.getGameData().getSelfId() == clientID)
            return Main.getGameData().getSelfId();
        for(int i=0; i<clientlist.size(); i++) {
            if(clientlist.get(i).getID() == clientID)
                return i;
        }
        System.out.println("no id matched");
        return -1;
    }

    public void appendIncommingMSG(boolean privateMsg, int senderID, String msg){
        System.out.print("appendIncommingMSG(...)");
        // PRIVATE
        if(privateMsg) {
            System.out.println(" (private msg, senderID="+senderID+")");
            openPrivateChatTab(getPrivateChatTabId(senderID));
            System.out.println("bla");

            // Integer.parseInt(jTabbedPane1.getSelectedComponent().getName())
//            clientlist.get(getPrivateChatTabId(senderID)).getTextArea().append(Main.getClientName(senderID) + ": " + msg + "\n");
        }
        // PUBLIC
        else {
            System.out.println(" (public msg, senderID="+senderID+")");
            jTextArea1.append(Main.getClientName(senderID) + ": " + msg + "\n");
        }
    }

    public void sendLobbyMsg(){
        if(!jTextField1.getText().equals("")){
            // PRIVATE LOBBY CHAT
            if(jTabbedPane1.getSelectedIndex()>0){
                net.send(Network.MASTERHOST,
                         Network.MASTERPORT,
                         ProtocolCmd.CLIENT_MASTER_CHAT_PRIVATE,
                         argInt(Integer.parseInt(jTabbedPane1.getSelectedComponent().getName())),
                         argStr(jTextField1.getText()));
            }
            // PUBLIC LOBBY CHAT
            else {
                if(jTextField1.getText() != null && !jTextField1.getText().equals(""))
                net.send(Network.MASTERHOST,
                         Network.MASTERPORT,
                         ProtocolCmd.CLIENT_MASTER_CHAT_LOBBY,
                         argStr(jTextField1.getText()));
            }
            jTextField1.setText("");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable1.setModel(sModel);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable1);

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 10));
        jButton5.setText("Aktualisieren");

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 10));
        jButton6.setText("Verbinden");

        jCheckBox1.setText("Filter");
        jCheckBox1.setEnabled(false);

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField2.setEnabled(false);

        jLabel2.setText("Map");
        jLabel2.setEnabled(false);

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField3.setEnabled(false);

        jLabel3.setText("Ping");
        jLabel3.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 378, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap())
        );

        jLabel1.setText("OPTIONEN");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(256, 256, 256)
                .addComponent(jLabel1)
                .addContainerGap(335, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addComponent(jLabel1)
                .addContainerGap(251, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("J.A.F.S.");
        setResizable(false);

        jPanel1.setLayout(new java.awt.GridLayout(1, 4));

        jButton1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jButton1.setText("Lobby");
        jPanel1.add(jButton1);

        jButton7.setFont(new java.awt.Font("Verdana", 1, 18));
        jButton7.setText("Serverbrowser");
        jPanel1.add(jButton7);

        jButton2.setFont(new java.awt.Font("Verdana", 1, 18));
        jButton2.setText("Optionen");
        jPanel1.add(jButton2);

        jButton4.setFont(new java.awt.Font("Verdana", 1, 18));
        jButton4.setText("Verlassen");
        jPanel1.add(jButton4);

        jPanel2.setAlignmentY(0.0F);
        jPanel2.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(610);
        jSplitPane1.setDividerSize(3);

        jList1.setModel(listModel);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setToolTipText("Doubleclick to open private chat tab.");
        jScrollPane1.setViewportView(jList1);

        jSplitPane1.setRightComponent(jScrollPane1);

        jTextField1.setMinimumSize(new java.awt.Dimension(6, 21));
        jTextField1.setPreferredSize(new java.awt.Dimension(6, 21));

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 10));
        jButton3.setText("Senden");
        jButton3.setMaximumSize(new java.awt.Dimension(67, 20));
        jButton3.setMinimumSize(new java.awt.Dimension(67, 20));
        jButton3.setPreferredSize(new java.awt.Dimension(67, 20));

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jTabbedPane1.addTab("Öffentlich", jScrollPane2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jSplitPane1.setLeftComponent(jPanel3);

        jPanel2.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
    private Color btn_bg_normal = new Color(238, 238, 238);
    private Color btn_bg_aktive = new Color(200, 221, 242);
    private ServerbrowserTableModel sModel;
    private DefaultListModel listModel;

   /**
    * @param args the command line arguments
    */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new MainMenu().setVisible(true);
//            }
//        });
//    }

    public void actionPerformed(ActionEvent e) {
        // Senden
        if(e.getSource() == jButton3){
            sendLobbyMsg();
        } else {
            jPanel2.removeAll();
            jButton1.setBackground(btn_bg_normal);
            jButton2.setBackground(btn_bg_normal);
            jButton4.setBackground(btn_bg_normal);
            jButton7.setBackground(btn_bg_normal);

            // Lobby
            if(e.getSource() == jButton1){
                jButton1.setBackground(btn_bg_aktive);
                jPanel2.add(jSplitPane1, BorderLayout.CENTER);
            }
            // Serverbrowser
            else if(e.getSource() == jButton7){
                jButton7.setBackground(btn_bg_aktive);
                jPanel2.add(jPanel4, BorderLayout.CENTER);
            }
            // Optionen
            else if(e.getSource() == jButton2){
                jButton2.setBackground(btn_bg_aktive);
                jPanel2.add(jPanel5, BorderLayout.CENTER);
            }
            // Verlassen
            else if(e.getSource() == jButton4){
                jButton4.setBackground(btn_bg_aktive);
                System.exit(0);
            }
        }
        repaint();
        pack();
    }

    public void mouseClicked(MouseEvent e) {
        // Open private tab
        if (e.getSource() == jList1 && e.getClickCount() == 2) {
            openPrivateChatTab(jList1.locationToIndex(e.getPoint()));
        }
        // Close private tab
        else if (e.getSource() == jTabbedPane1 && jTabbedPane1.getSelectedIndex() != 0 && e.getClickCount() == 2) {
            jTabbedPane1.remove(jTabbedPane1.getSelectedIndex());
        }
    }

    public void mousePressed(MouseEvent e) {  }

    public void mouseReleased(MouseEvent e) {  }

    public void mouseEntered(MouseEvent e) {  }

    public void mouseExited(MouseEvent e) {  }

    public void keyTyped(KeyEvent e) {  }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            sendLobbyMsg();
        }
    }

    public void keyReleased(KeyEvent e) {  }


    /**
     *
     * @author Julian Sanio
     */
    public class PrivateChatTab {
        private Client cl;
        private JTextArea txt;
        public PrivateChatTab(Client client){
            cl = client;
            txt = new JTextArea();
            txt.setEditable(false);
            txt.setName(""+client.getId());
        }
        public int getID(){ return cl.getId(); }
        public String getName(){ return cl.getPlayer().getName(); }
        public JTextArea getTextArea(){ return txt; }
        public Client getClient(){ return cl; }
    }


    /**
     *
     * @author Julian Sanio
     */
    public class ServerbrowserTableModel extends AbstractTableModel {

        private String[] columnNames = { "Server", "Map", "Spieler", "Ping" };
        private String[][] serverList = new String [][] { {"No Masterserver.", "", "", ""} };

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
            return (String)serverList[row][col];
        }

        public void setServerlist(String[][] list) {
            this.serverList = list;
        }

        public String[][] getServerlist() {
            return serverList;
        }
    }
}
