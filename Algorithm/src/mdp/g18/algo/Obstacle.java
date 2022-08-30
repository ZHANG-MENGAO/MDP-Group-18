package mdp.g18.algo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;

public class Obstacle {
	
	public int obstacleID;
	// coordinates at bottom left-hand side
	private int xCoordinate; // x coordinate
	private int yCoordinate; // y coordinate
	private Direction direction;  // orientation
	private int virtualx;  // x coordinate of virtual border
	private int virtualy;  // y coordinate of virtual border

	private static final int LENGTH = 10;
	private static final int VIRTUAL_LENGTH = 40;
	
	Obstacle(int id, int xCoordinate, int yCoordinate, Direction direction){
		this.obstacleID = id;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.direction = direction;
		this.virtualx = xCoordinate - 15;
		this.virtualy = yCoordinate - 15;
	}
	
	public void setObstacleID(int id) {
		this.obstacleID = id;
	}
	
	public int getObstacleID() {
		return this.obstacleID;
	}

	public int getxCoordinate() {
		return this.xCoordinate;
	}

	public int getyCoordinate() {
		return this.yCoordinate;
	}
	
	private void setDirection(Direction dir) {
		this.direction = dir;
	}
	
	public Direction getDirection() {
		return this.direction;
	}

	public int getLength() { return LENGTH; }
	
	public void paintObstacle(Graphics g, boolean selector) {
		
		// Obstacle body
		if (selector) {
			g.setColor(Color.lightGray);
		}
		else {
			g.setColor(Color.blue);
		}

		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < LENGTH; j++) {
				g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate - LENGTH + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			}
		}
				
		// Obstacle Outline
		if (selector) {
			g.setColor(Color.white);
		}
		else {
			g.setColor(Color.red);
		}

		for (int i = 0; i <= VIRTUAL_LENGTH; i +=2) {
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy - LENGTH) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy - LENGTH + VIRTUAL_LENGTH) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
				
		for (int j = 0; j <= VIRTUAL_LENGTH; j += 2) {
			g.fillRect((virtualx + VIRTUAL_LENGTH) * Arena.UNIT_SIZE, (virtualy - LENGTH + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect(virtualx * Arena.UNIT_SIZE, (virtualy - LENGTH + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
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
				// Color upper row of pixels
				for (int i = 0; i < LENGTH; i++) {
					for (int j = -LENGTH; j < 2 - LENGTH; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case SOUTH:
				// Color bottom pixels purple
				for (int i = 0; i < LENGTH; i++) {
					for (int j = 0; j > -2; j--) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case EAST:
				// Color right pixels purple
				for (int i = 8; i < LENGTH; i++) {
					for (int j = 0; j > -LENGTH; j--) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case WEST:
				// Color left pixels purple
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j > -LENGTH; j--) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case UNSET:
				break;
		}
	}
}