package mdp.g18.algo;

import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ControlFrame extends JPanel{
	
	JButton resetButton;
	JButton obstacleButton;
	JButton clearButton;
	JButton planButton;
	JButton startButton;
	
	ControlFrame(){
		this.setLayout(new GridLayout(1, 5, 10, 0));
		
		// Reset Button
		resetButton = new JButton("Reset");
		this.add(resetButton);
		
		// Add obstacle
		obstacleButton = new JButton("Add Obstacle");
		this.add(obstacleButton);
		
		// Add obstacle
		clearButton = new JButton("Clear");
		this.add(clearButton);
		
		// Add obstacle
		planButton = new JButton("Plan Path");
		this.add(planButton);
		
		// Add obstacle
		startButton = new JButton("Execute");
		this.add(startButton);
	}
	
}