package mdp.g18.algo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class Robot {
	
	private Point2D.Double center = new Point2D.Double();
    private static final double DEG_TO_RAD = Math.PI / 180;
    public TurningRadius turningRadius;

	public static final int ROBOT_SIZE = 30;
	
	private int tick;
	private int angle; // rotate angle
	private RobotOrientation orientation;

	Sensor sensor;
	RobotImage robotimage;
	
	// Constructor
	Robot(int x, int y, int angle){	
		setAngle(angle); // set angle
		setOrientation(angle);  // set orientation
		this.center.setLocation(x, y);
		//createCircleLeft(new int[] {x,y},"front");
		robotimage = new RobotImage(x,y,this.angle);
		sensor = new Sensor(x,y);
	}

	public void tick(){
		moveForward();
    }
	
    public void createCircleLeft(int[] coordinates, String movement) {
    	
    	int angle = this.angle;
    	if (movement == "front") {
    		setTick(-angle);
    	}
    	else {
    		setTick(angle);
    	}
    	
    	double x = coordinates[0] - TurningRadius.getRadius() * Math.cos(angle * DEG_TO_RAD);
    	double y = coordinates[1] - TurningRadius.getRadius() * Math.sin(angle * DEG_TO_RAD);
    	this.turningRadius = new TurningRadius(new Point2D.Double(x, y));
	}
    
    public void createCircleRight(int[] coordinates, String movement) {
    	
    	int angle = this.angle;
    	if (movement == "reverse") {
    		setTick(-angle);
    	}
    	else {
    		setTick(angle);
    	}
    	
    	double x = coordinates[0] + TurningRadius.getRadius() * Math.cos(angle * DEG_TO_RAD);
    	double y = coordinates[1] + TurningRadius.getRadius() * Math.sin(angle * DEG_TO_RAD);
    	this.turningRadius = new TurningRadius(new Point2D.Double(x, y));
	}
    
    public void turnLeft(){
    	this.tick += 1;
        final double xCenter = this.turningRadius.getCenter().getX(); // x center of turning radius
        final double yCenter = this.turningRadius.getCenter().getY(); // y center of turning radius
        final double rad = TurningRadius.getRadius(); // radius of robot
        this.center.setLocation(xCenter + rad * Math.cos(this.tick * DEG_TO_RAD),
                      yCenter + rad * Math.sin(-this.tick * DEG_TO_RAD));
        setRobotCenter(this.center);
        
        this.angle = (int) calculateAngle(this.center,xCenter - rad, yCenter);
        this.setOrientation(this.angle);
        
        //System.out.println(this.tick);
        //System.out.println(this.center);
        //System.out.println(this.angle);
    }

	public void reverseLeft(){
		this.tick += 1;
        final double xCenter = turningRadius.getCenter().getX(); // x center of turning radius
        final double yCenter = turningRadius.getCenter().getY(); // y center of turning radius
        final double rad = TurningRadius.getRadius(); // radius of robot
        this.center.setLocation(xCenter + rad * Math.cos(this.tick  * DEG_TO_RAD),
                      yCenter + rad * Math.sin(this.tick * DEG_TO_RAD));
        setRobotCenter(this.center);
        this.angle = (int) calculateAngle(this.center,xCenter - rad, yCenter);
        this.setOrientation(this.angle);

        //System.out.println(this.center);
        //System.out.println(this.angle);
    }

    public void turnRight(){
    	this.tick+=1;
        final double xCenter = this.turningRadius.getCenter().getX(); // x center of turning radius
        final double yCenter = this.turningRadius.getCenter().getY(); // y center of turning radius
        final double rad = TurningRadius.getRadius(); // radius of robot
        this.center.setLocation(xCenter - rad * Math.cos(this.tick * DEG_TO_RAD),
                      yCenter - rad * Math.sin(this.tick * DEG_TO_RAD));
        setRobotCenter(this.center);
        this.angle = (int) calculateAngle(this.center,xCenter + rad, yCenter);
        this.setOrientation(this.angle);
        //System.out.println(this.center);
        //System.out.println(this.angle);
    }
	
    // need reverse tick
    public void reverseRight(){
    	this.tick += 1;
    	final double xCenter = this.turningRadius.getCenter().getX(); // x center of turning radius
        final double yCenter = this.turningRadius.getCenter().getY(); // y center of turning radius
        final double rad = TurningRadius.getRadius(); // radius of robot
        this.center.setLocation(xCenter - rad * Math.cos(this.tick * DEG_TO_RAD),
                      yCenter - rad * Math.sin(-this.tick * DEG_TO_RAD));
        setRobotCenter(this.center);
        this.angle = (int) calculateAngle(this.center,xCenter + rad, yCenter);
        this.setOrientation(this.angle);
        
        //System.out.println(this.center);
        //System.out.println(this.angle);
    }
    
    public void moveForward() {
		int speed = 1;
		double dx = this.center.getX() + speed * Math.cos(Math.PI/2 - this.angle * DEG_TO_RAD);
		double dy = this.center.getY() - speed * Math.sin(Math.PI/2 - this.angle * DEG_TO_RAD);
		this.center.setLocation(dx,dy);
		setRobotCenter(this.center);
	}
    
    public void reverseBackward() {
		int speed = 1;
		double dx = this.center.getX() + speed * Math.cos(this.angle * DEG_TO_RAD + Math.PI/2);
		double dy = this.center.getY() + speed * Math.sin(this.angle * DEG_TO_RAD + Math.PI/2);
		this.center.setLocation(dx,dy);
		setRobotCenter(this.center);
	}
    
    public boolean checkBoundaries() {
    	// center of robot within the grid
    	if ((Math.max(15, this.center.getX()) == Math.min(this.center.getX(), 185)) && (Math.max(-184, this.center.getY()) == Math.min(this.center.getY(), -16))) {
    		return true;
    	}
    	return false;
    }
    
    // problem
    public boolean canReachStraight(int[] destination) {
    	int speed = 1;
    	double dx = this.center.getX();
    	double dy = this.center.getY();
    	
    	while(checkBoundaries()) {
    		dx = dx + speed * Math.cos(this.angle * DEG_TO_RAD + Math.PI/2);
    		dy = dy + speed * Math.sin(this.angle * DEG_TO_RAD + Math.PI/2);
    		
    		if (round(dx) == destination[0] && round(dy) == destination[1]) {
    			return true; // reach destination
    		}
    	}
    	
    	return false;
    }
    
    public boolean canReachTurn(int[] destination) {
    	
    	// true if robot can reach obstacle by turning left or right
    	if (this.turningRadius.getCenter().getX() == destination[0] && this.turningRadius.getCenter().getY() == destination[1]) {
    		return true;
    	}
    	return false;
    }
    
    public boolean canReachTurnTurn(double distance) {
    	
    	// true if robot can reach obstacle by turning lr or rl
    	//circle touch if distance between center is 2 * radius
    	if (distance == 2 * TurningRadius.getRadius()) {
    		return true;
    	}
    	return false;
    }
    
    private static int round(double val) {
        return (int) Math.round(val);
    }
    
    
    public void drawRobot(Graphics g){
    	robotimage.drawRobot(g, this.center, this.angle);
	}
	
	public double calculateAngle(Point2D.Double p1, double centerX, double centerY){
    	//p0(x,y) = (center.x, center.y - radius)
    	//radius = 25
    	//angle = atan2(p1.y - p0.y, p1.x - p0.x)
    	Point2D.Double p0 = new Point2D.Double();
    	p0.setLocation(centerX, centerY);
    	return (2 * Math.atan2(p1.getY() - p0.getY(), p1.getX() - p0.getX())) / DEG_TO_RAD;
    }
	
    public void setTick(int angle) {
		this.tick = angle;
	}
	
    public void setRobotCenter(Point2D.Double c){
        center = c;
    }
    
    public Point2D.Double getRobotCenter(){
        return this.center;
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

	public int[] getCoord() {
		return new int[] {this.x_coor, this.y_coor};
	}
	
	public int getAngle() {
		return this.angle;
	}
	
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public RobotOrientation getOrientation() {
		return this.orientation;
	}

	public void setOrientation(int angle) {
		switch(angle) {
		case 0:
			this.orientation = RobotOrientation.N;
			break;
		case 90:
			this.orientation = RobotOrientation.W;
			break;
		case -180:
			this.orientation = RobotOrientation.S;
			break;
		case -90:
			this.orientation = RobotOrientation.E;
			break;
		case 360:
			this.orientation = RobotOrientation.N;
			break;
		case 270:
			this.orientation = RobotOrientation.W;
			break;
		case -270:
			this.orientation = RobotOrientation.E;
			break;
		default:
			break;
		}
	}
	

	
	/*
	public void turnLeft() {
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
	
	public void reverseLeft() {
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
	
	public void turnRight() {
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
	
	public void reverseRight() {
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
		
	public void moveForward() {
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
	
	public void reverse() {
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
		
	}*/
}