package mdp.g18.algo;

public class Sensor {
	
	private static final int SENSOR_HEIGHT = 20;
	private static final int SENSOR_WIDTH = 50;
	private static final int NO_OF_SENSORS = 6;
	
	//public static int[][] sensorDirection = new int[3][1];
	public static int[][] sensorLocation = new int[NO_OF_SENSORS][2];
	
	public final static int SHORT_SENSOR_MAX_RANGE = 3; // This is in number of grid.
    public final static int FAR_SENSOR_MAX_RANGE = 7; // This is in number of grid.
    public final static int FAR_SENSOR_OFFSET = 13; // This is in cm.
	
    private int sensorX;
    private int sensorY;
	
	Sensor(int x, int y){
		setSensorX(x);
		setSensorY(y);
	}
	
	public int getSensorX() {
		return this.sensorX;
	}
	
	public int getSensorY() {
		return this.sensorY;
	}
	
	public void setSensorX(int x) {
		this.sensorX = x;
	}
	
	public void setSensorY(int y) {
		this.sensorY = y;
	}
	
	public void updateSensorDirection(RobotOrientation orientation, int robotX, int robotY) {
		switch(orientation) {
			case N:
				this.sensorY = robotY - 30;
				break;
			case S:
				this.sensorY = robotY + 30;
				break;
			case E:
				this.sensorX = robotX + 30;
				break;
			case W:
				this.sensorX = robotX - 30;
				break;
			default:
				break;
		}
				
	}
}
