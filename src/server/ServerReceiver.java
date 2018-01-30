package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerReceiver {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		InputStream in = null;
		InputStreamReader inr = null;
		BufferedReader br = null;
		try {
			serverSocket = new ServerSocket(7777);
			// serverSocket = new ServerSocket(7777);
			InetAddress ia = Inet4Address.getLocalHost();
			System.out.println(ia.getHostName());
			System.out.println(ia.getHostAddress());
			System.out.println("Start server...");
			while (true) {
				// socket
				socket = serverSocket.accept();// waiting for a client to come
				System.out.println("Client Connected...");
				// receiving data
				in = socket.getInputStream();
				inr = new InputStreamReader(in);
				br = new BufferedReader(inr);
				
				System.out.println(br.readLine() + " πŸ∫∏ ∏€√ª¿Ã ");
				System.out.println("End sever ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try { 
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				inr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
