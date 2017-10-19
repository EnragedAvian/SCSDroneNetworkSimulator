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
		//Initial trajectory coordinates
		int x = getWidth()/2 + 200;
		int y = getHeight()/2;
		
		Trajectory t = new Trajectory(x, y);
		//Adds first trajectory to list once
		if(trajList.size() == 0)
		{
			trajList.add(t);
		}
		
		//Input Box
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
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, "Enter Values For New Trajectory", JOptionPane.OK_CANCEL_OPTION);
		
		double ang = 0;
		int id = 0;
		
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			String temp2 = bField.getText();
			//Gets angle and id(for traj to add on to)
			ang = Double.parseDouble(temp1);
			id = Integer.parseInt(temp2);
		}
		
		//Hardcoded radius and distBetweenTraj values
		int radius = 100;
		int distBetweenTraj = 20;
		
		//Creates next trajectory
		float distX = (float)(trajList.get(id).getX() + Math.cos(Math.toRadians(ang)) * (radius + distBetweenTraj));
		float distY = (float)(trajList.get(id).getY() - Math.sin(Math.toRadians(ang)) * (radius + distBetweenTraj));
		Trajectory traj = new Trajectory(distX, distY);
		trajList.add(traj);
		
		//Draws Trajectories
	    for(Trajectory n : trajList)
	    {
	    	g.drawOval((int)(n.getX()), (int)(n.getY()), radius, radius);
	    }
	}
	
	public void createGrid(Graphics g) {
		//Input Box
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
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, "Enter Values For Grid", JOptionPane.OK_CANCEL_OPTION);
		
		int rows = 0;
		int cols = 0;
		
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			String temp2 = bField.getText();
			//Gets rows and columns
			rows = Integer.parseInt(temp1);
			cols = Integer.parseInt(temp2);
		}
		
		//Sets initial radius
		int radius = 10;
		//Gets largest size the radius should be
		if(rows > cols)
		{
			radius = (getWidth() - 300)/rows;
		}
		else
			radius = (getHeight() - 200)/cols;
		
	    int distBetweenTraj = getWidth()/70;
	    
	    Trajectory t;
	    //Assures space for menu
	    int distanceX = 400;
	    int distanceY = 100;
	    
	    //Makes each trajectory
	    for(int r = 0; r < rows; r++)
	    {
	    	for(int c = 0; c < cols; c++)
	    	{
	    		t = new Trajectory(distanceX + (radius * r) + (distBetweenTraj * r), distanceY + (radius * c) + (distBetweenTraj * c));
	    		trajList.add(t);
	    	}
	    }
		
	    //Draws each trajectory
	    for(Trajectory traj : trajList)
	    {
	    	g.drawOval((int)(traj.getX()), (int)(traj.getY()), radius, radius);
	    }		
	}
	 
	//@Override
	public void paintComponent(Graphics g) {

	    super.paintComponent( g ); // call superclass's paintComponent  
	    
	    //Graphics2D g2 = ( Graphics2D ) g; // cast g to Graphics2D  

	}

}
