package mdp.g18.algo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Obstacle {
	
	public int obstacleID;
	// coordinates at top right-hand side
	public int xCoordinate; // x coordinate
	public int yCoordinate; // y coordinate 
	public Direction direction;  // orientation
	public int virtualx;  // x coordinate of virtual border
	public int virtualy;  // y coordinate of virtual border

	private static final int LENGTH = 10;
	private static final int VIRTUAL_LENGTH = 40;
	
	Random random;
	
	Obstacle(int xCoordinate,int yCoordinate, Direction direction){
		random = new Random();
		this.obstacleID = random.nextInt((int)(Arena.ARENA_WIDTH/Arena.UNIT_SIZE)) * Arena.UNIT_SIZE;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.direction = direction;
		this.virtualx = xCoordinate + 15;
		this.virtualy = yCoordinate - 15;
	}
	
	public int getObstacleID() {
		return this.obstacleID;
	}

	public int getLength() { return LENGTH; }
	
	public void paintObstacle(Graphics g) {
		
		// Obstacle body
		for (int i = -LENGTH; i < 0; i++) {
			for (int j = 0; j < LENGTH; j++) {
				g.setColor(Color.blue);
				g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			}
		}
		
		// Obstacle Outline
		g.setColor(Color.red);
		for (int i = -VIRTUAL_LENGTH; i < 0; i +=2) {
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, virtualy * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy + VIRTUAL_LENGTH - 1) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
				
		for (int j = 0; j < VIRTUAL_LENGTH; j += 2) {
			g.fillRect(virtualx * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx - VIRTUAL_LENGTH + 1) * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
	}
	
	public void paintObstacleSelector(Graphics g) {
		
		// Obstacle body
		for (int i = -LENGTH; i < 0; i++) {
			for (int j = 0; j < LENGTH; j++) {
				g.setColor(Color.lightGray);
				g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			}
		}
		
		// Obstacle Outline
		g.setColor(Color.white);
		for (int i = -VIRTUAL_LENGTH; i < 0; i +=2) {
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, virtualy * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy + VIRTUAL_LENGTH - 1) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
				
		for (int j = 0; j < VIRTUAL_LENGTH; j += 2) {
			g.fillRect(virtualx * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx - VIRTUAL_LENGTH - 1) * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
	}

	private void setDirection(Direction dir) {
		this.direction = dir;
	}

	// Set image direction of an obstacle
	public void selectImage(Graphics g, boolean selector, Direction dir) {
		if (selector) {
			g.setColor(Color.pink);
		}
		else {
			g.setColor(Color.magenta);
			setDirection(dir);
		}

		switch (dir) {
			case NORTH:
				// Colour upper row of pixels
				for (int i = -LENGTH; i < 0; i++) {
					for (int j = 0; j <= 1; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case SOUTH:
				// Colour bottom pixels purple
				for (int i = -LENGTH; i < 0; i++) {
					for (int j = 8; j <LENGTH; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case EAST:
				// Colour right pixels purple
				for (int i = -LENGTH; i < -8; i++) {
					for (int j = 0; j < LENGTH; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case WEST:
				// Colour left pixels purple
				for (int i = -1; i <= 0; i++) {
					for (int j = 0; j < LENGTH; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
		}
	}
}
