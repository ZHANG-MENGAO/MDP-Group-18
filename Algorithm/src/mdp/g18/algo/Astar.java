package mdp.g18.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import mdp.g18.sim.Node.Edge;

public class Astar {
	ArrayList<Obstacle> obstacles;
	ArrayList<Node.Edge> nodeEdgePair;
	Robot robot;

	private ArrayList<Node> nodes;
	
	Astar(ArrayList<Obstacle> obstacleObjects, Robot robot){
		this.obstacles = obstacleObjects;
		this.robot = robot;
		init();
	}

	private void init() {
		// Run A* to update parent of each node
		//nodes = createNodes();
		//nodes = runAStar(nodes);
		this.nodes = runAlgo();
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
	
	private ArrayList<Node> runAlgo(){ 
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
				
				if(totalWeight < minDist && !closedList.contains(child)) {
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
	
	private ArrayList<Node> runAStar(ArrayList<Node> nodes){
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
		Node robotPos = new Node(0, new int[] {this.robot.getxCoordinate(), this.robot.getyCoordinate()}, null, -1);
		nodes.add(robotPos);

		for (Obstacle obstacle: obstacles) {
			//int[] obstacleCoord = new int[] {obstacle.getxCoordinate(), -(Arena.GRIDNO - obstacle.getyCoordinate() - 1)};
			int[] obstacleCoord = new int[] {obstacle.getxImageCoordinate(), -(Arena.GRIDNO - obstacle.getyImageCoordinate() - 1)};
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
		for(int i = 0; i < Arena.GRIDNO; i++) {
			for(int j = 0; j < Arena.GRIDNO; j++) {
				if (ArenaFrame.obstacles[i][j] == -1) {
					Node newNode = new Node(1, new int[] {i, -(Arena.GRIDNO - j - 1)}, null, -1);
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
				//double dist = calDistance(n.getCoord(), end.getCoord());
				Node.Edge e = new Node.Edge((int) 1, end);
				neighbours.add(e);
			}
			
			//right
			if ((end.getCoord()[0] - n.getCoord()[0] == 1) && (end.getCoord()[1] - n.getCoord()[1] == 0)) {
				//double dist = calDistance(n.getCoord(), end.getCoord());
				Node.Edge e = new Node.Edge((int) 1, end);
				neighbours.add(e);
			}
			
			//top
			if ((n.getCoord()[0] - end.getCoord()[0] == 0) && (end.getCoord()[1] - n.getCoord()[1] == -1)) {
				//double dist = calDistance(n.getCoord(), end.getCoord());
				Node.Edge e = new Node.Edge((int) 1, end);
				neighbours.add(e);
			}
			
			//bottom
			if ((n.getCoord()[0] - end.getCoord()[0] == 0) && (n.getCoord()[1] - end.getCoord()[1] == -1)) {
				//double dist = calDistance(n.getCoord(), end.getCoord());
				Node.Edge e = new Node.Edge((int) 1, end);
				neighbours.add(e);
			}
		}
		return neighbours;
	} 
	
	public static Node AstarSearch(Node source, Node goal, char originalDir){

        Set<Node> explored = new HashSet<Node>();

        PriorityQueue<Node> queue = new PriorityQueue<Node>(20, 
                new Comparator<Node>(){
                         //override compare method
         public int compare(Node i, Node j){
            if(i.getF() > j.getF()){
                return 1;
            }
            else if (i.getF() < j.getF()){
                return -1;
            }
            else{
                return 0;
            }
         }
         });

        //cost from start
        source.setG(0);
        source.setDirection(originalDir);

        queue.add(source);

        boolean found = false;

        while((!queue.isEmpty())&&(!found)){

                //the node in having the lowest f_score value
                Node current = queue.poll();
                //queue.clear();
                explored.add(current);

                //goal found
                if(current == goal){
                    return current;
                }

                //check every child of current node
                for(Node.Edge e : current.neighbour){
                        Node child = e.node;
                        child.setDirection(current.getDirection());
                        double cost = e.weight;
                        double temp_g_scores = 0;
                        
                        if (current.getDirection() == 'N' && child.getCoord()[1] == current.getCoord()[1]) {
        	            	if(child.getCoord()[0] < current.getCoord()[0]) {
        	            		child.setDirection('W');
        	            	} else if(child.getCoord()[0] > current.getCoord()[0]) {
        	            		child.setDirection('E');
        	            	} else {
        	            		child.setDirection(current.getDirection());
        	            	}
        	            	temp_g_scores = current.getG() + cost + 2;
        	            }
        	            else if (current.getDirection() == 'S' && child.getCoord()[1] == current.getCoord()[1]) {
        	            	if(child.getCoord()[0] < current.getCoord()[0]) {
        	            		child.setDirection('E');
        	            	} else if(child.getCoord()[0] > current.getCoord()[0]) {
        	            		child.setDirection('W');
        	            	}else {
        	            		child.setDirection(current.getDirection());
        	            	}
        	            	temp_g_scores = current.getG() + cost + 2;
        	            }
        	            else if (current.getDirection() == 'E' && child.getCoord()[0] == current.getCoord()[0]) {
        	            	if(child.getCoord()[1] < current.getCoord()[1]) {
        	            		child.setDirection('N');
        	            	} else if(child.getCoord()[1] > current.getCoord()[1]) {
        	            		child.setDirection('S');
        	            	}else {
        	            		child.setDirection(current.getDirection());
        	            	}
        	            	temp_g_scores = current.getG() + cost + 2;
        	            }
        	            else if (current.getDirection() == 'W' && child.getCoord()[0] == current.getCoord()[0]) {
        	            	if(child.getCoord()[1] < current.getCoord()[1]) {
        	            		child.setDirection('S');
        	            	} else if(child.getCoord()[1] > current.getCoord()[1]) {
        	            		child.setDirection('N');
        	            	}else {
        	            		child.setDirection(current.getDirection());
        	            	}
        	            	temp_g_scores = current.getG() + cost + 2;
        	            }else {
        	            	temp_g_scores = current.getG() + cost;
        	            }
                        double temp_f_scores = temp_g_scores + child.calculateH(child, goal);

                        //System.out.println(current.getDirection());
                        /*if child node has been evaluated and 
                        the newer f_score is higher, skip*/
                        
                        if((explored.contains(child)) && 
                                (temp_f_scores >= child.getF())){
                                continue;
                        }

                        /*else if child node is not in queue or 
                        newer f_score is lower*/
                        
                        else if((!queue.contains(child)) || 
                                (temp_f_scores < child.getF())){

                                child.setParent(current);
                                child.setG(temp_g_scores);
                                child.setF(temp_f_scores);

                                if(queue.contains(child)){
                                        queue.remove(child);
                                }

                                queue.add(child);
                        }
                }
        	}
        	return null;
		}
	
	public ArrayList<Data> findPath(Node target){
	    Node n = target;
	    if(n==null)
	        return null;

	    ArrayList<Data> ids = new ArrayList<>();

	    while(n.getParent() != null){
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
