package mdp.g18.algo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame implements ActionListener{

	public static final int WIDTH = 615;
	public static final int HEIGHT = 700;
	
	ArenaFrame arena;
	ControlFrame control;
	LabelFrame label;
	
	MainFrame(){		
		arena = new ArenaFrame();
		control = new ControlFrame();
		label = new LabelFrame();
		control.resetButton.addActionListener(this);
		control.obstacleButton.addActionListener(this);
		control.loadButton.addActionListener(this);
		control.connectButton.addActionListener(this);
		control.planButton.addActionListener(this);
		control.startButton.addActionListener(this);
		
		this.add(label, BorderLayout.NORTH);
		this.add(arena, BorderLayout.CENTER);
		this.add(control, BorderLayout.SOUTH);
		this.setTitle("Simulator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH,HEIGHT);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	
		// Reset Arena
		if (e.getSource() == control.resetButton) {
			this.remove(arena);
			arena = new ArenaFrame();
			this.add(arena);
			SwingUtilities.updateComponentTreeUI(this);
		}
		
		// connect to rpi
		if (e.getSource() == control.connectButton) {
			arena.connectToRPI();
		}

		// Load obstacles
		if (e.getSource() == control.loadButton) {
			arena.drawObstacles();
			arena.loadObstacles = true;
		}

		// Add Obstacles
		if (e.getSource() == control.obstacleButton) {
			arena.running = false;
			arena.setImage = true;
			arena.addObstacles = !arena.addObstacles;
			arena.loadObstacles = false;
			SwingUtilities.updateComponentTreeUI(this);
		}
		
		// Load obstacles
		if (e.getSource() == control.loadButton) {
			arena.addObstacles = false;
			arena.clearObstacles = false;
			arena.loadObstacles = true;
			arena.running = false;
			arena.setImage = false;
			SwingUtilities.updateComponentTreeUI(this);
		}

		// start simulation
		if (e.getSource() == control.planButton) {
			arena.addObstacles = false;
			arena.running = !arena.running;
			SwingUtilities.updateComponentTreeUI(this);
		}
		
		// start execute path
		if (e.getSource() == control.startButton) {
			arena.addObstacles = false;
			arena.start = !arena.start;
			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}