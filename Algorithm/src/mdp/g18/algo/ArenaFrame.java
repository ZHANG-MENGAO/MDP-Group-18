package mdp.g18.algo;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaFrame extends JPanel implements ActionListener{

	// list of obstacles
	public ArrayList<Obstacle> obstacleObjects = new ArrayList<Obstacle>();
	public ArrayList<Obstacle> obstacleSimulator = new ArrayList<Obstacle>();
	public ArrayList<Obstacle> obstacleCompleted = new ArrayList<Obstacle>();
	public ArrayList<Path> pathList = new ArrayList<Path>();
	public ArrayList<Path> pathComplete = new ArrayList<Path>();
	public ArrayList<Instruction> instructionCompleted = new ArrayList<Instruction>();
	public ArrayList<Node> nodes;
	
	// get mouse coordinates 
	public int mx = -100;
	public int my = -100;
	private double planDistance = 0;
	
	// Check for button pressed
	public boolean addObstacles = false;
	public boolean setImage = true;
	public boolean selectImage = false;
	public boolean clearObstacles = false;
	public boolean running = false;
	public boolean start = false;
	
	private Obstacle currentObstacle;
	private Path currentPath;
	private Instruction currentInstruction;
	private int sizeOfPath;
	private int iteration = 0;
	private double distance = 0;
	private double[] originalPosition;
	private double[] frontCenter = new double[2];
	private double[] backCenter = new double[2];
	private double originalAngle = 0;
	private boolean forwardComplete;
	private boolean reverseComplete;
	
	Direction mouseDir = Direction.UNSET;
	
	Move move;
	Click click;
	RealRobot robot;
	SimulatorRobot simrobot;
	Obstacle obstacle;
	Arena arena;
	Simulate simulator;
	Timer timer;
	Timer timer2;
	
	PathFinder pathfinder;
	
	private static int [][] obstacles = new int[Arena.GRIDNO][Arena.GRIDNO];
	
	ArenaFrame(){
		this.setPreferredSize(new Dimension(Arena.ARENA_WIDTH,Arena.ARENA_HEIGHT));

		move = new Move();
		this.addMouseMotionListener(move);
		
		click = new Click();
		this.addMouseListener(click);
		
		arena = new Arena();
		robot = new RealRobot(15,-15,0);
		
		// Initialize no obstacles
		for(int i = 0; i < Arena.GRIDNO; i++) {
			for(int j = 0; j < Arena.GRIDNO; j++) {
				obstacles[i][j] = 0;
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		arena.paintArena(g);
		robot.drawRobot(g);
		
		if (addObstacles) {
			obstacle = new Obstacle(getmaxID() + 1,coordinateX(),coordinateY(),Direction.UNSET);
			obstacle.paintObstacle(g, true);
		}
		
		// Draw Obstacles
		for( Obstacle obstacle: obstacleObjects) {
			obstacle.paintObstacle(g, false);
			mouseDir = move.mouseDirection();
			
			// Enables selection of the side the image
			if (obstacle.getDirection() == Direction.UNSET) {
				if (setImage) {
					// Remove outline of adding obstacles
					addObstacles = false;

					// Paint image selection animation on frame
					obstacle.selectImage(g, true, mouseDir);
				}
				else {
					// Paint image location in correct color and set direction of image
					obstacle.selectImage(g, false, mouseDir);
								// Allow image selection on next obstacle
					setImage = true;
					if (obstacleObjects.size() < 5) {
							addObstacles = true;
					}
				}
			}
			else {
				obstacle.selectImage(g, false, obstacle.getDirection());
			}
		}
		
		if (pathfinder != null) {
			pathfinder.paintPath(g);
		}
		
		// Simulation
		if(running) {
			timer = new Timer(500,this);
			timer.start();
		}
		
		if(start) {
			timer2 = new Timer(5000,this);
			timer2.start();
			
			//if (obstacleCompleted.size() == obstacleSimulator.size()) {
			//	start = false;
			//}
		}
	}

	public class Move implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
			LabelFrame.xLabel.setText("X Coordinate : " + String.valueOf(coordinateX()));
			LabelFrame.yLabel.setText("Y Coordinate : " + String.valueOf(Arena.GRIDNO - coordinateY() - 1));
			repaint();
		}
		
		public Direction mouseDirection() {
			if (coordinateY() <= obstacle.getyCoordinate() - obstacle.getLength() && coordinateX() >= obstacle.getxCoordinate() - obstacle.getLength()) {
				return Direction.NORTH;
			} else if (coordinateY() >= obstacle.getyCoordinate() && coordinateX() <= obstacle.getxCoordinate()) {
				return Direction.SOUTH;
			} else if (coordinateY() >= obstacle.getyCoordinate() - obstacle.getLength() && coordinateX() >= obstacle.getxCoordinate()) {
				return Direction.EAST;
			} else {
				return Direction.WEST;
			}
		}
	}
	
	public class Click implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			// click add obstacle
			if (obstacleObjects.size() < 5 && addObstacles && coordinateX() != -1 && coordinateY() != -1 && obstacles[coordinateX()][coordinateY()] == 0) {
				obstacle = new Obstacle(getmaxID() + 1,coordinateX(),coordinateY(),Direction.UNSET);
				addObstacle(obstacle);
				obstacleObjects.add(obstacle);
				repaint();
			}
			
			// click clear obstacle
			if (obstacleObjects.size() > 0  && clearObstacles && coordinateX() != -1 && coordinateY() != -1 && obstacles[coordinateX()][coordinateY()] != 0) {
				for( Obstacle obstacle: obstacleObjects) {
					if (obstacle.getObstacleID() == obstacles[coordinateX()][coordinateY()]) {
						removeObstacle(obstacle.getObstacleID());
						obstacleObjects.remove(obstacle);
						repaint();
						break;
					}
				}
			}
			
			// click image direction on obstacle
			if (setImage && !addObstacles) {
				setImage = false;
				repaint();
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			findBestPath();
		}
		
		if (start) {
			move();
			//perform();
		}
	    repaint();
	}
	
	public void perform() {
		if (!this.robot.checkBoundaries()) {
			this.robot.turnLeft();
		}
	}
	
	public double straightDistance(double[] p1, double[] p2) {
		double x = Math.abs(p2[0] - p1[0]);
		double y = Math.abs(p2[1] - p1[1]);
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
	}
	
	public void performMovement(String movement,double angle, double distance ,double[] target) {
		//System.out.println(movement);
		double tickNew = this.robot.getTick();
		switch(movement){
			case "S":
				System.out.println(this.distance);
				if(this.originalPosition == null) {
					this.originalPosition = new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()};
				}
				
				if (!this.robot.checkBoundaries()) {
					this.robot.moveForward();
				}
				
				this.distance = straightDistance(this.originalPosition, new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
				
				if (Arrays.equals(new double[] {round(this.robot.getRobotCenter().getX()), round(this.robot.getRobotCenter().getY())},new double[] {round(target[0]),round(target[1])}) ||
						this.distance >= this.currentInstruction.getDistance()) {
					
					// move forward to turn
					if(this.forwardComplete) {
						this.iteration++;
						instructionCompleted.add(this.currentInstruction);
					}
					else {
						this.forwardComplete = true;
					}
					this.distance = 0;
					this.currentInstruction = null;
					this.originalPosition = null;
				}
				break;
			case "Re":
				System.out.println(this.distance);
				if(this.originalPosition == null) {
					this.originalPosition = new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()};
				}
				System.out.println(this.originalPosition[0]);
				System.out.println(this.originalPosition[1]);
				if (!this.robot.checkBoundaries()) {
					this.robot.reverseBackward();
				}
				
				this.distance = straightDistance(this.originalPosition, new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
				
				if (Arrays.equals(new double[] {round(this.robot.getRobotCenter().getX()), round(this.robot.getRobotCenter().getY())},new double[] {round(target[0]),round(target[1])}) ||
						this.distance >= 40) {

					this.reverseComplete = true;
					this.distance = 0;
					this.currentInstruction = null;
					this.originalPosition = null;
				}
				break;	
			case "R":
				
				if (this.originalAngle == 0) {
					this.originalAngle = this.robot.getAngle();
					if (this.robot.getAngle() < 0) {
			    		this.originalAngle += 360;
			    	}
				}
		    	
				if (this.robot.turningRadius == null) {
					this.robot.createCircleRight(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()}, "Forward");
				}
				
				if (!this.robot.checkBoundaries()) {
					this.robot.turnRight();
				}
				
				if ((Math.abs(this.robot.getRobotCenter().getX() - target[0]) <= 0.08 && Math.abs(this.robot.getRobotCenter().getY() + target[1]) <= 0.08) ||
						(Math.abs((this.robot.getAngle() - originalAngle) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
	        			(Math.abs((360 - originalAngle + this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02)) {
					
					this.iteration++;
					this.originalAngle = 0;
					instructionCompleted.add(this.currentInstruction);
					this.currentInstruction = null;
				}
				this.robot.turningRadius = null;
				break;
				
			case "L":
				
				if (this.originalAngle == 0) {
					this.originalAngle = this.robot.getAngle();
					if (this.robot.getAngle() > 0) {
			    		this.originalAngle -= 360;
			    	}
				}
				
				if (this.robot.turningRadius == null) {
					this.robot.createCircleLeft(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()}, "Forward");
				}
				
				if (!this.robot.checkBoundaries()) {
					this.robot.turnLeft();
				}
				
				if ((Math.abs(this.robot.getRobotCenter().getX() - target[0]) <= 0.08 && Math.abs(this.robot.getRobotCenter().getY() + target[1]) <= 0.08) ||
						(Math.abs((this.originalAngle - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02) ||
	    				(Math.abs((this.originalAngle + 360 - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(angle)) <= 0.02)) {
					
					this.iteration++;
					this.originalAngle = 0;
					instructionCompleted.add(this.currentInstruction);
					this.currentInstruction = null;
				}
				this.robot.turningRadius = null;
				break;
			default:
				break;
		}
	}

	public void move() {
		
		// if no obstacle -> get obstacle
		if (this.currentObstacle == null && this.currentInstruction == null)
			getTargetObstacle();
		
		// if no path -> get path
		if (this.currentPath == null && this.currentInstruction == null) {
			getNewPath();
		}
		
		if (this.currentPath != null) {
			this.sizeOfPath = this.currentPath.getInstructions().size();  // get size of instructions
			
			// if no instruction -> get instruction
			if (this.currentInstruction == null) {
				getCurrentInstruction();
			}
		}

		if (this.currentInstruction != null || this.currentObstacle != null) {
			
			if (this.currentInstruction.getTurnDirection() == "S" && this.currentInstruction.getDistance() == 15) {
				performMovement(this.currentInstruction.getTurnDirection(),this.currentInstruction.getAngle(),this.currentInstruction.getDistance(),this.frontCenter);
			}
			else if (this.currentPath == null && this.currentInstruction.getTurnDirection() == "Re"){
				performMovement(this.currentInstruction.getTurnDirection(),this.currentInstruction.getAngle(),this.currentInstruction.getDistance(),this.backCenter);
			}
			else if (this.currentPath != null){
				if (this.sizeOfPath == 1) {
					performMovement(this.currentInstruction.getTurnDirection(),this.currentInstruction.getAngle(),this.currentInstruction.getDistance(),new double[] {this.currentObstacle.getObstacleCenter().getX(),this.currentObstacle.getObstacleCenter().getY()});
				}
				// two instructions
				else if (this.sizeOfPath == 2) {
					if (iteration == 0) {
						performMovement(this.currentInstruction.getTurnDirection(),this.currentInstruction.getAngle(),this.currentInstruction.getDistance(),this.currentPath.getPt1());
					} 
					else {
						performMovement(this.currentInstruction.getTurnDirection(),this.currentInstruction.getAngle(),this.currentInstruction.getDistance(),new double[] {this.currentObstacle.getObstacleCenter().getX(),this.currentObstacle.getObstacleCenter().getY()});
					}
				} 
				// three instructions
				else {
					if (iteration == 0) {
						performMovement(this.currentInstruction.getTurnDirection(),this.currentInstruction.getAngle(),this.currentInstruction.getDistance(),this.currentPath.getPt1());
					} 
					else if (iteration == 1) {
						performMovement(this.currentInstruction.getTurnDirection(),this.currentInstruction.getAngle(),this.currentInstruction.getDistance(),this.currentPath.getPt2());
					} 
					else {
						performMovement(this.currentInstruction.getTurnDirection(),this.currentInstruction.getAngle(),this.currentInstruction.getDistance(),new double[] {this.currentObstacle.getObstacleCenter().getX(),this.currentObstacle.getObstacleCenter().getY()});
					}
				}
				
				// reach obstacle
				if (this.robot.sensor.scanObstacle(new double[] {this.currentObstacle.getObstacleCenter().getX(),this.currentObstacle.getObstacleCenter().getY()})) {
					obstacleCompleted.add(this.currentObstacle); 
					pathComplete.add(this.currentPath);
					this.currentObstacle = null;
					this.currentPath = null;
					this.currentInstruction = null;
					this.instructionCompleted.clear();
					this.originalPosition = null;
					this.robot.turningRadius = null;
					this.originalAngle = 0;
					this.iteration = 0;
					this.distance = 0;
					
					if(!this.reverseComplete) {
						setBackCenter(new double[] {this.robot.getCenterBack().getX(),this.robot.getCenterBack().getY()});
						this.currentInstruction = new Instruction(this.robot.getAngle(),40,"Re");
					}
				}
				
			}
		}
	}
	
	public void getNewPath() {
		
		for(Path path: this.pathList) {
			if (!this.pathComplete.contains(path) && this.currentPath == null) {
				this.reverseComplete = false;
				this.forwardComplete = false;
				this.currentPath = path;
			}
		}
	}
	
	public void getCurrentInstruction() {
		
		for(Instruction instruction: this.currentPath.getInstructions()) {
			if (!this.instructionCompleted.contains(instruction) && this.currentInstruction == null) {
				this.currentInstruction = instruction;
				
				// if need to turn, move forward by 15
				if (this.currentInstruction.getTurnDirection() != "S" && this.iteration == 0 && !this.forwardComplete) {
					setFrontCenter(new double[] {this.robot.getCenterFront().getX(),this.robot.getCenterFront().getY()});
					this.currentInstruction = new Instruction(this.robot.getAngle(),15,"S");
				}
			}
		}
	}

	public void getTargetObstacle() {
		
		for(Obstacle obstacle: this.obstacleSimulator) {
			if (!this.obstacleCompleted.contains(obstacle) && this.currentObstacle == null) {
				this.currentObstacle  = obstacle;
			}
		}
	}
	
	public void findBestPath() {
		
		if (this.pathfinder == null) {
			this.simrobot = new SimulatorRobot(this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY(),this.robot.getAngle()); // create simulator robot --> no visualization
			this.pathfinder = new PathFinder(this.simrobot,this.obstacleObjects,ArenaFrame.obstacles);
		}
		
		if (this.pathfinder.getCurrentObstacle() == null)
			this.pathfinder.getClosestObstacle();
		
		if(this.simrobot != null && this.pathfinder.getCurrentObstacle() != null) {
			Path bestPath = this.pathfinder.bestPath(); // found best path
			if(bestPath != null) {
				pathList.add(bestPath);
					
				this.planDistance += bestPath.getDist();
				LabelFrame.distLabel.setText(String.format("Planned Distance : %.2f",this.planDistance));
			
				obstacleSimulator.add(this.pathfinder.getCurrentObstacle()); // remove from obstacle
				if (obstacleSimulator.size() == obstacleObjects.size()) {
					this.running = false;
				}
				else {
					this.pathfinder.getClosestObstacle();
				}
			} else {
				this.pathfinder.getNextClosestObstacle();
			}
			
		}
	}
	
	private static int round(double val) {
        return (int) Math.round(val);
    }
	
	// Add obstacle to array
	public void addObstacle(Obstacle obstacle) {

		for (int i = -9; i <= 0; i++) {
			for (int j = -9; j <= 0; j++) {
				if ((obstacle.getxCoordinate() + i >= 0) && (obstacle.getyCoordinate() + j >= 0) && (obstacle.getxCoordinate() + i < 200) && (obstacle.getyCoordinate() + j < 200)){
					obstacles[obstacle.getxCoordinate() + i][obstacle.getyCoordinate() + j] = obstacle.getObstacleID();
				}
			}
		}
	}
	
	// remove obstacle from array
	public void removeObstacle(int obstacleID) {

		for (int i = 0; i < Arena.GRIDNO; i++) {
			for (int j = 0; j < Arena.GRIDNO; j++) {
				if (obstacles[i][j] == obstacleID) {
					obstacles[i][j] = 0;
				}
			}
		}
	}
	
	// remove obstacle from array
	public int getmaxID() {
		int maxID = 0;
		for( Obstacle obstacle: obstacleObjects) {
			if (obstacle.getObstacleID() > maxID) {
				maxID = obstacle.getObstacleID();
			}
		}
		return maxID;
	}
	
	// Find x-coordinate of mouse
	public int coordinateX() {
		for(int i = 0; i < Arena.GRIDNO; i++) {
			for(int j = 0; j < Arena.GRIDNO; j++) {
				if ((mx >= i * Arena.UNIT_SIZE) && (mx < i * Arena.UNIT_SIZE + Arena.UNIT_SIZE) && (my >= j * Arena.UNIT_SIZE + Arena.UNIT_SIZE) && (my < j * Arena.UNIT_SIZE + Arena.UNIT_SIZE + Arena.UNIT_SIZE)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	// Find y-coordinate of mouse
	public int coordinateY() {
		for(int i = 0; i < Arena.GRIDNO; i++) {
			for(int j = 0; j < Arena.GRIDNO; j++) {
				if ((mx >= i * Arena.UNIT_SIZE) && (mx < i * Arena.UNIT_SIZE + Arena.UNIT_SIZE) && (my >= j * Arena.UNIT_SIZE + Arena.UNIT_SIZE) && (my < j * Arena.UNIT_SIZE + Arena.UNIT_SIZE + Arena.UNIT_SIZE)) {
					return j;
				}
			}
		}
		return -1;
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

}