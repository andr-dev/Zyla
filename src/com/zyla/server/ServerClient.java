package com.zyla.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.zyla.coin.Util;

public class ServerClient {
	public final static int PORT = 26373;
	
	public static void startServerClient() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(PORT);
			Util.printMessage("Client Socket Started");
		} catch (Exception e) {
			Util.printMessage("Client Socket Failed|" + e);
		}
		
		while (true) {
			try {
				socket = serverSocket.accept();
				Util.printMessage("Client Socket Connection Established");
			} catch (Exception e) {
				Util.printMessage("I/O Error|" + e);
				break;
			}
			
			new Server(socket).start();
		}
	}
}