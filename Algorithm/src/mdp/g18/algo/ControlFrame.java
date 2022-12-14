package mdp.g18.algo;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class ControlFrame extends JPanel{
	int NUM_OF_BUTTONS = 6;
	
	JButton resetButton;
	JButton obstacleButton;
	JButton connectButton;
	JButton loadButton;
	JButton planButton;
	JButton startButton;
	
	ControlFrame(){
		this.setLayout(new GridLayout(1, NUM_OF_BUTTONS, 10, 0));
		
		// Reset paths and obstacles
		resetButton = new JButton("Reset");
		this.add(resetButton);
		
		// Add obstacle
		obstacleButton = new JButton("Add Obstacle");
		this.add(obstacleButton);
		
		// Connect to Rpi
		connectButton = new JButton("Connect");
		this.add(connectButton);
		
		// Load obstacles from Android
		loadButton = new JButton("Load Obstacles");
		this.add(loadButton);
		
		// Display planned path
		planButton = new JButton("Plan Path");
		this.add(planButton);
		
		// Robot executes planned path
		startButton = new JButton("Execute");
		this.add(startButton);
	}
	
}