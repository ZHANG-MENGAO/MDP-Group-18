package mdp.g18.algo;

import java.util.ArrayList;
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
		for (Obstacle obstacle : obstacleObjects) {
			// TODO
		}

		// Get best path
		calculatePath(nodes.get(0), nextNode);

	}

	public void calculatePath(Node currNode, Node nextNode) {
		int[] currCoord = currNode.getCoord();
		int[] nextCoord = nextNode.getCoord();

		Direction imageDir = getImgDir(nextNode);

		boolean left = true;
		// TODO: initialise path array

		while (true) {
			int[] p1 = getP1(robot, left);
			int[] p2 = getP2(nextCoord, imageDir, left);

			// Find every possible path and add it to path array

			if (left) {
				left = false;
				// Get all paths possible starting with left turn

			} else {
				// Get all paths possible starting with right turn
				break;
			}
		}
		// Return shortest path by distance
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
	public boolean isValidPath(int[] robotCoord) {
		if (robotCoord == null) return false;

		int x = robotCoord[0];
		int y = robotCoord[1];
		int[][] robotPos = {
				{x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1},
				{x - 1, y}, {x, y}, {x + 1, y},
				{x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1}
		};

		if ((0 < x) && (x < Arena.ARENA_WIDTH - 1) && (0 < y) && (y < Arena.ARENA_HEIGHT - 1)) {
			for (int[] coordinate : robotPos) {
				if (ArenaFrame.obstacles[coordinate[0]][coordinate[1]] != 0) return false;
			}
		}

		return true;
	}

	public class Path {
		private double dist;
		private double[] pt1;
		private double[] pt2;

		public double getDist() {
			return this.dist;
		}

		public void setDist(double dist) {
			this.dist = dist;
		}
	}

}
