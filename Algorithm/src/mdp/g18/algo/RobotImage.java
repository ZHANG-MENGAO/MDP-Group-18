package mdp.g18.algo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RobotImage {
	
	private final static int RADIUS = Robot.ROBOT_SIZE * Arena.UNIT_SIZE;
	
	private int x_coor;
	private int y_coor;
	private int angle;
	
	BufferedImage robotImage;
	
	
	RobotImage(int x, int y, int angle){	
		this.x_coor = x;
		this.y_coor = y;
		this.angle = angle;
		try{
	        robotImage = ImageIO.read(getClass().getResource("/Resources/robot.png"));
	        robotImage = resizeImage(robotImage, RADIUS + Arena.UNIT_SIZE, RADIUS + Arena.UNIT_SIZE);
	    }catch(IOException e){e.printStackTrace();}
	    catch(Exception e){e.printStackTrace();}
	}
	
	private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	    graphics2D.dispose();
	    return resizedImage;
	}
	
	
	// Draw robot with center coordinates of robot
	public void drawRobot(Graphics g, Point2D.Double c, int angle){
		Graphics2D g2 = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        at.translate(round(c.getX()) * Arena.UNIT_SIZE,Arena.ARENA_HEIGHT + round(c.getY()) * Arena.UNIT_SIZE);
        //System.out.println(round(c.getX()));
        //System.out.println(round(c.getY()));
        setAngle(angle);
        at.rotate(Math.toRadians(getAngle()));
        at.translate(-RADIUS/2,-RADIUS/2);
        g2.drawImage(robotImage, at, null);
	}
	
	private static int round(double val) {
        return (int) Math.round(val);
    }

	public int getX() {
		return this.x_coor;
	}
	
	public int getY() {
		return this.y_coor;
	}
	
	public void setX(int x) {
		this.x_coor = x;
	}
	
	public void setY(int y) {
		this.y_coor = y;
	}
	
	public int getAngle() {
		return this.angle;
	}
	
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
}
