package mdp.g18.algo;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Simulate {
	
	private double[] originalCenter = new double[2]; // robot Original Center
	private double[] frontCenter = new double[2];
	private double[] backCenter = new double[2];
	private double[] obstacleImageCenter = new double[2];
    
    private ArrayList<Path> possiblePaths;
    private ArrayList<Instruction> pathInstructions;
    Instruction currentInstruction;
    
    ArrayList<Data> coordinate = new ArrayList<>();
	
	private double tickOriginal;
	private double angleOriginal;
	private boolean oneRound;
	
	private int [][] obstacles;
	
	Obstacle obstacle;
	Robot robot;
	
	// Constructor
	Simulate(int[][] obstacles){	
		setAllObstacles(obstacles);
		
	}
	
	public void updateRobot(Robot robot) {
		this.robot = robot;
		setOriginalTick(this.robot.getTick());
		setOriginalAngle(this.robot.getAngle());
		setOriginalCenter(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
		setFrontCenter(new double[] {this.robot.getCenterFront().getX(),this.robot.getCenterFront().getY()});
		setBackCenter(new double[] {this.robot.getCenterBack().getX(),this.robot.getCenterBack().getY()});
	}

	public Path simulateMove() {
		Path bestPath = null;
		int iteration;
		
		for (Path path: getPossiblePath()) {
			pathInstructions = path.getInstructions(); // get instructions in path
			int sizeOfPath = pathInstructions.size();  // get size of instructions
			iteration = 0;
			this.oneRound = false;

			for (Instruction instruction: pathInstructions) {
				// one instruction
				if (sizeOfPath == 1) {
					if (instruction.getTurnDirection() != "S") {
						moveForward(this.frontCenter,15);
					}
					performMovement(instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),new double[] {getObstacle().getImageCenter().getX(),getObstacle().getImageCenter().getY()});
				}
				// two instructions
				else if (sizeOfPath == 2) {
					if (iteration == 0) {
						if (instruction.getTurnDirection() != "S") {
							moveForward(this.frontCenter,15);
						}
						
						performMovement(instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),path.getPt1());
						iteration++;
					} 
					else {
						performMovement(instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),new double[] {getObstacle().getImageCenter().getX(),getObstacle().getImageCenter().getY()});
					}
				} 
				// three instructions
				else {
					if (iteration == 0) {
						if (instruction.getTurnDirection() != "S") {
							moveForward(this.frontCenter,15);
						}
						performMovement(instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),path.getPt1());
						iteration++;
					} 
					else if (iteration == 1) {
						performMovement(instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),path.getPt2());
						iteration++;
					} 
					else {
						performMovement(instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),new double[] {getObstacle().getImageCenter().getX(),getObstacle().getImageCenter().getY()});
					}
				}

			}
			
			// reach destination
			if (this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
				reverseBackward(this.backCenter,40);
				bestPath = path;
				break;
			}
			
			// reset position get next path
			this.robot.setRobotCenter(new Point2D.Double(getOriginalCenter()[0],getOriginalCenter()[1]));
			this.robot.setAngle(getOriginalAngle());
			this.robot.setTick(getOriginalTick());
			getPathCoordinates().clear();
		}

		return bestPath;
	}

	public void performMovement(String movement, double angle, double distance, double[] target) {
		switch(movement){
			case "S":
				moveForward(target,distance);
				break;
			case "R":
				this.robot.createCircleRight(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()}, "Forward");
				turnRight(target,angle);
				this.robot.turningRadius = null;
				break;
			case "L":
				this.robot.createCircleLeft(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()}, "Forward");
				turnLeft(target,angle);
				this.robot.turningRadius = null;
				break;
			default:
				break;
		}
	}

	public boolean checkValid(Point2D.Double left, Point2D.Double right, Point2D.Double center) {
		if (this.obstacles[(int) left.getX()][200 + ((int) left.getY()) - 1] != 0 && this.obstacles[(int) right.getX()][200 + ((int) right.getY()) - 1] != 0 &&
				this.obstacles[(int) center.getX()][200 + ((int) center.getY()) - 1] != 0) {
			return true;
		}
		return false;
	}
    
    public void turnRight(double[] target, double angle){
    	double tickNew = this.robot.getTick();
    	
    	double originalAngle = this.robot.getAngle();
    	if (this.robot.getAngle() < 0) {
    		originalAngle += 360;
    	}
    	
    	while((Math.abs(this.robot.getRobotCenter().getX() - target[0]) > 0.08 || Math.abs(this.robot.getRobotCenter().getY() - target[1]) > 0.08)) {

    		if (this.robot.getTick() - tickNew == 360) {
    			this.oneRound = true;
    			break;
    		}
    		
    		if (this.robot.checkBoundaries()  || checkValid(this.robot.getCenterLeft(),this.robot.getCenterRight(),this.robot.getRobotCenter()) ||
        			(Math.abs((this.robot.getAngle() - originalAngle) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
        			(Math.abs((360 - originalAngle + this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
    				this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
				break;
			}
    		
    		this.robot.turnRight();
    		this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())));
    	}
    }
    
    public void turnLeft(double[] target, double angle){
    	double tickNew = this.robot.getTick();
    	
    	double originalAngle = this.robot.getAngle();
    	if (this.robot.getAngle() > 0) {
    		originalAngle -= 360;
    	}
    	
    	while((Math.abs(this.robot.getRobotCenter().getX() - target[0]) > 0.08 || Math.abs(this.robot.getRobotCenter().getY() + target[1]) > 0.08)) {
    		
    		if (this.robot.getTick() - tickNew == 360) {
    			this.oneRound = true;
    			break;
    		}
    		
    		if (this.robot.checkBoundaries() || checkValid(this.robot.getCenterLeft(),this.robot.getCenterRight(),this.robot.getRobotCenter()) ||
    				(Math.abs((originalAngle - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
    				(Math.abs((originalAngle + 360 - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
    				this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
				break;
			}
    		this.robot.turnLeft();
    		this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())));
    	}
    }

    public void moveForward(double[] target, double distance) {
		
    	double dist = 0;
    	
    	double[] p1 = new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()};
    	
    	while(!Arrays.equals(new double[] {round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())},new double[] {round(target[0]),round(target[1])})) {
    		
    		
			if (this.robot.checkBoundaries() || (dist >= distance) 
					|| checkValid(this.robot.getCenterLeft(),this.robot.getCenterRight(),this.robot.getRobotCenter())  
					|| this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
				break;
			}
			
			this.robot.moveForward();
			setFrontCenter(new double[] {this.robot.getCenterFront().getX(),this.robot.getCenterFront().getY()});
			setBackCenter(new double[] {this.robot.getCenterBack().getX(),this.robot.getCenterBack().getY()});
			dist = straightDistance(p1, new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
			this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())));
		}
	}
    
    public void reverseBackward(double[] target, double distance) {
    	
    	double dist = 0;
    	
    	double[] p1 = new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()};
    	
		while(!Arrays.equals(new int[] {round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())},new int[] {round(target[0]),round(target[1])})) {
			
			if ((this.robot.checkBoundaries()) || (dist >= distance) || checkValid(this.robot.getCenterLeft(),this.robot.getCenterRight(),this.robot.getRobotCenter())) {
				break;
			}
			
			this.robot.reverseBackward();
			setFrontCenter(new double[] {this.robot.getCenterFront().getX(),this.robot.getCenterFront().getY()});
			setBackCenter(new double[] {this.robot.getCenterBack().getX(),this.robot.getCenterBack().getY()});
			dist = straightDistance(p1, new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
			this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())));
		}

	}
    
    public double straightDistance(double[] p1, double[] p2) {
		double x = Math.abs(p2[0] - p1[0]);
		double y = Math.abs(p2[1] - p1[1]);
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
	}

    private static int round(double val) {
        return (int) Math.round(val);
    }
	
	public double getOriginalTick() {
		return this.tickOriginal;
	}
	
	public void setOriginalTick(double angle) {
		this.tickOriginal = angle;
	}
	
	public double getOriginalAngle() {
		return this.angleOriginal;
	}
	
	public void setOriginalAngle(double angle) {
		this.angleOriginal = angle;
	}
	
    public void setOriginalCenter(double[] c){
    	originalCenter = c;
    }
    
    public double[] getOriginalCenter(){
        return this.originalCenter;
    }
    
    public void setFrontCenter(double[] c){
    	frontCenter = c;
    }
    
    public double[] getFrontCenter(){
        return this.frontCenter;
    }
    
    public void setBackCenter(double[] c){
    	backCenter = c;
    }
    
    public double[] getBackCenter(){
        return this.backCenter;
    }
    
	public void setObstacleCenter() {
		obstacleImageCenter[0] = round(this.obstacle.getImageCenter().getX());
		obstacleImageCenter[1] = round(this.obstacle.getImageCenter().getY());
	}
	
	public double[] getObstacleCenter() {
		return obstacleImageCenter;
	}
	
	public ArrayList<Data> getPathCoordinates() {
		return this.coordinate;
	}
	
	public void setPossiblePath(ArrayList<Path> paths) {
		this.possiblePaths = paths;
	}
	
	public ArrayList<Path> getPossiblePath() {
		return this.possiblePaths;
	}
	
	public void setObstacle(Obstacle obstacle) {
		this.obstacle = obstacle;
	}
	
	public Obstacle getObstacle() {
		return this.obstacle;
	}
	
	public void setAllObstacles(int[][] obstacles) {
		this.obstacles = obstacles;
	}
}