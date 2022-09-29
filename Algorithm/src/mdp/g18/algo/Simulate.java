package mdp.g18.algo;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Simulate {
	
	private double[] originalCenter = new double[2]; // robot Original Center
	private double[] frontCenter = new double[2];
	private double[] obstacleImageCenter = new double[2];
    
    private ArrayList<Path> possiblePaths;
    private ArrayList<Instruction> pathInstructions;
    private ArrayList<Obstacle> obstacleList;
    Instruction currentInstruction;
    
    ArrayList<Data> coordinate = new ArrayList<>();
    ArrayList<Data> coordinateEnding = new ArrayList<>();
    private ArrayList<Node> nodes;
	
	private double tickOriginal;
	private double angleOriginal;
	private boolean firstInstrTurn = false;
	
	private ArrayList<Double> updateAngle = new ArrayList<>();
	private ArrayList<Double> updateDistance = new ArrayList<>();
	
	Obstacle obstacle;
	Astar astar;
	SimulatorRobot robot;
	
	// Constructor
	Simulate(ArrayList<Obstacle> obstacleList, Astar astar){	
		this.setObstacleList(obstacleList);
		this.astar = astar;
		this.nodes = astar.createAllNodes();
	}
	
	public void updateRobot(SimulatorRobot robot) {
		this.robot = robot;
		setOriginalTick(this.robot.getTick());
		setOriginalAngle(this.robot.getAngle());
		setOriginalCenter(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
		setFrontCenter(new double[] {this.robot.getCenterFront().getX(),this.robot.getCenterFront().getY()});
		this.robot.updateSimulatorArea(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()}, this.robot.getAngle());
	}

	public Path simulateMove() {
		Path bestPath = null;
		int iteration;
		
		
		for (Path path: getPossiblePath()) {
			pathInstructions = path.getInstructions(); // get instructions in path
			int sizeOfPath = pathInstructions.size();  // get size of instructions
			
			if(sizeOfPath == 3) {
				if (path.getPt1() == null || path.getPt2() == null) {
					continue;
				}
			} else if (sizeOfPath == 2) {
				if (path.getPt1() == null) {
					continue;
				}
			}
			
			iteration = 0;
			
			this.updateAngle.clear();
			this.updateDistance.clear();

			for (Instruction instruction: pathInstructions) {
				// one instruction
				if (sizeOfPath == 1) {
					if (instruction.getTurnDirection() != "S") {
						moveForward(this.frontCenter,15);
						firstInstrTurn = true;
					}
					performMovement(iteration,instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),new double[] {getObstacle().getImageCenter().getX(),getObstacle().getImageCenter().getY()});
				}
				// two instructions
				else if (sizeOfPath == 2) {
					if (iteration == 0) {
						if (instruction.getTurnDirection() != "S") {
							moveForward(this.frontCenter,15);
							firstInstrTurn = true;
						}
						
						performMovement(iteration,instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),path.getPt1());
						iteration++;
					} 
					else {
						performMovement(iteration,instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),new double[] {getObstacle().getImageCenter().getX(),getObstacle().getImageCenter().getY()});
					}
				} 
				// three instructions
				else {
					if (iteration == 0) {
						if (instruction.getTurnDirection() != "S") {
							moveForward(this.frontCenter,15);
							firstInstrTurn = true;
						}
						performMovement(iteration,instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),path.getPt1());
						iteration++;
					} 
					else if (iteration == 1) {
						performMovement(iteration,instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),path.getPt2());
						iteration++;
					} 
					else {
						performMovement(iteration,instruction.getTurnDirection(),instruction.getAngle(),instruction.getDistance(),new double[] {getObstacle().getImageCenter().getX(),getObstacle().getImageCenter().getY()});
					}
				}
			}
			
			// reach destination
			if (this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
				
				this.coordinateEnding.add(new Data( -1 , -1, 0)); // delimiter for reach obstacle
				
				//if last instruction direction, reverse by that direction
				bestPath = path;
				// update values here
				for(int i = 0; i < this.updateAngle.size(); i++) {
					bestPath.getInstructions().get(i).setAngle(this.updateAngle.get(i));
					bestPath.getInstructions().get(i).setDistance(this.updateDistance.get(i));
				}
				
				if (firstInstrTurn)
					bestPath.addInstructions(0, new Instruction(getOriginalAngle() * Math.PI/180,15,"S","n"));
				
				//Realigning
				if(bestPath.getInstructions().get(bestPath.getSizeInstruction() - 1).getTurnDirection() == "R") {
					double[] reverseL = reverseLeft(0,90,180,270);
					bestPath.addInstructions(new Instruction(reverseL[0] * Math.PI/180,reverseL[1],"ReL","n"));
				}
				else if(bestPath.getInstructions().get(bestPath.getSizeInstruction() - 1).getTurnDirection() == "L") {
					double[] reverseR = reverseRight(0,90,180,270);
					bestPath.addInstructions(new Instruction(reverseR[0] * Math.PI/180,reverseR[1],"ReR","n"));
				}
				
				double reverse = reverseBackward(40);
				bestPath.addInstructions(new Instruction(this.robot.getAngle() * Math.PI/180,reverse,"Re","n"));
				
				break;
			}
			
			// reset position get next path
			this.robot.setRobotCenter(new Point2D.Double(getOriginalCenter()[0],getOriginalCenter()[1]));
			this.robot.updateSimulatorArea(originalCenter, getOriginalAngle());
			this.robot.setAngle(getOriginalAngle());
			this.robot.setTick(getOriginalTick());
			this.getPathCoordinates().clear();
			this.getPathCoordinatesEnd().clear();
		}
		
		// if no path use astar to find path
		if (bestPath == null) {
			Node start = this.findNode(originalCenter);
			Node target = this.findNode(obstacleImageCenter);
			//System.out.println(start.getCoord()[0]);
			//System.out.println(start.getCoord()[1]);
			Node result = astar.aStar(start, target);
			ArrayList<Data> newData = astar.findPath(result);
			this.coordinate.addAll(newData);
		}

		return bestPath;
	}


	public void performMovement(int iteration, String movement, double angle, double distance, double[] target) {
		
		double angle1 = angle;
		double dist1 = distance;
		
		switch(movement){
			case "S":
				dist1 = moveForward(target,distance);
				break;
			case "R":
				this.robot.createCircleRight(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
				angle1 = turnRight(target,angle);
				this.robot.turningRadius = null;
				break;
			case "L":
				this.robot.createCircleLeft(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
				angle1 = turnLeft(target,angle);
				this.robot.turningRadius = null;
				break;
			default:
				break;
		}
		
		this.updateAngle.add(iteration, angle1);
		this.updateDistance.add(iteration, dist1);
	}
	
	public boolean checkValid() {
		for (Obstacle obstacle: this.getObstacleList()) {
			if (this.robot.getSimulatorArea().intersects(obstacle.getObstacleArea())) {
				return true;
			}
		}
		return false;
	}
    
    public double turnRight(double[] target, double angle){
    	
    	double originalAngle = this.robot.getAngle();
    	if (this.robot.getAngle() < 0) {
    		originalAngle += 360;
    	}
    	
    	if(target != null) {
	    	while((Math.abs(this.robot.getRobotCenter().getX() - target[0]) > 0.08 || Math.abs(this.robot.getRobotCenter().getY() - target[1]) > 0.08)) {
	    		
	    		this.robot.turnRight();
	    		
	    		if (this.robot.checkBoundaries()  || checkValid() ||
	        			(Math.abs((this.robot.getAngle() - originalAngle) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
	        			(Math.abs((360 - originalAngle + this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
	    				this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
					break;
				}
	    		
	    		this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
	    	}
    	}
    	
    	this.coordinateEnding.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
    	
    	if (this.robot.getAngle() > originalAngle) {
    		return Math.abs((this.robot.getAngle() - originalAngle) * this.robot.DEG_TO_RAD);
    	}
    	else {
    		return Math.abs((360 - originalAngle + this.robot.getAngle()) * this.robot.DEG_TO_RAD);
    	}
    }
    
    public double turnLeft(double[] target, double angle){
    	
    	double originalAngle = this.robot.getAngle();
    	if (this.robot.getAngle() > 0) {
    		originalAngle -= 360;
    	}
    	
    	if(target != null) {
	    	while((Math.abs(this.robot.getRobotCenter().getX() - target[0]) > 0.08 || Math.abs(this.robot.getRobotCenter().getY() + target[1]) > 0.08)) {
	    		
	    		this.robot.turnLeft();
	    		
	    		if (this.robot.checkBoundaries() || checkValid() ||
	    				(Math.abs((originalAngle - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
	    				(Math.abs((originalAngle + 360 - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
	    				this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
					break;
				}
	    		
	    		this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
	    	}
    	}
    	
    	this.coordinateEnding.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
    	
    	if (this.robot.getAngle() < originalAngle) {
    		return Math.abs((originalAngle - this.robot.getAngle()) * this.robot.DEG_TO_RAD);
    	}
    	else {
    		return Math.abs((originalAngle + 360 - this.robot.getAngle()) * this.robot.DEG_TO_RAD);
    	}
    }
    
    public double moveForward(double[] target, double distance) {
		
    	double dist = 0;
    	
    	double[] p1 = new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()};
    	
    	if(target != null) {
	    	while(!Arrays.equals(new double[] {round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY())},new double[] {round(target[0]),round(target[1])})) {
	
				this.robot.moveForward();
				
				if (this.robot.checkBoundaries() || (dist >= distance) || checkValid()
						|| this.robot.sensor.scanObstacle(new double[] {this.obstacle.getObstacleCenter().getX(),this.obstacle.getObstacleCenter().getY()})) {
					break;
				}
				
				setFrontCenter(new double[] {this.robot.getCenterFront().getX(),this.robot.getCenterFront().getY()});
				dist = straightDistance(p1, new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
				this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
			}
    	}
    	
    	this.coordinateEnding.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
    	
    	return dist;
	}

    public double reverseBackward(double distance) {
    	
    	double dist = 0;
    	
    	double[] p1 = new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()};
    	
		while(dist < distance) {
			
			if ((this.robot.checkBoundaries()) || checkValid()) {
				break;
			}
			
			this.robot.reverseBackward();
			setFrontCenter(new double[] {this.robot.getCenterFront().getX(),this.robot.getCenterFront().getY()});
			dist = straightDistance(p1, new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
			this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
		}
		
		this.coordinateEnding.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
		
		return dist;
	}
    
    public double[] reverseLeft(double north, double east, double south, double west){ // take in angle to stop
    	
    	double arcLength[] = new double[2];
    	
    	double originalAngle = this.robot.getAngle();
    	
    	this.robot.createCircleLeft(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
    	
    	while(Math.abs((Math.abs(this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(north) * this.robot.DEG_TO_RAD)) > 0.02 &&
    			Math.abs((Math.abs(this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(south) * this.robot.DEG_TO_RAD)) > 0.02 &&
    			Math.abs((Math.abs(this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(west) * this.robot.DEG_TO_RAD)) > 0.02 &&
    			Math.abs((Math.abs(this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(east) * this.robot.DEG_TO_RAD)) > 0.02) {
  
    		this.robot.reverseLeft();
    		
    		if (this.robot.checkBoundaries() || checkValid()){
    			break;
    		}
    		
    		this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
    	}
    	
    	this.coordinateEnding.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
    	
    	this.robot.turningRadius = null; 	
    	
    	if (this.robot.getAngle() > originalAngle) {
    		arcLength[0] = Math.abs((this.robot.getAngle() - originalAngle) * this.robot.DEG_TO_RAD);
    	}
    	else {
    		arcLength[0] = Math.abs((360 - originalAngle + this.robot.getAngle()) * this.robot.DEG_TO_RAD);
    	}
    	
    	arcLength[1] = Math.abs(arcLength[0] * TurningRadius.getRadius());
    	
    	return arcLength;
    }
    
    public double[] reverseRight(double north, double east, double south, double west){
    	
    	double arcLength[] = new double[2];
    	
    	double originalAngle = this.robot.getAngle();
    	
    	this.robot.createCircleRight(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
    	while(Math.abs((Math.abs(this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(north) * this.robot.DEG_TO_RAD)) > 0.02 &&
    			Math.abs((Math.abs(this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(south) * this.robot.DEG_TO_RAD)) > 0.02 &&
    			Math.abs((Math.abs(this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(west) * this.robot.DEG_TO_RAD)) > 0.02 &&
    			Math.abs((Math.abs(this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(east) * this.robot.DEG_TO_RAD)) > 0.02) {
  
    		this.robot.reverseRight();
    		
    		if (this.robot.checkBoundaries() || checkValid()){
    			break;
    		}
    		
    		this.coordinate.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
    	}
    	
    	this.coordinateEnding.add(new Data(round(this.robot.getRobotCenter().getX()),round(this.robot.getRobotCenter().getY()),this.robot.getAngle()));
    	
    	this.robot.turningRadius = null;
    	
    	if (this.robot.getAngle() > originalAngle) {
    		arcLength[0] = Math.abs((this.robot.getAngle() - originalAngle) * this.robot.DEG_TO_RAD);
    	}
    	else {
    		arcLength[0] = Math.abs((360 - originalAngle + this.robot.getAngle()) * this.robot.DEG_TO_RAD);
    	}
    	
    	arcLength[1] = Math.abs(arcLength[0] * TurningRadius.getRadius());
    	
    	return arcLength;
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
	
	public ArrayList<Obstacle> getObstacleList() {
		return this.obstacleList;
	}
	
	public void setObstacleList(ArrayList<Obstacle> obstacleList) {
		this.obstacleList = obstacleList;
	}
	
	public ArrayList<Data> getPathCoordinates() {
		return this.coordinate;
	}
	
	public ArrayList<Data> getPathCoordinatesEnd() {
		return this.coordinateEnding;
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
	
	private Node findNode(double[] coord) {
		for (Node node : this.nodes) {
			if(round(node.getCoord()[0]) == round(coord[0]) && round(node.getCoord()[1]) == round(coord[1])) {
				return node;
			}
		}
		
		return null;
	}

}