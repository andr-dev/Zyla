package com.zyla.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import com.zyla.network.commands.NetworkCommand;
import com.zyla.network.commands.PingHandler;
import com.zyla.network.commands.TransactionHandler;

public class Peer {
    private Thread clientThread;
    private Socket clientSocket;
    private HashMap<Byte, NetworkCommand> networkCommands;
    private DataInputStream in;
    private DataOutputStream out;

    public Peer (Socket socket, HashMap<Byte, NetworkCommand> networkCommands) {
        this.clientSocket = socket;
        this.networkCommands = networkCommands;
        clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                listenForServer();
            }
        });
        clientThread.start();
    }

    protected void listenForServer() {
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIP() {
        return String.format("[%s:%s]", clientSocket.getInetAddress(), clientSocket.getPort());
    }

    public boolean isConnected () {
        return clientSocket.isConnected();
    }
}
