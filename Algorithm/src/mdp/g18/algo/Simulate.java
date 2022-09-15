package mdp.g18.algo;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Simulate {
	
	private double[] originalCenter = new double[2]; // robot Original Center
	private double[] frontCenter = new double[2]; // robot Original Center
	private double[] obstacleImageCenter = new double[2];
    //private TurningRadius turningRadius;
    private static final double DEG_TO_RAD = Math.PI / 180; // conversion to degree
    
    private ArrayList<Path> possiblePaths;
    private ArrayList<Instruction> pathInstructions;
    Instruction currentInstruction;
    
    ArrayList<Data> coordinate = new ArrayList<>();
   
	public static final int ROBOT_SIZE = 30;
	
	private double tickOriginal;
	private double angleOriginal;
	private boolean oneRound;
	
	private int [][] obstacles;
	
	Obstacle obstacle;
	SimulatorRobot robot;
	
	// Constructor
	Simulate(SimulatorRobot robot, ArrayList<Path> possiblePaths, Obstacle obstacle, int[][] obstacles){	
		
		setPossiblePath(possiblePaths);
		setObstacle(obstacle);
		setObstacleCenter();
		setAllObstacles(obstacles);
		
		this.robot = robot;
		setOriginalTick(robot.getTick());
		setOriginalAngle(robot.getAngle());
		setOriginalCenter(new double[] {robot.getRobotCenter().getX(),robot.getRobotCenter().getY()});
		setFrontCenter(new double[] {robot.getCenterFront().getX(),robot.getCenterFront().getY()});
		
		for (Path path: getPossiblePath()) {
			System.out.println(path.getDist());
			for (Instruction instruction: path.getInstructions()) {
				System.out.println(instruction.getTurnDirection());
			}
		}

	}

	public Path simulateMove() {
		Path bestPath = null;
		int iteration;
		
		for (Path path: getPossiblePath()) {
			pathInstructions = path.getInstructions(); // get instructions in path
			int sizeOfPath = pathInstructions.size();  // get size of instructions
			iteration = 0;
			this.oneRound = false;
			
			/*System.out.println(path.getPt1()[0]);
			System.out.println(path.getPt1()[1]);
			System.out.println(path.getPt2()[0]);
			System.out.println(path.getPt2()[1]);*/

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
					//System.out.println(instruction.getTurnDirection());
					if (iteration == 0) {
						if (instruction.getTurnDirection() != "S") {
							//System.out.println(instruction.getTurnDirection());
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
		//System.out.println(round(this.robot.getCenterFront().getX()));
		//System.out.println(round(this.robot.getCenterFront().getY()));
		//System.out.println(round(target[0]));
		//System.out.println(round(target[1]));
		switch(movement){
			case "S":
				moveForward(target,distance);
				break;
			case "R":

				this.robot.createCircleRight(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()}, "Forward");
				//System.out.println("R");
				//System.out.println(this.robot.turningRadius.getCenter().getX());
				//System.out.println(this.robot.turningRadius.getCenter().getY());
				
				turnRight(target,angle);
				this.robot.turningRadius = null;
				break;
			case "L":

				this.robot.createCircleLeft(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()}, "Forward");
				/*
				System.out.println("L");
				System.out.println(this.robot.turningRadius.getCenter().getX());
				System.out.println(this.robot.turningRadius.getCenter().getY());*/
				
				turnLeft(target,angle);
				this.robot.turningRadius = null;
				break;
			default:
				break;
		}
	}

	/*public boolean checkValid() {
		if (this.obstacles[(int) getCurrentCenter()[0]][(int) getCurrentCenter()[1]] != 0) {
			return false;
		}
		return true;
	}*/
    
    public void turnRight(double[] target, double angle){
    	System.out.println((int)(target[0]));
		System.out.println((int)(target[1]));
		System.out.println(round(this.robot.getRobotCenter().getX()));
		System.out.println(round(this.robot.getRobotCenter().getY()));
    	double tickNew = this.robot.getTick();
    	
    	double originalAngle = this.robot.getAngle();
    	if (this.robot.getAngle() < 0) {
    		originalAngle += 360;
    	}
    		
    	//while(!Arrays.equals(new double[] {round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())},new double[] {(int)(target[0]),(int)(target[1])})) {
    	while((Math.abs(this.robot.getRobotCenter().getX() - target[0]) > 0.08 || Math.abs(this.robot.getRobotCenter().getY() - target[1]) > 0.08)) {

    		if (this.robot.getTick() - tickNew == 360) {
    			this.oneRound = true;
    			break;
    		}
    		
    		if (this.robot.checkBoundaries()  || 
        			(Math.abs((this.robot.getAngle() - originalAngle) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) || 
    				this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
				break;
			}
    		
    		this.robot.turnRight();
    		this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())));
    		//System.out.println(this.robot.getAngle());
    		System.out.println(angle);
    		System.out.println(this.robot.getAngle());
    		//System.out.println(round(this.robot.getRobotCenter().getX()));
    		//System.out.println(round(this.robot.getRobotCenter().getY()));
    	}
    	System.out.println("R");
    	System.out.println(this.robot.getAngle());
    	//System.out.println(round(this.robot.getRobotCenter().getX()));
		//System.out.println(round(this.robot.getRobotCenter().getY()));
    }
    
    public void turnLeft(double[] target, double angle){
    	System.out.println((int)(target[0]));
		System.out.println((int)(target[1]));
		System.out.println(round(this.robot.getRobotCenter().getX()));
		System.out.println(round(this.robot.getRobotCenter().getY()));
    	double tickNew = this.robot.getTick();
    	
    	double originalAngle = this.robot.getAngle();
    	if (this.robot.getAngle() > 0) {
    		originalAngle -= 360;
    	}
    	
    	//while(!Arrays.equals(new double[] {round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())},new double[] {(int)(target[0]),(int)(target[1])})) {
    	while((Math.abs(this.robot.getRobotCenter().getX() - target[0]) > 0.08 || Math.abs(this.robot.getRobotCenter().getY() + target[1]) > 0.08)) {
    		
    		if (this.robot.getTick() - tickNew == 360) {
    			this.oneRound = true;
    			break;
    		}
    		
    		if (this.robot.checkBoundaries() || (Math.abs((originalAngle - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
    				this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
				break;
			}
    		this.robot.turnLeft();
    		this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())));
    		System.out.println(this.robot.getAngle());
    		System.out.println(angle);
    		System.out.println(originalAngle);
    		//System.out.println(round(this.robot.getRobotCenter().getX()));
    		//System.out.println(round(this.robot.getRobotCenter().getY()));
    	}
    	System.out.println("L");
    	System.out.println(this.robot.getAngle());
    }

    public void moveForward(double[] target, double distance) {
    	double dist = 0;
    	
    	double[] p1 = new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()};
    	
    	while(!Arrays.equals(new double[] {round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())},new double[] {round(target[0]),round(target[1])})) {
    		
    		
			if (this.robot.checkBoundaries() || (dist >= distance)
					|| this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
				break;
			}
			
			this.robot.moveForward();
			dist = straightDistance(p1, new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
			this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())));
			
	    	//System.out.println(round(this.robot.getRobotCenter().getX()));
			//System.out.println(round(this.robot.getRobotCenter().getY()));
		}
	}
    
    public void reverseBackward(double[] target, double angle) {

		while(!Arrays.equals(new int[] {round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())},new int[] {round(target[0]),round(target[1])})) {

			if (this.robot.checkBoundaries() || this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
				break;
			}
			
			this.robot.reverseBackward();
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
