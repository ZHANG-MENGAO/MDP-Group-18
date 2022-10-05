package mdp.g18.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Astar {
	ArrayList<Obstacle> obstacles;
	ArrayList<Node.Edge> nodeEdgePair;
	Robot robot;

	private ArrayList<Node> nodes;

	Astar(ArrayList<Obstacle> obstacleObjects, Robot robot) {
		this.obstacles = obstacleObjects;
		this.robot = robot;
		runAlgo();
	}

	public Obstacle getNextObstacle(Obstacle obs) {
		// Convert obstacle to node
		Node prev = null;
		if (obs == null) {
			prev = nodes.get(0);
		} else {
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
			} catch (NullPointerException e) {
				System.out.println("No more nodes.");
				return null;
			}
		}
		return null;
	}
	
	/*private ArrayList<Node> runAlgo(){
		// Create nodes of robot and obstacles
		nodes = createNodes();

		// Create another ArrayList to contain order of visiting
		ArrayList<Node> orderOfNodes = new ArrayList<>();
		orderOfNodes.add(nodes.get(0));

		// Loop through every neighbour of n to find nearest node of n
		for (Node n : nodes) {
			double minDist = 99999999;
			Node shortestNeighbour = null;
			Node parent = null;
			for (Node.Edge e : n.neighbour) {
				if (e.weight < minDist && !orderOfNodes.contains(e.node)) {
					minDist = e.weight;
					shortestNeighbour = e.node;
					parent = n;
				}
			}

			// Add nearest node to orderOfNodes
			if (shortestNeighbour != null) {
				orderOfNodes.add(shortestNeighbour);
				shortestNeighbour.setParent(parent);
			}
		}

		return orderOfNodes;
	}*/

	private ArrayList<Node> runAlgo() {
		nodes = createNodes();

		Node start = nodes.get(0);

		ArrayList<Node> closedList = new ArrayList<Node>(); // Visited
		ArrayList<Node> openList = new ArrayList<Node>(); // Frontier

		// Visiting start now
		openList.add(start);

		while (!openList.isEmpty()) {

			Node n = openList.get(0);

			double minDist = 99999999;
			Node parent = null;
			Node shortestNeighbour = null;

			for (Node.Edge e : n.neighbour) {

				Node child = e.node;
				double totalWeight = e.weight;

				if (totalWeight < minDist && !closedList.contains(child)) {
					minDist = totalWeight;
					shortestNeighbour = child;
					parent = n;
				}
			}

			openList.remove(n);
			closedList.add(n);

			if (shortestNeighbour != null) {
				openList.add(shortestNeighbour);
				shortestNeighbour.setParent(parent);
			}
		}

		return closedList;
	}

	private ArrayList<Node> runAStar(ArrayList<Node> nodes) {
		Node start = nodes.get(0);
		//Node target = nodes.get(nodes.size() - 1);
		ArrayList<Node> closedList = new ArrayList<Node>(); // Visited
		ArrayList<Node> openList = new ArrayList<Node>(); // Frontier


		// Visiting start now
		openList.add(start);

		while (!openList.isEmpty()) {
			Node current = openList.get(0);

			// Go through every neighbour of currently visiting node
			for (Node.Edge edge : current.neighbour) {
				Node child = edge.node;
				double totalWeight = edge.weight;

				// Child has not been visited
				if (!openList.contains(child) && !closedList.contains(child)) {
					child.setParent(current);
					child.setG(totalWeight);
					openList.add(child);
				}
				// Child has been visited
				else {
					if (totalWeight < child.getG() && !closedList.contains(child)) {
						child.setParent(current);
						child.setG(totalWeight);
					}
				}
			}

			// Mark current as visited
			openList.remove(current);
			closedList.add(current);
			Collections.sort(openList);
		}
		return closedList;
	}

	private double calDistance(int[] current, int[] destination) {
		double x = Math.abs(current[0] - destination[0]);
		double y = Math.abs(current[1] - destination[1]);
		return Math.sqrt(Math.abs(Math.pow(x, 2) + Math.pow(y, 2)));
	}

	// Creates an ArrayList of Nodes to be used for the A* algorithm
	private ArrayList<Node> createNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		Node robotPos = new Node(0, new int[]{this.robot.getxCoordinate(), this.robot.getyCoordinate()}, null, -1);
		nodes.add(robotPos);

		for (Obstacle obstacle : obstacles) {
			int[] obstacleCoord = new int[]{obstacle.getxCoordinate(), -(Arena.GRIDNO - obstacle.getyCoordinate() - 1)};
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


	// create all nodes
	public ArrayList<Node> createAllNodes() {

		ArrayList<Node> nodesAll = new ArrayList<Node>();
		for (int i = 0; i < Arena.GRIDNO; i++) {
			for (int j = 0; j < Arena.GRIDNO; j++) {
				if (ArenaFrame.obstacles[i][j] == -1) {
					Node newNode = new Node(0, new int[]{i, -(Arena.GRIDNO - j - 1)}, null, -1);
					nodesAll.add(newNode);
				}
			}
		}

		for (Node start : nodesAll) {
			ArrayList<Node.Edge> neighbours = createNeighbourNSEW(start, nodesAll);

			start.setNeighbour(neighbours);
		}

		return nodesAll;
	}

	// neighbours nsew
	private ArrayList<Node.Edge> createNeighbourNSEW(Node n, ArrayList<Node> nodesAll) {
		// Add neighbours
		ArrayList<Node.Edge> neighbours = new ArrayList<>();
		for (Node end : nodesAll) {
			//left
			if ((n.getCoord()[0] - end.getCoord()[0] == 1) && (n.getCoord()[1] - end.getCoord()[1] == 0)) {
				double dist = calDistance(n.getCoord(), end.getCoord());
				Node.Edge e = new Node.Edge((int) dist, end);
				neighbours.add(e);
			}

			//right
			if ((end.getCoord()[0] - n.getCoord()[0] == 1) && (end.getCoord()[1] - n.getCoord()[1] == 0)) {
				double dist = calDistance(n.getCoord(), end.getCoord());
				Node.Edge e = new Node.Edge((int) dist, end);
				neighbours.add(e);
			}

			//top
			if ((n.getCoord()[0] - end.getCoord()[0] == 0) && (end.getCoord()[1] - n.getCoord()[1] == -1)) {
				double dist = calDistance(n.getCoord(), end.getCoord());
				Node.Edge e = new Node.Edge((int) dist, end);
				neighbours.add(e);
			}

			//bottom
			if ((n.getCoord()[0] - end.getCoord()[0] == 0) && (n.getCoord()[1] - end.getCoord()[1] == -1)) {
				double dist = calDistance(n.getCoord(), end.getCoord());
				Node.Edge e = new Node.Edge((int) dist, end);
				neighbours.add(e);
			}
		}
		return neighbours;
	}

	public static ArrayList<Data> printPath(Node target) {
		ArrayList<Data> path = new ArrayList<Data>();

		for (Node node = target; node != null; node = node.getParent()) {
			path.add(new Data(node.getCoord()[0], node.getCoord()[1], Direction.NORTH));
			//path.add(node);
		}

		Collections.reverse(path);

		return path;
	}

	public static Node AstarSearch(Node source, Node goal) {

		Set<Node> explored = new HashSet<Node>();

		PriorityQueue<Node> queue = new PriorityQueue<Node>(20, new Comparator<Node>() {
			//override compare method
			public int compare(Node i, Node j) {
				if (i.getF() > j.getF()) {
					return 1;
				} else if (i.getF() < j.getF()) {
					return -1;
				} else {
					return 0;
				}
			}
		}
		);

		//cost from start
		source.setG(0);

		queue.add(source);

		boolean found = false;

		while ((!queue.isEmpty()) && (!found)) {

			//the node in having the lowest f_score value
			Node current = queue.poll();

			explored.add(current);

			//goal found
			if (current == goal) {
				return current;
			}

//			int[] currCoord = current.getCoord();

			//check every child of current node
			for (Node.Edge e : current.neighbour) {
				Node child = e.node;
				int[] childCoord = child.getCoord();

				// Get parent's coordinate to check for turns
//				if (current.getParent() != null) {
//					Node parent = current.getParent();
//					int[] parentCoord = parent.getCoord();
//
//					// Increase f_score if there are turns
//					if (childCoord[0] != parentCoord[0] && childCoord[1] != parentCoord[1]) {
////						child.setF(child.getF() + 0.1);
//						child.setH(child.getH() + 0.1);
//					}
//				}

				double cost = e.weight;
				double temp_g_scores = current.getG() + cost;
				double temp_f_scores = temp_g_scores + child.getH();

				// if child node is not in queue or newer f_score is lower
				if ((!queue.contains(child)) ||	(temp_f_scores < child.getF())) {
					child.setParent(current);
					child.setG(temp_g_scores);
					child.setF(temp_f_scores);

					if (queue.contains(child)) {
						queue.remove(child);
					}

					queue.add(child);
				}
			}
		}
		return null;
	}

	public Node aStar(Node start, Node target) {

		PriorityQueue<Node> closedList = new PriorityQueue<>(); // frontier
		PriorityQueue<Node> openList = new PriorityQueue<>();  // visited

		start.setF(start.getG() + start.calculateH(start, target));
		openList.add(start);

		while (!openList.isEmpty()) {

			Node n = openList.peek(); // get top

			if (n == target) { // reach target
				return n;
			}

			// for all neighbour
			for (Node.Edge edge : n.neighbour) {
				double totalWeight;

				Node m = edge.node;
				totalWeight = n.getG() + edge.weight;


				if (!openList.contains(m) && !closedList.contains(m)) {
					m.setParent(n);
					m.setG(totalWeight);
					m.setF(m.getG() + m.calculateH(m, target));
					openList.add(m);
				} else {
					if (totalWeight < m.getG()) {
						m.setParent(n);
						m.setG(totalWeight);
						m.setF(m.getG() + m.calculateH(m, target));

						if (closedList.contains(m)) {
							closedList.remove(m);
							openList.add(m);
						}
					}
				}
			}

			openList.remove(n);
			closedList.add(n);
		}
		return null;
	}

	public ArrayList<Data> findPath(Node target) {
		Node n = target;
		if (n == null)
			return null;

		ArrayList<Data> ids = new ArrayList<>();

		while (n.getParent() != null) {
//			Node parent = n.getParent();
//			int[] parentCoord = parent.getCoord();
//			int[] nCoord = n.getCoord();
//
//			// Increase f_score if there are turns
//			// Might need to check for parent's parent
//			if (nCoord[0] != parentCoord[0] && nCoord[1] != parentCoord[1]) {
////				child.setF(child.getF() + 0.1);
//			}
			ids.add(new Data(n.getCoord()[0], n.getCoord()[1], Direction.NORTH));
			n = n.getParent();
		}

		ids.add(new Data(n.getCoord()[0], n.getCoord()[1], Direction.NORTH));

		Collections.reverse(ids);

		return ids;
	}

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
