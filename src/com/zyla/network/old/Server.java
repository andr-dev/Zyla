package com.zyla.network.old;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import org.bouncycastle.util.Arrays;

import com.zyla.coin.Block;
import com.zyla.coin.Util;
import com.zyla.network.commands.NetworkCommand;

public class Server extends Thread {

    protected Socket socket;

    DataInputStream in;
    DataOutputStream out;

    public final int PORT = 26373;
    private ArrayList<Client> clients;
    private HashMap<String, NetworkCommand> commands = new HashMap<>();
    private int seqNum = 0;

    public Server (Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run () {
        in = null;
        out = null;

        initStreams();
    }

    public void initStreams () {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            return;
        }
    }

    public void listen () {
        byte[] msg = new byte[512];
        try {
            in.readFully(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Util.printMessage("RECIEVED BLOCK!!!");
    }
}