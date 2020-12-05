package com.zyla.server;

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
	protected Socket socket;
	
	DataInputStream in;
	DataOutputStream out;
	
	public final int IP = 26373;
	
	public Server (Socket clientSocket) {
		this.socket = clientSocket;
	}
	
	public void run() {
		in = null;
		out = null;
		
		initStreams();
		
		while (true) {
			try {
				byte[] msg = new byte[138];
				in.readFully(msg);
				
				if (msg[0] == 0 && msg[1] == 0) {
					Util.printMessage("BLOCK INCOMING!!!");
					
					byte[] print = new byte[msg.length - 2];
					
					for (int a = 2; a < msg.length; a++) {
						print[a - 2] = msg[a];
					}
					
					String previousHash = new String(print).substring(0, 64);
					String currentHash = new String(print).substring(64, 128);
					ByteBuffer tSBuffer = ByteBuffer.allocate(Long.BYTES);
					byte[] tSByte = new byte[8];
					for (int a = 0; a < 8; a++) {
						tSByte[a] = print[128 + a];
					}
					tSBuffer.put(tSByte);
					tSBuffer.flip();
					long timeStamp = tSBuffer.getLong();
					
					Util.printMessage(previousHash);
					Util.printMessage(currentHash);
					Util.printMessage(timeStamp + "");
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