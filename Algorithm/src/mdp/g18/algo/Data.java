package mdp.g18.algo;

public class Data{
    private int[] data;
    private char orientation;

    public Data(int s1, int s2, double angle){
        this.data = new int[] {s1,s2};
        this.setOrientation(angle);
    }

    public int getElement(int index){
        return data[index];
    }
    
    public int[] getCoordinates() {
    	return this.data;
    }
    
    public char getOrientation() {
    	return this.orientation;
    }
    
    public void setOrientation(double angle) {
    	
    	if ((Math.max(0, angle) == Math.min(angle, 45)) || (Math.max(315, angle) == Math.min(angle, 359)) ||
    			(Math.max(-45, angle) == Math.min(angle, 0)) || (Math.max(-359, angle) == Math.min(angle, -315))){
    		this.orientation = 'N';
    	}
    	else if ((Math.max(45, angle) == Math.min(angle, 135))||
    			(Math.max(-315, angle) == Math.min(angle, -225))){
    		this.orientation = 'E';
    	}
    	else if ((Math.max(225, angle) == Math.min(angle, 315))||
    			(Math.max(-135, angle) == Math.min(angle, -45))){
    		this.orientation = 'W';
    	}
    	else {
    		this.orientation = 'S';
    	}
    	
    }
}
