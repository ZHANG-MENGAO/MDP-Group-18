package mdp.g18.algo;

import java.awt.*;

public class Arena {
	
	public static final int GRIDNO = 200;
	public static final int UNIT_SIZE = 600 / GRIDNO;
	public static final int ARENA_WIDTH = 600;
	public static final int ARENA_HEIGHT = 600 + UNIT_SIZE;
	
	Arena(){
		
	}
	
	public void paintArena(Graphics g) {
		
		// Overall Grid
		for(int i = 0; i < GRIDNO; i++) {
			for(int j = 0; j < GRIDNO; j++) {
				
				// Draw Arena
				g.setColor(Color.gray);
				g.fillRect(i * UNIT_SIZE,j * UNIT_SIZE + UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
			}
		}
		
		// Start Zone
		g.setColor(Color.green);
		for(int i = 0; i < 30; i++) {
			for(int j = 1; j <= 30; j++) {
				g.fillRect(i * UNIT_SIZE, ARENA_HEIGHT - UNIT_SIZE * j , UNIT_SIZE, UNIT_SIZE);
			}
		}
	}
}