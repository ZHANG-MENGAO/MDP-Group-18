package mdp.g18.algo;

import java.io.IOException;
import java.util.Objects;

public class Main {
	private static RPiClient client = new RPiClient();
	private static RealRobot robot = new RealRobot(0, 0, 0);


	public static void main(String[] args) {
		new MainFrame();
//		try {
//			client.startConnection();
//			System.out.println("RPi connected.");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

//		client.endConnection();
	}

	private static void fastestTask() {
		// Connect to RPi
		try {
			client.startConnection();
			System.out.println("RPi connected.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String turn = "";

		// Move robot
		while (true) {
			String msg = client.receiveMsg();
			System.out.println(msg);
			if (Objects.equals(msg, "Left")) {
				robot.turnLeft();
				turn = "Left";
			} else if (Objects.equals(msg, "Right")) {
				robot.turnRight();
				turn = "Right";
			} else {
				robot.moveForward();
			}

			// Turn robot left/right after first turn
			if (Objects.equals(turn, "Left")) {
				robot.turnRight();
			}

		}

		// End connection with RPi
//		client.endConnection();

	}
}