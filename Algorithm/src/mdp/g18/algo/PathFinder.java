package mdp.g18.algo;

// import java.awt.geom.Point2D;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;

// public class PathFinder {
//	 public List<Obstacle> obstacleObjects;
//	 Arena arena;
//	 Robot robot;
//
//	 public static final int turnRad = 25;
//
//	 PathFinder(List<Obstacle> obstacles, Arena arena, Robot robot) {
//		 this.obstacleObjects = obstacles;
//		 this.arena = arena;
//		 this.robot = robot;
//	 }
//
//	 Astar Astar = new Astar(obstacleObjects, arena, robot);
//
//	 public void findBestPath() {
//		 // run A* to get next goal node
//		 List<Node> nodes = Astar.createNodes();
//		 int[] robotCurrCoord = nodes.get(0).getCoord();
//		 Node nextNode = Astar.runAStar(nodes.get(0), nodes.get(nodes.size() - 1));
//		 int[] nextCoord = nextNode.getCoord();
//		 Direction nextImgDir = getImgDir(nextNode);
//
//		 // If image is in front of robot, move robot forward
//		 if (isGoalAcrossRobot(robot, nextCoord, nextImgDir)) {
//			 // TODO: check if need to add loop for moveForward
//			 robot.moveForward();
//		 }
//
//		 // If image is too close to an obstacle, reverse
//		 if (ArenaFrame.obstacles[robotCurrCoord[0]][robotCurrCoord[1]] != 0) {
//			 robot.reverse();
//		 }
//
//		 // Get best path
//		 Path bestPath = calculatePath(nodes.get(0), nextNode);
//
//		 // TODO: how to execute path???
//
//	 }
//
//	 public Path calculatePath(Node currNode, Node nextNode) {
//		 int[] currCoord = currNode.getCoord();
//		 int[] nextCoord = nextNode.getCoord();
//
//		 Direction imageDir = getImgDir(nextNode);
//
//		 boolean left = true;
//		 // TODO: initialise path array
//		 List<Path> pathArray = new ArrayList<Path>();
//
//		 while (true) {
//			 int[] p1 = getP1(robot, left);
//			 int[] p2 = getP2(nextCoord, imageDir, left);
//			 Path path = new Path();
//
//			 // Find every possible path and add it to path array
//			 if (left) {
//				 left = false;
//				 /*
//				  * TODO: get all paths possible (starting with left turn?)
//				  * Main problem: how to loop through all possible paths as each path has a
//				  * different configuration (i.e. no. and direction of turns, whether there
//				  * is a straight line in the path)
//				  */
//				 double l = Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
//				 double[] v1 = new double[]{p2[0] - p1[0], p2[1] - p1[1]};
//				 double[] v2 = rotatePt(v1, Math.PI / 2);
//				 path.setPt1(new double[]{p1[0] + (turnRad / l) * v2[0], p1[1] + (turnRad / l) * v2[1]});
//				 path.setPt2(new double[]{path.getPt1()[0] + v1[0], path.getPt1()[1] + v1[1]});
//
//				 // Add path to path array if it is valid
//				 if (isValidPath(currCoord, path)) {
//					 pathArray.add(path);
//				 }
//
//
//			 } else {
//				 // Get all paths possible starting with right turn
//				 break;
//			 }
//		 }
//		 // Return shortest path by distance
//		 return Collections.min(pathArray);
//	 }
//
//	 // Rotate a set of coordinates by angle
//	 private double[] rotatePt(double[] coord, double angle) {
//		 double[] rotatedPt = new double[]{0, 0};
//		 if (angle == Math.PI / 2) {
//			 rotatedPt = new double[]{coord[1], -coord[0]};
//		 } else if (angle == -Math.PI / 2) {
//			 rotatedPt = new double[]{-coord[1], coord[0]};
//		 } else if (angle == Math.PI) {
//			 rotatedPt = new double[]{-coord[0], -coord[1]};
//		 } else {
//			 rotatedPt = new double[]{coord[0] * Math.cos(angle) - coord[1] * Math.sin(angle), coord[0] * Math.sin(angle) + coord[1] * Math.cos(angle)};
//		 }
//		 return rotatedPt;
//	 }
//
//	 private int[] getP1(Robot robot, boolean left) {
//		 Point2D.DOuble robotCoord = robot.getRobotCenter();
//		 int[] p1 = new int[2];
//		 switch (robot.getOrientaion()) {
//			 case N -> {
//				 if (left) {
//					 p1[0] = robotCoord[0] - turnRad;
//				 } else {
//					 p1[0] = robotCoord[0] + turnRad;
//				 }
//				 p1[1] = robotCoord[1];
//			 }
//			 case S -> {
//				 if (left) {
//					 p1[0] = robotCoord[0] + turnRad;
//				 } else {
//					 p1[0] = robotCoord[0] - turnRad;
//				 }
//				 p1[1] = robotCoord[1];
//			 }
//			 // TODO: check if y-coordinates are correct for case E and W
//			 case E -> {
//				 p1[0] = robotCoord[0];
//				 if (left) {
//					 p1[1] = robotCoord[1] + turnRad;
//				 } else {
//					 p1[1] = robotCoord[1] - turnRad;
//				 }
//			 }
//			 case W -> {
//				 p1[0] = robotCoord[0];
//				 if (left) {
//					 p1[1] = robotCoord[1] - turnRad;
//				 } else {
//					 p1[1] = robotCoord[1] + turnRad;
//				 }
//			 }
//		 }
//		 return p1;
//	 }
//
//	 //TODO: check if coordinates are correct (!!! goal coordinate != obstacle coordinate)
//	 private int[] getP2(int[] goalCoord, Direction imageDir, boolean left) {
//		 int[] p2 = new int[2];
//
//		 switch (imageDir) {
//			 case NORTH -> {
//				 if (left) {
//					 p2[0] = goalCoord[0] - turnRad;
//				 } else {
//					 p2[0] = goalCoord[0] + turnRad;
//				 }
//				 p2[1] = goalCoord[1];
//			 }
//			 case SOUTH -> {
//				 if (left) {
//					 p2[0] = goalCoord[0] + turnRad;
//				 } else {
//					 p2[0] = goalCoord[0] - turnRad;
//				 }
//				 p2[1] = goalCoord[1];
//			 }
//			 // TODO: check if y-coordinates are correct for case EAST and WEST
//			 case EAST -> {
//				 p2[0] = goalCoord[0];
//				 if (left) {
//					 p2[1] = goalCoord[1] + turnRad;
//				 } else {
//					 p2[1] = goalCoord[1] - turnRad;
//				 }
//			 }
//			 case WEST -> {
//				 p2[0] = goalCoord[0];
//				 if (left) {
//					 p2[1] = goalCoord[1] - turnRad;
//				 } else {
//					 p2[1] = goalCoord[1] + turnRad;
//				 }
//			 }
//			 default -> System.out.println("Image direction unset.");
//		 }
//		 return p2;
//	 }
//
//
//
// }

import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathFinder {

	public Robot robot;
	public Arena arena;
	public List<Obstacle> obstacleObjects;
	public TurningRadius turningRadius;
	private Obstacle obs;

	private static List<String> bothDirection = Arrays.asList("right","left");
	private static List<String> movement = Arrays.asList("front","reverse");

	private int[] obstacleCenter = new int[2];
	private int[] robotCenter = new int[2];

	PathFinder(Robot robot, List<Obstacle> obstacle) {
		this.robot = robot;
		this.obstacleObjects = obstacle;
		setRobotCenter();
	}


	 public void findBestPath() {
		 // run A* to get next goal node
		 Astar Astar = new Astar(obstacleObjects, arena, robot);
		 List<Node> nodes = Astar.createNodes();
		 int[] robotCurrCoord = nodes.get(0).getCoord();
		 Node nextNode = Astar.runAStar(nodes.get(0), nodes.get(nodes.size() - 1));
		 for (Obstacle obstacle : obstacleObjects) {
			 if (obstacle.getObstacleID() == nextNode.getObstacleID()) {
				 obs = obstacle;
				 break;
			 }
		 }
		 setObstacleCenter();

		 int[] nextCoord = nextNode.getCoord();
		 Direction nextImgDir = getImgDir(nextNode);

		 // If image is in front of robot, move robot forward
		 if (isGoalAcrossRobot(robot, nextCoord, nextImgDir)) {
			 robot.moveForward();
		 }

		 // If image is too close to an obstacle, reverse
		 if (ArenaFrame.obstacles[robotCurrCoord[0]][robotCurrCoord[1]] != 0) {
			 robot.reverseBackward();
		 }

		 // Get best path
//		 Path bestPath = calculatePath(nodes.get(0), nextNode);

		 // TODO: how to execute path???
	 }

	 // TODO: redo method, do a simulation to determine if path is valid
	 public boolean isValidPath(int[] robotCoord, Path path) {
		 if (robotCoord == null) return false;

		 int x = robotCoord[0];
		 int y = robotCoord[1];
		 int[][] robotPos = {
				 {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1},
				 {x - 1, y}, {x, y}, {x + 1, y},
				 {x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1},
				 {(int) path.pt1[0], (int) path.pt1[1]}, {(int) path.pt2[0], (int) path.pt2[1]}
		 };

		 if ((0 < x) && (x < Arena.ARENA_WIDTH - 1) && (0 < y) && (y < Arena.ARENA_HEIGHT - 1)) {
			 for (int[] coordinate : robotPos) {
				 if (ArenaFrame.obstacles[coordinate[0]][coordinate[1]] != 0) return false;
			 }
		 }

		 return true;
	 }

	// LSL & RSR shortest path finding
	public void CSCsameC () {
		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] p1 = new int[2];
		int[] p2 = new int[2];

		double robotArc;
		double obstacleArc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;

		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obs.createCircleRight(obstacleCenter);
				path = "RSR";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obs.createCircleLeft(obstacleCenter);
				path = "LSL";
			}

			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();

			p2[0] = (int) this.obs.turningRadius.getCenter().getX();
			p2[1] = (int) this.obs.turningRadius.getCenter().getY();

			double l = straightDistance(p1, p2);

			v1 = this.vector1(p1,p2);
			if (direction == "right") {
				v2 = this.vector2(v1, (Math.PI/2));
			} else {
				v2 = this.vector2(v1, -(Math.PI/2));
			}
			pt1 = this.pt1(p1, v2, l);
			pt2 = this.pt2(pt1, v1);

			System.out.println(p1[0]);
			System.out.println(p1[1]);
			System.out.println(p2[0]);
			System.out.println(p2[1]);

			robotArc = computeArcLength(robotCenter,p1,pt1,direction);
			obstacleArc = computeArcLength(pt2,p2,obstacleCenter,direction);

			new_distance = robotArc + l + obstacleArc;

			if(new_distance < distance) {
				distance = new_distance;
				bestDir = path;
			}

		}

		System.out.println(distance);
		System.out.println(bestDir);
	}

	// RSL & LSR shortest path finding
	public void CSCdiffC () {
		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] v3 = new int[2];
		int[] p1 = new int[2];
		int[] p2 = new int[2];

		double robotArc;
		double obstacleArc;
		double new_distance = 0;
		double theta = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;

		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obs.createCircleLeft(obstacleCenter);
				path = "RSL";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obs.createCircleRight(obstacleCenter);
				path = "LSR";
			}

			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();

			p2[0] = (int) this.obs.turningRadius.getCenter().getX();
			p2[1] = (int) this.obs.turningRadius.getCenter().getY();

			double d = straightDistance(p1, p2);
			double l = straightTravel(d,2 * TurningRadius.getRadius());

			theta = Math.acos(2 * TurningRadius.getRadius() / d);
			v1 = this.vector1(p1,p2);

			if (direction == "right") {
				v2 = this.vector2_version2(v1, theta);
			} else {
				v2 = this.vector2_version2(v1, -theta);
			}

			pt1 = this.pt1(p1, v2, d);
			v3 = this.vector3(v2);
			pt2 = this.pt1(p2, v3, d);

			if (direction == "right") {
				robotArc = computeArcLength(robotCenter,p1,pt1,direction);
				obstacleArc = computeArcLength(pt2,p2,obstacleCenter,"left");
			} else {
				robotArc = computeArcLength(robotCenter,p1,pt1,direction);
				obstacleArc = computeArcLength(pt2,p2,obstacleCenter,"right");
			}

			new_distance = robotArc + l + obstacleArc;

			if(new_distance < distance) {
				distance = new_distance;
				bestDir = path;
			}
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}

	// RLR & LRL shortest path finding(case 1)
	public void CCCcase1 () {
		int[] q = new int[2];
		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] p1 = new int[2];
		int[] p2 = new int[2];
		int[] p3 = new int[2];
		int[] c3 = new int[2];

		double robotArc;
		double obstacleArc;
		double c3Arc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;

		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obs.createCircleRight(obstacleCenter);
				path = "RLR";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obs.createCircleLeft(obstacleCenter);
				path = "LRL";
			}

			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();

			p2[0] = (int) this.obs.turningRadius.getCenter().getX();
			p2[1] = (int) this.obs.turningRadius.getCenter().getY();

			double d = straightDistance(p1, p2);

			q = this.midPoint(p1,p2);
			v1 = this.vector1(p1,p2);

			if(direction == "right") {
				v2 = this.vector2_version3(p1,p2);
			} else {
				v2 = this.vector2_version4(p1,p2);
			}

			double d1 = this.d1(TurningRadius.getRadius(),d);
			p3 = this.p3(q, v2, d1, d);
			pt1 = this.midPoint(p1, p3);
			pt2 = this.midPoint(p2, p3);

			if (direction == "right") {
				turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
				c3[0] = (int) this.turningRadius.getCenter().getX();
				c3[1] = (int) this.turningRadius.getCenter().getY();
				c3Arc = computeArcLength(pt1,c3,pt2,"left");
			} else {
				turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
				c3[0] = (int) this.turningRadius.getCenter().getX();
				c3[1] = (int) this.turningRadius.getCenter().getY();
				c3Arc = computeArcLength(pt1,c3,pt2,"right");
			}

			robotArc = computeArcLength(robotCenter,p1,pt1,direction);
			obstacleArc = computeArcLength(pt2,p2,obstacleCenter,direction);

			new_distance = robotArc + c3Arc + obstacleArc;

			if(new_distance < distance) {
				distance = new_distance;
				bestDir = path;
			}
			//System.out.println(p1[0]);
			//System.out.println(p1[1]);
			//System.out.println(p3[0]);
			//System.out.println(p3[1]);
			//System.out.println(p_new[0]);
			//System.out.println(p_new[1]);
			//System.out.println(pt1[0]);
			//System.out.println(pt1[1]);
			//System.out.println(pt2[0]);
			//System.out.println(pt2[1]);
			System.out.println(robotArc);
			System.out.println(c3Arc);
			System.out.println(obstacleArc);
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}

	// RLR & LRL shortest path finding(case 2)
	public void CCCcase2 () {
		int[] q = new int[2];
		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] p1 = new int[2];
		int[] p2 = new int[2];
		int[] p3 = new int[2];
		int[] c3 = new int[2];

		double robotArc;
		double obstacleArc;
		double c3Arc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;

		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obs.createCircleRight(obstacleCenter);
				path = "RLR";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obs.createCircleLeft(obstacleCenter);
				path = "LRL";
			}

			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();

			p2[0] = (int) this.obs.turningRadius.getCenter().getX();
			p2[1] = (int) this.obs.turningRadius.getCenter().getY();

			double d = straightDistance(p1, p2);

			q = this.midPoint(p1,p2);
			v1 = this.vector1(p1,p2);

			if(direction == "right") {
				v2 = this.vector2_version4(p1,p2);
			} else {
				v2 = this.vector2_version3(p1,p2);
			}

			double d1 = this.d1(TurningRadius.getRadius(),d);
			p3 = this.p3(q, v2, d1, d);
			pt1 = this.midPoint(p1, p3);
			pt2 = this.midPoint(p2, p3);

			if (direction == "right") {
				turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
				c3[0] = (int) this.turningRadius.getCenter().getX();
				c3[1] = (int) this.turningRadius.getCenter().getY();
				c3Arc = computeArcLength(pt1,c3,pt2,"left");
			} else {
				turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
				c3[0] = (int) this.turningRadius.getCenter().getX();
				c3[1] = (int) this.turningRadius.getCenter().getY();
				c3Arc = computeArcLength(pt1,c3,pt2,"right");
			}

			robotArc = computeArcLength(robotCenter,p1,pt1,direction);
			obstacleArc = computeArcLength(pt2,p2,obstacleCenter,direction);

			new_distance = robotArc + c3Arc + obstacleArc;

			if(new_distance < distance) {
				distance = new_distance;
				bestDir = path;
			}
				//System.out.println(d);
				//System.out.println(p1[0]);
				//System.out.println(p1[1]);
				//System.out.println(p3[0]);
				//System.out.println(p3[1]);
				//System.out.println(pt1[0]);
				//System.out.println(pt1[1]);
				//System.out.println(pt2[0]);
				//System.out.println(pt2[1]);
				//System.out.println(robotArc);
				//System.out.println(c3Arc);
				//System.out.println(obstacleArc);
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}

	// R & L shortest path finding
	public void C () {
		int[] p1 = new int[2];
		int[] p2 = new int[2];

		double robotArc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;

		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obs.createCircleRight(obstacleCenter);
				path = "R";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obs.createCircleLeft(obstacleCenter);
				path = "L";
			}

			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();

			p2[0] = (int) this.obs.turningRadius.getCenter().getX();
			p2[1] = (int) this.obs.turningRadius.getCenter().getY();

			//robot can reach obstacle
			if (this.robot.canReachTurn(p2)) {
				robotArc = computeArcLength(robotCenter,p1,obstacleCenter,direction);

				new_distance = robotArc;

				if(new_distance < distance) {
					distance = new_distance;
					bestDir = path;
				}
			}
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}

	// RL & LR shortest path finding
	public void CC () {
		int[] p1 = new int[2];
		int[] p2 = new int[2];
		int[] pt1 = new int[2];

		double robotArc;
		double obstacleArc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;

		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obs.createCircleLeft(obstacleCenter);
				path = "RL";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obs.createCircleRight(obstacleCenter);
				path = "LR";
			}

			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();

			p2[0] = (int) this.obs.turningRadius.getCenter().getX();
			p2[1] = (int) this.obs.turningRadius.getCenter().getY();

			pt1 = midPoint(p1,p2);
			double d = straightDistance(p1, p2);

			System.out.println(d);
			System.out.println(p1[0]);
			System.out.println(p1[1]);
			System.out.println(p2[0]);
			System.out.println(p2[1]);


			//robot can reach obstacle
			if (this.robot.canReachTurnTurn(d)) {
				robotArc = computeArcLength(robotCenter,p1,pt1,direction);
				obstacleArc = computeArcLength(pt1,p2,obstacleCenter,direction);

				new_distance = robotArc + obstacleArc;

				if(new_distance < distance) {
					distance = new_distance;
					bestDir = path;
				}
			}
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}

	public int[] vector1(int[] p1, int[] p2) {
		int[] v1 = new int[2];
		v1[0] = p2[0] - p1[0];
		v1[1] = p2[1]  - p1[1];
		return v1;
	}

	public int[] vector2(int[] v1, double theta) {
		int[] v2 = new int[2];
		v2[0] = round(v1[0] * Math.cos(theta) - v1[1] * Math.sin(theta));
		v2[1] = round(v1[0] * Math.sin(theta) + v1[1] * Math.cos(theta));
		return v2;
	}

	public int[] vector2_version2(int[] v1, double theta) {
		int[] v2 = new int[2];
		v2[0] = round(v1[0] * Math.cos(theta) + v1[1] * Math.sin(theta));
		v2[1] = round(v1[0] * Math.sin(theta) - v1[1] * Math.cos(theta));
		return v2;
	}

	public int[] vector2_version3(int[] p1, int[] p2) {
		int[] v2 = new int[2];
		v2[0] = p2[1] - p1[1];
		v2[1] = p1[0] - p2[0];
		return v2;
	}

	public int[] vector2_version4(int[] p1, int[] p2) {
		int[] v2 = new int[2];
		v2[0] = p1[1] - p2[1];
		v2[1] = p2[0] - p1[0];
		return v2;
	}

	public int[] vector3(int[] v2) {
		int[] v3 = new int[2];
		v3[0] = -v2[0];
		v3[1] = -v2[1];
		return v3;
	}

	public int[] pt1(int[] p1, int[] v2, double l) {
		int[] pt1 = new int[2];
		pt1[0] = round(p1[0] - (25.0/l * v2[0]));
		pt1[1] = round(p1[1] - (25.0/l * v2[1]));
		return pt1;
	}

	public int[] pt2(int[] pt1, int[] v1) {
		int[] pt2 = new int[2];
		pt2[0] = pt1[0] + v1[0];
		pt2[1] = pt1[1] + v1[1];
		return pt2;
	}

	public double straightDistance(int[] p1, int[] p2) {
		int x = Math.abs(p2[0] - p1[0]);
		int y = Math.abs(p2[1] - p1[1]);
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
	}

	public double straightTravel(double d, double r) {
		return Math.sqrt(Math.pow(d,2) - Math.pow(r,2));
	}

	public int[] midPoint(int[] v1, int[] v2) {
		int[] mid = new int[2];
		mid[0] = round((v1[0] + v2[0]) / 2);
		mid[1] = round((v1[1] + v2[1]) / 2);
		return mid;
	}

	public double d1(double r, double d) {
		return Math.sqrt(Math.pow(2 * r,2) - Math.pow(d / 2,2));
	}

	public int[] p3(int[] q, int[] v2, double d1, double d) {
		int[] pt1 = new int[2];
		pt1[0] = round(q[0] - (d1/d * v2[0]));
		pt1[1] = round(q[1] - (d1/d * v2[1]));
		return pt1;
	}

	public double computeArcLength(int[] p, int[] p1, int[] pt1, String direction) {

		int[] v1 = new int[2];
		int[] v2 = new int[2];
		double theta;

		v1[0] = p[0] - p1[0];
		v1[1] = (-p[1])  - (-p1[1]);
		v2[0] = pt1[0] - p1[0];
		v2[1] = (-pt1[1])  - (-p1[1]);

		theta = Math.atan2(v2[1], v2[0]) - Math.atan2(v1[1], v1[0]);

		if (theta < 0 && direction == "left") {
			theta += 2 * Math.PI;
		} else if (theta > 0 && direction == "right"){
			theta -= 2 * Math.PI;
		}
		//System.out.println(theta);
		//System.out.println(Math.atan2(v2[1], v2[0]));
		//System.out.println(Math.atan2(v1[1], v1[0]));
		return Math.abs(theta * TurningRadius.getRadius());
	}

	private static int round(double val) {
        return (int) Math.round(val);
    }

		 // Gets corresponding image direction based on node coordinate
	 private Direction getImgDir(Node node) {
		 for (Obstacle obstacle : obstacleObjects) {
			 if (obstacle.getObstacleID() == node.getObstacleID()) {
				 return obstacle.getDirection();
			 }
		 }
		 return Direction.UNSET;
	 }
	/*
	public void rsr(boolean running) {

		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] p1 = new int[2];

		// 22 - 32
		this.robot.createCircleRight(); // create turning radius for robot
		//double l = l(p1, obstacleCenter);

		p1[0] = (int) this.robot.turningRadius.getCenter().getX();
		p1[1] = (int) this.robot.turningRadius.getCenter().getY();
		//robotCenter = calculateRobotCenter(this.robot.getX(), this.robot.getY(), this.robot.getOrientation());
		v1 = this.vector1(p1); // done
		v2 = this.vector2(v1);
		System.out.println(v2[0]);
		System.out.println(v2[1]);
		/*v2 = this.vector2(v1);
		pt1 = this.pt1(p1, v2, l);
		pt2 = this.pt2(pt1, v1);

		System.out.println(robotCenter[0]);
		System.out.println(robotCenter[1]);
		System.out.println(pt1[0]);
		System.out.println(pt1[1]);

		this.robot.turnRight();
		this.robot.turnRight();
		this.robot.moveForward();*/

		/*do {
			this.robot.moveForward();
		}while(robotCenter[0] != pt2[0] && robotCenter[1] != pt2[1]);

		do {
			this.robot.turnRight();
		}while(this.robot.getOrientation() == RobotOrientation.E);
	}*/

	/*
	public void findPath(boolean running) {

		if(running) {
			// find best action
			String bestAction = findBestAction(this.robot.getX(),this.robot.getY(),this.robot.getOrientation());
			switch(bestAction) {
			case "reverse":
				this.robot.reverseBackward();
				this.robot.updateSensor();
				break;
			case "moveFroward":
				this.robot.moveForward();
				this.robot.updateSensor();
				break;
			case "reverseLeft":
				this.robot.reverseLeft();
				this.robot.updateSensor();
				break;
			case "reverseRight":
				this.robot.reverseRight();
				this.robot.updateSensor();
				break;
			case "turnLeft":
				this.robot.turnLeft();
				this.robot.updateSensor();
				break;
			case "turnRight":
				this.robot.turnRight();
				this.robot.updateSensor();
				break;
			default:
				break;
			}
			//System.out.print(this.robot.getSensorY());
		}
	}

	public String findBestAction(int x, int y, RobotOrientation orientation) {
		String methodname = "";
		double new_distance;
		double distance = Double.POSITIVE_INFINITY;
		int[] robotCenter = new int[2];

		int robotOriginalX = x;
		int robotOriginalY = y;
		RobotOrientation orientationOriginal = orientation;

		Method[] methods = this.robot.getClass().getMethods();

		for( int index =0; index < methods.length; index++){
			if( methods[index].getName().contains("move")||methods[index].getName().contains("reverse")||methods[index].getName().contains("turn")){
				try {
					methods[index].invoke(this.robot);
					robotCenter = calculateRobotCenter(this.robot.getX(), this.robot.getY(), this.robot.getOrientation());
					new_distance = straightDistance(robotCenter, obstacleCenter);
					//System.out.println(methods[index].getName());
					//System.out.println(robotCenter[0]);
					//System.out.println(robotCenter[1]);
					//System.out.println(new_distance);
					if(new_distance < distance) {
						distance = new_distance;
						methodname = methods[index].getName();
					}

					//set to original
					this.robot.setX(robotOriginalX);
					this.robot.setY(robotOriginalY);
					//this.robot.setOrientation(orientationOriginal);
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

		System.out.println(methodname);
		return methodname;
	}*/

	// Check if robot can travel straight to goal without any turns
	// TODO: change robot orientation to robot angle, account for range of values
	 public boolean isGoalAcrossRobot(Robot robot, int[] goalCoord, Direction imageDir) {
		 Point2D.Double robotCoord = robot.getRobotCenter();
		 int robotAngle = robot.getAngle();
		 if (robotAngle == 0) {
			 return imageDir == Direction.SOUTH && goalCoord[0] == (int) robotCoord.getX() && goalCoord[1] > robotCoord.getY();
		 } else if (robotAngle == Math.PI) {
			 return imageDir == Direction.NORTH && goalCoord[0] == (int) robotCoord.getX() && goalCoord[1] < robotCoord.getY();
		 } else if (robotAngle == Math.PI / 2) {
			 return imageDir == Direction.WEST && goalCoord[1] == (int) robotCoord.getY() && goalCoord[0] > robotCoord.getX();
		 } else if (robotAngle == -Math.PI / 2) {
			 return imageDir == Direction.EAST && goalCoord[1] == (int) robotCoord.getY() && goalCoord[0] < robotCoord.getX();
		 }
		 return false;
	 }

	// Center of obstacle
	public void setObstacleCenter() {
		obstacleCenter[0] = (int) this.obs.getObstacleCenter().getX();
		obstacleCenter[1] = (int) this.obs.getObstacleCenter().getY();
	}

	public int[] getObstacleCenter() {
		return obstacleCenter;
	}

	public void setRobotCenter() {
		robotCenter[0] = (int) this.robot.getRobotCenter().getX();
		robotCenter[1] = (int) this.robot.getRobotCenter().getY();
	}

	public int[] getRobotCenter() {
		return robotCenter;
	}

	public class Path implements Comparable<Path> {
		private double dist;
		ArrayList<Instruction> instructions = new ArrayList<>();
		private double[] pt1;
		private double[] pt2;

		public void setPt1(double[] pt1) {
			this.pt1 = pt1;
		}

		public void setPt2(double[] pt2) {
			this.pt2 = pt2;
		}

		public double[] getPt1() {
			return pt1;
		}

		public double[] getPt2() {
			return pt2;
		}

		public double getDist() {
			return this.dist;
		}

		public void setDist(double dist) {
			this.dist = dist;
		}

		public ArrayList<Instruction> getInstructions() {
			return instructions;
		}

		public void setInstructions(ArrayList<Instruction> instructions) {
			this.instructions = instructions;
		}

		@Override
		public int compareTo(Path o) {
			if (this.getDist() > o.getDist()) {
				return 1;
			} else if (this.getDist() < o.getDist()) {
				return -1;
			}
			return 0;
		}

		public class Instruction {
			int angle;
			String turnDirection;

			Instruction(int angle, String turnDirection) {
				this.angle = angle;
				this.turnDirection = turnDirection;
			}

			public int getAngle() {
				return angle;
			}

			public void setAngle(int angle) {
				this.angle = angle;
			}

			public String getTurnDirection() {
				return turnDirection;
			}

			public void setTurnDirection(String turnDirection) {
				this.turnDirection = turnDirection;
			}
		}
	}

}
