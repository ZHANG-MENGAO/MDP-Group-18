package mdp.g18.algo;

import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Graphics;

public class Robot {
	
	final static int radius = 90;
	
	public int x_coor = 0;
	public int y_coor = 0;
	public double dir = PI/2;
	public RobotOrientation orientation = RobotOrientation.N;
		
	ArenaFrame arena;
	
	// Constructor
	Robot(){	
	}
	
	Robot(int x_coor, int y_coor, double dir){
		this.x_coor = x_coor;
		this.y_coor = y_coor;
		this.dir = dir;
	}
	
	public int getX() {
		return x_coor;
	}
	
	public int getY() {
		return y_coor;
	}
	
	public void setX(int x_coor) {
		this.x_coor = x_coor;
	}
	
	public void setY(int y_coor) {
		this.y_coor = y_coor;
	}
	
	public void setOrientaion(RobotOrientation orientation) {
		this.orientation = orientation;
	}
	
	void turnLeft() {
		
	}
	
	void turnRight() {
		
	}
	
	void moveForward() {
		
		int movementDistance = 1;
		if (orientation == RobotOrientation.N)
			y_coor -= movementDistance;
		else if (orientation == RobotOrientation.S)
			y_coor += movementDistance;
		else if (orientation == RobotOrientation.E)
			x_coor += movementDistance;
		else if (orientation == RobotOrientation.W)
			x_coor -= movementDistance;
	}
	
	void reverse() {
		
		int movementDistance = 1;
		if (orientation == RobotOrientation.N)
			y_coor += movementDistance;
		else if (orientation == RobotOrientation.S)
			y_coor -= movementDistance;
		else if (orientation == RobotOrientation.E)
			x_coor -= movementDistance;
		else if (orientation == RobotOrientation.W)
			x_coor += movementDistance;
	}
	
	void reverseLeft() {
			
	}
	
	void reverseRight() {
		
	}
	
	void senseFront() {
		
	}
	
	void senseLeft() {
			
	}
	
	void senseRight() {
		
	}
	
	public void paintRobot(Graphics g) {
		g.setColor(Color.black);
		g.fillArc(x_coor, Arena.ARENA_HEIGHT - radius + y_coor, radius, radius, 0, 360);
		
		g.setColor(Color.yellow);

		int dirOffsetX = 0;
		int dirOffsetY = 0;
		
		if (orientation == RobotOrientation.N)
			dirOffsetY = -30;
		else if (orientation == RobotOrientation.S)
			dirOffsetY = 30;
		else if (orientation == RobotOrientation.W)
			dirOffsetX = -30;
		else if (orientation == RobotOrientation.E)
			dirOffsetX = 30;

		g.fillArc(x_coor + 30 + dirOffsetX, Arena.ARENA_HEIGHT - radius + y_coor + 30 + dirOffsetY, 30, 30, 0, 360);
	}
}
