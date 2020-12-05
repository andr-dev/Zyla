package com.zyla.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;
import javax.swing.text.DefaultCaret;

import com.zyla.coin.Util;
import com.zyla.network.Client;
import com.zyla.network.Server;

import javax.swing.JButton;

public class ClientGUI extends JFrame {
	
	private int pX, pY;
	private JPanel contentPane;
	
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
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int sc_width = gd.getDisplayMode().getWidth();
		int sc_height = gd.getDisplayMode().getHeight();
		int window_width = 1080;
		int window_height = 640;
		
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		setResizable(false);
		setUndecorated(true);
		setBounds((sc_width / 2) - (window_width / 2), (sc_height / 2) - (window_height / 2), window_width, window_height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setLayout(null);
		
		JPanel TopBar = new JPanel();
		TopBar.setToolTipText("HelloTest");
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
		
		JButton StartServer = new JButton("Start Server");
		StartServer.setFocusPainted(false);
		StartServer.setContentAreaFilled(false);
		StartServer.setActionCommand("StartServer");
		StartServer.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(255, 255, 255)));
		StartServer.setForeground(WHITE);
		StartServer.setFont(new Font("Calibri Light", Font.ITALIC, 12));
		StartServer.setBounds(16, 12, 160, 32);
		StartServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e){
				new Thread() {
					@Override
					public void run() {
						// ServerClient.startServerClient();
						int PORT = 26373;
						ServerSocket serverSocket = null;
						Socket socket = null;
						try {
							serverSocket = new ServerSocket(PORT);
							Util.printMessage("Client Socket Started");
						} catch (Exception e1) {
							Util.printMessage("Client Socket Failed|" + e1);
						}
						
						try {
							socket = serverSocket.accept();
							Util.printMessage("Client Socket Connection Established");
						} catch (Exception e1) {
							Util.printMessage("I/O Error|" + e1);
						}
						
						new Server(socket).start();
					}
				}.start();
			}
		});
		SettingsBox.add(StartServer);
		
		JButton StartClient = new JButton("Start Client");
		StartClient.setFocusPainted(false);
		StartClient.setContentAreaFilled(false);
		StartClient.setActionCommand("StartClient");
		StartClient.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(255, 255, 255)));
		StartClient.setForeground(WHITE);
		StartClient.setFont(new Font("Calibri Light", Font.ITALIC, 12));
		StartClient.setBounds(16, 56, 160, 32);
		StartClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e){
				new Thread() {
					@Override
					public void run() {
						Client.startClient();
					}
				}.start();
			}
		});
		SettingsBox.add(StartClient);
		
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
		
		JPanel ConsolePanel = new JPanel();
		ConsolePanel.setBackground(Grey4);
		ConsolePanel.setBounds(0, 100, (window_width - 192), (window_height - 32 - 100));
		MainPanel.add(ConsolePanel);
		ConsolePanel.setLayout(null);
		
		JTextArea Console = new JTextArea();
		Console.setFont(new Font("Courier", Font.PLAIN, 11));
		Console.setBackground(Grey2);
		Console.setForeground(WHITE);
		Console.setAutoscrolls(true);
		Console.setEditable(false);
		Console.setBounds(16, 16, (window_width - 192 - 32), (window_height - 32 - 100 - 32));
		Console.setLayout(null);
		
		JScrollPane ConsoleScroll = new JScrollPane(Console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ConsoleScroll.setBounds(16, 16, (window_width - 192 - 32), (window_height - 32 - 100 - 32));
		ConsoleScroll.getVerticalScrollBar().setUI(new CustomScrollUI());
		ConsoleScroll.getHorizontalScrollBar().setUI(new CustomScrollUI());
		ConsolePanel.add(ConsoleScroll);
		
		DefaultCaret ConsoleCaret = (DefaultCaret) Console.getCaret();
		ConsoleCaret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		
		PrintStream pStream = new PrintStream(new TextAreaOutputStream(Console));
		System.setOut(pStream);
		System.setErr(pStream);
	}
}