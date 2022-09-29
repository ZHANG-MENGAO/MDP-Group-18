package mdp.g18.algo;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ArenaFrame extends JPanel implements ActionListener{
	
	public static final int NUM_OF_OBSTACLES = 5;
	private static final double DEG_TO_RAD = Math.PI / 180;
	
	private String stringOfObstacles = "";
	private ArrayList<Obstacle> obstacleObjects = new ArrayList<Obstacle>();
	private ArrayList<Obstacle> obstacleSimulator = new ArrayList<Obstacle>();
	private ArrayList<Obstacle> obstacleCompleted = new ArrayList<Obstacle>();
	private ArrayList<String> listOfPaths;
	
	private int previousId = -1;
	
	private String stringOfCoords = "";
	private ArrayList<String> listOfCoords;

	private String stringOfInstructions = "";
	private ArrayList<String> listOfInstructions;
	private ArrayList<String> instructionCompleted = new ArrayList<String>();;
	private ArrayList<String> pathComplete = new ArrayList<String>();
	
	private String currentPath;
	private String currentInstruction;
	private Obstacle currentObstacle;

	// list of obstacles
	public ArrayList<Node> nodes;
	
	// get mouse coordinates 
	public int mx = -100;
	public int my = -100;
	private double planDistance = 0;
	
	// Check for button pressed
	public boolean addObstacles = false;
	public boolean setImage = true;
	public boolean selectImage = false;
	public boolean running = false;
	public boolean start = false;
	public boolean loadObstacles = false;
	public boolean connectToRpi = false;
	
	private int sizeOfPath;
	private int iteration = 0;
	private double distance = 0;
	private double[] originalPosition;
	private double[] frontCenter = new double[2];

	private double originalAngle = 0;
	private boolean forwardComplete;
	private boolean reverseComplete;
	
	Direction mouseDir = Direction.UNSET;
	
	private Move move;
	private Click click;
	private RealRobot robot;
	private SimulatorRobot simrobot;
	private Obstacle obstacle;
	private Arena arena;
	private RPiClient client;
	private Timer timer;
	private Timer timer2;
	
	PathFinder pathfinder;
	
	public static int [][] obstacles = new int[Arena.GRIDNO][Arena.GRIDNO];
	
	ArenaFrame(){
		this.setPreferredSize(new Dimension(Arena.ARENA_WIDTH,Arena.ARENA_HEIGHT));

		move = new Move();
		this.addMouseMotionListener(move);
		
		click = new Click();
		this.addMouseListener(click);
		
		arena = new Arena();
		robot = new RealRobot(15,-15,0);
		
		client = new RPiClient();
		
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

		// Draw Obstacles all obstacles from string from Rpi
		if(this.loadObstacles) {
			for( Obstacle obstacle: obstacleObjects) {
				obstacle.paintObstacle(g, false);
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
		}
	}
	
	/*public void paintComponent(Graphics g) {
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
			
			if (loadObstacles) {
				obstacle.paintObstacle(g, false);
				obstacle.selectImage(g, false, obstacle.getDirection());
			}
			
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
	}*/

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
			if (obstacleObjects.size() < NUM_OF_OBSTACLES && addObstacles && coordinateX() != -1 && coordinateY() != -1 && obstacles[coordinateX()][coordinateY()] == 0) {
				obstacle = new Obstacle(getmaxID() + 1,coordinateX(),coordinateY(),Direction.UNSET);
				addObstacle(obstacle);
				obstacleObjects.add(obstacle);
				repaint();
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
	
	public void connectToRPI() {
		/*try {
			client.startConnection();
			System.out.println("RPi connected.");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		this.waitForRPIMessage();
	}
	
	// Wait for RPI to send message
	public void waitForRPIMessage() {
		this.connectToRpi = true;
		
		while(this.connectToRpi) {
			//this.stringOfObstacles = client.receiveMsg();
			//this.stringOfObstacles = "153,50,-90,1";
			//if (!client.receiveMsg().isEmpty()) { // length of message more than 1 ==> string of obstacle
			//	this.stringOfObstacles = client.receiveMsg();
			//	System.out.print(this.stringOfObstacles);
			//	this.connectToRpi = false;
			//}
			this.stringOfObstacles = "50,90,-90,1:70,140,180,2:120,90,0,3:150,150,-90,4:150,40,180,5";
			// TODO comment out
			this.connectToRpi = false;
		}
	}
	
	public void sendRPIMessage() {
		
		for(String s: listOfPaths) {
			client.sendMsg(s);
			waitForRPIMessage();
		}
		
		//client.sendMsg(stringOfInstructions);
	}
	
	public void drawObstacles(){
		// add obstacles from RPI message
		ArrayList<Object[]> msg = client.processString(this.stringOfObstacles);
		
		for (Object[] o : msg) {
			Obstacle obstacle = new Obstacle((Integer) o[0], (Integer)o[1], (Integer)o[2], (Direction) o[3]);
			addObstacle(obstacle);
			obstacleObjects.add(obstacle);
		}
		
		if(obstacleObjects.size() == NUM_OF_OBSTACLES) {
			this.loadObstacles = false;
		}
			
		repaint();
	}
	
	public void findBestPath() {
		
		if (this.pathfinder == null) {
			this.simrobot = new SimulatorRobot(this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY(),this.robot.getAngle()); // create simulator robot --> no visualization
			this.pathfinder = new PathFinder(this.simrobot,this.obstacleObjects);
		}
		
		if (this.pathfinder.getCurrentObstacle() == null)
			this.pathfinder.getClosestObstacle();
		
		if(this.simrobot != null && this.pathfinder.getCurrentObstacle() != null) {
			Path bestPath = this.pathfinder.bestPath(); // found best path
			if(bestPath != null) {
				obstacleSimulator.add(this.pathfinder.getCurrentObstacle()); // add to obstacle completed in simulator
				
				// Add path to string
				this.pathToString(bestPath, this.pathfinder.getCurrentObstacle().getObstacleID());
				this.previousId = this.pathfinder.getCurrentObstacle().getObstacleID();
				
				// Add ending coordinates to string
				this.coordToString(this.pathfinder.getpathCoordinatesEnd());
					
				this.planDistance += bestPath.getDist();
				LabelFrame.distLabel.setText(String.format("Planned Distance : %.2f",this.planDistance));
			
				if (obstacleSimulator.size() == obstacleObjects.size()) {
					this.running = false;
					listOfPaths = new ArrayList<String>(Arrays.asList(this.stringOfInstructions.split(":")));
					listOfCoords = new ArrayList<String>(Arrays.asList(this.stringOfCoords.split(",:")));
					
					for (int i = 0; i < listOfPaths.size() ; i++) {
						System.out.println(listOfPaths.get(i));
						System.out.println(listOfCoords.get(i));
					}
				}
				else {
					this.pathfinder.getClosestObstacle();
				}
			} else {
				this.pathfinder.getNextClosestObstacle();
			}
		}
	}
	
	// obstacleID previous id
	// last instruction f000, obs, last id
	public void pathToString(Path path, int obstacleID){
		
		if(path != null) {
			for(Instruction instruction: path.getInstructions()) {
				String newString = "";
				if(this.stringOfInstructions.isEmpty()) {
					this.stringOfInstructions += "STMI,";
				}

				// direction, delimiter, distance, angle
				double angle = instruction.getAngle();
				if (angle < 0)
					angle = -angle;
				switch(instruction.getTurnDirection()) {
				case "S":
					newString += "f";
					newString += String.format("%03d",round(instruction.getDistance()));
					break;
				case "R":
					newString += "d";
					newString += String.format("%03d",round(instruction.getAngle() / DEG_TO_RAD));
					break;
				case "L":
					newString += "a";
					newString += String.format("%03d",round(instruction.getAngle() / DEG_TO_RAD));
					break;
				case "Re":
					newString += "b";
					newString += String.format("%03d",round(instruction.getDistance()));
					break;
				case "ReL":
					newString += "q";
					newString += String.format("%03d",round(instruction.getAngle() / DEG_TO_RAD));
					break;
				case "ReR":
					newString += "e";
					newString += String.format("%03d",round(instruction.getAngle() / DEG_TO_RAD));
					break;
				}
				this.stringOfInstructions += newString;
				
				if (instruction.getDelimiter() == "p") {
					if(this.previousId != -1)
						this.stringOfInstructions += String.format(",obs,%d",this.previousId);
					this.stringOfInstructions += ":STMI,";
					
					/*if(obstacleSimulator.size() == obstacleObjects.size()) {
						this.stringOfInstructions += "f000";
						this.stringOfInstructions += String.format(",obs,%d",obstacleID);
						break;
					}*/
				}
				else{
					this.stringOfInstructions += ",";
				}
			}
			
			if(obstacleSimulator.size() == obstacleObjects.size()) {
				this.stringOfInstructions += "f000";
				this.stringOfInstructions += String.format(",obs,%d",obstacleID);
			}
		}
	}
	
	//ANDROID|ROBOT,x,y,ori
	public void coordToString(ArrayList<Data> data){
		if (data != null) {
			for(Data d: data) {
				if(this.stringOfCoords.isEmpty()) {
					this.stringOfCoords += "ANDROID|";
				}
				if(d.getCoordinates()[0] == -1 && d.getCoordinates()[1] == -1 && d.getOrientation() == 'N') {
					this.stringOfCoords += ":ANDROID|";
				}
				else{
					this.stringOfCoords += String.format("ROBOT,%d,%d,%c", d.getCoordinates()[0],-d.getCoordinates()[1],d.getOrientation());
					
					if(data.indexOf(d) != data.size() - 1 || obstacleSimulator.size() != obstacleObjects.size()) {
						this.stringOfCoords += ",";
					}
					
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
	
	public void getNewPath() {
		
		for(String s: this.listOfPaths) {
			if (!this.pathComplete.contains(s) && this.currentPath == null) {
				this.currentPath = s;
			}
		}
	}
	
	public void getCurrentInstruction() {
		
		if(this.currentPath != null && this.currentInstruction == null) {
			this.listOfInstructions = new ArrayList<String>(Arrays.asList(this.currentPath.split(",")));
			this.listOfInstructions.remove("STMI");
			this.listOfInstructions.remove("obs");
			this.listOfInstructions.remove(String.valueOf(1));
		}
		
		for(String s: this.listOfInstructions) {
			if (!this.instructionCompleted.contains(s) && this.currentInstruction == null) {
				this.currentInstruction = s;
			}
		}
	}
	
	public void performMovement(String instruction) {
		//System.out.println(instruction.substring(0, 1));
		//double tickNew = this.robot.getTick();
		
		if (instruction != null) {
			double distOrAngle = Double.parseDouble(instruction.substring(1, 4));
			
			switch(instruction.substring(0, 1)){
			
				case "f":
					if(this.originalPosition == null) {
						this.originalPosition = new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()};
					}
					
					this.robot.moveForward();
					this.distance = straightDistance(this.originalPosition, new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
					
					// reach distance
					if(this.distance >= distOrAngle) {
						instructionCompleted.add(this.currentInstruction);
						this.distance = 0;
						this.currentInstruction = null;
						this.originalPosition = null;
					}
					break;
					
				case "b":
					if(this.originalPosition == null) {
						this.originalPosition = new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()};
					}
					
					this.robot.reverseBackward();
					this.distance = straightDistance(this.originalPosition, new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
					
					// reach distance
					if(this.distance >= distOrAngle) {
						instructionCompleted.add(this.currentInstruction);
						this.distance = 0;
						this.currentInstruction = null;
						this.originalPosition = null;
					}
					break;	
					
				case "d":
					if (this.originalAngle == 0) {
						this.originalAngle = this.robot.getAngle();
						if (this.robot.getAngle() < 0) {
				    		this.originalAngle += 360;
				    	}
					}
			    	
					if (this.robot.turningRadius == null) {
						this.robot.createCircleRight(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
					}
					
					this.robot.turnRight();
					
					if ((Math.abs((this.robot.getAngle() - originalAngle) * this.robot.DEG_TO_RAD - Math.abs(distOrAngle) * this.robot.DEG_TO_RAD) <= 0.02) ||
		        			(Math.abs((360 - originalAngle + this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(distOrAngle) * this.robot.DEG_TO_RAD) <= 0.02)) {
						
						instructionCompleted.add(this.currentInstruction);
						this.originalAngle = 0;
						this.currentInstruction = null;
					}
					this.robot.turningRadius = null;
					break;
					
				case "a":
					if (this.originalAngle == 0) {
						this.originalAngle = this.robot.getAngle();
						if (this.robot.getAngle() > 0) {
				    		this.originalAngle -= 360;
				    	}
					}
					
					if (this.robot.turningRadius == null) {
						this.robot.createCircleLeft(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
					}
					
					this.robot.turnLeft();
					
					if ((Math.abs((this.originalAngle - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(distOrAngle) * this.robot.DEG_TO_RAD) <= 0.02) ||
		    				(Math.abs((this.originalAngle + 360 - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(distOrAngle) * this.robot.DEG_TO_RAD) <= 0.02)) {
	
						instructionCompleted.add(this.currentInstruction);
						this.originalAngle = 0;
						this.currentInstruction = null;
					}
					this.robot.turningRadius = null;
					break;
					
				case "e":
					if (this.originalAngle == 0) {
						this.originalAngle = this.robot.getAngle();
						if (this.robot.getAngle() < 0) {
				    		this.originalAngle += 360;
				    	}
					}
			    	
					if (this.robot.turningRadius == null) {
						this.robot.createCircleRight(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
					}
	
					this.robot.reverseRight();
					
					if ((Math.abs((this.robot.getAngle() - originalAngle) * this.robot.DEG_TO_RAD - Math.abs(distOrAngle) * this.robot.DEG_TO_RAD) <= 0.02) ||
		        			(Math.abs((360 - originalAngle + this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(distOrAngle) * this.robot.DEG_TO_RAD) <= 0.02)) {
						
						instructionCompleted.add(this.currentInstruction);
						this.originalAngle = 0;
						this.currentInstruction = null;
					}
					this.robot.turningRadius = null;
					break;
					
				case "q":
					if (this.originalAngle == 0) {
						this.originalAngle = this.robot.getAngle();
						if (this.robot.getAngle() > 0) {
				    		this.originalAngle -= 360;
				    	}
					}
					
					if (this.robot.turningRadius == null) {
						this.robot.createCircleLeft(new double[] {this.robot.getRobotCenter().getX(),this.robot.getRobotCenter().getY()});
					}
					
					this.robot.reverseLeft();
					
					if ((Math.abs((this.originalAngle - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(distOrAngle) * this.robot.DEG_TO_RAD) <= 0.02) ||
		    				(Math.abs((this.originalAngle + 360 - this.robot.getAngle()) * this.robot.DEG_TO_RAD - Math.abs(distOrAngle) * this.robot.DEG_TO_RAD) <= 0.02)) {
	
						instructionCompleted.add(this.currentInstruction);
						this.originalAngle = 0;
						this.currentInstruction = null;
					}
					this.robot.turningRadius = null;
					break;
					
				default:
					break;
			}
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
			
			System.out.println(this.currentPath);
			
			// if no instruction -> get instruction
			if (this.currentInstruction == null) {
				getCurrentInstruction();
				//for(String s: this.listOfInstructions) {
					//System.out.println(s);
				//}
				System.out.println(this.currentInstruction);
			}
		}

		if (this.currentInstruction != null) {
			
			performMovement(this.currentInstruction);
			
			if (this.currentObstacle != null) {
				// reach obstacle
				if (this.robot.sensor.scanObstacle(new double[] {this.currentObstacle.getObstacleCenter().getX(),this.currentObstacle.getObstacleCenter().getY()})) {
					obstacleCompleted.add(this.currentObstacle); 
					pathComplete.add(this.currentPath);
					this.currentObstacle = null;
					this.currentPath = null;
					this.currentInstruction = null;
					this.instructionCompleted.clear();
					this.listOfInstructions.clear();
					this.originalPosition = null;
					this.robot.turningRadius = null;
					this.originalAngle = 0;
					this.iteration = 0;
					this.distance = 0;
				}
			} else {
				
			}
			
			/*if (this.currentInstruction != null && this.currentInstruction.getTurnDirection() == "S" && this.currentInstruction.getDistance() == 15) {
				performMovement(this.currentInstruction.getTurnDirection(),this.currentInstruction.getAngle(),this.currentInstruction.getDistance(),this.frontCenter);
			}
			else if (this.currentInstruction != null && this.currentPath == null && this.currentInstruction.getTurnDirection() == "Re"){
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
						this.currentInstruction = new Instruction(this.robot.getAngle(),60,"Re","n");
					}
				}
				
			}*/
		}
	}
	
	/*public void perform() {
		//if (!this.robot.checkBoundaries()) {
		this.robot.turnLeft();
		//}
	}*/

	private static int round(double val) {
        return (int) Math.round(val);
    }
	
	public double straightDistance(double[] p1, double[] p2) {
		double x = Math.abs(p2[0] - p1[0]);
		double y = Math.abs(p2[1] - p1[1]);
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
	}
	
	// Add obstacle to array
	public void addObstacle(Obstacle obstacle) {

		for (int i = -39; i <= 0; i++) {
			for (int j = -39; j <= 0; j++) {
				if ((obstacle.getxVirtual() + i >= 0) && (obstacle.getyVirtual() + j >= 0) && (obstacle.getxVirtual() + i < 200) && (obstacle.getyVirtual() + j < 200)){
					obstacles[obstacle.getxVirtual() + i][obstacle.getyVirtual() + j] = obstacle.getObstacleID();
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
}