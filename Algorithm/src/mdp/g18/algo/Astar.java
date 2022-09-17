package mdp.g18.algo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Astar {
	List<Obstacle> obstacles;
	Arena arena;
	Robot robot;

	private ArrayList<Node> nodes;
	
	Astar(List<Obstacle> obstacleObjects, Arena arena, Robot robot){
		this.obstacles = obstacleObjects;
		this.arena = arena;
		this.robot = robot;
		init();
	}

	private void init() {
		// Run A* to update parent of each node
		nodes = createNodes();
		runAStar(nodes);
	}

	public Obstacle getNextObstacle(Obstacle obs) {
		// Convert obstacle to node
		Node prev = null;
		if (obs == null) {
			prev = nodes.get(0);
		}
		else {
			for (Node n : nodes) {
				if (obs.getObstacleID() == n.getObstacleID()) {
					prev = n;
				}
			}
		}

		Node next = null;
		// Get next node to visit
		for (Node n : nodes) {
			if (!n.isVisited() && n.getParent() == prev) {
				n.setVisited(true);
				next = n;
				break;
			}
		}

		// Convert node to obstacle
		for (Obstacle o : obstacles) {
			try {
				if (o.getObstacleID() == next.getObstacleID()) {
					return o;
				}
			}
			catch (NullPointerException e) {
				System.out.println("No more nodes.");
				return null;
			}
		}
		return null;
	}
	
	private void runAStar(ArrayList<Node> nodes){
		Node start = nodes.get(0);
		Node target = nodes.get(nodes.size() - 1);
	    PriorityQueue<Node> closedList = new PriorityQueue<>(); // Visited
	    PriorityQueue<Node> openList = new PriorityQueue<>(); // Frontier

			// Calculate heuristics for each node
		start.setCost(start.getG(), start.calculateH(start, target));

		// Visiting start now
		openList.add(start);

		while (!openList.isEmpty()) {
			Node current = openList.peek();

			// Go through every neighbour of currently visiting node
			for (Node.Edge edge : current.neighbour) {
				Node child = edge.node;
				double totalWeight = current.getG() + edge.weight;

				// Child has not been visited
				if (!openList.contains(child) && !closedList.contains(child)) {
					child.setParent(current);
					child.setG(totalWeight);
					child.setCost(child.getG(), child.calculateH(child, target));
					openList.add(child);
				}
				// Child has been visited
				else {
					if (totalWeight < child.getG()) {
						child.setParent(current);
						child.setG(totalWeight);
						child.setCost(child.getG(), child.calculateH(child, target));

						if (closedList.contains(child)) {
							closedList.remove(child);
							openList.add(child);
						}
					}
				}
			}

			// Mark current as visited
			openList.remove(current);
			closedList.add(current);
			if (openList.isEmpty()) {
				return;
			}
		}
	}
	
	private double calDistance(int[] current, int[] destination) {
		int x = Math.abs(current[0] - destination[0]);
		int y = Math.abs(current[1] - destination[1]);
		return Math.sqrt(Math.abs(Math.pow(x, 2) + Math.pow(y, 2)));
	}

	// Creates an ArrayList of Nodes to be used for the A* algorithm
	private ArrayList<Node> createNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		Node robotPos = new Node(0, 0, new int[] {(int) robot.getRobotCenter().getX(), (int) robot.getRobotCenter().getX()}, null, -1);
		nodes.add(robotPos);

		for (Obstacle obstacle: obstacles) {
			int[] obstacleCoord = new int[] {obstacle.getxCoordinate() - 5, obstacle.getyCoordinate() + 5};
			Node newNode = new Node((int) calDistance(obstacleCoord, robotPos.getCoord()), 0, obstacleCoord, null, obstacle.getObstacleID());
			nodes.add(newNode);
		}

		// Add neighbours
		for (Node start : nodes) {
			ArrayList<Node.Edge> neighbours = createNeighbours(start, nodes);
			start.setNeighbour(neighbours);
		}
		return nodes;
	}

	private ArrayList<Node.Edge> createNeighbours(Node n, ArrayList<Node> nodes) {
		// Add neighbours
		ArrayList<Node.Edge> neighbours = new ArrayList<>();
		for (Node end : nodes) {
			if (n != end) {
				double dist = calDistance(n.getCoord(), end.getCoord());
				Node.Edge e = new Node.Edge((int) dist, end);
				neighbours.add(e);
			}
		}
		return neighbours;
	}

//	public ArrayList<Node> updateNodes(ArrayList<Node> nodes) {
//		Node robotPos = new Node(0, 0, new int[] {(int) robot.getRobotCenter().getX(), (int) robot.getRobotCenter().getX()}, null, -1);
//		nodes.set(0, robotPos);
//
//		for (Node start : nodes) {
//			double dist = calDistance(start.getCoord(), robotPos.getCoord());
//			Node.Edge e = new Node.Edge((int) dist, robotPos);
//			start.neighbour.set(0, e);
//		}
//
//		ArrayList<Node.Edge> robotNeighbour = createNeighbours(robotPos, nodes);
//		nodes.get(0).setNeighbour(robotNeighbour);
//
//		return nodes;
//	}
}