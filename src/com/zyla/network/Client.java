package com.zyla.network;

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
	
	public static void startClient() {
		Socket socket;
		OutputStream out;
		
		try {
			socket = new Socket("127.0.0.1", IP);
			
			out = socket.getOutputStream();
			
			ZylaBlockChain ZBC = new ZylaBlockChain();
			ZBC.genWallets();
			Wallet wallet = new Wallet("C:\\Users\\Andre\\Desktop\\publickey1.pub", "C:\\Users\\Andre\\Desktop\\privatekey1.key", true);
			Wallet wallet2 = new Wallet("C:\\Users\\Andre\\Desktop\\publickey2.pub", "C:\\Users\\Andre\\Desktop\\privatekey2.key", true);
			ZBC.genGenesis();
			
			if (ZBC.isChainValid()) {
				Util.printMessage("Zyla Block Chain Validated");
			} else {
				Util.printMessage("Zyla Block Chain Invalid");
			}
			
			ZylaProtocol packet = new ZylaProtocol(seqNum, (byte) 0, new BlockData(new BlockHeaderData(ZBC.getChain().get(0).getPrevHash(), ZBC.getChain().get(0).getMerkleRoot(), ZBC.getChain().get(0).getTimeStamp(), ZBC.getChain().get(0).getNonce()), 1, new CoinbaseData(null), new TransactionData[] {new TransactionData(ZBC.getChain().get(0).getTransactions().get(0))}));
			out.write(packet.getSize());
			out.write(packet.toByteArray());
			seqNum++;
			
			out.close();
			socket.close();
		} catch (IOException e) {
			Util.printMessage("IO Exception|" + e);
		}
	}
}