package com.zyla.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.zyla.coin.Util;
import com.zyla.network.commands.*;

public class P2P {
    private int port; // = 26373;
    private ArrayList<Peer> peers;
    private ServerSocket serverSocket;
    private Thread serverThread;
    private Socket socket;
    private HashMap<Byte, NetworkCommand> networkCommands;
    private DataOutputStream out;
    private boolean serverRunning = false;
    private boolean peerConnected = false;

    public P2P (int port) {
        this.port = port;
        peers = new ArrayList<>();
        networkCommands = new HashMap<>();
        initCommands();
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                serverListen();
            }
        });
        serverThread.start();
    }

    private void serverListen () {
        Peer newPeer;
        try {
            serverSocket = new ServerSocket(this.port);
            serverSocket.setSoTimeout(10000);
            serverRunning = true;
        } catch (IOException e) {
            Util.printMessage("ERROR : Failed to Initialize Server|" + e.getMessage());
            serverRunning = false;
        }
        while (serverRunning) {
            for (Peer a : peers) {
                if (!(a.isConnected())) {
                    peerConnected = false;
                }
            }
            try {
                socket = serverSocket.accept();
                newPeer = new Peer(socket, networkCommands);
                Util.printMessage("Server : Connection recieved from " + newPeer.getIP());
                peers.add(newPeer);
                Util.printMessage("Server : Added Peer " + newPeer.getIP());
                peerConnected = true;
            } catch (IOException e) {
                Util.printMessage("ERROR : Socket Connection Failed|");
            }
        }

    }

    private void initCommands () {
        this.networkCommands.put((byte) 0, new PingHandler());
        Util.printMessage("UPDATE : Command [PING] Added");
        this.networkCommands.put((byte) 1, new TransactionHandler());
        Util.printMessage("UPDATE : Command [TRANSACTION] Added");
    }

    public boolean isRunning () {
        return serverRunning;
    }

    public boolean isConnected () {
        return peerConnected;
    }
}
