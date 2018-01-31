package client;

public class Client {

	public static void main(String[] args) {
		String ip = "70.12.111.135";
		int port = 8888;
		@SuppressWarnings("unused")
		ClientChat chat = null;
		chat = new ClientChat(ip, port);
	} 
}