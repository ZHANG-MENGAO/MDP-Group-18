package mdp.g18.algo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Scanner;
import java.io.PrintWriter;
import java.time.LocalTime;

public class Server {
	public static final int PORT = 2763;
	public final String RPI_IP_ADDRESS = "";

	private static ServerSocket serverSocket;
	private PrintWriter toClient;
	private Scanner fromClient;

	public static void main(String[] args) throws IOException {
		// Connect to client via client request
		Server server = new Server();
		Socket client = serverSocket.accept();
		server.startConnection(client);
		System.out.println("Client connected.");

		// Print any received messages
		while (true){
			server.sendMsg("Read data");
			System.out.println("Received message " + server.receiveMsg());
		}

	}

	public Server() throws IOException{
		serverSocket = new ServerSocket(PORT);
	}

	public void startConnection(Socket client) throws IOException {
		toClient = new PrintWriter(client.getOutputStream());
		fromClient = new Scanner(client.getInputStream());
	}

	public void sendMsg(String msg) {
		toClient.println(msg);
		toClient.flush();
		System.out.println("Message sent at " + LocalTime.now());
	}

	public String receiveMsg() {
		String msg = fromClient.nextLine();
		System.out.println("Message received at " + LocalTime.now());
		return msg;
	}
}
