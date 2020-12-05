package com.zyla.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;

import com.zyla.coin.Util;
import com.zyla.coin.Wallet;
import com.zyla.coin.ZylaBlockChain;
import com.zyla.coin.Block;
import com.zyla.coin.Transaction;
import com.zyla.coin.TransactionOutput;

public class Client {
	
	public final static int IP = 26373;
	public final static int HEADER_SIZE = 142;
	private static int seqNum = 0;
	
	public static void startClient() {
		Socket socket;
		OutputStream out;
		
		try {
			socket = new Socket("127.0.0.1", IP);
			
			out = socket.getOutputStream();
			
			ZylaBlockChain ZBC = new ZylaBlockChain();
			ZBC.genWallets();
			ZBC.genGenesis();
			
			ZylaProtocol packet = new ZylaProtocol(seqNum, (byte) 0, 140, new BlockData(new BlockHeaderData(ZBC.getChain().get(0).getPrevHash(), ZBC.getChain().get(0).getMerkleRoot(), ZBC.getChain().get(0).getTimeStamp(), ZBC.getChain().get(0).getNonce()), 1));
			out.write(packet.toByteArray());
			seqNum++;
			
			out.close();
			socket.close();
		} catch (IOException e) {
			Util.printMessage("IO Exception|" + e);
		}
	}
}