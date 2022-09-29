package mdp.g18.algo;

import java.awt.geom.Point2D;

public class TurningRadius {
	
	private Point2D.Double center;
    private static int radius = 16;

    public TurningRadius(Point2D.Double c){
        this.center = c;
    }

    public void setCenter(Point2D.Double c){
        this.center = c;
    }
    
    public Point2D.Double getCenter(){
        return this.center;
    }
    
    public static int getRadius(){
        return radius;
    }

}
