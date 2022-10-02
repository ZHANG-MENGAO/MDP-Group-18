package mdp.g18.algo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Obstacle {
	
	private Point2D.Double center = new Point2D.Double(); // center of obstacle
	private Point2D.Double imageCenter = new Point2D.Double(); // center of image on obstacle
    private static final double DEG_TO_RAD = Math.PI / 180;
    private TurningRadius turningRadius;

	private int obstacleID;
	// coordinates at bottom right-hand side
	private int xCoordinate; // x coordinate
	private int yCoordinate; // y coordinate 
	private Direction direction;  // orientation
	private int virtualx;  // x coordinate of virtual border
	private int virtualy;  // y coordinate of virtual border

	private static final int LENGTH = 10;
	private static final int VIRTUAL_LENGTH = 40;
	
	Rectangle2D obstacleArea;
	
	Obstacle(int id,int xCoordinate,int yCoordinate, Direction direction){
		this.obstacleID = id;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.direction = direction;
		this.virtualx = xCoordinate + 15;
		this.virtualy = yCoordinate + 15;
		
		this.center.setLocation(this.xCoordinate - 5, -(200 - this.yCoordinate + 4));
	}
	
	public void updateObstacleArea(double[] c) {
		obstacleArea();
	}
	
	public void obstacleArea() {
		this.obstacleArea = new Rectangle2D.Double();
		this.obstacleArea.setRect(this.getObstacleCenter().getX() - 5, this.getObstacleCenter().getY() - 5, LENGTH, LENGTH);
	}
	
	public void createCircleLeft(double[] coordinates) {
    	
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
    
    public void createCircleRight(double[] coordinates) {
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
    
    public void setImageCenter(Direction dir) {
    	
    	switch(dir) {
		case NORTH:
			this.imageCenter.setLocation(this.center.getX(), this.center.getY() - 25);
			break;
		case EAST:
			this.imageCenter.setLocation(this.center.getX() + 25, this.center.getY());
			break;
		case WEST:
			this.imageCenter.setLocation(this.center.getX() - 25, this.center.getY());
			break;
		case SOUTH:
			this.imageCenter.setLocation(this.center.getX(), this.center.getY() + 25);
			break;
		default:
			break;
		}
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
	
	public int getxVirtual() {
		return this.virtualx;
	}

	public int getyVirtual() {
		return this.virtualy;
	}
	
	public Point2D.Double getObstacleCenter() {
		return this.center;
	}
	
	public Point2D.Double getImageCenter() {
		return this.imageCenter;
	}
	
	public void setDirection(Direction dir) {
		this.direction = dir;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public TurningRadius getTurningRadius() {
		return this.turningRadius;
	}

	public int getLength() { return LENGTH; }
	
	public Rectangle2D getObstacleArea() {
    	return this.obstacleArea;
    }
	
	public void paintObstacle(Graphics g, boolean selector) {
		// Obstacle body
		if (selector) {
			g.setColor(Color.lightGray);
		}
		else {
			g.setColor(Color.blue);
		}

		for (int i = -LENGTH + 1; i <= 0; i++) {
			for (int j = -LENGTH + 1; j <= 0; j++) {
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

		for (int i = -VIRTUAL_LENGTH + 1; i <= 0; i += 2) {
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, virtualy * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy - VIRTUAL_LENGTH + 1) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}

		for (int j = -VIRTUAL_LENGTH + 1; j <= 0; j += 2) {
			g.fillRect(virtualx * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
			g.fillRect((virtualx - VIRTUAL_LENGTH + 1) * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
	}

	// Paint obstacles directly (no selection)
//	public void paintObstacle(Graphics g) {
//		// Obstacle body
//		g.setColor(Color.blue);
//
//		for (int i = -LENGTH + 1; i <= 0; i++) {
//			for (int j = -LENGTH + 1; j <= 0; j++) {
//				g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
//			}
//		}
//		// Obstacle Outline
//		g.setColor(Color.red);
//
//		for (int i = -VIRTUAL_LENGTH + 1; i <= 0; i += 2) {
//			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, virtualy * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
//			g.fillRect((virtualx + i) * Arena.UNIT_SIZE, (virtualy - VIRTUAL_LENGTH + 1) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
//		}
//
//		for (int j = -VIRTUAL_LENGTH + 1; j <= 0; j += 2) {
//			g.fillRect(virtualx * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
//			g.fillRect((virtualx - VIRTUAL_LENGTH + 1) * Arena.UNIT_SIZE, (virtualy + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
//		}
//	}
	
	// Set image direction of an obstacle
	public void selectImage(Graphics g, boolean selector, Direction dir) {
		if (selector) {
			g.setColor(Color.pink);
		}
		else {
			setImage(g, dir);
		}

		switch (dir) {
			case NORTH:
				// Color upper row of pixels
				for (int i = -LENGTH + 1; i <= 0; i++) {
					for (int j = -LENGTH + 1; j <= -8; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case SOUTH:
				// Color bottom pixels purple
				for (int i = -LENGTH + 1; i <= 0; i++) {
					for (int j = -1 ; j <= 0; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case EAST:
				// Color right pixels purple
				for (int i = -1; i <= 0; i++) {
					for (int j = -LENGTH + 1; j <= 0; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case WEST:
				// Color left pixels purple
				for (int i = -LENGTH + 1; i <= -8; i++) {
					for (int j = -LENGTH + 1; j <= 0; j++) {
						g.fillRect((xCoordinate + i) * Arena.UNIT_SIZE, (yCoordinate + j) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
					}
				}
				break;
			case UNSET:
				break;
		}
	}
	
	// Set image given coordinates
	public void setImage(Graphics g, Direction dir) {
		g.setColor(Color.magenta);
		setDirection(dir);
		setImageCenter(dir);
		if (this.getObstacleArea() == null) {
			this.updateObstacleArea(new double[] {this.getObstacleCenter().getX(),this.getObstacleCenter().getY()});
		}
	}
}