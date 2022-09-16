package mdp.g18.algo;

import java.awt.Graphics;

public class RealRobot extends Robot{

	RobotImage robotimage;
	
	// Constructor
	RealRobot(int x, int y, int angle){
		super(x,y,angle);
		robotimage = new RobotImage(x,y,getAngle());
		createCircleLeft(new double[] {getCenterFront().getX(),getCenterFront().getY()}, "Forward");
	}

	public void turnLeft(){
		setTick(getTick() - 1);
        double xCenter = getTurningRadius().getCenter().getX(); // x center of turning radius
        double yCenter = getTurningRadius().getCenter().getY(); // y center of turning radius
        double rad = TurningRadius.getRadius(); // radius of robot
        double[] oriRobot = new double[] {xCenter + rad,yCenter};
        getRobotCenter().setLocation(xCenter + rad * Math.cos(getTick() * DEG_TO_RAD),
                      yCenter + rad * Math.sin(getTick() * DEG_TO_RAD));
        //setAngle((int) calculateAngle(getRobotCenter(),xCenter - rad, yCenter));
        setAngle(computeAngle(oriRobot,new double[] {getTurningRadius().getCenter().getX(),getTurningRadius().getCenter().getY()},new double[] {getRobotCenter().getX(),getRobotCenter().getY()},"L"));
        setCenterFront(getAngle());
        sensor.updateSensorCoordinates(new double[] {getRobotCenter().getX(),getRobotCenter().getY()}, getAngle());
    }

	public void reverseLeft(){
		setTick(getTick() + 1);
        double xCenter = getTurningRadius().getCenter().getX(); // x center of turning radius
        double yCenter = getTurningRadius().getCenter().getY(); // y center of turning radius
        double rad = TurningRadius.getRadius(); // radius of robot
        double[] oriRobot = new double[] {xCenter + rad,yCenter};
        getRobotCenter().setLocation(xCenter + rad * Math.cos(getTick()  * DEG_TO_RAD),
                      yCenter + rad * Math.sin(getTick() * DEG_TO_RAD));
        //setAngle((int) calculateAngle(getRobotCenter(),xCenter - rad, yCenter));
        setAngle(computeAngle(oriRobot,new double[] {getTurningRadius().getCenter().getX(),getTurningRadius().getCenter().getY()},new double[] {getRobotCenter().getX(),getRobotCenter().getY()},"L"));
        sensor.updateSensorCoordinates(new double[] {getRobotCenter().getX(),getRobotCenter().getY()}, getAngle());
    }

    public void turnRight(){
  
    	setTick(getTick() + 1);
        double xCenter = getTurningRadius().getCenter().getX(); // x center of turning radius
        double yCenter = getTurningRadius().getCenter().getY(); // y center of turning radius
        double rad = TurningRadius.getRadius(); // radius of robot
        double[] oriRobot = new double[] {xCenter - rad,yCenter};
        getRobotCenter().setLocation(xCenter - rad * Math.cos(getTick() * DEG_TO_RAD),
                      yCenter - rad * Math.sin(getTick() * DEG_TO_RAD));
        setAngle(computeAngle(oriRobot,new double[] {getTurningRadius().getCenter().getX(),getTurningRadius().getCenter().getY()},new double[] {getRobotCenter().getX(),getRobotCenter().getY()},"R"));
        setCenterFront(getAngle());
        sensor.updateSensorCoordinates(new double[] {getRobotCenter().getX(),getRobotCenter().getY()}, getAngle());
    }
	
    public void reverseRight(){
    	setTick(getTick() - 1);
    	double xCenter = getTurningRadius().getCenter().getX(); // x center of turning radius
        double yCenter = getTurningRadius().getCenter().getY(); // y center of turning radius
        double rad = TurningRadius.getRadius(); // radius of robot
        double[] oriRobot = new double[] {xCenter - rad,yCenter};
        getRobotCenter().setLocation(xCenter - rad * Math.cos(-getTick() * DEG_TO_RAD),
                      yCenter - rad * Math.sin(getTick() * DEG_TO_RAD));
        setAngle(computeAngle(oriRobot,new double[] {getTurningRadius().getCenter().getX(),getTurningRadius().getCenter().getY()},new double[] {getRobotCenter().getX(),getRobotCenter().getY()},"R"));
        setCenterFront(getAngle());
        sensor.updateSensorCoordinates(new double[] {getRobotCenter().getX(),getRobotCenter().getY()}, getAngle());
    }
    
    public void moveForward() {
		int speed = 1;
		double dx = getRobotCenter().getX() + speed * Math.cos(Math.PI/2 - getAngle() * DEG_TO_RAD);
		double dy = getRobotCenter().getY() - speed * Math.sin(Math.PI/2 - getAngle() * DEG_TO_RAD);
		getRobotCenter().setLocation(dx,dy);
		setCenterFront(getAngle());
		sensor.updateSensorCoordinates(new double[] {getRobotCenter().getX(),getRobotCenter().getY()}, getAngle());
	}
    
    public void reverseBackward() {
		int speed = 1;
		double dx = getRobotCenter().getX() + speed * Math.cos(getAngle() * DEG_TO_RAD + Math.PI/2);
		double dy = getRobotCenter().getY() + speed * Math.sin(getAngle() * DEG_TO_RAD + Math.PI/2);
		getRobotCenter().setLocation(dx,dy);
		setCenterFront(getAngle());
		sensor.updateSensorCoordinates(new double[] {getRobotCenter().getX(),getRobotCenter().getY()}, getAngle());
	}
    
    public void drawRobot(Graphics g){
    	robotimage.drawRobot(g, getRobotCenter(), getAngle());
	}
   
}