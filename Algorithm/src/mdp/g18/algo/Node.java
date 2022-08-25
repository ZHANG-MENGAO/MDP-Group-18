package mdp.g18.algo;

public class Node {
	private int g;
	private int h;
	private int cost;

	private int[] coord;

	private Node parent;

	Node(int g, int h, int[] coord, Node parent) {
		this.g = g;
		this.h = h;
		this.coord = coord;
		this.parent = parent;
	}

	public void updateCos(int g, int h) {
		this.cost = g + h;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
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
		return coord;
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
}
