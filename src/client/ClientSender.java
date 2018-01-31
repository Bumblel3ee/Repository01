package client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientSender {
	public static void main(String[] args) {

		String ip = "70.12.111.135";
		int port = 7777;
		Socket socket = null;
		OutputStream os = null;
		OutputStreamWriter osw = null;
		try {
			System.out.println("Hello World.");
			socket = new Socket(ip, port);
			System.out.println("Connection okay...");

			// send data
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);// for korean and etc
			osw.write("hi ya~");
			System.out.println("sned Complete");
		} catch (IOException e) {
 
			e.printStackTrace();
		} finally {
			try {
				// in FM way the exceptions supposed to be handled separately
				//
				osw.close();
				os.close();
				socket.close();
			} catch (IOException e) {
	 
				e.printStackTrace();
			}
		}
	}
}
