package mdp.g18.algo;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LabelFrame extends JPanel{
	
	static JLabel xLabel;
	static JLabel yLabel;
	static JLabel distLabel;
	static JLabel currentLabel;
	
	LabelFrame(){
		this.setLayout(new GridLayout());
		// Label x
		xLabel = new JLabel("X Coordinate : No X");
		this.add(xLabel);
			 		
		// Label y
		yLabel = new JLabel("Y Coordinate : No Y");
		this.add(yLabel);
		
		// Distance Traveled
		distLabel = new JLabel("Planned Distance : 0");
		this.add(distLabel);
	}
}