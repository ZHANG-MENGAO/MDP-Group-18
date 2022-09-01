package mdp.g18.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathFinder {
	public List<Obstacle> obstacleObjects;
	Arena arena;
	Robot robot;

	public static final int turnRad = 25;

	PathFinder(List<Obstacle> obstacles, Arena arena, Robot robot) {
		this.obstacleObjects = obstacles;
		this.arena = arena;
		this.robot = robot;
	}

	Astar Astar = new Astar(obstacleObjects, arena, robot);

	public void findBestPath() {
		// run A* to get next goal node
		List<Node> nodes = Astar.createNodes();
		int[] robotCurrCoord = nodes.get(0).getCoord();
		Node nextNode = Astar.runAStar(nodes.get(0), nodes.get(nodes.size() - 1));
		int[] nextCoord = nextNode.getCoord();
		Direction nextImgDir = getImgDir(nextNode);

		// If image is in front of robot, move robot forward
		if (isGoalAcrossRobot(robot, nextCoord, nextImgDir)) {
			// TODO: check if need to add loop for moveForward
			robot.moveForward();
		}

		// If image is too close to an obstacle, reverse
		if (ArenaFrame.obstacles[robotCurrCoord[0]][robotCurrCoord[1]] != 0) {
			robot.reverse();
		}

		// Get best path
		Path bestPath = calculatePath(nodes.get(0), nextNode);

		// TODO: how to execute path???

	}

	public Path calculatePath(Node currNode, Node nextNode) {
		int[] currCoord = currNode.getCoord();
		int[] nextCoord = nextNode.getCoord();

		Direction imageDir = getImgDir(nextNode);

		boolean left = true;
		// TODO: initialise path array
		List<Path> pathArray = new ArrayList<Path>();

		while (true) {
			int[] p1 = getP1(robot, left);
			int[] p2 = getP2(nextCoord, imageDir, left);
			Path path = new Path();

			// Find every possible path and add it to path array
			if (left) {
				left = false;
				/*
				* TODO: get all paths possible (starting with left turn?)
				* Main problem: how to loop through all possible paths as each path has a
				* different configuration (i.e. no. and direction of turns, whether there
				* is a straight line in the path)
				*/
				double l = Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
				double[] v1 = new double[] {p2[0] - p1[0], p2[1] - p1[1]};
				double[] v2 = rotatePt(v1, Math.PI/2);
				path.setPt1(new double[] {p1[0] + (turnRad / l) * v2[0], p1[1] + (turnRad / l) * v2[1]});
				path.setPt2(new double[] {path.getPt1()[0] + v1[0], path.getPt1()[1] + v1[1]});

				// Add path to path array if it is valid
				if (isValidPath(currCoord, path)) {
					pathArray.add(path);
				}


			} else {
				// Get all paths possible starting with right turn
				break;
			}
		}
		// Return shortest path by distance
		return Collections.min(pathArray);
	}

	// Rotate a set of coordinates by angle
	private double[] rotatePt(double[] coord, double angle) {
		double[] rotatedPt = new double[] {0, 0};
		if (angle == Math.PI/2) {
			rotatedPt = new double[]{coord[1], -coord[0]};
		} else if (angle == -Math.PI/2) {
			rotatedPt = new double[] {-coord[1], coord[0]};
		} else if (angle == Math.PI) {
			rotatedPt = new double[] {-coord[0], -coord[1]};
		} else {
			rotatedPt = new double[] {coord[0] * Math.cos(angle) - coord[1] * Math.sin(angle), coord[0] * Math.sin(angle) + coord[1] * Math.cos(angle)};
		}
		return rotatedPt;
	}

	private int[] getP1(Robot robot, boolean left) {
		int[] robotCoord = robot.getCoord();
		int[] p1 = new int[2];
		switch (robot.getOrientaion()) {
			case N -> {
				if (left) {
					p1[0] = robotCoord[0] - turnRad;
				} else {
					p1[0] = robotCoord[0] + turnRad;
				}
				p1[1] = robotCoord[1];
			}
			case S -> {
				if (left) {
					p1[0] = robotCoord[0] + turnRad;
				} else {
					p1[0] = robotCoord[0] - turnRad;
				}
				p1[1] = robotCoord[1];
			}
			// TODO: check if y-coordinates are correct for case E and W
			case E -> {
				p1[0] = robotCoord[0];
				if (left) {
					p1[1] = robotCoord[1] + turnRad;
				} else {
					p1[1] = robotCoord[1] - turnRad;
				}
			}
			case W -> {
				p1[0] = robotCoord[0];
				if (left) {
					p1[1] = robotCoord[1] - turnRad;
				} else {
					p1[1] = robotCoord[1] + turnRad;
				}
			}
		}
		return p1;
	}

	//TODO: check if coordinates are correct (!!! goal coordinate != obstacle coordinate)
	private int[] getP2(int[] goalCoord, Direction imageDir, boolean left) {
		int[] p2 = new int[2];

		switch (imageDir) {
			case NORTH -> {
				if (left) {
					p2[0] = goalCoord[0] - turnRad;
				} else {
					p2[0] = goalCoord[0] + turnRad;
				}
				p2[1] = goalCoord[1];
			}
			case SOUTH -> {
				if (left) {
					p2[0] = goalCoord[0] + turnRad;
				} else {
					p2[0] = goalCoord[0] - turnRad;
				}
				p2[1] = goalCoord[1];
			}
			// TODO: check if y-coordinates are correct for case EAST and WEST
			case EAST -> {
				p2[0] = goalCoord[0];
				if (left) {
					p2[1] = goalCoord[1] + turnRad;
				} else {
					p2[1] = goalCoord[1] - turnRad;
				}
			}
			case WEST -> {
				p2[0] = goalCoord[0];
				if (left) {
					p2[1] = goalCoord[1] - turnRad;
				} else {
					p2[1] = goalCoord[1] + turnRad;
				}
			}
			default -> System.out.println("Image direction unset.");
		}
		return p2;
	}

	// Check if robot can travel straight to goal without any turns
	public boolean isGoalAcrossRobot(Robot robot, int[] goalCoord, Direction imageDir) {
		int[] robotCoord = robot.getCoord();
		RobotOrientation robotOrient = robot.getOrientaion();

		switch(robotOrient) {
			case N:
				if (imageDir == Direction.SOUTH && goalCoord[0] == robotCoord[0] && goalCoord[1] > robotCoord[1]) {
					return true;
				}
				break;
			case S:
				if (imageDir == Direction.NORTH && goalCoord[0] == robotCoord[0] && goalCoord[1] < robotCoord[1]) {
					return true;
				}
				break;
			case E:
				if (imageDir == Direction.WEST && goalCoord[1] == robotCoord[1] && goalCoord[0] > robotCoord[0]) {
					return true;
				}
				break;
			case W:
				if (imageDir == Direction.EAST && goalCoord[1] == robotCoord[1] && goalCoord[0] < robotCoord[0]) {
					return true;
				}
				break;
		}
		return false;
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

	// TODO: implement logic for valid path check
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

	public class Path implements Comparable<Path>{
		private double dist;
		private double[] pt1;
		private double[] pt2;
		private String[] instructions; // Instruction format: turn at pt1, straight (if any), turn at pt2

		public double getDist() {
			return this.dist;
		}

		public void setDist(double dist) {
			this.dist = dist;
		}

		public void setPt1(double[] pt1) {
			this.pt1 = pt1;
		}

		public double[] getPt1() {
			return pt1;
		}

		public void setPt2(double[] pt2) {
			this.pt2 = pt2;
		}

		public double[] getPt2() {
			return pt2;
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
	}
}
