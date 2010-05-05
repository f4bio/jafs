/*
 * MainMenu.java
 *
 * Created on 16.04.2010, 16:40:19
 */

package client.ui;

import client.Main;
import common.net.Client;
import common.net.Network;
import common.net.ProtocolCmd;
import common.net.Server;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.DefaultCaret;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author Julian Sanio
 */
public class MainMenu extends javax.swing.JFrame implements ActionListener, MouseListener, KeyListener, ListSelectionListener {

    private ArrayList<PrivateChatTab> clientlist    = new ArrayList<PrivateChatTab>();
    private ArrayList<PrivateChatTab> clientlistOld = new ArrayList<PrivateChatTab>();
    
    /** Creates new form MainMenu
     * @param uiaListener UiActionListener object
     */
    public MainMenu(UiActionListener uiaListener) {
        sModel = new ServerbrowserTableModel();
        listModel = new DefaultListModel();
        initComponents();
        clearServerinfoPanel();
        DefaultCaret caret = (DefaultCaret)taLobbyChatPublic.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        tblServerlist.getSelectionModel().addListSelectionListener(this);
        btnMenuLobby.setBackground(btn_bg_aktive);
        btnMenuLobby.addActionListener(this);
        btnMenuOptions.setBackground(btn_bg_normal);
        btnMenuOptions.addActionListener(this);
        btnSendLobbyChat.setActionCommand(UiActionListener.CMD_LOBBYCHAT_SEND_MSG);
        btnSendLobbyChat.addActionListener(uiaListener);
        btnMenuExit.setBackground(btn_bg_normal);
        btnMenuExit.addActionListener(this);
        btnMenuExit.setActionCommand(UiActionListener.CMD_EXIT);
        btnMenuExit.addActionListener(uiaListener);
        btnRefresh.setActionCommand(UiActionListener.CMD_REFRESH_SERVERBROWSER);
        btnRefresh.addActionListener(uiaListener);
        btnConnect.setActionCommand(UiActionListener.CMD_CONNECT);
        btnConnect.addActionListener(uiaListener);
        btnCreate.setActionCommand(UiActionListener.CMD_CREATE_SERVER);
        btnCreate.addActionListener(uiaListener);
        btnMenuServerbrowser.setBackground(btn_bg_normal);
        btnMenuServerbrowser.addActionListener(this);
        btnMenuServerbrowser.setActionCommand(UiActionListener.CMD_REFRESH_SERVERBROWSER);
        btnMenuServerbrowser.addActionListener(uiaListener);
        btnApplyOptionsPlayer.setActionCommand(UiActionListener.CMD_NICKCHANGE);
        btnApplyOptionsPlayer.addActionListener(uiaListener);
        btnApplyOptionsNetwork.setActionCommand(UiActionListener.CMD_APPLY_NETWORK_SETTINGS);
        btnApplyOptionsNetwork.addActionListener(uiaListener);
        lClientlist.addMouseListener(this);
        tbpLobbyChat.addMouseListener(this);
        tfLobbyChat.addKeyListener(this);
        tfMasterserverIP.setText(Network.MASTERHOST+":"+Network.MASTERPORT);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlServerbrowser = new javax.swing.JPanel();
        scpServerlist = new javax.swing.JScrollPane();
        tblServerlist = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        btnConnect = new javax.swing.JButton();
        scpServerinfo = new javax.swing.JScrollPane();
        pnlServerinfo = new javax.swing.JPanel();
        lblServerinfoNameTxt = new javax.swing.JLabel();
        lblServerinfoName = new javax.swing.JLabel();
        lblServerinfoMapTxt = new javax.swing.JLabel();
        lblServerinfoMap = new javax.swing.JLabel();
        lblServerinfoPlayersTxt = new javax.swing.JLabel();
        lblServerinfoPlayers = new javax.swing.JLabel();
        lblServerinfoIpPortTxt = new javax.swing.JLabel();
        lblServerinfoIpPort = new javax.swing.JLabel();
        lblServerinfoHighscoreTxt = new javax.swing.JLabel();
        lblServerinfoHighscore = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        pnlOptions = new javax.swing.JPanel();
        pnlOptionsPlayer = new javax.swing.JPanel();
        tfPlayerName = new javax.swing.JTextField();
        lblPlayerName = new javax.swing.JLabel();
        btnApplyOptionsPlayer = new javax.swing.JButton();
        pnlOptionsNetwork = new javax.swing.JPanel();
        tfMasterserverIP = new javax.swing.JTextField();
        lblMasterserverIP = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        tfUsername = new javax.swing.JTextField();
        lblUsername = new javax.swing.JLabel();
        pfPassword = new javax.swing.JPasswordField();
        btnApplyOptionsNetwork = new javax.swing.JButton();
        pnlMenu = new javax.swing.JPanel();
        btnMenuLobby = new javax.swing.JButton();
        btnMenuServerbrowser = new javax.swing.JButton();
        btnMenuOptions = new javax.swing.JButton();
        btnMenuExit = new javax.swing.JButton();
        pnlLobbyChat = new javax.swing.JPanel();
        sppLobbyChat = new javax.swing.JSplitPane();
        pnlLobbyChatClientList = new javax.swing.JPanel();
        scpClientlist = new javax.swing.JScrollPane();
        lClientlist = new javax.swing.JList();
        lblLobbyChatPlayerName = new javax.swing.JLabel();
        pnlLobbyChatTabs = new javax.swing.JPanel();
        tfLobbyChat = new javax.swing.JTextField();
        btnSendLobbyChat = new javax.swing.JButton();
        tbpLobbyChat = new javax.swing.JTabbedPane();
        scpLobbyChat = new javax.swing.JScrollPane();
        taLobbyChatPublic = new javax.swing.JTextArea();

        tblServerlist.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblServerlist.setModel(sModel);
        tblServerlist.getTableHeader().setReorderingAllowed(false);
        scpServerlist.setViewportView(tblServerlist);

        btnRefresh.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnRefresh.setText("Aktualisieren");

        btnConnect.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnConnect.setText("Verbinden");

        scpServerinfo.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        lblServerinfoNameTxt.setText("Servername");

        lblServerinfoName.setForeground(new java.awt.Color(102, 102, 102));
        lblServerinfoName.setText("<Servername>");

        lblServerinfoMapTxt.setText("Map");

        lblServerinfoMap.setForeground(new java.awt.Color(102, 102, 102));
        lblServerinfoMap.setText("<Map>");

        lblServerinfoPlayersTxt.setText("Spieler");

        lblServerinfoPlayers.setForeground(new java.awt.Color(102, 102, 102));
        lblServerinfoPlayers.setText("<Spieler>");

        lblServerinfoIpPortTxt.setText("Server-IP");

        lblServerinfoIpPort.setForeground(new java.awt.Color(102, 102, 102));
        lblServerinfoIpPort.setText("<IP:Port>");

        lblServerinfoHighscoreTxt.setText("Dein Highscore");

        lblServerinfoHighscore.setForeground(new java.awt.Color(102, 102, 102));
        lblServerinfoHighscore.setText("<Highscore>");

        javax.swing.GroupLayout pnlServerinfoLayout = new javax.swing.GroupLayout(pnlServerinfo);
        pnlServerinfo.setLayout(pnlServerinfoLayout);
        pnlServerinfoLayout.setHorizontalGroup(
            pnlServerinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServerinfoLayout.createSequentialGroup()
                .addGroup(pnlServerinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlServerinfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlServerinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblServerinfoNameTxt)
                            .addGroup(pnlServerinfoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblServerinfoName))))
                    .addGroup(pnlServerinfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlServerinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlServerinfoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblServerinfoIpPort))
                            .addComponent(lblServerinfoIpPortTxt)))
                    .addGroup(pnlServerinfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlServerinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlServerinfoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblServerinfoMap))
                            .addComponent(lblServerinfoMapTxt)))
                    .addGroup(pnlServerinfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlServerinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlServerinfoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblServerinfoPlayers))
                            .addComponent(lblServerinfoPlayersTxt)))
                    .addGroup(pnlServerinfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlServerinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlServerinfoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblServerinfoHighscore))
                            .addComponent(lblServerinfoHighscoreTxt))))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        pnlServerinfoLayout.setVerticalGroup(
            pnlServerinfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServerinfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblServerinfoNameTxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblServerinfoName)
                .addGap(18, 18, 18)
                .addComponent(lblServerinfoIpPortTxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblServerinfoIpPort)
                .addGap(18, 18, 18)
                .addComponent(lblServerinfoMapTxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblServerinfoMap)
                .addGap(18, 18, 18)
                .addComponent(lblServerinfoPlayersTxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblServerinfoPlayers)
                .addGap(18, 18, 18)
                .addComponent(lblServerinfoHighscoreTxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblServerinfoHighscore)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        scpServerinfo.setViewportView(pnlServerinfo);

        btnCreate.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnCreate.setText("Erstellen");

        javax.swing.GroupLayout pnlServerbrowserLayout = new javax.swing.GroupLayout(pnlServerbrowser);
        pnlServerbrowser.setLayout(pnlServerbrowserLayout);
        pnlServerbrowserLayout.setHorizontalGroup(
            pnlServerbrowserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlServerbrowserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scpServerlist, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlServerbrowserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scpServerinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlServerbrowserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnCreate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnConnect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlServerbrowserLayout.setVerticalGroup(
            pnlServerbrowserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServerbrowserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlServerbrowserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlServerbrowserLayout.createSequentialGroup()
                        .addComponent(scpServerinfo, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scpServerlist, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)))
        );

        pnlOptionsPlayer.setBorder(javax.swing.BorderFactory.createTitledBorder("Spieler"));

        lblPlayerName.setText("Spielername");

        btnApplyOptionsPlayer.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnApplyOptionsPlayer.setText("Übernehmen");

        javax.swing.GroupLayout pnlOptionsPlayerLayout = new javax.swing.GroupLayout(pnlOptionsPlayer);
        pnlOptionsPlayer.setLayout(pnlOptionsPlayerLayout);
        pnlOptionsPlayerLayout.setHorizontalGroup(
            pnlOptionsPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsPlayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOptionsPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOptionsPlayerLayout.createSequentialGroup()
                        .addComponent(lblPlayerName)
                        .addGap(29, 29, 29)
                        .addComponent(tfPlayerName, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                    .addComponent(btnApplyOptionsPlayer, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        pnlOptionsPlayerLayout.setVerticalGroup(
            pnlOptionsPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsPlayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOptionsPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPlayerName)
                    .addComponent(tfPlayerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(btnApplyOptionsPlayer)
                .addContainerGap())
        );

        pnlOptionsNetwork.setBorder(javax.swing.BorderFactory.createTitledBorder("Netzwerk"));

        tfMasterserverIP.setText("xxx.xxx.xxx.xxx:xxxxx");

        lblMasterserverIP.setText("Masterserver");

        lblPassword.setText("Passwort");
        lblPassword.setEnabled(false);

        tfUsername.setEnabled(false);

        lblUsername.setText("Benutzername");
        lblUsername.setEnabled(false);

        pfPassword.setEnabled(false);

        btnApplyOptionsNetwork.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnApplyOptionsNetwork.setText("Übernehmen");

        javax.swing.GroupLayout pnlOptionsNetworkLayout = new javax.swing.GroupLayout(pnlOptionsNetwork);
        pnlOptionsNetwork.setLayout(pnlOptionsNetworkLayout);
        pnlOptionsNetworkLayout.setHorizontalGroup(
            pnlOptionsNetworkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsNetworkLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOptionsNetworkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOptionsNetworkLayout.createSequentialGroup()
                        .addComponent(lblMasterserverIP)
                        .addGap(23, 23, 23)
                        .addComponent(tfMasterserverIP, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                    .addGroup(pnlOptionsNetworkLayout.createSequentialGroup()
                        .addGroup(pnlOptionsNetworkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUsername)
                            .addComponent(lblPassword))
                        .addGap(18, 18, 18)
                        .addGroup(pnlOptionsNetworkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pfPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(tfUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)))
                    .addComponent(btnApplyOptionsNetwork, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        pnlOptionsNetworkLayout.setVerticalGroup(
            pnlOptionsNetworkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsNetworkLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOptionsNetworkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfMasterserverIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMasterserverIP))
                .addGap(18, 18, 18)
                .addGroup(pnlOptionsNetworkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsername))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlOptionsNetworkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(btnApplyOptionsNetwork)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlOptionsLayout = new javax.swing.GroupLayout(pnlOptions);
        pnlOptions.setLayout(pnlOptionsLayout);
        pnlOptionsLayout.setHorizontalGroup(
            pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlOptionsPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlOptionsNetwork, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlOptionsLayout.setVerticalGroup(
            pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlOptionsNetwork, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlOptionsPlayer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(207, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("J.A.F.S. Client");
        setResizable(false);

        pnlMenu.setLayout(new java.awt.GridLayout(1, 4));

        btnMenuLobby.setFont(new java.awt.Font("Verdana", 1, 18));
        btnMenuLobby.setText("Lobby");
        pnlMenu.add(btnMenuLobby);

        btnMenuServerbrowser.setFont(new java.awt.Font("Verdana", 1, 18));
        btnMenuServerbrowser.setText("Serverbrowser");
        pnlMenu.add(btnMenuServerbrowser);

        btnMenuOptions.setFont(new java.awt.Font("Verdana", 1, 18));
        btnMenuOptions.setText("Optionen");
        pnlMenu.add(btnMenuOptions);

        btnMenuExit.setFont(new java.awt.Font("Verdana", 1, 18));
        btnMenuExit.setText("Verlassen");
        pnlMenu.add(btnMenuExit);

        pnlLobbyChat.setAlignmentY(0.0F);
        pnlLobbyChat.setLayout(new java.awt.BorderLayout());

        sppLobbyChat.setBorder(null);
        sppLobbyChat.setDividerLocation(610);
        sppLobbyChat.setDividerSize(3);

        pnlLobbyChatClientList.setAlignmentX(0.0F);
        pnlLobbyChatClientList.setAlignmentY(0.0F);

        lClientlist.setModel(listModel);
        lClientlist.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lClientlist.setToolTipText("Doubleclick to open private chat tab.");
        lClientlist.setAlignmentX(0.0F);
        lClientlist.setAlignmentY(0.0F);
        lClientlist.setEnabled(false);
        scpClientlist.setViewportView(lClientlist);

        lblLobbyChatPlayerName.setEnabled(false);

        javax.swing.GroupLayout pnlLobbyChatClientListLayout = new javax.swing.GroupLayout(pnlLobbyChatClientList);
        pnlLobbyChatClientList.setLayout(pnlLobbyChatClientListLayout);
        pnlLobbyChatClientListLayout.setHorizontalGroup(
            pnlLobbyChatClientListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpClientlist, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
            .addComponent(lblLobbyChatPlayerName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
        );
        pnlLobbyChatClientListLayout.setVerticalGroup(
            pnlLobbyChatClientListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLobbyChatClientListLayout.createSequentialGroup()
                .addComponent(lblLobbyChatPlayerName, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpClientlist, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE))
        );

        sppLobbyChat.setRightComponent(pnlLobbyChatClientList);

        tfLobbyChat.setEnabled(false);
        tfLobbyChat.setMinimumSize(new java.awt.Dimension(6, 21));
        tfLobbyChat.setPreferredSize(new java.awt.Dimension(6, 21));

        btnSendLobbyChat.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnSendLobbyChat.setText("Senden");
        btnSendLobbyChat.setEnabled(false);
        btnSendLobbyChat.setMaximumSize(new java.awt.Dimension(67, 20));
        btnSendLobbyChat.setMinimumSize(new java.awt.Dimension(67, 20));
        btnSendLobbyChat.setPreferredSize(new java.awt.Dimension(67, 20));

        tbpLobbyChat.setEnabled(false);

        taLobbyChatPublic.setColumns(20);
        taLobbyChatPublic.setEditable(false);
        taLobbyChatPublic.setRows(5);
        scpLobbyChat.setViewportView(taLobbyChatPublic);

        tbpLobbyChat.addTab("Öffentlich", scpLobbyChat);

        javax.swing.GroupLayout pnlLobbyChatTabsLayout = new javax.swing.GroupLayout(pnlLobbyChatTabs);
        pnlLobbyChatTabs.setLayout(pnlLobbyChatTabsLayout);
        pnlLobbyChatTabsLayout.setHorizontalGroup(
            pnlLobbyChatTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLobbyChatTabsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tfLobbyChat, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSendLobbyChat, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(tbpLobbyChat, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );
        pnlLobbyChatTabsLayout.setVerticalGroup(
            pnlLobbyChatTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLobbyChatTabsLayout.createSequentialGroup()
                .addComponent(tbpLobbyChat, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLobbyChatTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLobbyChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSendLobbyChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        sppLobbyChat.setLeftComponent(pnlLobbyChatTabs);

        pnlLobbyChat.add(sppLobbyChat, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlLobbyChat, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLobbyChat, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApplyOptionsNetwork;
    private javax.swing.JButton btnApplyOptionsPlayer;
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnMenuExit;
    private javax.swing.JButton btnMenuLobby;
    private javax.swing.JButton btnMenuOptions;
    private javax.swing.JButton btnMenuServerbrowser;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSendLobbyChat;
    private javax.swing.JList lClientlist;
    private javax.swing.JLabel lblLobbyChatPlayerName;
    private javax.swing.JLabel lblMasterserverIP;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPlayerName;
    private javax.swing.JLabel lblServerinfoHighscore;
    private javax.swing.JLabel lblServerinfoHighscoreTxt;
    private javax.swing.JLabel lblServerinfoIpPort;
    private javax.swing.JLabel lblServerinfoIpPortTxt;
    private javax.swing.JLabel lblServerinfoMap;
    private javax.swing.JLabel lblServerinfoMapTxt;
    private javax.swing.JLabel lblServerinfoName;
    private javax.swing.JLabel lblServerinfoNameTxt;
    private javax.swing.JLabel lblServerinfoPlayers;
    private javax.swing.JLabel lblServerinfoPlayersTxt;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JPanel pnlLobbyChat;
    private javax.swing.JPanel pnlLobbyChatClientList;
    private javax.swing.JPanel pnlLobbyChatTabs;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlOptions;
    private javax.swing.JPanel pnlOptionsNetwork;
    private javax.swing.JPanel pnlOptionsPlayer;
    private javax.swing.JPanel pnlServerbrowser;
    private javax.swing.JPanel pnlServerinfo;
    private javax.swing.JScrollPane scpClientlist;
    private javax.swing.JScrollPane scpLobbyChat;
    private javax.swing.JScrollPane scpServerinfo;
    private javax.swing.JScrollPane scpServerlist;
    private javax.swing.JSplitPane sppLobbyChat;
    private javax.swing.JTextArea taLobbyChatPublic;
    private javax.swing.JTable tblServerlist;
    private javax.swing.JTabbedPane tbpLobbyChat;
    private javax.swing.JTextField tfLobbyChat;
    private javax.swing.JTextField tfMasterserverIP;
    private javax.swing.JTextField tfPlayerName;
    private javax.swing.JTextField tfUsername;
    // End of variables declaration//GEN-END:variables
    private Color btn_bg_normal = new Color(238, 238, 238);
    private Color btn_bg_aktive = new Color(200, 221, 242);
    private ServerbrowserTableModel sModel;
    private DefaultListModel listModel;


// --- ActionListener ---
    /**
     * Invoked when an action occurs.
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        pnlLobbyChat.removeAll();
        btnMenuLobby.setBackground(btn_bg_normal);
        btnMenuOptions.setBackground(btn_bg_normal);
        btnMenuExit.setBackground(btn_bg_normal);
        btnMenuServerbrowser.setBackground(btn_bg_normal);

        // Lobby
        if(e.getSource() == btnMenuLobby){
            btnMenuLobby.setBackground(btn_bg_aktive);
            pnlLobbyChat.add(sppLobbyChat, BorderLayout.CENTER);
        }
        // Serverbrowser
        else if(e.getSource() == btnMenuServerbrowser){
            btnMenuServerbrowser.setBackground(btn_bg_aktive);
            pnlLobbyChat.add(pnlServerbrowser, BorderLayout.CENTER);
        }
        // Optionen
        else if(e.getSource() == btnMenuOptions){
            btnMenuOptions.setBackground(btn_bg_aktive);
            pnlLobbyChat.add(pnlOptions, BorderLayout.CENTER);
        }
        // Verlassen
        else if(e.getSource() == btnMenuExit){
            btnMenuExit.setBackground(btn_bg_aktive);
            System.exit(0);
        }
        repaint();
        pack();
    }


// --- MouseListener ---

    public void mouseClicked(MouseEvent e) {
        // Open private tab
        if (e.getSource() == lClientlist && !listModel.isEmpty() && e.getClickCount() == 2) {
            openPrivateChatTab(lClientlist.locationToIndex(e.getPoint()));
        }
        // Close private tab
        else if (e.getSource() == tbpLobbyChat && tbpLobbyChat.getSelectedIndex() != 0 && e.getClickCount() == 2) {
            tbpLobbyChat.remove(tbpLobbyChat.getSelectedIndex());
        }
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }


// --- KeyListener ---

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            sendLobbyMsg();
        }
    }

    public void keyReleased(KeyEvent e) { }


// --- ListSelectionListener ---

    public void valueChanged(ListSelectionEvent e) {
        //System.out.println("refreshServerinfoPanel()");
        refreshServerinfoPanel();
    }


// --- Client ---

    /**
     * Clear list of clients
     */
    public void clearClientlist(){
        clientlistOld.clear();
        for(int i=0; i<clientlist.size(); i++){
            clientlistOld.add(clientlist.get(i));
        }
        clientlist.clear();
        listModel.clear();
        repaint();
        pack();
    }

    /**
     *
     * @param client Client you want to add
     */
    public void addClientToList(Client client){
        clientlist.add(new PrivateChatTab(client));
        listModel.addElement(client.getPlayer().getName());
        repaint();
        pack();
    }

    /**
     * sync clientlist with old list, correcting opened tabs
     */
    public void completeClientlist(){
        //System.out.println("completeClientlist() "+(tbpLobbyChat.getComponentCount()-1)+" opened private tabs");
        //System.out.print(" sync clientlist with old list...");
        for(int i=0; i<clientlist.size(); i++){
            for(int j=0; j<clientlistOld.size(); j++){
                if(clientlist.get(i).getID() == clientlistOld.get(j).getID()){
                    //System.out.print("id"+clientlistOld.get(j).getID()+" ");
                    clientlist.get(i).appendText(clientlistOld.get(j).getTextArea().getText());
                    break;
                }
            }
        }
        //System.out.println("done!\n correcting opened tabs...");
        for(int i=1; i<tbpLobbyChat.getComponentCount(); i++){
            if(!tbpLobbyChat.getComponentAt(i).getName().equals("offline")){
                int id = Integer.parseInt(tbpLobbyChat.getComponentAt(i).getName());
                //System.out.print("id"+id+" ");
                int index = getClientlistIndex(id, clientlist);
                if(index == -1) { // client offline, not in clientlist -> in clientlistOld
                    int indexOld = getClientlistIndex(id, clientlistOld);
                    JTextArea a = new JTextArea();
                    a.setText(clientlistOld.get(indexOld).getTextArea().getText());
                    JScrollPane s = new JScrollPane();
                    s.setName("offline");
                    s.setViewportView(a);
                    tbpLobbyChat.setComponentAt(i, s);
                    tbpLobbyChat.setTitleAt(i, tbpLobbyChat.getTitleAt(i)+ " (offline)");
                } else {
                    String name = clientlist.get(index).getName();
                    if(!name.equals(tbpLobbyChat.getTitleAt(i))) {
                        clientlist.get(index).appendText("\n= Nick changed =\n\n");
                        tbpLobbyChat.setTitleAt(i, name);
                    }
                    tbpLobbyChat.setComponentAt(i, clientlist.get(index).getScrollPane());
                }
            }
        }
        //System.out.println("done!");
    }

    /**
     *
     * @param clientID ID of client you want to get index in list
     * @param list Clientlist
     */
    private int getClientlistIndex(int clientID, ArrayList<PrivateChatTab> list){
        //System.out.print(" getClientlistIndex(...) -> ");
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getID() == clientID){
                //System.out.println("found index:"+i);
                return i;
            }
        }
        //System.out.println("no id matched");
        return -1;
    }


// -- Server --

    /**
     *
     * @param list String array for server table
     */
    public void setServerlist(String[][] list) {
        sModel.setServerlist(list);
        repaint();
        pack();
    }

    /**
     *
     * @return Index of selected server in list
     */
    public int getSelectedServerlistIndex(){
        return tblServerlist.getSelectedRow();
    }

    /**
     *
     * @param value Value you want to refresh
     * @param row Row of value
     * @param col Column of value
     */
    public void refreshServerTableValue(String value, int row, int col){
        sModel.setValueAt(value, row, col);
        repaint();
        pack();
    }

    /**
     * Clear Info-Panel in Serverbrowser
     */
    public void clearServerinfoPanel(){
        lblServerinfoName.setText("-");
        lblServerinfoMap.setText("-");
        lblServerinfoPlayers.setText("-");
        lblServerinfoIpPort.setText("-");
        lblServerinfoHighscore.setText("-");
    }

    /**
     *
     */
    public void refreshServerinfoPanel(){
        Server s = Main.getSelectedServer();
        if(s != null){
            lblServerinfoName.setText(s.getName());
            lblServerinfoMap.setText(s.getMap());
            lblServerinfoPlayers.setText(s.getCurPlayers());
            lblServerinfoIpPort.setText(s.getHostPort());
            lblServerinfoHighscore.setText(""+s.getClientHighscore());
        }
    }

    public void setOptionsMasterHostPort(String host, int port){
        tfMasterserverIP.setText(host+":"+port);
    }

    /**
     *
     * @param enabled
     */
    public void enableLobby(boolean enabled){
        tbpLobbyChat.setEnabled(enabled);
        tfLobbyChat.setEnabled(enabled);
        btnSendLobbyChat.setEnabled(enabled);
        lClientlist.setEnabled(enabled);
        lblLobbyChatPlayerName.setEnabled(enabled);
    }

    /**
     *
     * @param enabled
     */
    public void enableOptions(boolean enabled){
        pnlOptions.setEnabled(enabled);
    }

    /**
     *
     * @param isPrivateMsg
     * @param senderID
     * @param recieverID
     * @param msg
     */
    public void appendIncommingMSG(boolean isPrivateMsg, int senderID, int recieverID, String msg){
        //System.out.print("appendIncommingMSG(...)");
        // PRIVATE
        if(isPrivateMsg) {
            //System.out.print(" (private msg, id"+senderID+" to id"+recieverID+" :"+msg+")");
            if(recieverID == Main.getGameData().getSelfId()) {
                //System.out.println(" (recieverID==SefID)");
                //System.out.println("msg to this client -> from id"+senderID);
                openPrivateChatTab(getClientlistIndex(senderID, clientlist));
                clientlist.get(getClientlistIndex(senderID, clientlist)).getTextArea().append(Main.getClientName(senderID) + ": " + msg + "\n");
            } else {
                //System.out.println("");
                //System.out.println("msg to other client -> to id"+recieverID);
                clientlist.get(getClientlistIndex(recieverID, clientlist)).getTextArea().append(Main.getClientName(senderID) + ": " + msg + "\n");
            }
        }
        // PUBLIC
        else {
            //System.out.println(" (public msg, senderID="+senderID+")");
            taLobbyChatPublic.append(Main.getClientName(senderID) + ": " + msg + "\n");
        }
    }

    private void openPrivateChatTab(int clientID) {
        //System.out.print("openPrivateChatTab(...) clientID="+clientID+" -> ");
        //System.out.print(""+(tbpLobbyChat.getComponentCount()-1)+" private tabs opened -> ");
        for(int i=1; i<tbpLobbyChat.getComponentCount(); i++){
            //System.out.println(" tab"+i+": "+tbpLobbyChat.getTitleAt(i));
            if(tbpLobbyChat.getTitleAt(i).equals(Main.getClientName(clientID))){
                tbpLobbyChat.setSelectedIndex(i);
                //System.out.println("tab allready opened -> nothing to do!");
                return;
            }
        }
        //System.out.println("specific tab not opened -> opening new tab!");
        tbpLobbyChat.addTab(clientlist.get(clientID).getName(), clientlist.get(clientID).getScrollPane());
        tbpLobbyChat.setToolTipTextAt(tbpLobbyChat.getTabCount()-1, "Doubleclick to close private chat tab.");
        tbpLobbyChat.setSelectedIndex(tbpLobbyChat.getTabCount()-1);
    }

    /**
     *
     */
    public void sendLobbyMsg(){
        if(!tfLobbyChat.getText().equals("")){
            // PRIVATE LOBBY CHAT
            if(tbpLobbyChat.getSelectedIndex()>0){
                Main.getNetwork().send(Network.MASTERHOST,
                                       Network.MASTERPORT,
                                       ProtocolCmd.CLIENT_MASTER_CHAT_PRIVATE,
                                       argInt(Integer.parseInt(tbpLobbyChat.getSelectedComponent().getName())),
                                       argStr(tfLobbyChat.getText()));
            }
            // PUBLIC LOBBY CHAT
            else {
                if(tfLobbyChat.getText() != null && !tfLobbyChat.getText().equals(""))
                Main.getNetwork().send(Network.MASTERHOST,
                                       Network.MASTERPORT,
                                       ProtocolCmd.CLIENT_MASTER_CHAT_LOBBY,
                                       argStr(tfLobbyChat.getText()));
            }
            tfLobbyChat.setText("");
        }
    }

    /**
     *
     * @param selfName
     */
    public void setSelfName(String selfName){
        tfPlayerName.setText(selfName);
        lblLobbyChatPlayerName.setText(" " + selfName);
    }

    /**
     *
     * @return Selfname set in GUI
     */
    public String getSelfName(){
        return tfPlayerName.getText();
    }

    /**
     *
     * @return Host and port of masterserver set in GUI
     */
    public String getMasterHostPortSettings(){
        return this.tfMasterserverIP.getText();
    }



    /**
     * Custom Object for JTabbedPane
     * @author Julian Sanio
     */
    public class PrivateChatTab {

        private Client cl;
        private JTextArea txt;
        private JScrollPane sp;

        public PrivateChatTab(Client client){
            cl = client;
            txt = new JTextArea();
            txt.setEditable(false);
            sp = new JScrollPane();
            sp.setName(""+client.getId());
            sp.setViewportView(txt);
            DefaultCaret caret = (DefaultCaret)txt.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }

        public int getID(){ return cl.getId(); }

        public String getName(){ return cl.getPlayer().getName(); }

        public JTextArea getTextArea(){ return txt; }

        public void appendText(String s){ txt.append(s);}

        public JScrollPane getScrollPane(){ return sp; }

        public Client getClient(){ return cl; }
    }


    /**
     * Custom TableModel for JTable
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

        /**
         *
         * @param value
         * @param row
         * @param col
         */
        public void setValueAt(String value, int row, int col) {
            serverList[row][col] = value;
        }

        public String getValueAt(int row, int col) {
            return (String)serverList[row][col];
        }

        /**
         *
         * @param list
         */
        public void setServerlist(String[][] list) {
            this.serverList = list;
        }

        /**
         *
         * @return
         */
        public String[][] getServerlist() {
            return serverList;
        }
    }
}
