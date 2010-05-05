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
import javax.swing.JTextField;

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
        listModel.addElement("Alle");
        initComponents();
        jList1.addMouseListener(this);
        setUndecorated(true);
        setMoveable(false);
        setSize(getPreferredSize().width, getPreferredSize().height);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setAlignmentX(0.0F);
        setAlignmentY(0.0F);
        setName("InGameChat"); // NOI18N
        setPreferredSize(new java.awt.Dimension(160, 262));
        setRequestFocusEnabled(false);
        setLayout(null);

        jTextField1.setBackground(new java.awt.Color(240, 240, 240));
        jTextField1.setAlignmentX(0.0F);
        jTextField1.setAlignmentY(0.0F);
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jTextField1.setMinimumSize(new java.awt.Dimension(2, 21));
        jTextField1.setPreferredSize(new java.awt.Dimension(2, 21));
        jTextField1.setSelectionColor(new java.awt.Color(204, 204, 204));
        add(jTextField1);
        jTextField1.setBounds(0, 240, 160, 21);

        jList1.setModel(listModel);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setAlignmentX(0.0F);
        jList1.setAlignmentY(0.0F);
        jScrollPane2.setViewportView(jList1);

        add(jScrollPane2);
        jScrollPane2.setBounds(0, -2, 160, 240);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane2;
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
        listModel.addElement("Alle");
        idlist = new int[16];
        namelist = new String[16];
        repaint();
    }

    /**
     *
     * @param clientID ID of client you want to add
     * @param playerName Name of client you want to add
     */
    public void addClientToList(int clientID, String playerName){
        idlist[listModel.size()-1] = clientID;
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

    public JTextField getTextField(){
        return jTextField1;
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
        if (e.getSource() == jList1 && !listModel.isEmpty() && jList1.locationToIndex(e.getPoint()) > 0) {
            privateChatID = idlist[jList1.locationToIndex(e.getPoint())];
            System.out.println("selected id="+privateChatID);
            privateChatMode = true;
        }
        else {
//            jList1.clearSelection();
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
