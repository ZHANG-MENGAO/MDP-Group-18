package mdp.g18.algo;

public class Sensor {
	
	private static final int SENSOR_HEIGHT = 20;
	private static final int SENSOR_WIDTH = 50;
	public int[] sensorCoord;
	public int robotX;
	public int robotY;
	public Direction direction;
	
	Sensor(int x, int y, Direction direction){
		this.robotX = x;
		this.robotY = y;
		this.direction = direction;
	}
	
	public void updateSensor() {
		
	}
	
}
