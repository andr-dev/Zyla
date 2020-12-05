package com.zyla.network.old;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.zyla.coin.Util;
import com.zyla.coin.Wallet;
import com.zyla.coin.ZylaBlockChain;
import com.zyla.coin.Block;
import com.zyla.coin.Transaction;
import com.zyla.coin.TransactionInput;
import com.zyla.coin.TransactionOutput;

public class Client {

    public final static int IP = 26373;
    public final static int HEADER_SIZE = 142;
    private static int seqNum = 0;

    private ZylaBlockChain ZBC;

    public Client () {
        Socket socket;
        OutputStream out;

        try {
            socket = new Socket("127.0.0.1", IP);

            out = socket.getOutputStream();

            ZBC = new ZylaBlockChain();

        } catch (IOException e) {
            Util.printMessage("IO Exception|" + e);
        }
    }

    public ArrayList<Wallet> genWallets () {
        return ZBC.genWallets();
    }

    public void genGenesis() {
        ZBC.genGenesis();
    }

    public boolean isChainValid () {
        return ZBC.isChainValid();
    }

    public ArrayList<Block> getChain () {
        return ZBC.getChain();
    }

    public void addBlock (Block b) {
        Util.printMessage("Update : Block added to the Blockchain|Mining in progress");
        b.mineBlock(ZBC.getDif());
        ZBC.bChain.add(b);
    }
}