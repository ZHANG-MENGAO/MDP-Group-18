package mdp.g18.algo;

import java.awt.Graphics;

public class RealRobot extends Robot{
	
	private RobotImage robotImage;

	RealRobot(int x, int y, Direction direction) {
		super(x, y, direction);
		robotImage = new RobotImage(x,y, direction);
	}
	
	public void drawRobot(Graphics g){
    	this.robotImage.drawRobot(g, this.getxCoordinate(), this.getyCoordinate(), this.getDirection());
	}

}
