package mdp.g18.algo;

import java.awt.Color;
import java.awt.Graphics;

public class Robot {
	
	private static final int ROBOT_SIZE = 30;
	private final static int radius = ROBOT_SIZE * Arena.UNIT_SIZE;
	
	private int x_coor;
	private int y_coor;
	private RobotOrientation orientation;
		
	ArenaFrame arena;
	Sensor sensor;
	
	// Constructor
	Robot(){	
		this.x_coor = 0;
		this.y_coor = 0;
		this.orientation = RobotOrientation.N;
		sensor = new Sensor(this.x_coor,this.y_coor);
	}
	
	public int getRobotSize() {
		return ROBOT_SIZE;
	}
	
	public int getSensorX() {
		return this.sensor.getSensorX();
	}
	
	public int getSensorY() {
		return this.sensor.getSensorY();
	}
	
	public int getX() {
		return this.x_coor;
	}
	
	public int getY() {
		return this.y_coor;
	}
	
	public RobotOrientation getOrientaion() {
		return this.orientation;
	}
	
	public void setX(int x_coor) {
		this.x_coor = x_coor;
	}
	
	public void setY(int y_coor) {
		this.y_coor = y_coor;
	}
	
	public void setOrientation(RobotOrientation orientation) {
		this.orientation = orientation;
	}
	
	public void updateSensor() {
		sensor.updateSensorDirection(this.orientation, this.x_coor, this.y_coor);
	}
	
	void turnLeft() {
		switch(orientation){
		case N:
			setOrientation(RobotOrientation.NW1);
			setX(getX() + 10);
			setY(getY() - 8);
			break;
		case W: // 5 -25
			setOrientation(RobotOrientation.SW3);
			setX(getX() - 8);
			setY(getY() - 10);
			break;
		case S: // -20 -30
			setOrientation(RobotOrientation.SE1);
			setX(getX() - 10);
			setY(getY() + 8);
			break;
		case E: // -25 -5
			setOrientation(RobotOrientation.NE3);
			setX(getX() + 8);
			setY(getY() + 10);
			break;
		case NW1: // 10 -8
			setOrientation(RobotOrientation.NW2);
			setX(getX() + 5);
			setY(getY() - 5);
			break;
		case NW2: // 15 -13
			setOrientation(RobotOrientation.NW3);
			setX(getX() - 9);
			setY(getY() - 8);
			break;
		case NW3: // 6 -21
			setOrientation(RobotOrientation.W);
			setX(getX() - 1);
			setY(getY() - 4);
			break;
		case SW1: // -16 -31
			setOrientation(RobotOrientation.S);
			setX(getX() - 4);
			setY(getY() + 1);
			break;
		case SW2: // -8 -40
			setOrientation(RobotOrientation.SW1);
			setX(getX() - 8);
			setY(getY() + 9);
			break;
		case SW3: // -3 -35
			setOrientation(RobotOrientation.SW2);
			setX(getX() - 4);
			setY(getY() - 5);
			break;
		case SE1: //-30 -22
			setOrientation(RobotOrientation.SE2);
			setX(getX() - 5);
			setY(getY() + 5);
			break;
		case SE2: // -35 -17
			setOrientation(RobotOrientation.SE3);
			setX(getX() + 9);
			setY(getY() + 8);
			break;
		case SE3: // -26 -9
			setOrientation(RobotOrientation.E);
			setX(getX() + 1);
			setY(getY() + 4);
			break;
		case NE1: // -4 1
			setOrientation(RobotOrientation.N);
			setX(getX() + 4);
			setY(getY() - 1);
			break;
		case NE2: // -12 10
			setOrientation(RobotOrientation.NE1);
			setX(getX() + 8);
			setY(getY() - 9);
			break;
		case NE3: // -17 5
			setOrientation(RobotOrientation.NE2);
			setX(getX() + 5);
			setY(getY() + 5);
			break;
		default:
			break;
		}
	}
	
	void reverseLeft() {
		switch(orientation){
		case N: // 0 0
			setOrientation(RobotOrientation.NE1);
			setX(getX() - 4);
			setY(getY() + 1);
			break;
		case W: // 5 -25
			setOrientation(RobotOrientation.NW3);
			setX(getX() + 1);
			setY(getY() + 4);
			break;
		case S: // -20 -30
			setOrientation(RobotOrientation.SW1);
			setX(getX() + 4);
			setY(getY() - 1);
			break;
		case E: // -25 -5
			setOrientation(RobotOrientation.SE3);
			setX(getX() - 1);
			setY(getY() - 4);
			break;
		case NW1: // 10 -8
			setOrientation(RobotOrientation.N);
			setX(getX() - 10);
			setY(getY() + 8);
			break;
		case NW2: // 15 -13
			setOrientation(RobotOrientation.NW1);
			setX(getX() - 5);
			setY(getY() + 5);
			break;
		case NW3: // 6 -21
			setOrientation(RobotOrientation.NW2);
			setX(getX() + 9);
			setY(getY() + 8);
			break;
		case SW1: // -16 -31
			setOrientation(RobotOrientation.SW2);
			setX(getX() + 8);
			setY(getY() - 9);
			break;
		case SW2: // -8 -40
			setOrientation(RobotOrientation.SW3);
			setX(getX() + 5);
			setY(getY() + 5);
			break;
		case SW3: // -3 -35
			setOrientation(RobotOrientation.W);
			setX(getX() + 8);
			setY(getY() + 10);
			break;
		case SE1: //-30 -22
			setOrientation(RobotOrientation.S);
			setX(getX() + 10);
			setY(getY() - 8);
			break;
		case SE2: // -35 -17
			setOrientation(RobotOrientation.SE1);
			setX(getX() + 5);
			setY(getY() - 5);
			break;
		case SE3: // -26 -9
			setOrientation(RobotOrientation.SE2);
			setX(getX() - 9);
			setY(getY() - 8);
			break;
		case NE1: // -4 1
			setOrientation(RobotOrientation.NE2);
			setX(getX() - 8);
			setY(getY() + 9);
			break;
		case NE2: // -12 10
			setOrientation(RobotOrientation.NE3);
			setX(getX() - 5);
			setY(getY() - 5);
			break;
		case NE3: // -17 5
			setOrientation(RobotOrientation.E);
			setX(getX() - 8);
			setY(getY() - 10);
			break;
		default:
			break;
		}
	}
	
	void turnRight() {
		switch(orientation){
			case N:
				setOrientation(RobotOrientation.NE1);
				setX(getX() - 3);
				setY(getY() - 12);
				break;
			case E:
				setOrientation(RobotOrientation.SE3);
				setX(getX() + 12);
				setY(getY() - 3);
				break;
			case S:
				setOrientation(RobotOrientation.SW1);
				setX(getX() + 3);
				setY(getY() + 12);
				break;
			case W:
				setOrientation(RobotOrientation.NW3);
				setX(getX() - 12);
				setY(getY() + 3);
				break;
			case NE1:
				setOrientation(RobotOrientation.NE2);
				setX(getX() + 2);
				setY(getY() - 13);
				break;
			case NE2:
				setOrientation(RobotOrientation.NE3);
				setX(getX() + 9);
				setY(getY() - 13);
				break;
			case NE3:
				setOrientation(RobotOrientation.E);
				setX(getX() + 17);
				setY(getY() - 17);
				break;
			case SE1:
				setOrientation(RobotOrientation.S);
				setX(getX() + 17);
				setY(getY() + 17);
				break;
			case SE2:
				setOrientation(RobotOrientation.SE1);
				setX(getX() + 13);
				setY(getY() + 9);
				break;
			case SE3:
				setOrientation(RobotOrientation.SE2);
				setX(getX() + 13);
				setY(getY() + 2);
				break;
			case NW1:
				setOrientation(RobotOrientation.N);
				setX(getX() - 17);
				setY(getY() - 17);
				break;
			case NW2:
				setOrientation(RobotOrientation.NW1);
				setX(getX() - 13);
				setY(getY() - 9);
				break;
			case NW3:
				setOrientation(RobotOrientation.NW2);
				setX(getX() - 13);
				setY(getY() - 2);
				break;
			case SW1:
				setOrientation(RobotOrientation.SW2);
				setX(getX() - 2);
				setY(getY() + 13);
				break;
			case SW2:
				setOrientation(RobotOrientation.SW3);
				setX(getX() - 9);
				setY(getY() + 13);
				break;
			case SW3:
				setOrientation(RobotOrientation.W);
				setX(getX() - 17);
				setY(getY() + 17);
				break;
			default:
				break;
		}
	}
	
	void reverseRight() {
		switch(orientation){
		case N:
			setOrientation(RobotOrientation.NW1);
			setX(getX() + 17);
			setY(getY() + 17);
			break;
		case E:
			setOrientation(RobotOrientation.NE3);
			setX(getX() - 17);
			setY(getY() + 17);
			break;
		case S:
			setOrientation(RobotOrientation.SE1);
			setX(getX() - 17);
			setY(getY() - 17);
			break;
		case W:
			setOrientation(RobotOrientation.SW3);
			setX(getX() + 17);
			setY(getY() - 17);
			break;
		case NE1:
			setOrientation(RobotOrientation.N);
			setX(getX() + 3);
			setY(getY() + 12);
			break;
		case NE2:
			setOrientation(RobotOrientation.NE1);
			setX(getX() - 2);
			setY(getY() + 13);
			break;
		case NE3:
			setOrientation(RobotOrientation.NE2);
			setX(getX() - 9);
			setY(getY() + 13);
			break;
		case SE1:
			setOrientation(RobotOrientation.SE2);
			setX(getX() - 13);
			setY(getY() - 9);
			break;
		case SE2:
			setOrientation(RobotOrientation.SE3);
			setX(getX() - 13);
			setY(getY() - 2);
			break;
		case SE3:
			setOrientation(RobotOrientation.E);
			setX(getX() - 12);
			setY(getY() + 3);
			break;
		case NW1:
			setOrientation(RobotOrientation.NW2);
			setX(getX() + 13);
			setY(getY() + 9);
			break;
		case NW2:
			setOrientation(RobotOrientation.NW3);
			setX(getX() + 13);
			setY(getY() + 2);
			break;
		case NW3:
			setOrientation(RobotOrientation.W);
			setX(getX() + 12);
			setY(getY() - 3);
			break;
		case SW1:
			setOrientation(RobotOrientation.S);
			setX(getX() - 3);
			setY(getY() - 12);
			break;
		case SW2:
			setOrientation(RobotOrientation.SW1);
			setX(getX() + 2);
			setY(getY() - 13);
			break;
		case SW3:
			setOrientation(RobotOrientation.SW2);
			setX(getX() + 9);
			setY(getY() - 13);
			break;
		default:
			break;
		}
	}
		
	void moveForward() {
		switch(orientation){
			case N:
				this.y_coor -= 1;
				break;
			case S:
				this.y_coor += 1;
				break;
			case E:
				this.x_coor += 1;
				break;
			case W:
				this.x_coor -= 1;
				break;
			case NE1:
				this.y_coor -= 1;
				this.x_coor += 1;
				break;
			case NE2:
				this.y_coor -= 1;
				this.x_coor += 2;
				break;
			case NE3:
				this.y_coor -= 1;
				this.x_coor += 3;
				break;
			case SE1:
				this.y_coor += 1;
				this.x_coor += 1;
				break;
			case SE2:
				this.y_coor += 1;
				this.x_coor += 2;
				break;
			case SE3:
				this.y_coor += 1;
				this.x_coor += 3;
				break;
			case NW1:
				this.y_coor -= 1;
				this.x_coor -= 1;
				break;
			case NW2:
				this.y_coor -= 1;
				this.x_coor -= 2;
				break;
			case NW3:
				this.y_coor -= 1;
				this.x_coor -= 3;
				break;
			case SW1:
				this.y_coor += 1;
				this.x_coor -= 1;
				break;
			case SW2:
				this.y_coor += 1;
				this.x_coor -= 2;
				break;
			case SW3:
				this.y_coor += 1;
				this.x_coor -= 3;
				break;
			default:
				break;
		}
	}
	
	void reverse() {
		switch(orientation){
			case N:
				this.y_coor += 1;
				break;
			case S:
				this.y_coor -= 1;
				break;
			case E:
				this.x_coor -= 1;
				break;
			case W:
				this.x_coor += 1;
				break;
			case NE1:
				this.y_coor += 1;
				this.x_coor -= 1;
				break;
			case NE2:
				this.y_coor += 1;
				this.x_coor -= 2;
				break;
			case NE3:
				this.y_coor += 1;
				this.x_coor -= 3;
				break;
			case SE1:
				this.y_coor -= 1;
				this.x_coor -= 1;
				break;
			case SE2:
				this.y_coor -= 1;
				this.x_coor -= 2;
				break;
			case SE3:
				this.y_coor -= 1;
				this.x_coor -= 3;
				break;
			case NW1:
				this.y_coor += 1;
				this.x_coor += 1;
				break;
			case NW2:
				this.y_coor += 1;
				this.x_coor += 2;
				break;
			case NW3:
				this.y_coor += 1;
				this.x_coor += 3;
				break;
			case SW1:
				this.y_coor -= 1;
				this.x_coor += 1;
				break;
			case SW2:
				this.y_coor -= 1;
				this.x_coor += 2;
				break;
			case SW3:
				this.y_coor -= 1;
				this.x_coor += 3;
				break;
			default:
				break;
		}
	}

	void senseFront() {
		
	}
	
	void senseLeft() {
			
	}
	
	void senseRight() {
		
	}
	
	public void paintRobot(Graphics g) {
		
		int dirOffsetX = 0;
		int dirOffsetY = 0;
		int x_position = 0;
		int y_position = 0;
		
		switch(orientation){
			case N:
				x_position = getX() * Arena.UNIT_SIZE;
				y_position = Arena.ARENA_HEIGHT - radius + getY() * Arena.UNIT_SIZE;
				dirOffsetY = -30;
				break;
			case NE1:
				x_position = (getX() + 2) * Arena.UNIT_SIZE;
				y_position = Arena.ARENA_HEIGHT - radius + (getY() + 8) * Arena.UNIT_SIZE;
				dirOffsetY = -27;
				dirOffsetX = 10;
				break;
			case NE2:
				x_position = (getX() + 2) * Arena.UNIT_SIZE;
				y_position = Arena.ARENA_HEIGHT - radius + (getY() + 7) * Arena.UNIT_SIZE;
				dirOffsetY = -24;
				dirOffsetX = 18;
				break;
			case NE3:
				x_position = (getX() + 8) * Arena.UNIT_SIZE;
				y_position = Arena.ARENA_HEIGHT - radius + (getY() + 15) * Arena.UNIT_SIZE;
				dirOffsetY = -15;
				dirOffsetX = 25;
				break;
			case E:
				x_position = getX() * Arena.UNIT_SIZE;
				y_position = Arena.ARENA_HEIGHT + getY() * Arena.UNIT_SIZE;
				dirOffsetX = 30;
				break;
			case SE1:
				x_position = (getX() - 10) * Arena.UNIT_SIZE;
				y_position = Arena.ARENA_HEIGHT + (getY() + 10) * Arena.UNIT_SIZE;
				dirOffsetY = 25;
				dirOffsetX = 18;
				break;
			case SE2:
				x_position = (getX() - 2) * Arena.UNIT_SIZE;
				y_position = Arena.ARENA_HEIGHT + (getY() + 12) * Arena.UNIT_SIZE;
				dirOffsetY = 18;
				dirOffsetX = 24;
				break;
			case SE3:
				x_position = getX() * Arena.UNIT_SIZE;
				y_position = Arena.ARENA_HEIGHT + (getY() + 5) * Arena.UNIT_SIZE;
				dirOffsetY = 10;
				dirOffsetX = 28;
				break;
			case S:
				x_position = getX() * Arena.UNIT_SIZE - radius;
				y_position = Arena.ARENA_HEIGHT + getY() * Arena.UNIT_SIZE;
				dirOffsetY = 30;
				break;	
			case SW1:
				x_position = (getX() - 5) * Arena.UNIT_SIZE - radius;
				y_position = Arena.ARENA_HEIGHT + getY() * Arena.UNIT_SIZE;
				dirOffsetY = 25;
				dirOffsetX = -18;
				break;
			case SW2:
				x_position = (getX() - 8) * Arena.UNIT_SIZE - radius;
				y_position = Arena.ARENA_HEIGHT + (getY() - 8) * Arena.UNIT_SIZE;
				dirOffsetY = 18;
				dirOffsetX = -24;
				break;
			case SW3:
				x_position = (getX() - 5) * Arena.UNIT_SIZE - radius;
				y_position = Arena.ARENA_HEIGHT + (getY() - 15) * Arena.UNIT_SIZE;
				dirOffsetY = 14;
				dirOffsetX = -26;
				break;
			case W:
				x_position = getX() * Arena.UNIT_SIZE - radius;
				y_position = Arena.ARENA_HEIGHT - radius + getY() * Arena.UNIT_SIZE;
				dirOffsetX = -30;
				break;
			case NW1:
				x_position = (getX() + 15) * Arena.UNIT_SIZE - radius;
				y_position = Arena.ARENA_HEIGHT - radius + (getY() - 3) * Arena.UNIT_SIZE;
				dirOffsetY = -27;
				dirOffsetX = -10;
				break;
			case NW2:
				x_position = (getX() + 5) * Arena.UNIT_SIZE - radius;
				y_position = Arena.ARENA_HEIGHT - radius + (getY() - 5) * Arena.UNIT_SIZE;
				dirOffsetY = -24;
				dirOffsetX = -18;
				break;
			case NW3:
				x_position = (getX() + 8) * Arena.UNIT_SIZE - radius;
				y_position = Arena.ARENA_HEIGHT - radius + (getY() - 2) * Arena.UNIT_SIZE;
				dirOffsetY = -15;
				dirOffsetX = -25;
				break;
			default:
				break;
		}
		g.setColor(Color.black);
		g.fillArc(x_position, y_position, radius, radius, 0, 360);
		g.setColor(Color.yellow);
		g.fillArc(x_position + ROBOT_SIZE + dirOffsetX, y_position + ROBOT_SIZE + dirOffsetY, ROBOT_SIZE, ROBOT_SIZE, 0, 360);
	}
}
