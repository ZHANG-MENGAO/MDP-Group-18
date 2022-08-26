package mdp.g18.algo;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Astar {
	
	Astar(List<Obstacle> obstacleObjects,Arena arena,Robot robot){
		
	}
	
	public static Node aStar(Node start, Node target){
	    PriorityQueue<Node> closedList = new PriorityQueue<>(); // visited
	    PriorityQueue<Node> openList = new PriorityQueue<>(); //frontier
	    

	    start.setCost(start.getG(),start.calculateH(start,target));
	    openList.add(start);

	    while(!openList.isEmpty()){
	        Node n = openList.peek();
	        if(openList.isEmpty()){
	            return n;
	        }

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
	    }
	    return null;
	}
	
	public double calDistance(int[] current, int[] destination) {
		int x = Math.abs(current[0] - destination[0]);
		int y = Math.abs(current[1] - destination[1]);
		return Math.sqrt(Math.pow(x,2) - Math.pow(y,2));
	}
}
