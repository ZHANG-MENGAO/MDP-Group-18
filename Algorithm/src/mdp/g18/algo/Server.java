package mdp.g18.algo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Scanner;
import java.io.PrintWriter;
import java.time.LocalTime;

import static java.lang.Thread.sleep;

public class Server {
	public static final int PORT = 2763;
	public final String RPI_IP_ADDRESS = "";

	private static ServerSocket serverSocket;
	private PrintWriter toClient;
	private Scanner fromClient;

	public static void main(String[] args) throws IOException, InterruptedException {
		// Connect to client via client request
		Server server = new Server();
		Socket client = serverSocket.accept();
		server.startConnection(client);
		System.out.println("Client connected.");


//		String raw = "153,50,-90,1:52,118,180,2:170,117,180,3:99,172,-90,4:69,69,0,5";
		String raw = "30,120,-90,1:130,70,180,2:100,150,180,3";
		server.sendMsg(raw);

		// Print any received messages
		while (true) {
			sleep(100);
			server.sendMsg("g\n");
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
		System.out.println("Starting server...");
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