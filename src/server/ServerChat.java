package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ServerChat {
	ServerSocket serverSocket;
	Scanner scanner;
	HashMap<String,DataOutputStream> list;
	
	public ServerChat() {
	}

	public ServerChat(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Ready Server ...");
			list = new HashMap<>();
			start();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}

	}

	private void start() throws IOException {
		while (true) {
			System.out.println("Server Ready ....");
			Socket socket = serverSocket.accept();
			System.out.println(socket.getInetAddress()+" Connected..");
			Receiver receiver = new Receiver(socket);
			receiver.start();
		}
	}

	private void sendAllMsg(String msg,String key) throws IOException {
		Sender sender = new Sender();
		Thread t = new Thread(sender);
		sender.setSendMsg(msg);
		sender.setKey(key);
		t.start();
	}
	
	
	
	// Message Sender .....................................
	class Sender implements Runnable {
		String key;
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		String msg;

		public Sender() {
		}

		public void setSendMsg(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			try {
				if(list.size() != 0) {
					for(DataOutputStream dout:list.values()) {
						dout.writeUTF(key+" : "+msg);
					}
					/*for(String key:list.keySet()) {
						//System.out.println(list.(key));
						if(!key.equals(getKey()))list.get(key).writeUTF(msg);// ban same ip msg 
					}*/
				}
			} catch (Exception e) {
				System.out.println("Not Available");
			}
		}

	}

	// Message Receiver .....................................
	class Receiver extends Thread {
		InputStream in;
		DataInputStream din;
		OutputStream out;
		DataOutputStream dout;
		
		Socket socket;
		public Receiver(Socket socket) throws IOException {
			this.socket = socket;
			in = socket.getInputStream();
			din = new DataInputStream(in);
			out = socket.getOutputStream();
			dout = new DataOutputStream(out);
			
			list.put((""+socket.getInetAddress()),dout);
		/*	String key =(""+socket.getInetAddress());
			System.out.println(key.substring(7, 14));
			list.put(key.substring(7, 14),dout);*/
			System.out.println("접속자 수:"+list.size());
		}

		public void close() throws IOException {
			socket.close();
			in.close();
			din.close();
		}

		@Override
		public void run() {
			while (true) {
				String msg = null;
				try {
					msg = din.readUTF();
					System.out.println(socket.getInetAddress()+" :"+msg);
					sendAllMsg(msg,""+socket.getInetAddress());
				} catch (IOException e) {
					try {
						this.close();
						System.out.println("End server receiver");
						list.remove(""+socket.getInetAddress());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					break;				
				}

			}
		}
	}

}






