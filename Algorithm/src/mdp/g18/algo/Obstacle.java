package mdp.g18.algo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Obstacle {
	
	public int obstacleID;
	// coordinates at top right hand side
	public int xCoordinate; // x coordinate
	public int yCoordinate; // y coordinate 
	public char direction;  // orientation
	public int virtualx;  // x coordinate of virtual border
	public int virtualy;  // y coordinate of virtual border
	
	Random random;
	
	Obstacle(int xCoordinate,int yCoordinate, char direction){
		random = new Random();
		this.obstacleID = random.nextInt((int)(Arena.ARENA_WIDTH/Arena.UNIT_SIZE)) * Arena.UNIT_SIZE;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.direction = direction;
		this.virtualx = xCoordinate + 15;
		this.virtualy = yCoordinate - 15;
	}
	
	public int getobstacleID() {
		return this.obstacleID;
	}
	
	public void paintObstacle(Graphics g) {
		
		// Obstacle body
		for (int i = -9; i <= 0; i++) {
			for (int j = 0; j <= 9; j++) {
				g.setColor(Color.blue);
				g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			}
		}
		
		// Obstacle Outline
		g.setColor(Color.red);
		for (int i = -39; i <= 0; i +=2) {
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, virtualy * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy + 39) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
				
		for (int j = 0; j <= 39; j += 2) {
			g.fillRect(virtualx * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx - 39) * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
	}
	
	public void paintObstacleSelector(Graphics g) {
		
		// Obstacle body
		for (int i = -9; i <= 0; i++) {
			for (int j = 0; j <= 9; j++) {
				g.setColor(Color.lightGray);
				g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			}
		}
		
		// Obstacle Outline
		g.setColor(Color.white);
		for (int i = -39; i <= 0; i +=2) {
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, virtualy * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy + 39) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
				
		for (int j = 0; j <= 39; j += 2) {
			g.fillRect(virtualx * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx - 39) * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
	}
}
