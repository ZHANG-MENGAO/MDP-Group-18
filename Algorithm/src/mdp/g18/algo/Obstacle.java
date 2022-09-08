package mdp.g18.algo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Obstacle {
	
	private Point2D.Double center = new Point2D.Double();
    private static final double DEG_TO_RAD = Math.PI / 180;
    public TurningRadius turningRadius;

	public int obstacleID;
	// coordinates at bottom left-hand side
	private int xCoordinate; // x coordinate
	private int yCoordinate; // y coordinate
	private Direction direction;  // orientation
	private int virtualx;  // x coordinate of virtual border
	private int virtualy;  // y coordinate of virtual border

	private static final int LENGTH = 10;
	private static final int VIRTUAL_LENGTH = 40;
	
	Obstacle(int id,int xCoordinate,int yCoordinate, Direction direction){
		this.obstacleID = id;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.direction = direction;
		this.virtualx = xCoordinate + 15;
		this.virtualy = yCoordinate - 15;
		
		this.center.setLocation(this.xCoordinate - 5, this.yCoordinate - 5);
	}
	
	public void createCircleLeft(int[] coordinates) {
    	
		int angle = 0;
		
		switch(getDirection()) {
		case NORTH:
			angle = 180;
			break;
		case EAST:
			angle = -90;
			break;
		case WEST:
			angle = 90;
			break;
		default:
			break;
		}
		
    	double x = coordinates[0] - TurningRadius.getRadius() * Math.cos(angle * DEG_TO_RAD);
    	double y = coordinates[1] - TurningRadius.getRadius() * Math.sin(angle * DEG_TO_RAD);
    	this.turningRadius = new TurningRadius(new Point2D.Double(x, y));
	}
    
    public void createCircleRight(int[] coordinates) {
    	int angle = 0;
		
		switch(getDirection()) {
		case NORTH:
			angle = 180;
			break;
		case EAST:
			angle = -90;
			break;
		case WEST:
			angle = 90;
			break;
		default:
			break;
		}
    	
    	double x = coordinates[0] + TurningRadius.getRadius() * Math.cos(angle * DEG_TO_RAD);
    	double y = coordinates[1] + TurningRadius.getRadius() * Math.sin(angle * DEG_TO_RAD);
    	this.turningRadius = new TurningRadius(new Point2D.Double(x, y));
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
	
	public Point2D.Double getObstacleCenter() {
		return this.center;
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

		for (int i = -LENGTH; i <= 0; i++) {
			for (int j = -LENGTH; j <= 0; j++) {
				g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			}
		}
		// Obstacle Outline
		if (selector) {
			g.setColor(Color.white);
		}
		else {
			g.setColor(Color.red);
		}
		
		for (int i = -VIRTUAL_LENGTH; i <= 0; i +=2) {
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy - LENGTH) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy - LENGTH + VIRTUAL_LENGTH) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
				
		for (int j = 0; j <= VIRTUAL_LENGTH; j += 2) {
			g.fillRect(virtualx * Arena.UNIT_SIZE, (virtualy - LENGTH + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx - VIRTUAL_LENGTH) * Arena.UNIT_SIZE, (virtualy - LENGTH + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
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
				for (int i = -LENGTH; i <= 0; i++) {
					for (int j = -LENGTH; j < -8; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case SOUTH:
				// Color bottom pixels purple
				for (int i = -LENGTH; i <= 0; i++) {
					for (int j = -1 ; j <= 0; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case EAST:
				// Color right pixels purple
				for (int i = -1; i <= 0; i++) {
					for (int j = -LENGTH; j <= 0; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case WEST:
				// Color left pixels purple
				for (int i = -LENGTH; i < -8; i++) {
					for (int j = -LENGTH; j <= 0; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case UNSET:
				break;
		}
	}
}