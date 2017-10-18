import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DrawPanel extends JPanel {
	private List<Trajectory> trajList = new ArrayList();
	private Graphics g;
	
	DrawPanel () {
	}
	
	public void createDrone(Graphics g) {
		// add drone to arraylist
		// redraw arraylist
		
	}
	
	public void autoFillDrones(Graphics g) {
		
	}
	
	public void createTraj(Graphics g) {
	//	if(tList.size() == 0)
		{
		//	draw.createGrid(getGraphics());
			//Trajectory t = new Trajectory(0,0);
			//tList.add(t);
		}
		JTextField aField = new JTextField(5);
		JTextField bField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

		myPanel.add(new JLabel("Enter Angle:"));
		myPanel.add(aField);

		myPanel.add(Box.createVerticalStrut(15));

		myPanel.add(new JLabel("Trajectory's ID:"));
		myPanel.add(bField);

		myPanel.add(Box.createVerticalStrut(15));
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, " Enter Values For New SCS Simulation", JOptionPane.OK_CANCEL_OPTION);
		Trajectory existingTraj = null;
		int tempInt = 0;
		
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			String temp2 = bField.getText();
			int ang = Integer.parseInt(temp1);
			Trajectory t = new Trajectory(0,0);
		//	tList.add(t);
		}
		repaint();
	}
	
	public void createGrid(Graphics g) {
		JTextField aField = new JTextField(5);
		JTextField bField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

		myPanel.add(new JLabel("Enter number of rows:"));
		myPanel.add(aField);

		myPanel.add(Box.createVerticalStrut(15));

		myPanel.add(new JLabel("Enter number of columns:"));
		myPanel.add(bField);

		myPanel.add(Box.createVerticalStrut(15));
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, " Enter Values For New SCS Simulation", JOptionPane.OK_CANCEL_OPTION);
		
		int rows = 0;
		int cols = 0;
		
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			String temp2 = bField.getText();
			rows = Integer.parseInt(temp1);
			cols = Integer.parseInt(temp2);
		}
		
		int radius = 10;
		if(rows > cols)
		{
			radius = (getWidth() - 300)/rows;
		}
		else
			radius = (getHeight() - 200)/cols;
		
	    int distBetweenTraj = getWidth()/70;
	    
	    Trajectory t;
	    int distanceX = 400;
	    int distanceY = 100;
	    for(int r = 0; r < rows; r++)
	    {
	    	for(int c = 0; c < cols; c++)
	    	{
	    		t = new Trajectory(distanceX + (radius * r) + (distBetweenTraj * r), distanceY + (radius * c) + (distBetweenTraj * c));
	    		trajList.add(t);
	    	}
	    }
		
	    for(Trajectory traj : trajList)
	    {
	    	g.drawOval((int)(traj.getX()), (int)(traj.getY()), radius, radius);
	    }		
	}
	 
	//@Override
	public void paintComponent(Graphics g) {

	    super.paintComponent( g ); // call superclass's paintComponent  
	    
	    //Graphics2D g2 = ( Graphics2D ) g; // cast g to Graphics2D  

	   //Make Trajectories
	    //Trees
	    //Make initial Trajectory
	    int radius = getWidth()/10;
	    int distBetweenTraj = getWidth()/100;
	    if(trajList.size() > 0)
	    {
	    	g.drawOval((int)(getWidth()/2 - getWidth()/20), (int)(getHeight()/2 - radius), radius, radius);
	    	//g.drawOval((int)(getWidth()/2 + Math.cos(Math.toRadians(180)) * distBetweenTraj), (int)(getHeight()/2 + Math.sin(Math.toRadians(180) * distBetweenTraj)), (int)(getWidth()/10), (int)(getWidth()/10));
	//    	trajList.add(new Trajectory((float)(getWidth()/2 - getWidth()/20), (float)(getHeight()/2 - radius)));
	    }
	    //Make next trajectories
	    if(trajList.size() > 2){
	    	Trajectory traj = trajList.get(0);
	    	g.drawOval((int)(getWidth()/2 + Math.cos(Math.toRadians(45)) * distBetweenTraj), (int)(getHeight()/2 + Math.sin(Math.toRadians(45)) * distBetweenTraj), (int)(getWidth()/10), (int)(getWidth()/10));
	    }
	    
	    //Make grid
	    
	    
	    //Make Robots
	    
	    
	    //Drawing Trajectories
	    
	    //Drawing Robots
	}

}
