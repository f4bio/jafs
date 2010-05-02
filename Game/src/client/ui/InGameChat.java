/*
 * LobbyChat.java
 *
 * Created on 24.03.2010, 21:38:14
 */

package client.ui;

import client.Main;
import client.render.MainScreen;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListModel;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Julian Sanio
 */
public class InGameChat extends UiWindow implements MouseListener {

    /** Creates new form LobbyChat
     * @param scr MainScreen
     */
    public InGameChat(MainScreen scr) {
        super(scr);
        listModel = new DefaultListModel();
        initComponents();
        jList1.addMouseListener(this);
        DefaultCaret caret = (DefaultCaret)jTextArea1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        setUndecorated(true);
        setMoveable(false);
        setSize(getPreferredSize().width, getPreferredSize().height);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setAlignmentX(0.0F);
        setAlignmentY(0.0F);
        setName("InGameChat"); // NOI18N
        setPreferredSize(new java.awt.Dimension(302, 406));
        setRequestFocusEnabled(false);
        setLayout(null);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jTextArea1.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setSelectionColor(new java.awt.Color(204, 204, 204));
        jScrollPane1.setViewportView(jTextArea1);

        add(jScrollPane1);
        jScrollPane1.setBounds(0, 240, 300, 137);

        jTextField1.setBackground(new java.awt.Color(240, 240, 240));
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jTextField1.setMinimumSize(new java.awt.Dimension(2, 21));
        jTextField1.setPreferredSize(new java.awt.Dimension(2, 21));
        jTextField1.setSelectionColor(new java.awt.Color(204, 204, 204));
        add(jTextField1);
        jTextField1.setBounds(0, 380, 213, 21);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton1.setText("Senden");
        jButton1.setMaximumSize(new java.awt.Dimension(73, 21));
        jButton1.setMinimumSize(new java.awt.Dimension(73, 21));
        jButton1.setPreferredSize(new java.awt.Dimension(73, 21));
        add(jButton1);
        jButton1.setBounds(220, 380, 81, 21);

        jList1.setModel(listModel);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jList1);

        add(jScrollPane2);
        jScrollPane2.setBounds(0, 0, 156, 238);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    private DefaultListModel listModel;
    private int[] idlist = new int[16];
    private String[] namelist = new String[16];
    private int privateChatID = 0;
    private boolean privateChatMode = false;

    /**
     *
     * @param a ActionListener you want to implement
     */
    @Override
    public void addActionListener(ActionListener a) {
        jButton1.setActionCommand(UiActionListener.CMD_INGAMECHAT_SEND_MSG);
        jButton1.addActionListener(a);
        jTextField1.setActionCommand(UiActionListener.CMD_INGAMECHAT_SEND_MSG);
        jTextField1.addActionListener(a);
    }

    /**
     *
     * @param clientID ID of client you want to get th name
     * @return name of client
     */
    public String getClientName(int clientID){
        if(clientID == -1)
            return "Masterserver";
        else if(clientID == Main.getGameData().getSelfId())
            return Main.getGameData().getName();
        else
            return namelist[clientID];
    }

    /**
     *
     * @return ID of receiver client
     */
    public int getSelectedPrivateChatID(){
        return privateChatID;
    }

    /**
     *
     * @return true: private chat mode, false: public chat mode
     */
    public boolean isPrivateChatMode(){
        return privateChatMode;
    }

    /**
     * Clear list of clients
     */
    public void clearClientlist(){
        listModel.clear();
        idlist = new int[16];
        repaint();
    }

    /**
     *
     * @param clientID ID of client you want to add
     * @param playerName Name of client you want to add
     */
    public void addClientToList(int clientID, String playerName){
        idlist[listModel.size()] = clientID;
        namelist[clientID] = playerName;
        listModel.addElement(playerName);
        repaint();
    }

    /**
     *
     * @return Message you want to send
     */
    public String getMSG() {
        String str = jTextField1.getText();
        clearMsgField();
        return str;
    }

    /**
     *
     * @param msg Messag you want to add to InGame chat
     */
    public void appendMSG(String msg) {
        jTextArea1.append(msg + "\n");
    }

    /**
     * Clear Messagefield of InGame chat
     */
    public void clearMsgField() {
        jTextField1.setText("");
    }

    
    @Override
    public void mouseClicked(MouseEvent e) {
        // Open private tab
        if (e.getSource() == jList1 && !listModel.isEmpty()) {
            privateChatID = idlist[jList1.locationToIndex(e.getPoint())];
            System.out.println("selected id="+privateChatID);
            privateChatMode = true;
        }
        else {
            //jList1.setSelectedIndex(-1);
            jList1.clearSelection();
            privateChatMode = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {  }

    @Override
    public void mouseReleased(MouseEvent e) {  }

    @Override
    public void mouseEntered(MouseEvent e) {  }

    @Override
    public void mouseExited(MouseEvent e) {  }
}
