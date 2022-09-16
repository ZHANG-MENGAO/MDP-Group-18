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
	public List<Obstacle> obstacleObjects = new ArrayList<Obstacle>();
	public List<Obstacle> obstacleSimulator = new ArrayList<Obstacle>();
	public List<Obstacle> obstacleCompleted = new ArrayList<Obstacle>();
	public List<Path> pathList = new ArrayList<Path>();
	public List<Path> pathComplete = new ArrayList<Path>();
	public List<Instruction> instructionCompleted = new ArrayList<Instruction>();
	public List<Node> nodes;
	
	// get mouse coordinates 
	public int mx = -100;
	public int my = -100;
	private double planDistance = 0;
	
	// Check for button pressed
	public boolean addObstacles = false;
	public boolean setImage = true;
	public boolean clearObstacles = false;
	public boolean running = false;
	public boolean start = false;
	
	private Obstacle currentObstacle;
	private Path currentPath;
	private Instruction currentInstruction;
	private int sizeOfPath;
	private int iteration = 0;
	
	Direction mouseDir = Direction.UNSET;
	
	Move move;
	Click click;
	RealRobot robot;
	SimulatorRobot simrobot;
	Obstacle obstacle;
	Arena arena;
	Astar astar;
	Simulate simulator;
	Timer timer;
	Timer timer2;
	
	PathFinder pathfinder;
	
	public static int [][] obstacles = new int[Arena.GRIDNO][Arena.GRIDNO];
	
	ArenaFrame(){
		this.setPreferredSize(new Dimension(Arena.ARENA_WIDTH,Arena.ARENA_HEIGHT));

		move = new Move();
		this.addMouseMotionListener(move);
		
		click = new Click();
		this.addMouseListener(click);
		
		arena = new Arena();
		robot = new RealRobot(100,-16,0);
		
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
			timer2 = new Timer(500,this);
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
			//ControlFrame.yLabel.setText("Y Coordinate : " + String.valueOf(coordinateY()));
			LabelFrame.yLabel.setText("Y Coordinate : " + String.valueOf(Arena.GRIDNO - coordinateY() - 1));
			//System.out.println(obstacles[coordinateX()][coordinateY()]);
			repaint();
		}
		
		public Direction mouseDirection() {
			if (coordinateY() <= obstacle.getyCoordinate() - obstacle.getLength() && coordinateX() >= obstacle.getxCoordinate()) {
				return Direction.NORTH;
			} else if (coordinateY() >= obstacle.getyCoordinate() && coordinateX() <= obstacle.getxCoordinate() + obstacle.getLength()) {
				return Direction.SOUTH;
			} else if (coordinateY() >= obstacle.getyCoordinate() - obstacle.getLength() && coordinateX() >= obstacle.getxCoordinate() - obstacle.getLength()) {
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
				addObstacle();
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
			
			//move();
			perform();
		}
	    repaint();
	}
	
	public void perform() {
		if (!this.robot.checkBoundaries()) {
			this.robot.turnLeft();
			System.out.println(round(this.robot.getAngle()));
			//System.out.println(round(this.robot.getRobotCenter().getX()));
    		//System.out.println(round(this.robot.getRobotCenter().getY()));
		}
	}
	
	public void performMovement(String movement, double[] target) {
		//System.out.println(movement);
		double tickNew = this.robot.getTick();
		switch(movement){
			case "S":
				if (!this.robot.checkBoundaries()) {
					this.robot.moveForward();
				}
				break;
			case "R":
				if (this.robot.turningRadius == null) {
					this.robot.createCircleRight(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()}, "Forward");
				}
				if (!this.robot.checkBoundaries()) {
					this.robot.turnRight();
				}
				this.robot.turningRadius = null;
				break;
			case "L":
				if (this.robot.turningRadius == null) {
					this.robot.createCircleLeft(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()}, "Forward");
				}
				if (!this.robot.checkBoundaries()) {
					this.robot.turnLeft();
				}
				this.robot.turningRadius = null;
				break;
			default:
				break;
		}
		//System.out.println(round(this.robot.getRobotCenter().getX()));
		//System.out.println(round(this.robot.getRobotCenter().getY()));
		//System.out.println(round(target[0]));
		//System.out.println(round(target[1]));
		//System.out.println(this.currentInstruction.getTurnDirection());
		// scan obstacle or reach target
		if (Arrays.equals(new double[] {round(this.robot.getRobotCenter().getX()), round(this.robot.getRobotCenter().getY())},new double[] {round(target[0]),round(target[1])})) {
			this.iteration++;
			instructionCompleted.add(this.currentInstruction);
			this.currentInstruction = null;
		}
	}

	public void move() {
		
		// if no obstacle -> get obstacle
		if (this.currentObstacle == null)
			getTargetObstacle();
		
		// if no path -> get path
		if (this.currentPath == null) {
			getNewPath();
		}
		
		// if no instruction -> get instruction
		if (this.currentInstruction == null) {
			getCurrentInstruction();
		}
		
		if (this.currentPath != null) {
			this.sizeOfPath = this.currentPath.getInstructions().size();  // get size of instructions
		}

		if (this.currentInstruction != null || this.currentObstacle != null) {
			if (this.sizeOfPath == 1) {
				performMovement(this.currentInstruction.getTurnDirection(),new double[] {this.currentObstacle.getImageCenter().getX(),this.currentObstacle.getImageCenter().getY()});
			}
			// two instructions
			else if (this.sizeOfPath == 2) {
				if (iteration == 0) {
					performMovement(this.currentInstruction.getTurnDirection(),this.currentPath.getPt1());
				} 
				else {
					performMovement(this.currentInstruction.getTurnDirection(),new double[] {this.currentObstacle.getImageCenter().getX(),this.currentObstacle.getImageCenter().getY()});
				}
			} 
			// three instructions
			else {
				if (iteration == 0) {
					performMovement(this.currentInstruction.getTurnDirection(),this.currentPath.getPt1());
				} 
				else if (iteration == 1) {
					performMovement(this.currentInstruction.getTurnDirection(),this.currentPath.getPt2());
				} 
				else {
					performMovement(this.currentInstruction.getTurnDirection(),new double[] {this.currentObstacle.getImageCenter().getX(),this.currentObstacle.getImageCenter().getY()});
				}
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
			this.iteration = 0;
		}
	}
	
	public void getNewPath() {
		
		for(Path path: this.pathList) {
			if (!this.pathComplete.contains(path) && this.currentPath == null) {
				this.currentPath = path;
			}
		}
	}
	
	public void getCurrentInstruction() {
		
		for(Instruction instruction: this.currentPath.getInstructions()) {
			if (!this.instructionCompleted.contains(instruction) && this.currentInstruction == null) {
				this.currentInstruction = instruction;
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
	
	// from astar get node
	public void getClosestObstacle() {
		
		for(Obstacle obstacle: this.obstacleObjects) {
			if (!this.obstacleSimulator.contains(obstacle) && this.currentObstacle == null) {
				this.currentObstacle  = obstacle;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private Obstacle getObstacle(Node node) {
		 for (Obstacle obstacle : obstacleObjects) {
			 if (obstacle.getObstacleID() == node.getObstacleID()) {
				 return obstacle;
			 }
		 }
		 return null;
	 }
	
	public void findBestPath() {
		
		if (this.currentObstacle == null)
			getClosestObstacle();
			
		if (this.pathfinder == null) {
			this.simrobot = new SimulatorRobot(25,-16,0); // create simulator robot --> no visualization
			this.pathfinder = new PathFinder(ArenaFrame.obstacles); // input robot and nearest obstacle object
		}
		
		if(this.simrobot != null && this.currentObstacle != null) {
			// Set robot and obstacle
			this.pathfinder.setRobot(this.simrobot);
			this.pathfinder.setObstacle(this.currentObstacle);
	
			Path bestPath = this.pathfinder.bestPath(); // found best path
			pathList.add(bestPath);
				
			this.planDistance += bestPath.getDist();
			LabelFrame.distLabel.setText(String.format("Planned Distance : %.2f",this.planDistance));
		
			if (bestPath != null) {
				obstacleSimulator.add(this.currentObstacle); // remove from obstacle
				this.currentObstacle = null;
				/*System.out.println(bestPath.getDist());
				for (Instruction instruction: bestPath.getInstructions()) {
					System.out.println(instruction.getTurnDirection());
				}*/
			}
			
			if (obstacleSimulator.size() == obstacleObjects.size()) {
				running = false;
			}
		}
	}
	
	private static int round(double val) {
        return (int) Math.round(val);
    }
	
	// Add obstacle to array
	public void addObstacle() {
		// TODO: insert virtual obstacle into obstacles as -1
		for (int i = -10; i <= 0; i++) {
			for (int j = -10; j <= 0; j++) {
				obstacles[obstacle.getxCoordinate() + i][obstacle.getyCoordinate() + j] = obstacle.getObstacleID(); // top right coordinate
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
			if (obstacle.obstacleID > maxID) {
				maxID = obstacle.obstacleID;
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
}