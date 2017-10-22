import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DrawPanel extends JPanel {
	private List<Trajectory> trajList = new ArrayList();
	private List<Trajectory> tempTrajList = new ArrayList();
	private List<Robot> droneList = new ArrayList();
	private Graphics g;
	private float radius;
	private int distBetweenTraj;
	
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
		
		//Hardcoded radius and distBetweenTraj values
		//if radius isn't already set - keeps a consistent radius
		if(radius == 0){
			radius = 100;
		}
		distBetweenTraj = 20;
		
		//Adds first trajectory to list once
		if(trajList.size() == 0)
		{
			Trajectory t = new Trajectory(x, y, 1);
			trajList.add(t);
		}
		else{
			//Input Box
			JTextField aField = new JTextField(5);
			JTextField bField = new JTextField(5);
	
			JPanel input = new JPanel();
			input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
	
			input.add(new JLabel("Enter Angle:"));
			input.add(aField);
	
			input.add(Box.createVerticalStrut(15));
	
			input.add(new JLabel("Relative to trajectory with ID:"));
			input.add(bField);
	
			input.add(Box.createVerticalStrut(15));
			
			int result = JOptionPane.showConfirmDialog(null, input, "Enter Values For New Trajectory", JOptionPane.OK_CANCEL_OPTION);
			
			double ang = 0;
			int id = 0;
			
			if (result == JOptionPane.OK_OPTION) {
				String temp1 = aField.getText();
				String temp2 = bField.getText();
				//Gets angle and id(for traj to add on to)
				ang = Double.parseDouble(temp1);
				id = Integer.parseInt(temp2);
			}
			
			//Creates next trajectory
			float distX = (float)(trajList.get(id - 1).getX() + Math.cos(Math.toRadians(ang)) * (radius + distBetweenTraj));
			float distY = (float)(trajList.get(id - 1).getY() - Math.sin(Math.toRadians(ang)) * (radius + distBetweenTraj));
			Trajectory traj = new Trajectory(distX, distY, trajList.size() + 1);
			trajList.add(traj);
		}			
	}
	
	public void createGrid(Graphics g) {
		//Input Box
		JTextField aField = new JTextField(5);
		JTextField bField = new JTextField(5);

		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

		input.add(new JLabel("Enter number of rows:"));
		input.add(aField);

		input.add(Box.createVerticalStrut(15));

		input.add(new JLabel("Enter number of columns:"));
		input.add(bField);

		input.add(Box.createVerticalStrut(15));
		
		int result = JOptionPane.showConfirmDialog(null, input, "Enter Values For Grid", JOptionPane.OK_CANCEL_OPTION);
		
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
		radius = 10;
		//Gets largest size the radius should be
		if(rows > cols)
		{
			radius = (getWidth() - 600)/rows;
		}
		else
			radius = (getHeight() - 400)/cols;
		
	    int distBetweenTraj = getWidth()/200;
	    
	    Trajectory t;
	    //Assures space for menu
	    int distanceX = 100;
	    int distanceY = 100;
	    
	    //Makes each trajectory
	    for(int r = 0; r < rows; r++)
	    {
	    	for(int c = 0; c < cols; c++)
	    	{
	    		t = new Trajectory(distanceX + (radius * 2 * r) + (distBetweenTraj * r), distanceY + (radius * 2 * c) + (distBetweenTraj * c), trajList.size() + 1);
	    		trajList.add(t);
	    		tempTrajList.add(t);
	    	}
	    }
	}
	
	public void removeTraj(){
		//Input Box
		JTextField aField = new JTextField(5);
		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

		input.add(new JLabel("Enter trajectory ID for removal"));
		input.add(aField);

		int result = JOptionPane.showConfirmDialog(null, input, "Remove Trajectory", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			trajList.remove(Integer.parseInt(temp1) - 1);
		}
		
	}
	 
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent( g ); // call superclass's paintComponent
	    //Graphics2D g2 = ( Graphics2D ) g; // cast g to Graphics2D  
	    g.drawString("Drone Simulator", 50, 50);
	    
	    float originalRadius = radius;
	    
	    radius = radius*Math.min(getHeight(), getWidth())/700;
	    
	    for(Trajectory n : tempTrajList)
	    {
	    	n.setX(n.getX() - 500);
	    	n.setY(n.getY() - 500);
	    }
	    
	    //Draws each trajectory
	    for(Trajectory n : tempTrajList)
	    {
	    	g.drawOval((int)(n.getX()), (int)(n.getY()), (int)radius, (int)radius);
	    	g.drawString("" + n.getID(), (int)(n.getX() + radius/2), (int)(n.getY() + radius/2));
	    }
	    
	    radius = originalRadius;
	    
	    for(int i = 0; i < tempTrajList.size(); i++)
	    {
	    	tempTrajList.get(i).setX(trajList.get(i).getX());
	    	tempTrajList.get(i).setY(trajList.get(i).getY());
	    }
	}
	
	public void clear(){
		trajList.clear();
		radius = 0;
		repaint();
	}
}
