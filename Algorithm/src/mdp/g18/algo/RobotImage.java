package mdp.g18.algo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RobotImage {
	
	private final static int RADIUS = Robot.ROBOT_SIZE * Arena.UNIT_SIZE;
	
	private int x_coor;
	private int y_coor;
	private RobotOrientation orientation;
	
	BufferedImage robotImage;
	
	
	RobotImage(int x, int y, RobotOrientation orientation){	
		this.x_coor = x;
		this.y_coor = y;
		this.orientation = orientation;
		try{
	        robotImage = ImageIO.read(getClass().getResource("/Resources/robot.png"));
	        robotImage = resizeImage(robotImage, RADIUS + Arena.UNIT_SIZE, RADIUS + Arena.UNIT_SIZE);
	    }catch(IOException e){e.printStackTrace();}
	    catch(Exception e){e.printStackTrace();}
	}
	
	public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	    graphics2D.dispose();
	    return resizedImage;
	}
	
	public void paintImage(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        
        int theta; // in degrees
        int ty;
        int tx;

        switch (getOrientation()){
        case E:
        	theta = 90;
        	ty = - RADIUS - Arena.UNIT_SIZE;
        	tx = - Arena.UNIT_SIZE;
        	break;
        case S:
        	theta = 180;
        	ty =  - RADIUS;
        	tx = - Arena.UNIT_SIZE;
        	break;
        case W:
        	theta = -90;
        	ty =  - RADIUS;
        	tx = 0;
        	break;
        case NE1:
        	theta = 20;
        	ty = - RADIUS - Arena.UNIT_SIZE;
        	tx = 0;
        	break;
        case NE2:
        	theta = 37;
        	ty = - RADIUS - Arena.UNIT_SIZE;
        	tx = 0;
        	break;
        case NE3:
        	theta = 53;
        	ty = - RADIUS - Arena.UNIT_SIZE;
        	tx = 0;
        	break;
        case SE1:
        	theta = 143;
        	ty = - RADIUS - Arena.UNIT_SIZE;
        	tx = - Arena.UNIT_SIZE;
        	break;
        case SE2:
        	theta = 127;
        	ty = - RADIUS - Arena.UNIT_SIZE;
        	tx = - Arena.UNIT_SIZE;
        	break;
        case SE3:
        	theta = 110;
        	ty = - RADIUS - Arena.UNIT_SIZE;
        	tx = - Arena.UNIT_SIZE;
        	break;
        case NW1:
        	theta = -20;
        	ty =  - RADIUS;
        	tx = 0;
        	break;
        case NW2:
        	theta = -37;
        	ty =  - RADIUS - Arena.UNIT_SIZE;
        	tx = 0;
        	break;
        case NW3:
        	theta = -53;
        	ty = - RADIUS;
        	tx = 0;
        	break;
        case SW1:
        	theta = -143;
        	ty =  - RADIUS;
        	tx = 0;
        	break;
        case SW2:
        	theta = -127;
        	ty =  - RADIUS;
        	tx = 0;
        	break;
        case SW3:
        	theta = -110;
        	ty =  - RADIUS;
        	tx = 0;
        	break;
        default:
        	theta = 0;
        	ty = - RADIUS - Arena.UNIT_SIZE;
        	tx = 0;
        	break;
        }
        
        at.translate(getX() * Arena.UNIT_SIZE,Arena.ARENA_HEIGHT + getY() * Arena.UNIT_SIZE);
        at.rotate(Math.toRadians(theta));
        at.translate(tx,ty);
        g2.drawImage(robotImage, at, null);
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
	
	public RobotOrientation getOrientation() {
		return this.orientation;
	}
	
	public void setOrientation(RobotOrientation orientation) {
		this.orientation = orientation;
	}
}
