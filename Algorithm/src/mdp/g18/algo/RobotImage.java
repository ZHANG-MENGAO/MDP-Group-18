package mdp.g18.algo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class RobotImage extends Robot{
	
	private final static int RADIUS = Robot.ROBOT_SIZE * Arena.UNIT_SIZE;
	
	BufferedImage robotImage;
	
	RobotImage(double x, double y, double angle){	
		super(x,y,angle);
		try{
	        robotImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("robot.png")));
	        robotImage = resizeImage(robotImage, RADIUS, RADIUS);
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
	public void drawRobot(Graphics g, Point2D.Double c, double angle){
		Graphics2D g2 = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        at.translate(round(c.getX()) * Arena.UNIT_SIZE,Arena.ARENA_HEIGHT + round(c.getY()) * Arena.UNIT_SIZE);
        setAngle(angle);
        at.rotate(Math.toRadians(getAngle()));
        at.translate(-RADIUS/2,-RADIUS/2);
        g2.drawImage(robotImage, at, null);
	}
	
	private static int round(double val) {
        return (int) Math.round(val);
    }

	@Override
	public void turnLeft() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void reverseLeft() {
		// TODO Auto-generated method stub
	}

	@Override
	public void turnRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reverseRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveForward() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reverseBackward() {
		// TODO Auto-generated method stub
		
	}
}
