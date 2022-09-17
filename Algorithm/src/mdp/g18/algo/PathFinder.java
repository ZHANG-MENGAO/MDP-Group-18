package mdp.g18.algo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PathFinder {
	
	private final double DEG_TO_RAD = Math.PI / 180; // conversion to degree
	private static List<String> bothDirection = Arrays.asList("R","L");
	
	ArrayList<Path> pathList = new ArrayList<>();
	public ArrayList<Path> bestPath = new ArrayList<Path>();
	ArrayList<Data> pathCoordinates = new ArrayList<Data>();
	
	ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>();
	ArrayList<Node> nodeList = new ArrayList<Node>();
	
	public SimulatorRobot robot;
	public Obstacle obstacle;
	public TurningRadius turningRadius;
	public Simulate simulator;
	public Astar astar;
	public Path path;
	public Path newBestPath;
	
	private double[] obstacleImageCenter = new double[2];
	private double[] robotCenter = new double[2];
	private int[][] obstacleCoordinates;
	
	PathFinder(SimulatorRobot robot,ArrayList<Obstacle> obstacleList, int[][] obstacleCoordinates){
		this.obstacleList = obstacleList;
		this.obstacleCoordinates = obstacleCoordinates;
		this.setRobot(robot);
		
		if (astar == null) {
			astar = new Astar(this.obstacleList,this.robot);
		}
		
		if(nodeList.isEmpty()) {
			this.nodeList = astar.createNodes();
		}
	}

	public void getClosestObstacle() {

		if (getCurrentObstacle() == null) {
			Node currentObstacle = astar.runAStar(this.nodeList.get(0),this.nodeList.get(this.nodeList.size() - 1)); // get closest obstacle
			
			// set new obstacle
			setObstacle(getObstacle(currentObstacle));
			setObstacleCenter();
			this.nodeList.remove(currentObstacle);
		}
	}
		
	private Obstacle getObstacle(Node node) {
		for (Obstacle obstacle : this.obstacleList) {
			if (obstacle.getObstacleID() == node.getObstacleID()) {
				return obstacle;
			}
		}
		return null;
	}
	
	public ArrayList<Path> getBestPath(){
		
		while(this.nodeList.size() > 1) {
			getClosestObstacle();
			bestPath();
			this.bestPath.add(this.newBestPath);
		}
		
		return this.bestPath;
	}
	
	public void bestPath() {
		ArrayList <Path> possiblePaths = possiblePaths(); // all possible path
		
		if(simulator == null) {
			simulator = new Simulate(this.obstacleCoordinates);
		}
		
		simulator.setObstacle(this.obstacle);
		simulator.setObstacleCenter();
		simulator.setPossiblePath(possiblePaths);
		simulator.updateRobot(this.robot);
		
		this.newBestPath = simulator.simulateMove();
		pathCoordinates.addAll(simulator.getPathCoordinates());
		setObstacle(null);
		pathList.clear();
		this.nodeList = astar.updateNodes(this.nodeList); // update nodes
	}
	
	public ArrayList<Path> possiblePaths() {
		
		listOfPath();
		Collections.sort(this.pathList);
		return this.pathList;
	}

	public void listOfPath(){
		Method[] methods = this.getClass().getMethods(); // get all methods

		for( int index =0; index < methods.length; index++){
			// find those that have find in method name
			if( methods[index].getName().contains("find")){ 
				try {
					// execute method
					methods[index].invoke(this);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) { 
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// S shortest path finding
	public void findS() {
		
		if (this.robot.checkObstacleFront(this.obstacle)) {
			
			Instruction instruction;
			
			path = new Path();

			double d = straightDistance(this.robotCenter, this.obstacleImageCenter);
			
			instruction = new Instruction(this.robot.getAngle() * DEG_TO_RAD,d,"S");
			path.addInstructions(instruction);
			path.setDist(d);
			this.pathList.add(path);
		}
	}
	
	// R & L shortest path finding
	public void findC () {
		double[] p1 = new double[2];
		double[] p2 = new double[2];
					
		double[] robotArc = new double[2];
		
		Instruction instruction;
					
		for (String direction: bothDirection) {
			
			double distance = 0;
			
			path = new Path();
			
			if (direction == "R") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleImageCenter);
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleImageCenter);
			}
						
			p1[0] = this.robot.turningRadius.getCenter().getX();
			p1[1] = this.robot.turningRadius.getCenter().getY();
						
			p2[0] = this.obstacle.getTurningRadius().getCenter().getX();
			p2[1] = this.obstacle.getTurningRadius().getCenter().getY();
				
			//robot can reach obstacle by turning
			if (this.robot.canReachTurn(p2)) {
				
				robotArc = computeArcLength(robotCenter,p1,obstacleImageCenter,direction);
				distance = robotArc[1];
				
				instruction = new Instruction(robotArc[0],distance,direction);

				path.addInstructions(instruction);
				path.setDist(distance);
				this.pathList.add(path);
			}
		}
	}
	
	
	// SL & SR shortest path finding
	public void findSC () {
		double p2[] = new double[2];
		double pt1[] = new double[2];
		
		double[] obstacleArc = new double[2];
		
		Instruction instruction1;
		Instruction instruction2;
				
		for (String direction: bothDirection) {
			
			double distance = 0;
			
			path = new Path();
			
			if (direction == "R") {
				this.obstacle.createCircleRight(obstacleImageCenter);
			} else {
				this.obstacle.createCircleLeft(obstacleImageCenter);
			}
			
			p2[0] = this.obstacle.getTurningRadius().getCenter().getX();
			p2[1] = this.obstacle.getTurningRadius().getCenter().getY(); 
				
			if (direction == "R") {
				if (this.robot.getAngle() == 0) {
					pt1 = new double[] {p2[0] - 25, p2[1]};
				} else if (this.robot.getAngle() == 180) {
					pt1 = new double[] {p2[0] + 25, p2[1]};
				} else if (this.robot.getAngle() == -90) {
					pt1 = new double[] {p2[0], p2[1] + 25};
				} else if (this.robot.getAngle() == 90) {
					pt1 = new double[] {p2[0], p2[1] - 25};
				}
			}
			else {
				if (this.robot.getAngle() == 0) {
					pt1 = new double[] {p2[0] + 25, p2[1]};
				} else if (this.robot.getAngle() == 180) {
					pt1 = new double[] {p2[0] - 25, p2[1]};
				} else if (this.robot.getAngle() == -90) {
					pt1 = new double[] {p2[0], p2[1] - 25};
				} else if (this.robot.getAngle() == 90) {
					pt1 = new double[] {p2[0], p2[1] + 25};
				}
			}

			 // Check if robot center can reach pt1
			if (this.robot.canReachStraightPoint(pt1)) {
				
				double d = straightDistance(this.robotCenter, pt1);
				obstacleArc = computeArcLength(pt1, p2, obstacleImageCenter, direction);
				distance = d + obstacleArc[1];
				
				instruction1 = new Instruction(this.robot.getAngle() * DEG_TO_RAD,d,"S");
				instruction2 = new Instruction(obstacleArc[0],obstacleArc[1],direction);
					
				path.addInstructions(instruction1);
				path.addInstructions(instruction2);
				path.setDist(distance);
				path.setPt1(pt1);
				this.pathList.add(path);
			}
		}
	}
	
	
	// RS & LS shortest path finding
	public void findCS () {
		double p1[] = new double[2];
		double pt1[] = new double[2];
		
		double[] robotArc = new double[2];
		
		Instruction instruction1;
		Instruction instruction2;
				
		for (String direction: bothDirection) {
			
			double distance = Double.POSITIVE_INFINITY;
			
			path = new Path();
			
			if (direction == "R") {
				this.robot.createCircleRight(robotCenter,"front");
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
			}
			
			p1[0] = this.robot.turningRadius.getCenter().getX();
			p1[1] = this.robot.turningRadius.getCenter().getY(); 
				
			if (direction == "R") {
				if (this.obstacle.getDirection() == Direction.SOUTH) {
					pt1 = new double[] {p1[0] - 25, p1[1]};
				} else if (this.obstacle.getDirection() == Direction.NORTH) {
					pt1 = new double[] {p1[0] + 25, p1[1]};
				} else if (this.obstacle.getDirection() == Direction.EAST) {
					pt1 = new double[] {p1[0], p1[1] + 25};
				} else if (this.obstacle.getDirection() == Direction.WEST) {
					pt1 = new double[] {p1[0], p1[1] - 25};
				}
			}
			else {
				if (this.obstacle.getDirection() == Direction.SOUTH) {
					pt1 = new double[] {p1[0] + 25, p1[1]};
				} else if (this.obstacle.getDirection() == Direction.NORTH) {
					pt1 = new double[] {p1[0] - 25, p1[1]};
				} else if (this.obstacle.getDirection() == Direction.EAST) {
					pt1 = new double[] {p1[0], p1[1] - 25};
				} else if (this.obstacle.getDirection() == Direction.WEST) {
					pt1 = new double[] {p1[0], p1[1] + 25};
				}
			}

			robotArc = computeArcLength(robotCenter, p1, pt1, direction);
			double d = straightDistance(pt1, obstacleImageCenter);
			distance = robotArc[1] + d;
				
			instruction1 = new Instruction(robotArc[0],robotArc[1],direction);
			instruction2 = new Instruction(robotArc[0],d,"S");
			
			//System.out.println(robotArc[0]);
					
			path.addInstructions(instruction1);
			path.addInstructions(instruction2);
			path.setDist(distance);
			path.setPt1(pt1);
			this.pathList.add(path);
		}
	}
	
	
	// RL & LR shortest path finding
	public void findCC () {
		double[] p1 = new double[2];
		double[] p2 = new double[2];
		double[] pt1 = new double[2];
				
		double[] robotArc = new double[2];
		double[] obstacleArc = new double[2];
		
		Instruction instruction1;
		Instruction instruction2;
				
		for (String direction: bothDirection) {
			
			double distance = 0;
			
			path = new Path();
			
			if (direction == "R") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleImageCenter);
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleImageCenter);
			}
					
			p1[0] = this.robot.turningRadius.getCenter().getX();
			p1[1] = this.robot.turningRadius.getCenter().getY();
					
			p2[0] = this.obstacle.getTurningRadius().getCenter().getX();
			p2[1] = this.obstacle.getTurningRadius().getCenter().getY();
			
			pt1 = midPoint(p1,p2);
			double d = straightDistance(p1, p2);		
			
			//robot can reach obstacle
			if (this.robot.canReachTurnTurn(d)) {
				
				robotArc = computeArcLength(robotCenter,p1,pt1,direction);
				
				if (direction == "R") {
					obstacleArc = computeArcLength(pt1,p2,obstacleImageCenter,"L");
					instruction2 = new Instruction(obstacleArc[0],obstacleArc[1],"L");
				} else {
					obstacleArc = computeArcLength(pt1,p2,obstacleImageCenter,"R");
					instruction2 = new Instruction(obstacleArc[0],obstacleArc[1],"R");
				}
				
				distance = robotArc[1] + obstacleArc[1];
				
				instruction1 = new Instruction(robotArc[0],robotArc[1],direction);
					
				path.addInstructions(instruction1);
				path.addInstructions(instruction2);
				path.setDist(distance);
				path.setPt1(pt1);
				this.pathList.add(path);
			}
		}
	}
	
	// LSL & RSR shortest path finding
	public void findCSCsameC () {
		
		double[] pt1 = new double[2];
		double[] pt2 = new double[2];
		double[] v1 = new double[2];
		double[] v2 = new double[2];
		double[] p1 = new double[2];
		double[] p2 = new double[2];
		
		// 0 for angle 1 for distance
		double[] robotArc = new double[2];
		double[] obstacleArc = new double[2];
		
		Instruction instruction1;
		Instruction instruction2;
		Instruction instruction3;
		
		for (String direction: bothDirection) {
			
			double distance = 0;
			
			path = new Path();
			
			if (direction == "R") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleImageCenter);
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleImageCenter);
			}
			
			p1[0] = this.robot.turningRadius.getCenter().getX();
			p1[1] = this.robot.turningRadius.getCenter().getY();
				
			p2[0] = this.obstacle.getTurningRadius().getCenter().getX();
			p2[1] = this.obstacle.getTurningRadius().getCenter().getY();
			
			double l = straightDistance(p1, p2);
			
			v1 = this.vector1(p1,p2);
			if (direction == "R") {
				v2 = this.vector2(v1, (Math.PI/2));
			} else {
				v2 = this.vector2(v1, -(Math.PI/2));
			}
			pt1 = this.pt1(p1, v2, l);
			pt2 = this.pt2(pt1, v1);

			robotArc = computeArcLength(robotCenter,p1,pt1,direction);
			obstacleArc = computeArcLength(pt2,p2,obstacleImageCenter,direction);
			distance = robotArc[1] + l + obstacleArc[1];
				
			instruction1 = new Instruction(robotArc[0],robotArc[1],direction);
			instruction2 = new Instruction(robotArc[0],l,"S");
			instruction3 = new Instruction(obstacleArc[0],obstacleArc[1],direction);
				
			path.addInstructions(instruction1);
			path.addInstructions(instruction2);
			path.addInstructions(instruction3);
			path.setDist(distance);
			path.setPt1(pt1);
			path.setPt2(pt2);
			this.pathList.add(path);
		}
	}
	
	// RSL & LSR shortest path finding
	public void findCSCdiffC () {
		double[] pt1 = new double[2];
		double[] pt2 = new double[2];
		double[] v1 = new double[2];
		double[] v2 = new double[2];
		double[] v3 = new double[2];
		double[] p1 = new double[2];
		double[] p2 = new double[2];
			
		double[] robotArc = new double[2];
		double[] obstacleArc = new double[2];
		double theta = 0;
		
		Instruction instruction1;
		Instruction instruction2;
		Instruction instruction3;
			
		for (String direction: bothDirection) {
			
			double distance = 0;
			
			path = new Path();
			
			if (direction == "R") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleImageCenter);
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleImageCenter);
			}
				
			p1[0] = this.robot.turningRadius.getCenter().getX();
			p1[1] = this.robot.turningRadius.getCenter().getY();
			
			p2[0] = this.obstacle.getTurningRadius().getCenter().getX();				
			p2[1] = this.obstacle.getTurningRadius().getCenter().getY();
				
			double d = straightDistance(p1, p2);
			double l = straightTravel(d,2 * TurningRadius.getRadius());
				
			theta = Math.acos(2 * TurningRadius.getRadius() / d);
			v1 = this.vector1(p1,p2);
				
			if (direction == "R") {
				v2 = this.vector2_version2(v1, theta);
			} else {
				v2 = this.vector2_version2(v1, -theta);
			}
			
			pt1 = this.pt1_version2(p1, v2, d);
			v3 = this.vector3(v2);
			pt2 = this.pt1_version2(p2, v3, d);
				
			if (direction == "R") {
				robotArc = computeArcLength(robotCenter,p1,pt1,direction);
				obstacleArc = computeArcLength(pt2,p2,obstacleImageCenter,"L");
				
				instruction3 = new Instruction(obstacleArc[0],obstacleArc[1],"L");
				
			} else {
				robotArc = computeArcLength(robotCenter,p1,pt1,direction);
				obstacleArc = computeArcLength(pt2,p2,obstacleImageCenter,"R");
				
				instruction3 = new Instruction(obstacleArc[0],obstacleArc[1],"R");
			}
			
			instruction1 = new Instruction(robotArc[0],robotArc[1],direction);
			instruction2 = new Instruction(robotArc[0],l,"S");
					
			distance = robotArc[1] + l + obstacleArc[1];
				
			path.addInstructions(instruction1);
			path.addInstructions(instruction2);
			path.addInstructions(instruction3);
			path.setDist(distance);
			path.setPt1(pt1);
			path.setPt2(pt2);
			this.pathList.add(path);
		}
	}
	
	// RLR & LRL shortest path finding(case 1)
	public void findCCCcase1 () {
		double[] q =  new double [2];
		double[] pt1 = new double[2];
		double[] pt2 = new double[2];
		double[] v1 = new double[2];
		double[] v2 = new double[2];
		double[] p1 = new double[2];
		double[] p2 = new double[2];
		double[] p3 = new double[2];
		double[] c3 = new double[2];
			
		double[] robotArc = new double[2];
		double[] obstacleArc = new double[2];
		double[] c3Arc = new double[2];
		
		Instruction instruction1;
		Instruction instruction2;
		Instruction instruction3;
			
		for (String direction: bothDirection) {
			
			double distance = 0;
			
			path = new Path();
			
			if (direction == "R") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleImageCenter);
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleImageCenter);
			}
				
			p1[0] = this.robot.turningRadius.getCenter().getX();
			p1[1] = this.robot.turningRadius.getCenter().getY();
				
			p2[0] = this.obstacle.getTurningRadius().getCenter().getX();
			p2[1] = this.obstacle.getTurningRadius().getCenter().getY();
				
			double d = straightDistance(p1, p2);
			
			if (d < 4 * TurningRadius.getRadius()) {
				q = this.midPoint(p1,p2);
				v1 = this.vector1(p1,p2);
				
				if(direction == "R") {
					v2 = this.vector2_version3(p1,p2);
				} else {
					v2 = this.vector2_version4(p1,p2);
				}
				
				double d1 = this.d1(TurningRadius.getRadius(),d);
				p3 = this.p3(q, v2, d1, d);
				pt1 = this.midPoint(p1, p3);
				pt2 = this.midPoint(p2, p3);
				
				if (this.robot.canReachTurnTurn(Math.ceil(straightDistance(p1, p3))) && this.robot.canReachTurnTurn(Math.ceil(straightDistance(p2, p3)))) {
					
					if (direction == "R") {
						turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
						c3[0] = (int) this.turningRadius.getCenter().getX();
						c3[1] = (int) this.turningRadius.getCenter().getY();
						c3Arc = computeArcLength(pt1,c3,pt2,"L");
						
						instruction2 = new Instruction(c3Arc[0],c3Arc[1],"L");
						
					} else {
						turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
						c3[0] = (int) this.turningRadius.getCenter().getX();
						c3[1] = (int) this.turningRadius.getCenter().getY();
						c3Arc = computeArcLength(pt1,c3,pt2,"R");
	
						instruction2 = new Instruction(c3Arc[0],c3Arc[1],"R");
					}
					
					robotArc = computeArcLength(robotCenter,p1,pt1,direction);
					obstacleArc = computeArcLength(pt2,p2,obstacleImageCenter,direction);
					
					distance = robotArc[1] + c3Arc[1] + obstacleArc[1];
					
					instruction1 = new Instruction(robotArc[0],robotArc[1],direction);
					instruction3 = new Instruction(obstacleArc[0],obstacleArc[1],direction);
					
					path.addInstructions(instruction1);
					path.addInstructions(instruction2);
					path.addInstructions(instruction3);
					path.setDist(distance);
					path.setPt1(pt1);
					path.setPt2(pt2);
					this.pathList.add(path);
				}
			}
		}
	}
	
	// RLR & LRL shortest path finding(case 2)
	public void findCCCcase2 () {
		double[] q = new double[2];
		double[] pt1 = new double[2];
		double[] pt2 = new double[2];
		double[] v1 = new double[2];
		double[] v2 = new double[2];
		double[] p1 = new double[2];
		double[] p2 = new double[2];
		double[] p3 = new double[2];
		double[] c3 = new double[2];
				
		double[] robotArc = new double[2];
		double[] obstacleArc = new double[2];
		double[] c3Arc = new double[2];
		
		Instruction instruction1;
		Instruction instruction2;
		Instruction instruction3;
				
		for (String direction: bothDirection) {
			
			double distance = 0;
			
			path = new Path();
			
			if (direction == "R") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleImageCenter);
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleImageCenter);
			}
					
			p1[0] = this.robot.turningRadius.getCenter().getX();
			p1[1] = this.robot.turningRadius.getCenter().getY();
					
			p2[0] = this.obstacle.getTurningRadius().getCenter().getX();
			p2[1] = this.obstacle.getTurningRadius().getCenter().getY();
					
			double d = straightDistance(p1, p2);
			
			if (d < 4 * TurningRadius.getRadius()) {
				q = this.midPoint(p1,p2);
				v1 = this.vector1(p1,p2);
					
				if(direction == "R") {
					v2 = this.vector2_version4(p1,p2);
				} else {
					v2 = this.vector2_version3(p1,p2);
				}
					
				double d1 = this.d1(TurningRadius.getRadius(),d);
				p3 = this.p3(q, v2, d1, d);
				pt1 = this.midPoint(p1, p3);
				pt2 = this.midPoint(p2, p3);
				
				if (this.robot.canReachTurnTurn(straightDistance(p1, p3)) && this.robot.canReachTurnTurn(straightDistance(p2, p3))) {
					
					if (direction == "R") {
						turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
						c3[0] = (int) this.turningRadius.getCenter().getX();
						c3[1] = (int) this.turningRadius.getCenter().getY();
						c3Arc = computeArcLength(pt1,c3,pt2,"L");
						
						instruction2 = new Instruction(c3Arc[0],c3Arc[1],"L");
					} else {
						turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
						c3[0] = (int) this.turningRadius.getCenter().getX();
						c3[1] = (int) this.turningRadius.getCenter().getY();
						c3Arc = computeArcLength(pt1,c3,pt2,"R");
	
						instruction2 = new Instruction(c3Arc[0],c3Arc[1],"R");
					}
					
					robotArc = computeArcLength(robotCenter,p1,pt1,direction);
					obstacleArc = computeArcLength(pt2,p2,obstacleImageCenter,direction);
					
					distance = robotArc[1] + c3Arc[1] + obstacleArc[1];
					
					instruction1 = new Instruction(robotArc[0],robotArc[1],direction);
					instruction3 = new Instruction(obstacleArc[0],obstacleArc[1],direction);
					
					path.addInstructions(instruction1);
					path.addInstructions(instruction2);
					path.addInstructions(instruction3);
					path.setDist(distance);
					path.setPt1(pt1);
					path.setPt2(pt2);
					this.pathList.add(path);
				}
			}
		}
	}
	
	public double[] vector1(double[] p1, double[] p2) {
		double[] v1 = new double[2];
		v1[0] = p2[0] - p1[0];
		v1[1] = p2[1]  - p1[1];
		return v1;
	}
	
	public double[] vector2(double[] v1, double theta) {
		double[] v2 = new double[2];
		v2[0] = v1[0] * Math.cos(theta) - v1[1] * Math.sin(theta);
		v2[1] = v1[0] * Math.sin(theta) + v1[1] * Math.cos(theta);
		return v2;
	}
	
	public double[] vector2_version2(double[] v1, double theta) {
		double[] v2 = new double[2];
		v2[0] = v1[0] * Math.cos(theta) + v1[1] * Math.sin(theta);
		v2[1] = -(v1[0] * Math.sin(theta) - v1[1] * Math.cos(theta));
		return v2;
	}
	
	public double[] vector2_version3(double[] p1, double[] p2) {
		double[] v2 = new double[2];
		v2[0] = p2[1] - p1[1];
		v2[1] = p1[0] - p2[0];
		return v2;
	}
	
	public double[] vector2_version4(double[] p1, double[] p2) {
		double[] v2 = new double[2];
		v2[0] = p1[1] - p2[1];
		v2[1] = p2[0] - p1[0];
		return v2;
	}
	
	public double[] vector3(double[] v2) {
		double[] v3 = new double[2];
		v3[0] = -v2[0];
		v3[1] = -v2[1];
		return v3;
	}
	
	public double[] pt1(double[] p1, double[] v2, double l) {
		double[] pt1 = new double[2];
		pt1[0] = p1[0] - (25.0/l * v2[0]);
		pt1[1] = p1[1] - (25.0/l * v2[1]);
		return pt1;
	}
	
	public double[] pt1_version2(double[] p1, double[] v2, double l) {
		double[] pt1 = new double[2];
		pt1[0] = p1[0] + (25.0/l * v2[0]);
		pt1[1] = p1[1] + (25.0/l * v2[1]);
		return pt1;
	}
	
	public double[] pt2(double[] pt1, double[] v1) {
		double[] pt2 = new double[2];
		pt2[0] = pt1[0] + v1[0];
		pt2[1] = pt1[1] + v1[1];
		return pt2;
	}
	
	public double straightDistance(double[] p1, double[] p2) {
		double x = Math.abs(p2[0] - p1[0]);
		double y = Math.abs(p2[1] - p1[1]);
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
	}
	
	public double straightTravel(double d, double r) {
		return Math.sqrt(Math.pow(d,2) - Math.pow(r,2));
	}
	
	public double[] midPoint(double[] v1, double[] v2) {
		double[] mid = new double[2];
		mid[0] = (v1[0] + v2[0]) / 2;
		mid[1] = (v1[1] + v2[1]) / 2;
		return mid;
	}
	
	public double d1(double r, double d) {
		return Math.sqrt(Math.pow(2 * r,2) - Math.pow(d / 2,2));
	}
	
	public double[] p3(double[] q, double[] v2, double d1, double d) {
		double[] p3 = new double[2];
		p3[0] = q[0] - (d1/d * v2[0]);
		p3[1] = q[1] - (d1/d * v2[1]);
		return p3;
	}
	
	public double[] computeArcLength(double[] p, double[] p1, double[] pt1, String direction) {
		
		double[] v1 = new double[2];
		double[] v2 = new double[2];
		double[] arcLength = new double[2]; // angle in radians, arcLength
		
		v1[0] = p[0] - p1[0];
		v1[1] = (-p[1])  - (-p1[1]);
		v2[0] = pt1[0] - p1[0];
		v2[1] = (-pt1[1])  - (-p1[1]);

		arcLength[0] = Math.atan2(v2[1], v2[0]) - Math.atan2(v1[1], v1[0]);
		
		if (arcLength[0] < 0 && direction == "L") {
			arcLength[0] += 2 * Math.PI;
		} else if (arcLength[0] > 0 && direction == "R"){
			arcLength[0] -= 2 * Math.PI;
		}
		
		//System.out.println(arcLength[0]);
		arcLength[1] = Math.abs(arcLength[0] * TurningRadius.getRadius());
		return arcLength;
	}
	
	private static int round(double val) {
        return (int) Math.round(val);
    }
	
	public void setObstacleCenter() {
		obstacleImageCenter[0] = round(this.obstacle.getImageCenter().getX());
		obstacleImageCenter[1] = round(this.obstacle.getImageCenter().getY());
	}
	
	public double[] getObstacleCenter() {
		return obstacleImageCenter;
	}
	
	public void setRobotCenter() {
		robotCenter[0] = this.robot.getCenterFront().getX();
		robotCenter[1] = this.robot.getCenterFront().getY();
	}
	
	public double[] getRobotCenter() {
		return robotCenter;
	}
	
	public void setObstacle(Obstacle obstacle) {
		this.obstacle = obstacle;
	}
	
	public Obstacle getCurrentObstacle() {
		return this.obstacle;
	}
	
	public void setRobot(SimulatorRobot robot) {
		this.robot = robot;
		setRobotCenter();
	}
	
	public void paintPath(Graphics g) {
		g.setColor(Color.red);
		for(Data path: pathCoordinates) {
			g.fillRect((path.getElement(0)) * Arena.UNIT_SIZE, (200 + path.getElement(1)) * Arena.UNIT_SIZE + Arena.UNIT_SIZE, Arena.UNIT_SIZE, Arena.UNIT_SIZE);
		}
	}

}
