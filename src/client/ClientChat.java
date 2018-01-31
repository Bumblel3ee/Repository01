package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat {

	private String ip;
	private int port;
	private Socket socket;
	private Scanner scanner;

	public ClientChat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientChat(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
		try {
			this.socket = new Socket(ip, port);
			System.out.println("Server connected ");
			start();
		} catch (IOException e) {
			System.out.println("Connection refused ");
			// e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Scanner getScanner() {
		return scanner;
	}

	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	public void start() throws IOException {

		scanner = new Scanner(System.in);
		// Inherit thread
		Receiver receiver = new Receiver();
		receiver.start();

		while (true) {
			// sender part
			// runnable implemented
			Sender sender = new Sender();
			Thread t = new Thread(sender);
			System.out.println("Input Msg to Server");
			String msg = scanner.nextLine();
			if (msg.equals("q")) {
				scanner.close();
				sender.close();
				receiver.close();
				break;
			}
			sender.setMsg(msg);
			t.start();//start run method in class sender

		}
		System.out.println("End Chatting");
	}

	// sender
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
					if(msg.equals("q"))this.close();
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
				String msg="";
				try {
					if(socket.isClosed()) {
						//this.close();
						//break;
					}
					else{
						msg = din.readUTF();
						System.out.println("Msg from server :"+msg);
					}
				} catch (IOException e) {
					try {
						this.close();
						System.out.println("End client receiver ");
						//e.printStackTrace();
						break;
					} catch (IOException e1) {
						
						//e1.printStackTrace();
					}
					
				}
			}
		}
	}
}
