package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;

public class MultichatServer {
	HashMap<String,DataOutputStream> clients;

	public MultichatServer() {
		clients = new HashMap<>();
		Collections.synchronizedMap(clients);
	}
	public void start() {
		ServerSocket severSocket =null; 
		Socket socket = null;
		try {
			severSocket = new ServerSocket(111);
			System.out.println("Starting Sever ");
			while(true ) {
				socket =severSocket.accept();
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"] has been connected"); 
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		} catch (IOException e) {
 
			//e.printStackTrace();
		}
		
		
	}
	void sendToAll(String msg) {
		
		for(DataOutputStream dout : clients.values()) {
			try {
				dout.writeUTF(msg);
			} catch (IOException e) {
				
			}
		}
	}
	class ServerReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		public ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		@Override
		public void run() {
			String name ="";
			try {
				name = in.readUTF();
				sendToAll(name + " has entered" );
				clients.put(name, out);
				System.out.println("current member :"+clients.size());
				while(in != null) {
					String msg =in.readUTF();
					sendToAll(msg);
					System.out.println(name+" says "+msg);
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}finally {
				sendToAll(name+" has left the room");
				clients.remove(name);
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"] has disconnected ");
				System.out.println("Current member :"+clients.size());
				try {
					out.close();
				} catch (IOException e) {
 
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
 
					e.printStackTrace();
				}
				try {
					socket.close();
				} catch (IOException e) {
 
					e.printStackTrace();
				}
			}
		}
		
	}
	
}