package mdp.g18.algo;

import java.awt.geom.Point2D;

public abstract class Robot {
	
	private Point2D.Double center = new Point2D.Double(); // robot Center
	private Point2D.Double centerFront = new Point2D.Double(); // robot Front
    protected final double DEG_TO_RAD = Math.PI / 180; // conversion to degree
    public TurningRadius turningRadius;

	public static final int ROBOT_SIZE = 30;
	
	protected double tick;
	private double angle; // rotate angle
	
	protected Sensor sensor;
	
	// Constructor
	Robot(int x, int y, int angle){
		this.center.setLocation(x, y); // set center location
		setAngle(angle); // set angle
		setCenterFront(angle);
		sensor = new Sensor(new double[] {getRobotCenter().getX(),getRobotCenter().getY()}, getAngle());
	}
	
	// Create only when turning left
    public void createCircleLeft(double[] coordinates, String movement) {
    	
    	double angle = getAngle();
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
    
    // create only when turning right
    public void createCircleRight(double[] coordinates, String movement) {
    	
    	double angle = getAngle();
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
    
    public boolean checkBoundaries() {
    	// center of robot within the grid
    	if ((Math.max(15, this.center.getX()) == Math.min(this.center.getX(), 185)) && (Math.max(-184, this.center.getY()) == Math.min(this.center.getY(), -16))) {
    		return false;
    	}
    	return true;
    }
    
    public boolean checkObstacleFront(Obstacle obstacle) {
    	
    	// Up
    	if (this.angle == 0 && obstacle.getDirection() == Direction.SOUTH && (obstacle.getObstacleCenter().getY() < this.center.getY() - 15) && (Math.max(this.center.getX() - 15, obstacle.getObstacleCenter().getX()) == Math.min(obstacle.getObstacleCenter().getX(), this.center.getX() + 15))) {
    		return true;
    	} 
    	//Down
    	else if (this.angle == 180 && obstacle.getDirection() == Direction.NORTH && (obstacle.getObstacleCenter().getY() > this.center.getY() + 15) && (Math.max(this.center.getX() - 15, obstacle.getObstacleCenter().getX()) == Math.min(obstacle.getObstacleCenter().getX(), this.center.getX() + 15))) {
    		return true;
    	}
    	
    	//Left
    	else if (this.angle == -90 && obstacle.getDirection() == Direction.EAST && (obstacle.getObstacleCenter().getX() < this.center.getX() - 15) && (Math.max(this.center.getY() - 15, obstacle.getObstacleCenter().getY()) == Math.min(obstacle.getObstacleCenter().getY(), this.center.getY() + 15))) {
    		return true;
    	}
    	
    	//Right
    	else if (this.angle == 90 && obstacle.getDirection() == Direction.WEST && (obstacle.getObstacleCenter().getX() > this.center.getX() + 15) && (Math.max(this.center.getY() - 15, obstacle.getObstacleCenter().getY()) == Math.min(obstacle.getObstacleCenter().getY(), this.center.getY() + 15))) {
    		return true;
    	}

    	return false;
    }
    
    // problem
    public boolean canReachStraightPoint(double[] destination) {
    	
    	// Up
    	if (this.angle == 0 && (this.getRobotCenter().getX() == destination[0])) {
    		return true;
    	} 
    	
    	//Down
    	else if (this.angle == 180 && (this.getRobotCenter().getX() == destination[0])) {
    		return true;
    	}
    	
    	//Left
    	else if (this.angle == -90 && (this.getRobotCenter().getY() == destination[1])) {
    		
    	}
    	
    	//Right
    	else if (this.angle == 90 && (this.getRobotCenter().getY() == destination[1])) {
    		
    	}

    	return false;
    }
    
    public boolean canReachTurn(double[] destination) {
    	
    	// true if robot can reach obstacle by turning left or right
    	if (this.turningRadius.getCenter().getX() == destination[0] && this.turningRadius.getCenter().getY() == destination[1]) {
    		return true;
    	}
    	return false;
    }
    
    public boolean canReachTurnTurn(double distance) {
    	
    	// true if robot can reach obstacle by turning lr or rl
    	//circle touch if distance between center is 2 * radius
    	if ((Math.max(2 * TurningRadius.getRadius() - 1, distance) == Math.min(distance, 2 * TurningRadius.getRadius() + 1))) {
    		return true;
    	}
    	return false;
    }
	
    /*
	public double calculateAngle(Point2D.Double p1, double centerX, double centerY){
    	//p0(x,y) = (center.x, center.y - radius)
    	//radius = 25
    	//angle = atan2(p1.y - p0.y, p1.x - p0.x)
    	Point2D.Double p0 = new Point2D.Double();
    	p0.setLocation(centerX, centerY);
    	return (2 * Math.atan2(p1.getY() - p0.getY(), p1.getX() - p0.getX())) / DEG_TO_RAD;
    }*/
	
	public double computeAngle(double[] p, double[] p1, double[] pt1, String direction) {
		
		double[] v1 = new double[2];
		double[] v2 = new double[2];
		double angle;
		
		v1[0] = p[0] - p1[0];
		v1[1] = (-p[1])  - (-p1[1]);
		v2[0] = pt1[0] - p1[0];
		v2[1] = (-pt1[1])  - (-p1[1]);

		angle = Math.atan2(v2[1], v2[0]) - Math.atan2(v1[1], v1[0]);
		
		if (angle < 0 && direction == "L") {
			angle += 2 * Math.PI;
		} else if (angle > 0 && direction == "R"){
			angle -= 2 * Math.PI;
		}

		return -angle / DEG_TO_RAD ;
	}

	public double getTick() {
		return this.tick;
	}
	
    public void setTick(double angle) {
		this.tick = angle;
	}
	
    public void setRobotCenter(Point2D.Double c){
        center = c;
        sensor.updateSensorCoordinates(new double[] {getRobotCenter().getX(),getRobotCenter().getY()}, getAngle());
    }
    
    public Point2D.Double getRobotCenter(){
        return this.center;
    }
    
	public int getRobotSize() {
		return ROBOT_SIZE;
	}
	
	public double getAngle() {
		return this.angle;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public TurningRadius getTurningRadius() {
		return this.turningRadius;
	}
	
	public void setCenterFront(double angle) {
    	
    	double dx = this.getRobotCenter().getX() + 15 * Math.sin(angle * DEG_TO_RAD);
    	double dy = this.getRobotCenter().getY() - 15 * Math.cos(angle * DEG_TO_RAD);
    	
    	this.centerFront.setLocation(dx, dy);
	}
	
	public Point2D.Double getCenterFront(){
        return this.centerFront;
    }
    
    public abstract void turnLeft();

	public abstract void reverseLeft();

    public abstract void turnRight();
	
    public abstract void reverseRight();
    
    public abstract void moveForward(); 
    
    public abstract void reverseBackward();
}