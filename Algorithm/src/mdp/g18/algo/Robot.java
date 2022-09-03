package mdp.g18.algo;

public class Robot {
	
	public static final int ROBOT_SIZE = 30;
	
	private int x_coor;
	private int y_coor;
	private RobotOrientation orientation;

	Sensor sensor;
	RobotImage robotimage;
	
	// Constructor
	Robot(){	
		this.x_coor = 0;
		this.y_coor = 0;
		this.orientation = RobotOrientation.N;
		robotimage = new RobotImage(this.x_coor,this.y_coor,this.orientation);
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
	
	public RobotOrientation getOrientation() {
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
			setX(getX() + 4);
			setY(getY() - 2);
			break;
		case W:
			setOrientation(RobotOrientation.SW3);
			setX(getX() - 2);
			setY(getY() - 4);
			break;
		case S:
			setOrientation(RobotOrientation.SE1);
			setX(getX() - 4);
			setY(getY() + 2);
			break;
		case E:
			setOrientation(RobotOrientation.NE3);
			setX(getX() + 2);
			setY(getY() + 4);
			break;
		case NW1:
			setOrientation(RobotOrientation.NW2);
			setX(getX() + 3);
			setY(getY() - 4);
			break;
		case NW2: 
			setOrientation(RobotOrientation.NW3);
			setX(getX() - 3);
			setY(getY() - 7);
			break;
		case NW3:
			setOrientation(RobotOrientation.W);
			setX(getX() + 1);
			setY(getY() - 12);
			break;
		case SW1:
			setOrientation(RobotOrientation.S);
			setX(getX() - 12);
			setY(getY() - 1);
			break;
		case SW2:
			setOrientation(RobotOrientation.SW1);
			setX(getX() - 7);
			setY(getY() + 3);
			break;
		case SW3:
			setOrientation(RobotOrientation.SW2);
			setX(getX() - 4);
			setY(getY() - 3);
			break;
		case SE1:
			setOrientation(RobotOrientation.SE2);
			setX(getX() - 3);
			setY(getY() + 4);
			break;
		case SE2:
			setOrientation(RobotOrientation.SE3);
			setX(getX() + 3);
			setY(getY() + 7);
			break;
		case SE3:
			setOrientation(RobotOrientation.E);
			setX(getX() - 1);
			setY(getY() + 12);
			break;
		case NE1:
			setOrientation(RobotOrientation.N);
			setX(getX() + 12);
			setY(getY() + 1);
			break;
		case NE2:
			setOrientation(RobotOrientation.NE1);
			setX(getX() + 7);
			setY(getY() - 3);
			break;
		case NE3:
			setOrientation(RobotOrientation.NE2);
			setX(getX() + 4);
			setY(getY() + 3);
			break;
		default:
			break;
		}
		
		robotimage.setOrientation(getOrientation());
		robotimage.setX(getX());
		robotimage.setY(getY());
	}
	
	void reverseLeft() {
		switch(orientation){
		case N: 
			setOrientation(RobotOrientation.NE1);
			setX(getX() - 12);
			setY(getY() - 1);
			break;
		case W:
			setOrientation(RobotOrientation.NW3);
			setX(getX() - 1);
			setY(getY() + 12);
			break;
		case S:
			setOrientation(RobotOrientation.SW1);
			setX(getX() + 12);
			setY(getY() + 1);
			break;
		case E:
			setOrientation(RobotOrientation.SE3);
			setX(getX() + 1);
			setY(getY() - 12);
			break;
		case NW1:
			setOrientation(RobotOrientation.N);
			setX(getX() - 4);
			setY(getY() + 2);
			break;
		case NW2:
			setOrientation(RobotOrientation.NW1);
			setX(getX() - 3);
			setY(getY() + 4);
			break;
		case NW3:
			setOrientation(RobotOrientation.NW2);
			setX(getX() + 3);
			setY(getY() + 7);
			break;
		case SW1:
			setOrientation(RobotOrientation.SW2);
			setX(getX() + 7);
			setY(getY() - 3);
			break;
		case SW2:
			setOrientation(RobotOrientation.SW3);
			setX(getX() + 4);
			setY(getY() + 3);
			break;
		case SW3:
			setOrientation(RobotOrientation.W);
			setX(getX() + 2);
			setY(getY() + 4);
			break;
		case SE1:
			setOrientation(RobotOrientation.S);
			setX(getX() + 4);
			setY(getY() - 2);
			break;
		case SE2:
			setOrientation(RobotOrientation.SE1);
			setX(getX() + 3);
			setY(getY() - 4);
			break;
		case SE3:
			setOrientation(RobotOrientation.SE2);
			setX(getX() - 3);
			setY(getY() - 7);
			break;
		case NE1:
			setOrientation(RobotOrientation.NE2);
			setX(getX() - 7);
			setY(getY() + 3);
			break;
		case NE2:
			setOrientation(RobotOrientation.NE3);
			setX(getX() - 4);
			setY(getY() - 3);
			break;
		case NE3:
			setOrientation(RobotOrientation.E);
			setX(getX() - 2);
			setY(getY() - 4);
			break;
		default:
			break;
		}
		
		robotimage.setOrientation(getOrientation());
		robotimage.setX(getX());
		robotimage.setY(getY());
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
		
		robotimage.setOrientation(getOrientation());
		robotimage.setX(getX());
		robotimage.setY(getY());
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
		
		robotimage.setOrientation(getOrientation());
		robotimage.setX(getX());
		robotimage.setY(getY());
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
		
		robotimage.setX(getX());
		robotimage.setY(getY());
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
		
		robotimage.setX(getX());
		robotimage.setY(getY());
	}

	void senseFront() {
		
	}
	
	void senseLeft() {
			
	}
	
	void senseRight() {
		
	}
	
}