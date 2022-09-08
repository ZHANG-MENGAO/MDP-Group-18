package mdp.g18.algo;

import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class PathFinder {
	
	public Robot robot;
	public Obstacle obstacle;
	public TurningRadius turningRadius;
	
	private static List<String> bothDirection = Arrays.asList("right","left");
	private static List<String> movement = Arrays.asList("front","reverse");
	
	private int[] obstacleCenter = new int[2];
	private int[] robotCenter = new int[2];
	
	PathFinder(Robot robot, Obstacle obstacle){
		
		this.robot = robot;
		this.obstacle = obstacle;
		setObstacleCenter();
		setRobotCenter();
	}
	
	// LSL & RSR shortest path finding
	public void CSCsameC () {
		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] p1 = new int[2];
		int[] p2 = new int[2];
		
		double robotArc;
		double obstacleArc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;
		
		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleCenter);
				path = "RSR";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleCenter);
				path = "LSL";
			}
			
			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();
				
			p2[0] = (int) this.obstacle.turningRadius.getCenter().getX();
			p2[1] = (int) this.obstacle.turningRadius.getCenter().getY();
			
			double l = straightDistance(p1, p2);
			
			v1 = this.vector1(p1,p2);
			if (direction == "right") {
				v2 = this.vector2(v1, (Math.PI/2));
			} else {
				v2 = this.vector2(v1, -(Math.PI/2));
			}
			pt1 = this.pt1(p1, v2, l);
			pt2 = this.pt2(pt1, v1);
			
			System.out.println(p1[0]);
			System.out.println(p1[1]);
			System.out.println(p2[0]);
			System.out.println(p2[1]);
			
			robotArc = computeArcLength(robotCenter,p1,pt1,direction);
			obstacleArc = computeArcLength(pt2,p2,obstacleCenter,direction);
				
			new_distance = robotArc + l + obstacleArc;
			
			if(new_distance < distance) {
				distance = new_distance;
				bestDir = path;
			}
		
		}
		
		System.out.println(distance);
		System.out.println(bestDir);
	}
	
	// RSL & LSR shortest path finding
	public void CSCdiffC () {
		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] v3 = new int[2];
		int[] p1 = new int[2];
		int[] p2 = new int[2];
			
		double robotArc;
		double obstacleArc;
		double new_distance = 0;
		double theta = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;
			
		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleCenter);
				path = "RSL";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleCenter);
				path = "LSR";
			}
				
			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();
			
			p2[0] = (int) this.obstacle.turningRadius.getCenter().getX();				
			p2[1] = (int) this.obstacle.turningRadius.getCenter().getY();
				
			double d = straightDistance(p1, p2);
			double l = straightTravel(d,2 * TurningRadius.getRadius());
				
			theta = Math.acos(2 * TurningRadius.getRadius() / d);
			v1 = this.vector1(p1,p2);
				
			if (direction == "right") {
				v2 = this.vector2_version2(v1, theta);
			} else {
				v2 = this.vector2_version2(v1, -theta);
			}
			
			pt1 = this.pt1(p1, v2, d);
			v3 = this.vector3(v2);
			pt2 = this.pt1(p2, v3, d);
				
			if (direction == "right") {
				robotArc = computeArcLength(robotCenter,p1,pt1,direction);
				obstacleArc = computeArcLength(pt2,p2,obstacleCenter,"left");
			} else {
				robotArc = computeArcLength(robotCenter,p1,pt1,direction);
				obstacleArc = computeArcLength(pt2,p2,obstacleCenter,"right");
			}
					
			new_distance = robotArc + l + obstacleArc;
				
			if(new_distance < distance) {
				distance = new_distance;
				bestDir = path;
			}
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}
	
	// RLR & LRL shortest path finding(case 1)
	public void CCCcase1 () {
		int[] q = new int[2];
		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] p1 = new int[2];
		int[] p2 = new int[2];
		int[] p3 = new int[2];
		int[] c3 = new int[2];
			
		double robotArc;
		double obstacleArc;
		double c3Arc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;
			
		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleCenter);
				path = "RLR";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleCenter);
				path = "LRL";
			}
				
			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();
				
			p2[0] = (int) this.obstacle.turningRadius.getCenter().getX();
			p2[1] = (int) this.obstacle.turningRadius.getCenter().getY();
				
			double d = straightDistance(p1, p2);
				
			q = this.midPoint(p1,p2);
			v1 = this.vector1(p1,p2);
			
			if(direction == "right") {
				v2 = this.vector2_version3(p1,p2);
			} else {
				v2 = this.vector2_version4(p1,p2);
			}
			
			double d1 = this.d1(TurningRadius.getRadius(),d);
			p3 = this.p3(q, v2, d1, d);
			pt1 = this.midPoint(p1, p3);
			pt2 = this.midPoint(p2, p3);
			
			if (direction == "right") {
				turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
				c3[0] = (int) this.turningRadius.getCenter().getX();
				c3[1] = (int) this.turningRadius.getCenter().getY();
				c3Arc = computeArcLength(pt1,c3,pt2,"left");
			} else {
				turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
				c3[0] = (int) this.turningRadius.getCenter().getX();
				c3[1] = (int) this.turningRadius.getCenter().getY();
				c3Arc = computeArcLength(pt1,c3,pt2,"right");
			}
			
			robotArc = computeArcLength(robotCenter,p1,pt1,direction);
			obstacleArc = computeArcLength(pt2,p2,obstacleCenter,direction);
			
			new_distance = robotArc + c3Arc + obstacleArc;
				
			if(new_distance < distance) {
				distance = new_distance;
				bestDir = path;
			}
			//System.out.println(p1[0]);
			//System.out.println(p1[1]);
			//System.out.println(p3[0]);
			//System.out.println(p3[1]);
			//System.out.println(p_new[0]);
			//System.out.println(p_new[1]);
			//System.out.println(pt1[0]);
			//System.out.println(pt1[1]);
			//System.out.println(pt2[0]);
			//System.out.println(pt2[1]);
			System.out.println(robotArc);
			System.out.println(c3Arc);
			System.out.println(obstacleArc);
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}
	
	// RLR & LRL shortest path finding(case 2)
	public void CCCcase2 () {
		int[] q = new int[2];
		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] p1 = new int[2];
		int[] p2 = new int[2];
		int[] p3 = new int[2];
		int[] c3 = new int[2];
				
		double robotArc;
		double obstacleArc;
		double c3Arc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;
				
		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleCenter);
				path = "RLR";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleCenter);
				path = "LRL";
			}
					
			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();
					
			p2[0] = (int) this.obstacle.turningRadius.getCenter().getX();
			p2[1] = (int) this.obstacle.turningRadius.getCenter().getY();
					
			double d = straightDistance(p1, p2);
					
			q = this.midPoint(p1,p2);
			v1 = this.vector1(p1,p2);
				
			if(direction == "right") {
				v2 = this.vector2_version4(p1,p2);
			} else {
				v2 = this.vector2_version3(p1,p2);
			}
				
			double d1 = this.d1(TurningRadius.getRadius(),d);
			p3 = this.p3(q, v2, d1, d);
			pt1 = this.midPoint(p1, p3);
			pt2 = this.midPoint(p2, p3);
				
			if (direction == "right") {
				turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
				c3[0] = (int) this.turningRadius.getCenter().getX();
				c3[1] = (int) this.turningRadius.getCenter().getY();
				c3Arc = computeArcLength(pt1,c3,pt2,"left");
			} else {
				turningRadius = new TurningRadius(new Point2D.Double(p3[0], p3[1]));
				c3[0] = (int) this.turningRadius.getCenter().getX();
				c3[1] = (int) this.turningRadius.getCenter().getY();
				c3Arc = computeArcLength(pt1,c3,pt2,"right");
			}
				
			robotArc = computeArcLength(robotCenter,p1,pt1,direction);
			obstacleArc = computeArcLength(pt2,p2,obstacleCenter,direction);
				
			new_distance = robotArc + c3Arc + obstacleArc;
					
			if(new_distance < distance) {
				distance = new_distance;
				bestDir = path;
			}
				//System.out.println(d);
				//System.out.println(p1[0]);
				//System.out.println(p1[1]);
				//System.out.println(p3[0]);
				//System.out.println(p3[1]);
				//System.out.println(pt1[0]);
				//System.out.println(pt1[1]);
				//System.out.println(pt2[0]);
				//System.out.println(pt2[1]);
				//System.out.println(robotArc);
				//System.out.println(c3Arc);
				//System.out.println(obstacleArc);
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}
	
	// R & L shortest path finding
	public void C () {
		int[] p1 = new int[2];
		int[] p2 = new int[2];
				
		double robotArc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;
				
		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleCenter);
				path = "R";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleCenter);
				path = "L";
			}
					
			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();
					
			p2[0] = (int) this.obstacle.turningRadius.getCenter().getX();
			p2[1] = (int) this.obstacle.turningRadius.getCenter().getY();
			
			//robot can reach obstacle
			if (this.robot.canReachTurn(p2)) {
				robotArc = computeArcLength(robotCenter,p1,obstacleCenter,direction);
				
				new_distance = robotArc;
				
				if(new_distance < distance) {
					distance = new_distance;
					bestDir = path;
				}
			}
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}
	
	// RL & LR shortest path finding
	public void CC () {
		int[] p1 = new int[2];
		int[] p2 = new int[2];
		int[] pt1 = new int[2];
				
		double robotArc;
		double obstacleArc;
		double new_distance = 0;
		String path = "";
		String bestDir = "";
		double distance = Double.POSITIVE_INFINITY;
				
		for (String direction: bothDirection) {
			if (direction == "right") {
				this.robot.createCircleRight(robotCenter,"front");
				this.obstacle.createCircleLeft(obstacleCenter);
				path = "RL";
			} else {
				this.robot.createCircleLeft(robotCenter,"front");
				this.obstacle.createCircleRight(obstacleCenter);
				path = "LR";
			}
					
			p1[0] = (int) this.robot.turningRadius.getCenter().getX();
			p1[1] = (int) this.robot.turningRadius.getCenter().getY();
					
			p2[0] = (int) this.obstacle.turningRadius.getCenter().getX();
			p2[1] = (int) this.obstacle.turningRadius.getCenter().getY();
			
			pt1 = midPoint(p1,p2);
			double d = straightDistance(p1, p2);
			
			System.out.println(d);
			System.out.println(p1[0]);
			System.out.println(p1[1]);
			System.out.println(p2[0]);
			System.out.println(p2[1]);
			
			
			//robot can reach obstacle
			if (this.robot.canReachTurnTurn(d)) {
				robotArc = computeArcLength(robotCenter,p1,pt1,direction);
				obstacleArc = computeArcLength(pt1,p2,obstacleCenter,direction);
				
				new_distance = robotArc + obstacleArc;
				
				if(new_distance < distance) {
					distance = new_distance;
					bestDir = path;
				}
			}
		}
		System.out.println(distance);
		System.out.println(bestDir);
	}
	
	public int[] vector1(int[] p1, int[] p2) {
		int[] v1 = new int[2];
		v1[0] = p2[0] - p1[0];
		v1[1] = p2[1]  - p1[1];
		return v1;
	}
	
	public int[] vector2(int[] v1, double theta) {
		int[] v2 = new int[2];
		v2[0] = round(v1[0] * Math.cos(theta) - v1[1] * Math.sin(theta));
		v2[1] = round(v1[0] * Math.sin(theta) + v1[1] * Math.cos(theta));
		return v2;
	}
	
	public int[] vector2_version2(int[] v1, double theta) {
		int[] v2 = new int[2];
		v2[0] = round(v1[0] * Math.cos(theta) + v1[1] * Math.sin(theta));
		v2[1] = round(v1[0] * Math.sin(theta) - v1[1] * Math.cos(theta));
		return v2;
	}
	
	public int[] vector2_version3(int[] p1, int[] p2) {
		int[] v2 = new int[2];
		v2[0] = p2[1] - p1[1];
		v2[1] = p1[0] - p2[0];
		return v2;
	}
	
	public int[] vector2_version4(int[] p1, int[] p2) {
		int[] v2 = new int[2];
		v2[0] = p1[1] - p2[1];
		v2[1] = p2[0] - p1[0];
		return v2;
	}
	
	public int[] vector3(int[] v2) {
		int[] v3 = new int[2];
		v3[0] = -v2[0];
		v3[1] = -v2[1];
		return v3;
	}
	
	public int[] pt1(int[] p1, int[] v2, double l) {
		int[] pt1 = new int[2];
		pt1[0] = round(p1[0] - (25.0/l * v2[0]));
		pt1[1] = round(p1[1] - (25.0/l * v2[1]));
		return pt1;
	}
	
	public int[] pt2(int[] pt1, int[] v1) {
		int[] pt2 = new int[2];
		pt2[0] = pt1[0] + v1[0];
		pt2[1] = pt1[1] + v1[1];
		return pt2;
	}
	
	public double straightDistance(int[] p1, int[] p2) {
		int x = Math.abs(p2[0] - p1[0]);
		int y = Math.abs(p2[1] - p1[1]);
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
	}
	
	public double straightTravel(double d, double r) {
		return Math.sqrt(Math.pow(d,2) - Math.pow(r,2));
	}
	
	public int[] midPoint(int[] v1, int[] v2) {
		int[] mid = new int[2];
		mid[0] = round((v1[0] + v2[0]) / 2);
		mid[1] = round((v1[1] + v2[1]) / 2);
		return mid;
	}
	
	public double d1(double r, double d) {
		return Math.sqrt(Math.pow(2 * r,2) - Math.pow(d / 2,2));
	}
	
	public int[] p3(int[] q, int[] v2, double d1, double d) {
		int[] pt1 = new int[2];
		pt1[0] = round(q[0] - (d1/d * v2[0]));
		pt1[1] = round(q[1] - (d1/d * v2[1]));
		return pt1;
	}
	
	public double computeArcLength(int[] p, int[] p1, int[] pt1, String direction) {
		
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		double theta;
		
		v1[0] = p[0] - p1[0];
		v1[1] = (-p[1])  - (-p1[1]);
		v2[0] = pt1[0] - p1[0];
		v2[1] = (-pt1[1])  - (-p1[1]);

		theta = Math.atan2(v2[1], v2[0]) - Math.atan2(v1[1], v1[0]);
		
		if (theta < 0 && direction == "left") {
			theta += 2 * Math.PI;
		} else if (theta > 0 && direction == "right"){
			theta -= 2 * Math.PI;
		}
		//System.out.println(theta);
		//System.out.println(Math.atan2(v2[1], v2[0]));
		//System.out.println(Math.atan2(v1[1], v1[0]));
		return Math.abs(theta * TurningRadius.getRadius());
	}
	
	private static int round(double val) {
        return (int) Math.round(val);
    }
	/*
	public void rsr(boolean running) {
		
		int[] pt1 = new int[2];
		int[] pt2 = new int[2];
		int[] v1 = new int[2];
		int[] v2 = new int[2];
		int[] p1 = new int[2];
		
		// 22 - 32
		this.robot.createCircleRight(); // create turning radius for robot
		//double l = l(p1, obstacleCenter);
		
		p1[0] = (int) this.robot.turningRadius.getCenter().getX();
		p1[1] = (int) this.robot.turningRadius.getCenter().getY();
		//robotCenter = calculateRobotCenter(this.robot.getX(), this.robot.getY(), this.robot.getOrientation());
		v1 = this.vector1(p1); // done
		v2 = this.vector2(v1);
		System.out.println(v2[0]);
		System.out.println(v2[1]);
		/*v2 = this.vector2(v1);
		pt1 = this.pt1(p1, v2, l);
		pt2 = this.pt2(pt1, v1);
		
		System.out.println(robotCenter[0]);
		System.out.println(robotCenter[1]);
		System.out.println(pt1[0]);
		System.out.println(pt1[1]);

		this.robot.turnRight();
		this.robot.turnRight();
		this.robot.moveForward();*/
		
		/*do {
			this.robot.moveForward();
		}while(robotCenter[0] != pt2[0] && robotCenter[1] != pt2[1]);
		
		do {
			this.robot.turnRight();
		}while(this.robot.getOrientation() == RobotOrientation.E);
	}*/

	/*
	public void findPath(boolean running) {
		
		if(running) {
			// find best action
			String bestAction = findBestAction(this.robot.getX(),this.robot.getY(),this.robot.getOrientation());
			switch(bestAction) {
			case "reverse":
				this.robot.reverseBackward();
				this.robot.updateSensor();
				break;
			case "moveFroward":
				this.robot.moveForward();
				this.robot.updateSensor();
				break;
			case "reverseLeft":
				this.robot.reverseLeft();
				this.robot.updateSensor();
				break;
			case "reverseRight":
				this.robot.reverseRight();
				this.robot.updateSensor();
				break;
			case "turnLeft":
				this.robot.turnLeft();
				this.robot.updateSensor();
				break;
			case "turnRight":
				this.robot.turnRight();
				this.robot.updateSensor();
				break;
			default:
				break;
			}
			//System.out.print(this.robot.getSensorY());
		}
	}
	
	public String findBestAction(int x, int y, RobotOrientation orientation) {
		String methodname = "";
		double new_distance;
		double distance = Double.POSITIVE_INFINITY;
		int[] robotCenter = new int[2];
		
		int robotOriginalX = x;
		int robotOriginalY = y;
		RobotOrientation orientationOriginal = orientation;
		
		Method[] methods = this.robot.getClass().getMethods();

		for( int index =0; index < methods.length; index++){
			if( methods[index].getName().contains("move")||methods[index].getName().contains("reverse")||methods[index].getName().contains("turn")){
				try {
					methods[index].invoke(this.robot);
					robotCenter = calculateRobotCenter(this.robot.getX(), this.robot.getY(), this.robot.getOrientation());
					new_distance = straightDistance(robotCenter, obstacleCenter);
					//System.out.println(methods[index].getName());
					//System.out.println(robotCenter[0]);
					//System.out.println(robotCenter[1]);
					//System.out.println(new_distance);
					if(new_distance < distance) {
						distance = new_distance;
						methodname = methods[index].getName();
					}
					
					//set to original
					this.robot.setX(robotOriginalX);
					this.robot.setY(robotOriginalY);
					//this.robot.setOrientation(orientationOriginal);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		System.out.println(methodname);
		return methodname;
	}*/

	public int[] calculateRobotCenter(int x, int y, RobotOrientation orientation) {
		
		int valuex;
		int valuey;
		int[] robotCenter = new int[2];
		
		switch(orientation) {
		case N:
			valuex = x + 15;
			valuey = y - 15;
			break;
		case S:
			valuex = x - 15;
			valuey = y + 15;
			break;
		case E:
			valuex = x + 15;
			valuey = y + 15;
			break;
		case W:
			valuex = x - 15;
			valuey = y - 15;
			break;
		case NE1:
			valuex = x + 19;
			valuey = y - 8;
			break;
		case NE2:
			valuex = x + 21;
			valuey = y - 2;
			break;
		case NE3:
			valuex = x + 20;
			valuey = y - 4;
			break;
		case SE1:
			valuex = x - 3;
			valuey = y + 21;
			break;
		case SE2:
			valuex = x + 3;
			valuey = y + 21;
			break;
		case SE3:
			valuex = x + 9;
			valuey = y + 19;
			break;
		case NW1:
			valuex = x + 9;
			valuey = y - 18;
			break;
		case NW2:
			valuex = x + 3;
			valuey = y - 21;
			break;
		case NW3:
			valuex = x - 2;
			valuey = y - 21;
			break;
		case SW1:
			valuex = x - 22;
			valuey = y + 2;
			break;
		case SW2:
			valuex = x + 21;
			valuey = y - 4;
			break;
		case SW3:
			valuex = x - 19;
			valuey = y - 9;
			break;
		default:
			valuex = 0;
			valuey = 0;
			break;
		}
		
		robotCenter[0] = valuex;
		robotCenter[1] = valuey;
		
		return robotCenter;
	}
	
	public int[] originalRobotCenter(int x, int y, RobotOrientation orientation) {
		
		int valuex;
		int valuey;
		int[] robotCenter = new int[2];
		
		switch(orientation) {
		case N:
			valuex = x + 15;
			valuey = y - 15;
			break;
		case S:
			valuex = x - 15;
			valuey = y + 15;
			break;
		case E:
			valuex = x + 15;
			valuey = y + 15;
			break;
		case W:
			valuex = x - 15;
			valuey = y - 15;
			break;
		case NE1:
			valuex = x + 19;
			valuey = y - 8;
			break;
		case NE2:
			valuex = x + 21;
			valuey = y - 2;
			break;
		case NE3:
			valuex = x + 20;
			valuey = y - 4;
			break;
		case SE1:
			valuex = x - 3;
			valuey = y + 21;
			break;
		case SE2:
			valuex = x + 3;
			valuey = y + 21;
			break;
		case SE3:
			valuex = x + 9;
			valuey = y + 19;
			break;
		case NW1:
			valuex = x + 9;
			valuey = y - 18;
			break;
		case NW2:
			valuex = x + 3;
			valuey = y - 21;
			break;
		case NW3:
			valuex = x - 2;
			valuey = y - 21;
			break;
		case SW1:
			valuex = x - 22;
			valuey = y + 2;
			break;
		case SW2:
			valuex = x + 21;
			valuey = y - 4;
			break;
		case SW3:
			valuex = x - 19;
			valuey = y - 9;
			break;
		default:
			valuex = 0;
			valuey = 0;
			break;
		}
		
		robotCenter[0] = valuex;
		robotCenter[1] = valuey;
		
		return robotCenter;
	}
	
	// Center of obstacle
	public void setObstacleCenter() {
		obstacleCenter[0] = (int) this.obstacle.getObstacleCenter().getX();
		obstacleCenter[1] = (int) this.obstacle.getObstacleCenter().getY();
	}
	
	public int[] getObstacleCenter() {
		return obstacleCenter;
	}
	
	public void setRobotCenter() {
		robotCenter[0] = (int) this.robot.getRobotCenter().getX();
		robotCenter[1] = (int) this.robot.getRobotCenter().getY();
	}
	
	public int[] getRobotCenter() {
		return robotCenter;
	}
	
}
