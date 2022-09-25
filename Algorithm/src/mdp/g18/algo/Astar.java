package mdp.g18.algo;

import java.util.ArrayList;

public class Astar {
	ArrayList<Obstacle> obstacles;
	Robot robot;

	private ArrayList<Node> nodes;
	
	Astar(ArrayList<Obstacle> obstacleObjects, Robot robot){
		this.obstacles = obstacleObjects;
		this.robot = robot;
		nodes = runAlgo();
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

	private ArrayList<Node> runAlgo(){
		// Create nodes of robot and obstacles
		nodes = createNodes();

		// Create another ArrayList to contain order of visiting
		ArrayList<Node> orderOfNodes = new ArrayList<>();
		orderOfNodes.add(nodes.get(0));

		// Loop through every neighbour of n to find nearest node of n
		for (Node n : nodes) {
			double minDist = 99999999;
			Node shortestNeighbour = null;
			for (Node.Edge e : n.neighbour) {
				if (e.weight < minDist && !orderOfNodes.contains(e.node)) {
					minDist = e.weight;
					shortestNeighbour = e.node;
				}
			}

			// Add nearest node to orderOfNodes
			if (shortestNeighbour != null) {
				orderOfNodes.add(shortestNeighbour);
				shortestNeighbour.setParent(n);
			}
		}

		return orderOfNodes;

//		Node start = nodes.get(0);
//		Node target = new Node(0, new double[] {0, 0}, null, -1);
//	    ArrayList<Node> closedList = new ArrayList<Node>(); // Visited
//	    ArrayList<Node> openList = new ArrayList<Node>(); // Frontier
//
//
//		// Visiting start now
//		openList.add(start);
//
//		while (!openList.isEmpty()) {
//			Node current = openList.get(0);
//
//			// Go through every neighbour of currently visiting node
//			for (Node.Edge edge : current.neighbour) {
//				Node child = edge.node;
//				double totalWeight = edge.weight;
//
//				// Child has not been visited
//				if (!openList.contains(child) && !closedList.contains(child)) {
//					child.setParent(current);
//					child.setG(totalWeight);
//					openList.add(child);
//				}
//				// Child has been visited
//				else {
//					if (totalWeight < child.getG() && !closedList.contains(child)) {
//						child.setParent(current);
//						child.setG(totalWeight);
//					}
//				}
//			}
//
//			// Mark current as visited
//			openList.remove(current);
//			closedList.add(current);
//			Collections.sort(openList);
//		}
//		return closedList;
	}
	
	private double calDistance(double[] current, double[] destination) {
		double x = Math.abs(current[0] - destination[0]);
		double y = Math.abs(current[1] - destination[1]);
		return Math.sqrt(Math.abs(Math.pow(x, 2) + Math.pow(y, 2)));
	}

	// Creates an ArrayList of Nodes to be used for the algorithm
	private ArrayList<Node> createNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		Node robotPos = new Node(0, new double[] {this.robot.getRobotCenter().getX(), this.robot.getRobotCenter().getY()}, null, -1);
		nodes.add(robotPos);

		for (Obstacle obstacle: obstacles) {
			double[] obstacleCoord = new double[] {obstacle.getObstacleCenter().getX(), obstacle.getObstacleCenter().getY()};
			Node newNode = new Node(calDistance(obstacleCoord, robotPos.getCoord()), obstacleCoord, null, obstacle.getObstacleID());
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