package com.zyla.client;

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
	public final static int HEADER_SIZE = 146;
	
	public static void startClient() {
		Socket socket;
		OutputStream out;
		
		try {
			socket = new Socket("127.0.0.1", IP);
			
			out = socket.getOutputStream();
			
			ZylaBlockChain ZBC = new ZylaBlockChain();
			ZBC.genWallets();
			ZBC.genGenesis();
			
			out.write(createHeader(ZBC.getChain().get(0)));
			
			out.close();
			socket.close();
		} catch (IOException e) {
			Util.printMessage("IO Exception| " + e);
		}
	}
	
	private static byte[] createHeader (Block b) {
		byte[] tSByte = ByteBuffer.allocate(Long.BYTES).putLong(b.getTimeStamp()).array();
		byte[] out = new byte[HEADER_SIZE];
		byte[] str = null;
		int buf = 0;
		
		/***************************/
		/* Block Definition        */
		/* 2  : dataType           */
		/* 64 : previousHash()     */
		/* 64 : merkleRoot()       */
		/* 8  : timeStamp()        */
		/* 8  : nonce              */
		/***************************/
		
		out[0] = 0;
		out[1] = 0;
		buf += 2;
		
		// Previous Hash
		str = b.getPrevHash().getBytes();
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		
		// Merkle Root
		str = b.getMerkleRoot().getBytes();
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		
		// Time Stamp
		str = tSByte;
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		
		// Nonce
		str = ByteBuffer.allocate(8).putInt(b.getNonce()).array();
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		
		Util.printMessage(" P Hash : " + Arrays.toString(b.getPrevHash().getBytes()) + "| Merkle : " +  Arrays.toString(b.getMerkleRoot().getBytes()) + "| T Stamp : " + Arrays.toString(tSByte) + "| Nonce : " + b.getNonce());
		
		return out;
	}
}