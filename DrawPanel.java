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
	private float diam;
	private int distBetweenTraj;
	private boolean showEdges = false;
	
	DrawPanel () {
	}
	
	public void createDrone(Graphics g) {
		JTextField aField = new JTextField(5);
		JTextField bField = new JTextField(5);

		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

		input.add(new JLabel("Enter Angle in Degrees:"));
		input.add(aField);

		input.add(Box.createVerticalStrut(15));

		input.add(new JLabel("On trajectory:"));
		input.add(bField);

		input.add(Box.createVerticalStrut(15));
		
		int result = JOptionPane.showConfirmDialog(null, input, "Enter Values For New Trajectory", JOptionPane.OK_CANCEL_OPTION);
		
		float ang = 0;
		int id = 0;
		
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			String temp2 = bField.getText();
			//Gets angle and id
			ang = Float.parseFloat(temp1);
			id = Integer.parseInt(temp2);
		}
		
		new Robot(Trajectory.trajectories.get(id-1), (float)(Math.toRadians(ang)));
		
		//Robot r = new Robot(trajList.get(id - 1), ang);
		//droneList.add(r);
		
	}
	
	public void autoFillDrones(Graphics g) {
		// TODO Create function that automatically fills trajectories, likely involves creation of master list of neighbor classes.
	}
	
	public void createTraj(Graphics g) {
		//Initial trajectory coordinates
		//int x = getWidth()/2 + 200;
		//int y = getHeight()/2;
		
		//Hardcoded radius and distBetweenTraj values
		//if radius isn't already set - keeps a consistent radius
		/*if(diam == 0){
			diam = 100;
		}
		distBetweenTraj = 50;*/
		
		//Adds first trajectory to list once
		if(Trajectory.trajectories.size() == 0)
		{
			new Trajectory(0,0);
			//trajList.add(new Trajectory(0, 0));
			//tempTrajList.add(new Trajectory(0, 0));
		}
		else{
			//Input Box
			JTextField aField = new JTextField(5);
			JTextField bField = new JTextField(5);
	
			JPanel input = new JPanel();
			input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
	
			input.add(new JLabel("Enter Angle in Degrees:"));
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
			float distX = (float)(Math.cos(Math.toRadians(ang)) * (Constants.trajRadius*2 + 30));
			float distY = (float)(Math.sin(Math.toRadians(ang)) * (Constants.trajRadius*2 + 30));
			float newX = Trajectory.trajectories.get(id-1).getX() + distX;
			float newY = Trajectory.trajectories.get(id-1).getY() + distY;
			new Trajectory(newX, newY);
			//trajList.add(new Trajectory(distX, distY));
			//tempTrajList.add(new Trajectory(distX, distY));
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
		/*
		//Sets initial radius
		diam = 10;
		//Gets largest size the radius should be
		if(rows > cols)
		{
			diam = (getWidth() - 650)/rows;
		}
		else
			diam = (getHeight() - 420)/cols;
		
	    int distBetweenTraj = getWidth()/20;
	    
	    Trajectory t;
	    //Assures space for menu
	    int distanceX = 50;
	    int distanceY = 100;
	    */
	    //Makes each trajectory
		float anchorX = -(cols-1)*(Constants.trajRadius*2 + 20)/2;
		float anchorY = -(rows-1)*(Constants.trajRadius*2 + 20)/2;
		
		
	    for(int r = 0; r < rows; r++)
	    {
	    	for(int c = 0; c < cols; c++)
	    	{
	    		//+ (distBetweenTraj * r)  + (distBetweenTraj * c)
	    		new Trajectory(anchorX + c*(Constants.trajRadius*2 + 20), anchorY + r*(Constants.trajRadius*2 + 20));
	    		//trajList.add(new Trajectory(distanceX + (diam * 2 * r) - (distBetweenTraj * r), distanceY + (diam * 2 * c) - (distBetweenTraj * c)));
	    		//tempTrajList.add(new Trajectory(distanceX + (diam * 2 * r) - (distBetweenTraj * r), distanceY + (diam * 2 * c) - (distBetweenTraj * c)));
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
	
	public void showEdges() {
		showEdges = true;		
	}
	
	public void removeEdges() {
		showEdges = false;
	}
	 
	@Override
	public void paint(Graphics g) {
		super.paint( g );
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent( g ); // call superclass's paintComponent
	    //Graphics2D g2 = ( Graphics2D ) g; // cast g to Graphics2D  
	    g.drawString("Drone Simulator", 50, 50);
	    
	    for(Robot r: Robot.robots) {
	    		r.move();
	    		repaint();
	    }
	    
	    for(Robot r: Robot.robots) {
	    		r.logic();
	    }
	    
	    float pixelRatio;	// Creating the pixel ratio, which is the number of pixels divided by the number of units for the window size
	    pixelRatio = (float)(Math.min(getHeight(), getWidth())/800.0);
	    
	    for(Trajectory n : Trajectory.trajectories) {
	    		if (n.getDir() == 1) {
	    			g.setColor(Color.RED);
	    		} else {
	    			g.setColor(Color.BLUE);
	    		}
	    		g.drawOval((int)(n.getX()*pixelRatio + getWidth()/2 - Constants.trajRadius*pixelRatio), (int)(getHeight()/2 - n.getY()*pixelRatio - Constants.trajRadius*pixelRatio), (int)(Constants.trajRadius*2.0*pixelRatio), (int)(Constants.trajRadius*2.0*pixelRatio));
	    }
	    
	    for(Robot r: Robot.robots) {
	    		g.setColor(Color.BLACK);
	    		g.fillOval((int)(r.getX()*pixelRatio + getWidth()/2)-10, (int)((getHeight()/2 - r.getY()*pixelRatio))-10, 20, 20);
	    }
	    
	    // show/hide edges
	    if (showEdges) {
	    	g.setColor(Color.BLACK);
	    	for(Trajectory trajectory : Trajectory.trajectories) {
	    		for(Neighbor neighbor : trajectory.neighbors) {
	    			//System.out.println("TRAJ_A: " + neighbor.traj_a.getID() + " | TRAJ_B: " + neighbor.traj_b.getID());
	    			g.drawLine((int)(neighbor.traj_a.getX()*pixelRatio + getWidth()/2), (int)(getHeight()/2 - neighbor.traj_a.getY()*pixelRatio), (int)(neighbor.traj_b.getX()*pixelRatio + getWidth()/2), (int)(getHeight()/2 - neighbor.traj_b.getY()*pixelRatio));
	    		}
			}
	    }
	    
	    /*float originalRadius = diam;
	    
	    diam = diam*Math.min(getHeight(), getWidth())/700;
	    
	    for(Trajectory n : tempTrajList)
	    {
	    	n.setX(n.getX() - getWidth()/200); //getWidth() / 4 + n.getX()
	    	n.setY(n.getY() - getHeight()/200);
	    }
	    
	    //Draws each trajectory
	    for(Trajectory n : tempTrajList)
	    {
	    	g.drawOval((int)(n.getX()), (int)(n.getY()), (int)diam, (int)diam);
	    	g.drawString("" + n.getID(), (int)(n.getX() + diam/2), (int)(n.getY() + diam/2));
	    }
	    
	    //Draws each drone
	    for(Robot r : droneList)
	    {
	    	g.setColor(Color.BLACK);
	    	//g.drawOval(getWidth() / 2, getHeight() / 2, 100, 100);
	    	g.fillOval((int)(r.getX()), (int)(r.getTrajectory().getY() - diam/2*Math.sin(Math.toRadians(r.getAngle())) + diam/2 - 15), 30, 30);
	    }
	    
	    //sample
	    //g.drawOval(getWidth() / 2, getHeight() / 2, (int)radius, (int)radius);
	    
	    diam = originalRadius;
	    
	    for(int i = 0; i < tempTrajList.size(); i++)
	    {
	    	tempTrajList.get(i).setX(trajList.get(i).getX());
	    	tempTrajList.get(i).setY(trajList.get(i).getY());
	    }*/
	    
	}
	
	public void clear(){
		Robot.robots.clear();
		Trajectory.trajectories.clear();
		diam = 0;
		repaint();
	}
}
