package mdp.g18.algo;

import java.awt.event.*;
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
	    PriorityQueue<Node> closedList = new PriorityQueue<>(); // visited
	    PriorityQueue<Node> openList = new PriorityQueue<>(); //frontier
	    

	    start.setCost(start.getG(),start.calculateH(start,target));
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
		try {
			int x = Math.abs(current[0] - destination[0]);
			int y = Math.abs(current[1] - destination[1]);
			return Math.sqrt(Math.pow(x, 2) - Math.pow(y, 2));
		} catch (Exception e) {
			System.out.println("Null value in either node.");
		}
		return -1;
	}

	// Creates an ArrayList of Nodes to be used for the A* algorithm
	public List<Node> createNodes() {
		List<Node> nodes = new ArrayList<Node>();
		Node robotPos = new Node(0, 0, new int[] {robot.getX(), robot.getY()}, null, -1);
		nodes.add(robotPos);

		for (Obstacle obstacle: obstacles) {
			int[] obstacleCoord = new int[] {obstacle.getxCoordinate(), obstacle.getyCoordinate()};
			Node newNode = new Node(0, 0, obstacleCoord, null, obstacle.getObstacleID());
			nodes.add(newNode);
		}
		return nodes;
	}
}
