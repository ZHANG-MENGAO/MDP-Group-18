package mdp.g18.algo;

public class Instruction {
	double angle;
	double distance;
	String turnDirection;

	Instruction(double angle, double distance, String turnDirection) {
		this.angle = angle;
		this.distance = distance;
		this.turnDirection = turnDirection;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getTurnDirection() {
		return turnDirection;
	}

	public void setTurnDirection(String turnDirection) {
		this.turnDirection = turnDirection;
	}
}
