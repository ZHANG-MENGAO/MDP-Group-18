package mdp.g18.algo;

public class Simulate {
	
	public static final int ROBOT_SIZE = 30;
	
	private int x_coor;
	private int y_coor;
	private RobotOrientation orientation;
	
	private int center_x;
	private int center_y;
	
	Sensor sensor;
	
	// Constructor
	Simulate(){	
			this.x_coor = 0;
			this.y_coor = 0;
			this.setOrientation(RobotOrientation.N);
			sensor = new Sensor(this.x_coor,this.y_coor);
	}
	
	public void getCenter(RobotOrientation orientation) {
		switch(orientation) {
		case N:
			this.center_x = this.x_coor + 15;
			this.center_y = this.y_coor - 15;
			break;
		case S:
			this.center_x = this.x_coor - 15;
			this.center_y = this.y_coor + 15;
			break;
		case E:
			this.center_x = this.x_coor + 15;
			this.center_y = this.y_coor + 15;
			break;
		case W:
			this.center_x = this.x_coor - 15;
			this.center_y = this.y_coor - 15;
			break;
		default:
			break;
		}
	}
	
	public void getObstacleCenter(int obstacleX, int obstacleY) {
		
	}

	public RobotOrientation getOrientation() {
		return orientation;
	}

	public void setOrientation(RobotOrientation orientation) {
		this.orientation = orientation;
	}
	
	public void possiblePath(int d) {
		
	}
}
