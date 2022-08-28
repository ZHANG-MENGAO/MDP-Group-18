package mdp.g18.algo;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ArenaFrame extends JPanel{

	// list of obstacles
	public List<Obstacle> obstacleObjects = new ArrayList<Obstacle>();
	
	// get mouse coordinates 
	public int mx = -100;
	public int my = -100;
	
	// Check for button pressed
	public boolean addObstacles = false;
	public boolean setImage = true;
	public boolean selectImage = false;
	public boolean clearObstacles = false;
	
	public boolean running = false;
	
	Direction mouseDir = Direction.UNSET;
	
	Move move;
	Click click;
	Robot robot;
	Obstacle obstacle;
	Arena arena;
	
	private static int [][] obstacles = new int[Arena.GRIDNO][Arena.GRIDNO];
	
	ArenaFrame(){
		this.setPreferredSize(new Dimension(Arena.ARENA_WIDTH,Arena.ARENA_HEIGHT));
		
		move = new Move();
		this.addMouseMotionListener(move);
		
		click = new Click();
		this.addMouseListener(click);
		
		arena = new Arena();
		robot = new Robot();
		
		// Initialize no obstacles
		for(int i = 0; i < Arena.GRIDNO; i++) {
			for(int j = 0; j < Arena.GRIDNO; j++) {
				obstacles[i][j] = 0;
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		arena.paintArena(g);
		robot.paintRobot(g);
		
		if (addObstacles) {
			obstacle = new Obstacle(getmaxID() + 1,coordinateX(),coordinateY(),Direction.UNSET);
			obstacle.paintObstacle(g, true);
		}
		
		// Draw Obstacles
		for( Obstacle obstacle: obstacleObjects) {
			obstacle.paintObstacle(g, false);
			mouseDir = move.mouseDirection();
			
			// Enables selection of the side the image
			if (obstacle.direction == Direction.UNSET) {
				if (setImage) {
					// Remove outline of adding obstacles
					addObstacles = false;

					// Paint image selection animation on frame
					obstacle.selectImage(g, true, mouseDir);
				}
				else {
					// Paint image location in correct colour and set direction of image
					obstacle.selectImage(g, false, mouseDir);
								// Allow image selection on next obstacle
					setImage = true;
					if (obstacleObjects.size() < 5) {
							addObstacles = true;
					}
				}
			}
			else {
				obstacle.selectImage(g, false, obstacle.direction);
			}
		}
		
		// Start
		if(running) {
			robot.moveForward();
			robot.updateSensor();
			System.out.println(robot.getSensorY());
			try {
				checkObstacle(robot.getOrientaion());
				checkCollisions();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			repaint();
		}
	}
	
	public void setSensor(RobotOrientation orientation, int sensorX, int sensorY) {
		for(int i = -10; i <= 40; i++) {
			for(int j = 0; j <= 20; j++) {
				ArenaFrame.obstacles[sensorX + i][Arena.GRIDNO + sensorY - 20 + j] = 1;
			}
		}
	}
	
	public void checkCollisions() {
		// Check Top & Bottom Border
		if(robot.getY() - robot.getRobotSize() <= -Arena.GRIDNO || robot.getY() + robot.getRobotSize() >= 0) {
			running = false;
		}
		
		// Check Right & Left Border
		if((robot.getX() + robot.getRobotSize() >= Arena.GRIDNO) || (robot.getX() - robot.getRobotSize() <= 0)) {
			running = false;
		}
		
		if (!running) {
			Thread.yield();
		}
	}
	
	// Check hit obstacle
	public void checkObstacle(RobotOrientation orientation) {
		switch (orientation) {
		case N:
			for(int i = -10; i <= 40; i++) {
				for(int j = 0; j <= 20; j++) {
					if ((obstacles[robot.getSensorX() + i][Arena.GRIDNO + robot.getSensorY() - 20 + j] != 0) && (Arena.GRIDNO + robot.getSensorY() - 20 + j > -200)) {
						for( Obstacle obstacle: obstacleObjects) {
							if (obstacle.getObstacleID() == obstacles[robot.getSensorX() + i][Arena.GRIDNO + robot.getSensorY() + j]) {
								System.out.println(obstacle.getDirection());
							}
						}
						running = false;
					}
				}
			}
			break;
		case S:
			for(int i = -40; i <= 10; i++) {
				for(int j = 0; j <= 20; j++) {
					if ((obstacles[robot.getSensorX() + i][Arena.GRIDNO + robot.getSensorY() + j] != 0) && (Arena.GRIDNO + robot.getSensorY() + j > 0)) {
						for( Obstacle obstacle: obstacleObjects) {
							if (obstacle.getObstacleID() == obstacles[robot.getSensorX() + i][Arena.GRIDNO + robot.getSensorY() + j]) {
								System.out.println(obstacle.getDirection());
							}
						}
						running = false;
					}
				}
			}
			break;
		case E:
			for(int i = 0; i <= 20; i++) {
				for(int j = -10; j <= 40; j++) {
					if ((obstacles[robot.getSensorX() + i][Arena.GRIDNO + robot.getSensorY() + j] != 0) && (robot.getSensorX() + i < 200)) {
						for( Obstacle obstacle: obstacleObjects) {
							if (obstacle.getObstacleID() == obstacles[robot.getSensorX() + i][Arena.GRIDNO + robot.getSensorY() + j]) {
								System.out.println(obstacle.getDirection());
							}
						}
						running = false;
					}
				}
			}
			break;
		case W:
			for(int i = 0; i <= 20; i++) {
				for(int j = -40; j <= 10; j++) {
					if ((obstacles[robot.getSensorX() - 20 + i][Arena.GRIDNO + robot.getSensorY() + j] != 0) && (robot.getSensorX() - 20 + i > 0)) {
						for( Obstacle obstacle: obstacleObjects) {
							if (obstacle.getObstacleID() == obstacles[robot.getSensorX() - 20 + i][Arena.GRIDNO + robot.getSensorY() +  j]) {
								System.out.println(obstacle.getDirection());
							}
						}
						running = false;
					}
				}
			}
			break;
		default:
			break;
		}

	}

	public class Move implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
			ControlFrame.xLabel.setText("X Coordinate : " + String.valueOf(coordinateX()));
			//ControlFrame.yLabel.setText("Y Coordinate : " + String.valueOf(coordinateY()));
			ControlFrame.yLabel.setText("Y Coordinate : " + String.valueOf(Arena.GRIDNO - coordinateY() - 1));
			//System.out.println(obstacles[coordinateX()][coordinateY()]);
			repaint();
		}
		
		public Direction mouseDirection() {
			if (coordinateY() <= obstacle.yCoordinate - obstacle.getLength() && coordinateX() >= obstacle.xCoordinate) {
				return Direction.NORTH;
			} else if (coordinateY() >= obstacle.yCoordinate && coordinateX() <= obstacle.xCoordinate + obstacle.getLength()) {
				return Direction.SOUTH;
			} else if (coordinateY() >= obstacle.yCoordinate - obstacle.getLength() && coordinateX() >= obstacle.xCoordinate - obstacle.getLength()) {
				return Direction.EAST;
			} else {
				return Direction.WEST;
			}
		}
	}
	
	public class Click implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			// click add obstacle
			if (obstacleObjects.size() < 5 && addObstacles && coordinateX() != -1 && coordinateY() != -1 && obstacles[coordinateX()][coordinateY()] == 0) {
				obstacle = new Obstacle(getmaxID() + 1,coordinateX(),coordinateY(),Direction.UNSET);
				addObstacle();
				obstacleObjects.add(obstacle);
				repaint();
			}
			
			// click clear obstacle
			if (obstacleObjects.size() > 0  && clearObstacles && coordinateX() != -1 && coordinateY() != -1 && obstacles[coordinateX()][coordinateY()] != 0) {
				for( Obstacle obstacle: obstacleObjects) {
					if (obstacle.getObstacleID() == obstacles[coordinateX()][coordinateY()]) {
						removeObstacle(obstacle.getObstacleID());
						obstacleObjects.remove(obstacle);
						repaint();
						break;
					}
				}
			}
			
			// click image direction on obstacle
			if (setImage && !addObstacles) {
				setImage = false;
				repaint();
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	// Add obstacle to array
	public void addObstacle() {

		for (int i = -10; i <= 0; i++) {
			for (int j = -10; j <= 0; j++) {
				obstacles[obstacle.xCoordinate + i][obstacle.yCoordinate + j] = obstacle.getObstacleID(); // top right coordinate
			}
		}
	}
	
	// remove obstacle from array
	public void removeObstacle(int obstacleID) {

		for (int i = 0; i < Arena.GRIDNO; i++) {
			for (int j = 0; j < Arena.GRIDNO; j++) {
				if (obstacles[i][j] == obstacleID) {
					obstacles[i][j] = 0;
				}
			}
		}
	}
	
	// remove obstacle from array
	public int getmaxID() {
		int maxID = 0;
		for( Obstacle obstacle: obstacleObjects) {
			if (obstacle.obstacleID > maxID) {
				maxID = obstacle.obstacleID;
			}
		}
		return maxID;
	}
	
	// Find x-coordinate of mouse
	public int coordinateX() {
		for(int i = 0; i < Arena.GRIDNO; i++) {
			for(int j = 0; j < Arena.GRIDNO; j++) {
				if ((mx >= i * Arena.UNIT_SIZE) && (mx < i * Arena.UNIT_SIZE + Arena.UNIT_SIZE) && (my >= j * Arena.UNIT_SIZE + Arena.UNIT_SIZE) && (my < j * Arena.UNIT_SIZE + Arena.UNIT_SIZE + Arena.UNIT_SIZE)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	// Find y-coordinate of mouse
	public int coordinateY() {
		for(int i = 0; i < Arena.GRIDNO; i++) {
			for(int j = 0; j < Arena.GRIDNO; j++) {
				if ((mx >= i * Arena.UNIT_SIZE) && (mx < i * Arena.UNIT_SIZE + Arena.UNIT_SIZE) && (my >= j * Arena.UNIT_SIZE + Arena.UNIT_SIZE) && (my < j * Arena.UNIT_SIZE + Arena.UNIT_SIZE + Arena.UNIT_SIZE)) {
					return j;
				}
			}
		}
		return -1;
	}

}