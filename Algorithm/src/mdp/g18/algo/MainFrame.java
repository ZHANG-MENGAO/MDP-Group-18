package mdp.g18.algo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame implements ActionListener{

	ArenaFrame arena;
	ControlFrame control;
	
	MainFrame(){		
		arena = new ArenaFrame();
		control = new ControlFrame();
		control.resetButton.addActionListener(this);
		control.obstacleButton.addActionListener(this);
		control.clearButton.addActionListener(this);
		control.startButton.addActionListener(this);
		
		this.add(arena, BorderLayout.WEST);
		this.add(control, BorderLayout.EAST);
		this.setTitle("Testing");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
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
		
		// Add Obstacles
		if (e.getSource() == control.obstacleButton) {
			arena.clearObstacles = false;
			arena.running = false;
			arena.setImage = true;
			arena.addObstacles = !arena.addObstacles;
			SwingUtilities.updateComponentTreeUI(this);
		}
		
		// Clear Obstacles
		if (e.getSource() == control.clearButton) {
			arena.addObstacles = false;
			arena.running = false;
			arena.clearObstacles = !arena.clearObstacles;
		}
		
		// start simulation
		if (e.getSource() == control.startButton) {
			arena.addObstacles = false;
			arena.clearObstacles = false;
			arena.running = !arena.running;
			SwingUtilities.updateComponentTreeUI(this);
		}
	}

}