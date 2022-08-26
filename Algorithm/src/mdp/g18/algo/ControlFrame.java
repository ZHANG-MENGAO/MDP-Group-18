package mdp.g18.algo;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ControlFrame extends JPanel{
	
	public static final int CONTROL_WIDTH = 600;
	public static final int CONTROL_HEIGHT = 600;
	
	JButton resetButton;
	JButton obstacleButton;
	JButton clearButton;
	JButton startButton;
	JLabel controlLabel;
	static JLabel xLabel;
	static JLabel yLabel;
	
	ControlFrame(){
		this.setPreferredSize(new Dimension(CONTROL_WIDTH,CONTROL_HEIGHT));

		// Label
		controlLabel = new JLabel("Control Panel");
		controlLabel.setFont(new Font("Ink Free",Font.BOLD, 60));
	    this.add(controlLabel,JLabel.CENTER);

		// Reset Button
		resetButton = new JButton();
		resetButton.setText("Reset");
		resetButton.setSize(100,50);
		resetButton.setLocation(0,200);
		this.add(resetButton);
		
		// Add obstacle
		obstacleButton = new JButton();
		obstacleButton.setText("Add Obstacle");
		obstacleButton.setSize(100,50);
		obstacleButton.setLocation(0,200);
		this.add(obstacleButton);
		
		// Add obstacle
		clearButton = new JButton();
		clearButton.setText("Clear Obstacle");
		clearButton.setSize(100,50);
		clearButton.setLocation(0,200);
		this.add(clearButton);
		
		// Add obstacle
		startButton = new JButton();
		startButton.setText("Start");
		startButton.setSize(100,50);
		startButton.setLocation(0,200);
		this.add(startButton);
		
		// Label x
		xLabel = new JLabel();
		xLabel.setFont(new Font("Ink Free",Font.BOLD, 20));
		this.add(xLabel);
			 		
		// Label y
		yLabel = new JLabel();
		yLabel.setFont(new Font("Ink Free",Font.BOLD, 20));
		this.add(yLabel);
	}
	
}