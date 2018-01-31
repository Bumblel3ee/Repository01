package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("type ur nick name ");
		String nickName = sc.nextLine();
		Socket socket=null;
		if (nickName.length() == 0) {
			System.out.println("USAGE: Client ¥Î»≠∏Ì");
			System.exit(0);
		}
		try {
			String serverIp = "70.12.111.135";
			socket = new Socket(serverIp, 111);
			System.out.println("Sever Connected");
			Thread sender = new ClientSender(socket, nickName);
			Thread receiver = new ClientReceiver(socket);

			sender.start();
			receiver.start();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
		}finally {
			sc.close();
			try {
				if(socket!=null)socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	static class ClientSender extends Thread {
		Socket socket;
		DataOutputStream out;
		String name;

		ClientSender(Socket socket, String name) {
			this.socket = socket;
			try {
				out = new DataOutputStream(socket.getOutputStream());
				this.name = name;
			} catch (IOException e) {

			}

		}

		public void run() {
			Scanner scanner = new Scanner(System.in);
			try {
				if (out != null) {
					out.writeUTF(name);
				}
				while (out != null)
					out.writeUTF("[" + name + "]" + scanner.nextLine());
			} catch (IOException e) {
			} finally {
				 scanner.close();
			}
		}
	}

	static class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream in;

		public ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
			}

		}

		public void run() {
			while (in != null) {
				try {
					System.out.println(in.readUTF());
				} catch (IOException e) {
				}
			}
		}

	}

}
