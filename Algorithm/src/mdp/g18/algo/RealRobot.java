package mdp.g18.algo;

import java.awt.Graphics;

public class RealRobot extends Robot{

	RobotImage robotimage;
	
	// Constructor
	RealRobot(double x, double y, double angle){
		super(x,y,angle);
		robotimage = new RobotImage(x,y,getAngle());
	}
    
    public void drawRobot(Graphics g){
    	robotimage.drawRobot(g, getRobotCenter(), getAngle());
	}
   
}
