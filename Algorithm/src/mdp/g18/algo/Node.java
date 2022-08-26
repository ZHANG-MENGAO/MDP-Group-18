package mdp.g18.algo;

import java.util.ArrayList;

public class Node implements Comparable<Node>{
	private double g;
	private int h;
	private int cost;

	private int[] coord;
	public ArrayList<Edge> neighbour;

	private Node parent;

	Node(int g, int h, int[] coord, Node parent) {
		this.g = g;
		this.h = h;
		this.coord = coord;
		this.parent = parent;
	}

	public void setCost(double g, int h) {
		this.cost = (int)g + h;
	}

	public double getG() {
		return g;
	}

	public void setG (double g){
		this.g = g;
	}
	
	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getCost() {
		return cost;
	}

	public int[] getCoord() {
		return this.coord;
	}

	public void setCoord(int[] coord) {
		this.coord = coord;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public int calculateH(Node current, Node target) {
		int x = Math.abs(current.getCoord()[0] - target.getCoord()[0]);
        int y = Math.abs(current.getCoord()[1] - target.getCoord()[1]);

        if (current.getParent().coord[0] == current.coord[0] && x == 0) return y;
        else if (current.getParent().coord[1] == current.coord[1] && y == 0) return x;
        else return x + y;
	}

	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static class Edge {
        Edge(int weight, Node node){
              this.weight = weight;
              this.node = node;
        }

        public int weight;
        public Node node;
	}
}
