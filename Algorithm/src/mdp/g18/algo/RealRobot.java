package mdp.g18.algo;

import java.awt.Graphics;

public class RealRobot extends Robot{

	RobotImage robotimage;
	
	// Constructor
	RealRobot(double x, double y, double angle){
		super(x,y,angle);
		robotimage = new RobotImage(x,y,getAngle());
		//createCircleLeft(new double[] {getCenterFront().getX(),getCenterFront().getY()},"front");
	}
    
    public void drawRobot(Graphics g){
    	robotimage.drawRobot(g, getRobotCenter(), getAngle());
	}
   
}
