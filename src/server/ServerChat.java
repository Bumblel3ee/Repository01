package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class ServerChat {
	ServerSocket serverSocket;
	Socket socket;
	Scanner scanner;

	public ServerChat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServerChat(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server Ready");
			start();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * public ServerChat(ServerSocket serverSocket, Socket socket) { super();
	 * this.serverSocket = serverSocket; this.socket = socket; }
	 */

	private void start() throws IOException {
		socket = serverSocket.accept();
		scanner = new Scanner(System.in);
		String msg ="";
		
		Receiver receiver = new Receiver();
		receiver.start();

		while (true) {
			Sender sender = new Sender();
			Thread t = new Thread(sender);
			System.out.println("Input Msg to Server");
			msg = scanner.nextLine();
			if (msg.equals("q")) {
				receiver.interrupt();
				scanner.close();
				sender.close();
				receiver.close();
				break;
			}
			sender.setMsg(msg);
			t.start();// start run method in class sender
		}
		System.out.println("End server chat");

	}

	class Sender implements Runnable {
		OutputStream out;
		DataOutputStream dout;
		String msg;

		public Sender() throws IOException {
			out = socket.getOutputStream();
			dout = new DataOutputStream(out);

		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public void close() throws IOException {
			dout.close();
			out.close();
		}

		@Override
		public void run() {
			try {
				if (dout != null) {
					dout.writeUTF(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	class Receiver extends Thread {
		InputStream in;
		DataInputStream din;

		public Receiver() throws IOException {
			in = socket.getInputStream();
			din = new DataInputStream(in);
		}

		public void close() throws IOException {
			din.close();
			in.close();
		}

		@Override
		public void run() {
			while (true) {
				String msg = "";
				try {
					if(socket.isClosed()) {
						//this.close();
					//	break;
					}
					else{
						msg = din.readUTF();
						System.out.println("Msg from client :"+msg);}
				} catch (IOException e) {
					try {
						this.close();
						System.out.println("End server receiver");
						//e.printStackTrace();
						break;
					} catch (IOException e1) {
						
					}
					
				}
			}
		}
	}

}
