package mdp.g18.algo;

import java.awt.Polygon;
import java.awt.geom.Point2D;

public class SimulatorRobot extends Robot{
	
	private int[] xs = new int[4];
	private int[] ys = new int[4];
	
	Polygon simulatorArea;

	// Constructor
	SimulatorRobot(double x, double y, double angle){
		super(x,y,angle);
		updateSimulatorArea(new double[] {this.getRobotCenter().getX(),this.getRobotCenter().getY()},this.getAngle());
	}
	
	public void updateSimulatorArea(double[] c, double angle) {
		updateX(c[0], angle);
		updateY(c[1], angle);
		simulatorArea();
	}
	
	public void simulatorArea() {
		this.simulatorArea = new Polygon(xs, ys, 4);
	}
	
	public void updateX(double x, double angle) {
		this.xs[0] = round(x + 15 * Math.sin(angle * DEG_TO_RAD) - 15 * Math.cos(angle * DEG_TO_RAD));
		this.xs[1] = round(x + 15 * Math.sin(angle * DEG_TO_RAD) + 15 * Math.cos(angle * DEG_TO_RAD));
		this.xs[2] = round(x - 15 * Math.sin(angle * DEG_TO_RAD) + 15 * Math.cos(angle * DEG_TO_RAD));
		this.xs[3] = round(x - 15 * Math.sin(angle * DEG_TO_RAD) - 15 * Math.cos(angle * DEG_TO_RAD));
	}
	
	public void updateY(double y, double angle) {
		this.ys[0] = round(y - 15 * Math.cos(angle * DEG_TO_RAD) - 15 * Math.sin(angle * DEG_TO_RAD));
		this.ys[1] = round(y - 15 * Math.cos(angle * DEG_TO_RAD) + 15 * Math.sin(angle * DEG_TO_RAD));
		this.ys[2] = round(y + 15 * Math.cos(angle * DEG_TO_RAD) + 15 * Math.sin(angle * DEG_TO_RAD));
		this.ys[3] = round(y + 15 * Math.cos(angle * DEG_TO_RAD) - 15 * Math.sin(angle * DEG_TO_RAD));
	}
	
	private static int round(double val) {
        return (int) Math.round(val);
    }
	
    public void turnLeft(){
		super.turnLeft();
		updateSimulatorArea(new double[] {this.getRobotCenter().getX(),this.getRobotCenter().getY()},this.getAngle());
    }

	public void reverseLeft(){
		super.reverseLeft();
		updateSimulatorArea(new double[] {this.getRobotCenter().getX(),this.getRobotCenter().getY()},this.getAngle());
    }

    public void turnRight(){
    	super.turnRight();
    	updateSimulatorArea(new double[] {this.getRobotCenter().getX(),this.getRobotCenter().getY()},this.getAngle());
    }
	
    public void reverseRight(){
    	super.reverseRight();
    	updateSimulatorArea(new double[] {this.getRobotCenter().getX(),this.getRobotCenter().getY()},this.getAngle());
    }
    
    public void moveForward() {
		super.moveForward();
		updateSimulatorArea(new double[] {this.getRobotCenter().getX(),this.getRobotCenter().getY()},this.getAngle());
	}
    
    public void reverseBackward() {
		super.reverseBackward();
		updateSimulatorArea(new double[] {this.getRobotCenter().getX(),this.getRobotCenter().getY()},this.getAngle());
	}
    
    public Polygon getSimulatorArea() {
    	return this.simulatorArea;
    }
}
