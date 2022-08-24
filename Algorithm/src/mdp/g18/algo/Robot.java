package mdp.g18.algo;

import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Graphics;

public class Robot {
	
	final static int radius = 90;
	
	public int x_coor = 0;
	public int y_coor = 0;
	public double dir = PI/2;
	public Direction orientation = Direction.NORTH;
	
	//ArenaFrame arena;
	
	// Constructor
	Robot(){
		
	}
	
	Robot(int x_coor, int y_coor, double dir){
		this.x_coor = x_coor;
		this.y_coor = y_coor;
		this.dir = dir;
	}
	
	void turnLeft() {
		
	}
	
	void turnRight() {
		
	}
	
	void moveForward() {
		
	}
	
	void reverse() {
		
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
	}
}
