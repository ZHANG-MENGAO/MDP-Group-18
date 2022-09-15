package mdp.g18.algo;

import java.util.ArrayList;

public class Path implements Comparable<Path> {
	private double dist;
	// TODO: create array of instructions with following format:
	//private [angle1, direction1, angle 2, direction 2, angle 3 direction 3] // set to NULL if segment does not exist
	ArrayList<Instruction> instructions = new ArrayList<>();
	private double[] pt1;
	private double[] pt2;
	
	Path(){
		
	}
	
	public int getSizeInstruction() {
		return instructions.size();
	}

	public void setPt1(double[] pt1) {
		this.pt1 = pt1;
	}

	public void setPt2(double[] pt2) {
		this.pt2 = pt2;
	}

	public double[] getPt1() {
		return pt1;
	}

	public double[] getPt2() {
		return pt2;
	}

	public double getDist() {
		return this.dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	public ArrayList<Instruction> getInstructions() {
		return instructions;
	}
	
	public void addInstructions(Instruction instruction) {
		this.instructions.add(instruction);
	}
	
	@Override
	public int compareTo(Path o) {
		if (this.getDist() > o.getDist()) {
			return 1;
		} else if (this.getDist() < o.getDist()) {
			return -1;
		}
		
		if (this.getSizeInstruction() > o.getSizeInstruction() ) {
			return 1;
		} else if ( this.getSizeInstruction()  < o.getSizeInstruction() ) {
			return -1;
		}
		return 0;
	}
}
