package mdp.g18.algo;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;
import java.time.LocalTime;

public class RPiClient {
	private Socket socket;
	private PrintWriter toRPi;
	private Scanner fromRPi;

	// TODO: find out RPi IP address and port
	public final String RPI_IP_ADDRESS = "";
	public final int RPI_PORT = 2763;

	public static void main (String[] args) throws IOException {
		RPiClient client = new RPiClient();
		client.startConnection();
		System.out.println("RPi connected.");
		while (true){
			client.sendMsg("Read data");
			System.out.println("Received message " + client.receiveMsg());
		}
	}

	public void startConnection() throws IOException {
		System.out.println("Starting connection to RPi...");
		socket = new Socket(RPI_IP_ADDRESS, RPI_PORT);
		toRPi = new PrintWriter(socket.getOutputStream());
		fromRPi = new Scanner(socket.getInputStream());
	}

	public void endConnection() throws IOException {
		System.out.println("Ending connection...");
		socket.close();
	}

	public void sendMsg(String msg) {
		toRPi.println(msg);
		toRPi.flush();
		System.out.println("Message sent at " + LocalTime.now());
	}

	public String receiveMsg() {
		String msg = fromRPi.nextLine();
		System.out.println("Message received at " + LocalTime.now());
		return msg;
	}

	public ArrayList<Object[]> processString(String msg) {
		// Split msg into each obstacle info
		String[] rawObsInfo = msg.split(":");

		ArrayList<Object[]> obs = new ArrayList<>();

		// Split rawObsInfo into individual variable of obstacle
		for (String s : rawObsInfo) {
			String[] obsVars = s.split(",");
			int x = Integer.parseInt(obsVars[0]);
			int y = 200 - Integer.parseInt(obsVars[1]);
			int obsID = Integer.parseInt(obsVars[3]);

			// Convert direction from String to Direction
			Direction dir = switch (obsVars[2]) {
				case "90" -> Direction.NORTH;
				case "-90" -> Direction.SOUTH;
				case "0" -> Direction.EAST;
				case "180" -> Direction.WEST;
				default -> Direction.UNSET;
			};

			// Add obstacle info to obs
			Object[] obstacle = new Object[] {obsID, x, y, dir};
			obs.add(obstacle);
		}

		return obs;
	}

}
