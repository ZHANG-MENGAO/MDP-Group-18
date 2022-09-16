package mdp.g18.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Astar {
	List<Obstacle> obstacles;
	Arena arena;
	Robot robot;
	
	Astar(List<Obstacle> obstacleObjects, Arena arena, Robot robot){
		this.obstacles = obstacleObjects;
		this.arena = arena;
		this.robot = robot;
	}
	
	public Node runAStar(Node start, Node target){
	    PriorityQueue<Node> closedList = new PriorityQueue<>(); // Visited
	    PriorityQueue<Node> openList = new PriorityQueue<>(); // Frontier

	    start.setCost(start.getG(), start.calculateH(start,target));
	    openList.add(start);

	    while(!openList.isEmpty()){
	        Node n = openList.peek();

	        for(Node.Edge edge : n.neighbour){
	            Node m = edge.node;
	            double totalWeight = n.getG() + edge.weight;

	            if(!openList.contains(m) && !closedList.contains(m)){
	                m.setParent(n);
	                m.setG(totalWeight);
	                m.setCost(m.getG(),m.calculateH(m,target));
	                openList.add(m);
	            } else {
	                if(totalWeight < m.getG()){
	                	m.setParent(n);
		                m.setG(totalWeight);
		                m.setCost(m.getG(),m.calculateH(m,target));

	                    if(closedList.contains(m)){
	                        closedList.remove(m);
	                        openList.add(m);
	                    }
	                }
	            }
	        }

	        openList.remove(n);
	        closedList.add(n);
		    if(openList.isEmpty()){
			    return n;
		    }
	    }
	    return null;
	}
	
	public double calDistance(int[] current, int[] destination) {
		int x = Math.abs(current[0] - destination[0]);
		int y = Math.abs(current[1] - destination[1]);
		return Math.sqrt(Math.abs(Math.pow(x, 2) + Math.pow(y, 2)));
	}

	// Creates an ArrayList of Nodes to be used for the A* algorithm
	public ArrayList<Node> createNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		Node robotPos = new Node(0, 0, new int[] {(int) robot.getRobotCenter().getX(), (int) robot.getRobotCenter().getX()}, null, -1);
		nodes.add(robotPos);

		for (Obstacle obstacle: obstacles) {
			int[] obstacleCoord = new int[] {obstacle.getxCoordinate() - 5, obstacle.getyCoordinate() - 5};
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

	public ArrayList<Node> updateNodes(ArrayList<Node> nodes) {
		Node robotPos = new Node(0, 0, new int[] {(int) robot.getRobotCenter().getX(), (int) robot.getRobotCenter().getX()}, null, -1);
		nodes.set(0, robotPos);

		for (Node start : nodes) {
			double dist = calDistance(start.getCoord(), robotPos.getCoord());
			Node.Edge e = new Node.Edge((int) dist, robotPos);
			start.neighbour.set(0, e);
		}

		ArrayList<Node.Edge> robotNeighbour = createNeighbours(robotPos, nodes);
		nodes.get(0).setNeighbour(robotNeighbour);

		return nodes;
	}
}