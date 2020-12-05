package com.zyla.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.DefaultCaret;
import javax.swing.text.NumberFormatter;

import com.zyla.coin.Block;
import com.zyla.coin.Util;
import com.zyla.coin.Wallet;
import com.zyla.network.old.Client;
import com.zyla.network.old.Server;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;

public class ClientGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private int pX, pY;
    private JPanel contentPane;
    private Client client;
    private ArrayList<Wallet> wallets;
    private Block newBlock = null;

    // Make some nice rounded corners
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 24, 24));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientGUI frame = new ClientGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ClientGUI() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Color WHITE = new Color (255, 255, 255);
        Color BLACK = new Color (0, 0, 0);
        Color GREEN = new Color (46, 204, 113);
        Color WATERMELON = new Color (231, 76, 60);
        Color YELLOW = new Color (243, 156, 18);
        Color Grey1 = new Color (50, 50, 50);
        Color Grey2 = new Color (65, 65, 65);
        Color Grey3 = new Color (80, 80, 80);
        Color Grey4 = new Color (95, 95, 95);
        Color Grey5 = new Color (105, 105, 105);

        wallets = new ArrayList<Wallet>();

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int sc_width = gd.getDisplayMode().getWidth();
        int sc_height = gd.getDisplayMode().getHeight();
        int window_width = 1080;
        int window_height = 640;

        String[] walletSelections = new String[] {"Genesis Wallet", "Wallet #2", "Wallet #3", "Wallet #4"};

        contentPane = new JPanel();
        contentPane.setBorder(null);
        setContentPane(contentPane);
        setResizable(false);
        setUndecorated(true);
        setBounds((sc_width / 2) - (window_width / 2), (sc_height / 2) - (window_height / 2), window_width, window_height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(null);

        JPanel TopBar = new JPanel();
        TopBar.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 255, 255)));
        TopBar.setBackground(Grey1);
        TopBar.setBounds(0, 0, window_width, 32);
        TopBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                pX = me.getX();
                pY = me.getY();
            }
        });
        TopBar.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent me) {
                // Add current window coordinate x / y and current mouse (dragged) coordinate x / y
                // Subtract from most recent mouse pressed x / y coordinate
                setLocation(withinWinW(getLocation().x + me.getX() - pX), withinWinH(getLocation().y + me.getY() - pY));
            }

            public int withinWinW (int a) {
                if (a < 0) {
                    return 0;
                } else if (a > (sc_width - window_width)){
                    return (sc_width - window_width);
                } else {
                    return a;
                }
            }
            public int withinWinH (int a) {
                if (a < 0) {
                    return 0;
                } else if (a > (sc_height - window_height)){
                    return (sc_height - window_height);
                } else {
                    return a;
                }
            }
        });
        contentPane.add(TopBar);
        TopBar.setLayout(null);

        JRoundButton closeButton = new JRoundButton("x");
        closeButton.setBackground(Grey3);
        closeButton.setColorPressed(WATERMELON);
        closeButton.setTopBarColor(TopBar.getBackground());
        closeButton.setBounds(1048, 4, 24, 24);
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        TopBar.add(closeButton);

        JPanel BoxWrapper = new JPanel();
        BoxWrapper.setBounds(0, 32, (window_width), (window_height - 32));
        contentPane.add(BoxWrapper);
        BoxWrapper.setLayout(null);

        JPanel SettingsBox = new JPanel();
        SettingsBox.setBorder(new MatteBorder(0, 0, 0, 1, (Color) new Color(255, 255, 255)));
        SettingsBox.setBackground(Grey3);
        SettingsBox.setBounds(0, 0, 192, (window_height - 32));
        BoxWrapper.add(SettingsBox);
        SettingsBox.setLayout(null);

        JPanel ZylaInfo = new JPanel();
        ZylaInfo.setBorder(new MatteBorder(0, 0, 0, 1, (Color) new Color(255, 255, 255)));
        ZylaInfo.setBounds(0, 0, 192, 100);
        ZylaInfo.setBackground(Grey3);
        SettingsBox.add(ZylaInfo);
        ZylaInfo.setLayout(null);

        JPanel ZylaIconPanel = new JPanel();
        ZylaIconPanel.setBounds(16, 16, 160, 40);
        ZylaIconPanel.setBackground(Grey3);
        ZylaInfo.add(ZylaIconPanel);
        ZylaIconPanel.setLayout(null);

        ImageIcon ZylaBannerImageIcon = new ImageIcon("src/images/ZylaIconBanner.png");
        JLabel ZylaBanner = new JLabel("", ZylaBannerImageIcon, JLabel.CENTER);
        ZylaBanner.setBounds(0, 0, 160, 40);
        ZylaIconPanel.add(ZylaBanner);

        JPanel ZylaStatus = new JPanel();
        ZylaStatus.setBounds(16, 64, 160, 20);
        ZylaStatus.setBackground(Grey3);
        ZylaInfo.add(ZylaStatus);
        ZylaStatus.setLayout(null);

        ImageIcon[] ConnectionImageIcon = new ImageIcon[] {new ImageIcon("src/images/RedBTN.png"), new ImageIcon("src/images/YellowBTN.png"), new ImageIcon("src/images/GreenBTN.png")};

        JLabel RedCon = new JLabel("", ConnectionImageIcon[0], JLabel.CENTER);
        RedCon.setBounds((160 - 16 - 4), 2, 16, 16);
        ZylaStatus.add(RedCon);
        RedCon.setVisible(true);

        JLabel YellowCon = new JLabel("", ConnectionImageIcon[1], JLabel.CENTER);
        YellowCon.setBounds((160 - 16 - 4), 2, 16, 16);
        ZylaStatus.add(YellowCon);
        YellowCon.setVisible(false);

        JLabel GreenCon = new JLabel("", ConnectionImageIcon[2], JLabel.CENTER);
        GreenCon.setBounds((160 - 16 - 4), 2, 16, 16);
        ZylaStatus.add(GreenCon);
        GreenCon.setVisible(false);

        JLabel ZylaStatusLabel = new JLabel("Connection Status");
        ZylaStatusLabel.setForeground(Color.WHITE);
        ZylaStatusLabel.setFont(new Font("Coco Gothic Light", Font.PLAIN, 13));
        ZylaStatusLabel.setBounds(4, 0, 120, 20);
        ZylaStatus.add(ZylaStatusLabel);

        JButton StartServer = new JButton("Start Server");
        StartServer.setFocusPainted(false);
        StartServer.setBackground(Grey4);
        StartServer.setActionCommand("StartServer");
        StartServer.setBorder(null);
        StartServer.setForeground(WHITE);
        StartServer.setFont(new Font("Calibri Light", Font.ITALIC, 12));
        StartServer.setBounds(16, 400, 160, 32);
        SettingsBox.add(StartServer);

        JButton StartClient = new JButton("Start Client");
        StartClient.setFocusPainted(false);
        StartClient.setBackground(Grey4);
        StartClient.setActionCommand("StartClient");
        StartClient.setBorder(null);
        StartClient.setForeground(WHITE);
        StartClient.setFont(new Font("Calibri Light", Font.ITALIC, 12));
        StartClient.setBounds(16, 448, 160, 32);
        StartClient.setEnabled(false);
        SettingsBox.add(StartClient);

        JButton CreateWallets = new JButton("Create Wallets");
        CreateWallets.setForeground(Color.WHITE);
        CreateWallets.setFont(new Font("Calibri Light", Font.ITALIC, 12));
        CreateWallets.setFocusPainted(false);
        CreateWallets.setBorder(null);
        CreateWallets.setBackground(new Color(95, 95, 95));
        CreateWallets.setActionCommand("StartClient");
        CreateWallets.setBounds(16, 496, 160, 32);
        CreateWallets.setEnabled(false);
        SettingsBox.add(CreateWallets);

        JButton StartBlockchain = new JButton("Start Zyla Blockchain");
        StartBlockchain.setForeground(Color.WHITE);
        StartBlockchain.setFont(new Font("Calibri Light", Font.ITALIC, 12));
        StartBlockchain.setFocusPainted(false);
        StartBlockchain.setBorder(null);
        StartBlockchain.setBackground(new Color(95, 95, 95));
        StartBlockchain.setActionCommand("StartClient");
        StartBlockchain.setBounds(16, 544, 160, 32);
        StartBlockchain.setEnabled(false);
        SettingsBox.add(StartBlockchain);

        JPanel MenuToggle = new JPanel();
        MenuToggle.setBounds(16, 100 + 16, 160, (window_height - 32 - 100 - 32));
        MenuToggle.setBackground(Grey3);
        SettingsBox.add(MenuToggle);
        MenuToggle.setLayout(null);

        UIManager.put("ToggleButton.select", Grey5);
        ButtonGroup menuButtonGroup = new ButtonGroup();
        ActionListener menuButtonListener = actionEvent -> actionEvent.getActionCommand();

        int a = 0;

        JToggleButton BTNStart = new JToggleButton();
        BTNStart.addActionListener(menuButtonListener);
        BTNStart.setBounds(0, (a * 80), 160, 80);
        a++;
        BTNStart.setBackground(Grey4);
        BTNStart.setBorderPainted(false);
        BTNStart.setFocusPainted(false);
        menuButtonGroup.add(BTNStart);
        MenuToggle.add(BTNStart);
        BTNStart.setLayout(null);


        JPanel MainPanel = new JPanel();
        MainPanel.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(255, 255, 255)));
        MainPanel.setBackground(Grey4);
        MainPanel.setBounds(192, 0, (window_width - 128), (window_height - 32));
        BoxWrapper.add(MainPanel);
        MainPanel.setLayout(null);

        JPanel TransactionPanel = new JPanel();
        TransactionPanel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 255, 255)));
        TransactionPanel.setBackground(Grey4);
        TransactionPanel.setBounds(0, 0, (window_width - 192), 100);
        MainPanel.add(TransactionPanel);
        TransactionPanel.setLayout(null);

        JPanel SendPanel = new JPanel();
        SendPanel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 255, 255)));
        SendPanel.setBackground(Grey3);
        SendPanel.setBounds(0, 0, (window_width - 192), 100);
        TransactionPanel.add(SendPanel);
        SendPanel.setLayout(null);

        JLabel TransactionPanelInfo = new JLabel("Transaction Manager");
        TransactionPanelInfo.setForeground(Color.WHITE);
        TransactionPanelInfo.setFont(new Font("Dialog", Font.PLAIN, 13));
        TransactionPanelInfo.setBounds(16, 8, 128, 20);
        SendPanel.add(TransactionPanelInfo);

        JLabel Sender = new JLabel("Sender");
        Sender.setHorizontalAlignment(SwingConstants.CENTER);
        Sender.setForeground(Color.WHITE);
        Sender.setFont(new Font("Dialog", Font.PLAIN, 13));
        Sender.setBounds(16, 40, 120, 20);
        SendPanel.add(Sender);

        JLabel Receiver = new JLabel("Receiver");
        Receiver.setHorizontalAlignment(SwingConstants.CENTER);
        Receiver.setForeground(Color.WHITE);
        Receiver.setFont(new Font("Dialog", Font.PLAIN, 13));
        Receiver.setBounds(152, 40, 120, 20);
        SendPanel.add(Receiver);

        JLabel Amount = new JLabel("Amount");
        Amount.setHorizontalAlignment(SwingConstants.CENTER);
        Amount.setForeground(Color.WHITE);
        Amount.setFont(new Font("Dialog", Font.PLAIN, 13));
        Amount.setBounds(284, 40, 120, 20);
        SendPanel.add(Amount);


        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(new Color(255, 255, 255, 0)));

        @SuppressWarnings({ "unchecked", "rawtypes" })
        JComboBox WalletSelection1 = new JComboBox(walletSelections);
        WalletSelection1.setFont(new Font("Dialog", Font.PLAIN, 10));
        WalletSelection1.setBounds(16, 64, 120, 16);
        WalletSelection1.setBorder(null);
        SendPanel.add(WalletSelection1);


        @SuppressWarnings({ "unchecked", "rawtypes" })
        JComboBox WalletSelection2 = new JComboBox(walletSelections);
        WalletSelection2.setFont(new Font("Dialog", Font.PLAIN, 10));
        WalletSelection2.setBounds(152, 64, 120, 16);
        WalletSelection2.setBorder(null);
        SendPanel.add(WalletSelection2);

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        JFormattedTextField AmountSelection = new JFormattedTextField(formatter);
        AmountSelection.setHorizontalAlignment(SwingConstants.CENTER);
        AmountSelection.setToolTipText("1");
        AmountSelection.setFont(new Font("Dialog", Font.PLAIN, 10));
        AmountSelection.setBounds(284, 64, 120, 16);
        AmountSelection.setBorder(null);
        SendPanel.add(AmountSelection);
        AmountSelection.setColumns(10);

        JButton SendTransaction = new JButton();
        SendTransaction.setFont(new Font("Dialog", Font.PLAIN, 11));
        SendTransaction.setText("Send Transaction");
        SendTransaction.setForeground(Color.WHITE);
        SendTransaction.setFocusPainted(false);
        SendTransaction.setBorderPainted(false);
        SendTransaction.setBackground(new Color(95, 95, 95));
        SendTransaction.setBounds(416, 40, 140, 40);
        SendTransaction.setEnabled(false);
        SendPanel.add(SendTransaction);

        JButton CreateNewBlock = new JButton();
        CreateNewBlock.setText("Create New Block");
        CreateNewBlock.setForeground(Color.WHITE);
        CreateNewBlock.setFont(new Font("Dialog", Font.PLAIN, 11));
        CreateNewBlock.setFocusPainted(false);
        CreateNewBlock.setBorderPainted(false);
        CreateNewBlock.setBackground(new Color(95, 95, 95));
        CreateNewBlock.setBounds(568, 40, 140, 40);
        CreateNewBlock.setEnabled(false);
        SendPanel.add(CreateNewBlock);

        JButton VerifyBlockchain = new JButton();
        VerifyBlockchain.setText("Verify Blockchain");
        VerifyBlockchain.setForeground(Color.WHITE);
        VerifyBlockchain.setFont(new Font("Dialog", Font.PLAIN, 11));
        VerifyBlockchain.setFocusPainted(false);
        VerifyBlockchain.setBorderPainted(false);
        VerifyBlockchain.setBackground(new Color(95, 95, 95));
        VerifyBlockchain.setBounds(720, 40, 140, 40);
        VerifyBlockchain.setEnabled(false);
        SendPanel.add(VerifyBlockchain);

        JPanel ConsolePanel = new JPanel();
        ConsolePanel.setBackground(Grey4);
        ConsolePanel.setBounds(0, 100, (window_width - 192), (window_height - 32 - 100));
        MainPanel.add(ConsolePanel);
        ConsolePanel.setLayout(null);

        JTextArea Console = new JTextArea();
        Console.setFont(new Font("Consolas", Font.PLAIN, 11));
        Console.setBackground(Grey2);
        Console.setForeground(WHITE);
        Console.setAutoscrolls(true);
        Console.setEditable(false);
        Console.setBounds(16, 16, (window_width - 192 - 32), (window_height - 32 - 100 - 32));
        Console.setLayout(null);

        JScrollPane ConsoleScroll = new JScrollPane(Console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ConsoleScroll.setBounds(16, 16, (window_width - 192 - 32), (window_height - 32 - 100 - 32));
        ConsoleScroll.getVerticalScrollBar().setUI(new CustomScrollUI());
        ConsoleScroll.getHorizontalScrollBar().setUI(new CustomScrollUI());
        ConsolePanel.add(ConsoleScroll);

        DefaultCaret ConsoleCaret = (DefaultCaret) Console.getCaret();
        ConsoleCaret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

        PrintStream pStream = new PrintStream(new TextAreaOutputStream(Console));
        System.setOut(pStream);
        System.setErr(pStream);

        // JButton Action Listeners (Mouse Click)

        StartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                new Thread() {
                    @Override
                    public void run () {
                        StartServer.setEnabled(false);
                        int PORT = 26373;
                        ServerSocket serverSocket = null;
                        Socket socket = null;
                        try {
                            serverSocket = new ServerSocket(PORT);
                            Util.printMessage("Client Socket Started");
                            conVisible(false, true, false);
                        } catch (Exception e1) {
                            Util.printMessage("Client Socket Failed|" + e1);
                            conVisible(true, false, false);
                            StartServer.setEnabled(true);
                        }

                        StartClient.setEnabled(true);

                        try {
                            socket = serverSocket.accept();
                            Util.printMessage("Client Socket Connection Established");
                            conVisible(false, false, true);
                        } catch (Exception e1) {
                            Util.printMessage("I/O Error|" + e1);
                            conVisible(true, false, false);
                            StartServer.setEnabled(true);
                        }
                        new Server(socket).start();
                    }

                    public void conVisible (boolean a, boolean b, boolean c) {
                        RedCon.setVisible(a);
                        YellowCon.setVisible(b);
                        GreenCon.setVisible(c);
                    }
                }.start();
            }
        });

        StartClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                new Thread () {
                    @Override
                    public void run () {
                        StartClient.setEnabled(false);
                        CreateWallets.setEnabled(true);
                        client = new Client();
                    }
                }.start();
            }
        });

        CreateWallets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new Thread () {
                    @Override
                    public void run () {
                        CreateWallets.setEnabled(false);
                        wallets = client.genWallets();
                        StartBlockchain.setEnabled(true);
                    }
                }.start();
            }
        });

        StartBlockchain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new Thread () {
                    @Override
                    public void run () {
                        StartBlockchain.setEnabled(false);
                        client.genGenesis();
                        printWalletBalances();
                        SendTransaction.setEnabled(true);
                        VerifyBlockchain.setEnabled(true);
                    }
                }.start();
            }
        });

        SendTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new Thread() {
                    @Override
                    public void run () {
                        if (WalletSelection1.getSelectedIndex() != WalletSelection2.getSelectedIndex()) {
                            if (newBlock == null) {
                                newBlock = new Block(client.getChain().get(client.getChain().size() - 1).getHash());
                            }
                            try {
                                newBlock.addTransaction(wallets.get(WalletSelection1.getSelectedIndex()).createTransaction(wallets.get(WalletSelection2.getSelectedIndex()).getPublicKey(), Integer.parseInt(AmountSelection.getText())));
                                CreateNewBlock.setEnabled(true);
                            } catch (Exception e) {
                                Util.printMessage("ERROR : Invalid Parameters for Transaction");
                            }
                        } else {
                            Util.printMessage("ERROR : Sender and Recipient Are The Same");
                        }
                    }
                }.start();
            }
        });

        CreateNewBlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new Thread () {
                    @Override
                    public void run () {
                        try {
                            if (newBlock != null) {
                                CreateNewBlock.setEnabled(false);
                                SendTransaction.setEnabled(false);
                                VerifyBlockchain.setEnabled(false);
                                client.addBlock(newBlock);
                                newBlock = null;
                                printWalletBalances();
                                SendTransaction.setEnabled(true);
                                VerifyBlockchain.setEnabled(true);
                            } else {
                                Util.printMessage("ERROR : newBlock is null");
                            }
                        } catch (Exception e) {
                            Util.printMessage("FATAL ERROR - SHUTING DOWN IN 10 SECONDS");
                            System.exit(10000);
                        }
                    }
                }.start();
            }
        });

        VerifyBlockchain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new Thread () {
                    @Override
                    public void run () {
                        SendTransaction.setEnabled(false);
                        if (client.isChainValid()) {
                            Util.printMessage("Blockchain is valid!");
                        } else {
                            Util.printMessage("Blockchain is invalid!");
                        }
                        SendTransaction.setEnabled(true);
                    }
                }.start();
            }
        });
    }

    protected ImageIcon createImageIcon(String path) {

        java.net.URL imgURL = getClass().getResource(path);

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            Util.printMessage("Could not find icon @ path : " + path);
            return null;
        }
    }

    private void printWalletBalances () {
        Util.printMessage("Wallet Balances:|Genesis Wallet Balance : " + wallets.get(0).getBalance() + "|Wallet 2 Balance : " + wallets.get(1).getBalance() + "|Wallet 3 Balance : " + wallets.get(2).getBalance() + "|Wallet 4 Balance : " + wallets.get(3).getBalance());
    }
}

// New umethod that was not implemented
//@Override
//public void run() {
//	StartServer.setEnabled(false);
//	// ServerClient.startServerClient();
//	P2P p2p = new P2P(26373);
//	if (p2p.isRunning()) {
//		if (p2p.isConnected()) {
//			conVisible(false, false, true);
//		} else {
//			conVisible(false, true, false);
//		}
//	} else {
//		conVisible(true, false, false);
//	}
//}
//
//public void conVisible (boolean a, boolean b, boolean c) {
//	RedCon.setVisible(a);
//	YellowCon.setVisible(b);
//	GreenCon.setVisible(c);
//}