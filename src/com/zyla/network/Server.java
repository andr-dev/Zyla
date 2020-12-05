package com.zyla.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.util.Arrays;

import com.zyla.coin.Block;
import com.zyla.coin.Util;

public class Server extends Thread {
	
	public final static int HEADER_SIZE = 142;
	
	protected Socket socket;
	
	DataInputStream in;
	DataOutputStream out;
	
	public final int IP = 26373;
	private int seqNum = 0;
	
	public Server (Socket clientSocket) {
		this.socket = clientSocket;
	}
	
	public void run() {
		in = null;
		out = null;
		
		initStreams();
		
		while (true) {
			try {
				byte[] msg = new byte[512];
				
				in.readFully(msg);
				
				if (msg[0] == 0) {
					Util.printMessage("BLOCK INCOMING!!!");
					
					ZylaProtocol newBlock = new ZylaProtocol(msg[0]);
					seqNum++;
					
					byte[] print = new byte[newBlock.getSize()];
					
					for (int a = 2; a < msg.length; a++) {
						print[a - 2] = msg[a];
					}
					
					String previousHash = new String(print).substring(0, 64);
					String currentHash = new String(print).substring(64, 128);
					ByteBuffer tSBuffer = ByteBuffer.allocate(Long.BYTES);
					byte[] tSByte = new byte[Long.BYTES];
					for (int a = 0; a < Long.BYTES; a++) {
						tSByte[a] = print[128 + a];
					}
					tSBuffer.put(tSByte);
					tSBuffer.flip();
					long timeStamp = tSBuffer.getLong();
					
					ByteBuffer nBuffer = ByteBuffer.allocate(Integer.BYTES);
					byte[] nByte = new byte[Integer.BYTES];
					for (int a = 0; a < Integer.BYTES; a++) {
						nByte[a] = print[136 + a];
					}
					nBuffer.put(nByte);
					nBuffer.flip();
					int nonce = nBuffer.getInt();
					
					Util.printMessage(previousHash);
					Util.printMessage(currentHash);
					Util.printMessage(timeStamp + "");
					Util.printMessage(nonce + "");
				}
			} catch (Exception e ) {
				Util.printMessage("ERROR|" + e);
				return;
			}
		}
	}
	
	public void initStreams() {
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			return;
		}
	}
}