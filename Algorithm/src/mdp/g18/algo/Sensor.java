package mdp.g18.algo;

public class Sensor {

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
				this.sensorY = robotY - Robot.ROBOT_SIZE;
				break;
			case S:
				this.sensorY = robotY + Robot.ROBOT_SIZE;
				break;
			case E:
				this.sensorX = robotX + Robot.ROBOT_SIZE;
				break;
			case W:
				this.sensorX = robotX - Robot.ROBOT_SIZE;
				break;
			case NE1:
				this.sensorX = robotX + 10;
				this.sensorY = robotY - 28;
				break;
			case NE2:
				this.sensorX = robotX + 18;
				this.sensorY = robotY - 24;
				break;
			case NE3:
				this.sensorX = robotX + 24;
				this.sensorY = robotY - 18;
				break;
			case SE1:
				this.sensorX = robotX + 18;
				this.sensorY = robotY + 25;
				break;
			case SE2:
				this.sensorX = robotX + 24;
				this.sensorY = robotY + 18;
				break;
			case SE3:
				this.sensorX = robotX + 28;
				this.sensorY = robotY + 10;
				break;
			case NW1:
				this.sensorX = robotX - 10;
				this.sensorY = robotY - 28;
				break;
			case NW2:
				this.sensorX = robotX - 18;
				this.sensorY = robotY - 24;
				break;
			case NW3:
				this.sensorX = robotX - 24;
				this.sensorY = robotY - 18;
				break;
			case SW1:
				this.sensorX = robotX - 19;
				this.sensorY = robotY + 24;
				break;
			case SW2:
				this.sensorX = robotX - 25;
				this.sensorY = robotY + 18;
				break;
			case SW3:
				this.sensorX = robotX - 18;
				this.sensorY = robotY + 10;
				break;
			default:
				break;
		}	
	}
}
