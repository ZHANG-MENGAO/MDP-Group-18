package mdp.g18.algo;

import java.awt.geom.Point2D;
import java.awt.Polygon;

public class Sensor {

	private static final double DEG_TO_RAD = Math.PI / 180;

	private Point2D.Double center = new Point2D.Double(); // sensor Center
	private double angle;
	
	private int[] xs = new int[4];
	private int[] ys = new int[4];
	
	Polygon sensorArea;

	Sensor(double[] robotCenter, double angle){
		updateSensorCoordinates(robotCenter,angle);
	}
	
	public void updateSensorCoordinates(double[] c, double angle) {
		updateX(c[0], angle);
		updateY(c[1], angle);
		sensorArea();
	}
	
	public void sensorArea() {
		
		sensorArea = new Polygon(xs, ys, 4);
	}
	
	public boolean scanObstacle(double[] target) {
		if(this.sensorArea.contains(target[0],target[1])) {
			return true;
		}
		return false;
	}
	
	public void updateX(double x, double angle) {
		this.xs[0] = round(x + 15 * Math.sin(angle * DEG_TO_RAD) - 15 * Math.cos(angle * DEG_TO_RAD));
		this.xs[1] = round(x + 15 * Math.sin(angle * DEG_TO_RAD) + 15 * Math.cos(angle * DEG_TO_RAD));
		this.xs[2] = round(x + 35 * Math.sin(angle * DEG_TO_RAD) + 15 * Math.cos(angle * DEG_TO_RAD));
		this.xs[3] = round(x + 35 * Math.sin(angle * DEG_TO_RAD) - 15 * Math.cos(angle * DEG_TO_RAD));
	}
	
	public void updateY(double y, double angle) {
		this.ys[0] = round(y - 15 * Math.cos(angle * DEG_TO_RAD) - 15 * Math.sin(angle * DEG_TO_RAD));
		this.ys[1] = round(y - 15 * Math.cos(angle * DEG_TO_RAD) + 15 * Math.sin(angle * DEG_TO_RAD));
		this.ys[2] = round(y - 35 * Math.cos(angle * DEG_TO_RAD) + 15 * Math.sin(angle * DEG_TO_RAD));
		this.ys[3] = round(y - 35 * Math.cos(angle * DEG_TO_RAD) - 15 * Math.sin(angle * DEG_TO_RAD));
	}
	
	private static int round(double val) {
        return (int) Math.round(val);
    }
	
	public Point2D.Double getSensorCenter(){
        return this.center;
    }
    
    public double getAngle() {
		return this.angle;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public Polygon getSensorArea() {
		return this.sensorArea;
	}
}
