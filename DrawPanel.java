import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class DrawPanel extends JPanel {
	private List<Trajectory> trajList = new ArrayList();
	private List<Trajectory> tempTrajList = new ArrayList();
	private List<Robot> droneList = new ArrayList();
	private Graphics g;
	private float diam;
	private int distBetweenTraj;
	private boolean showEdges = false;
	private ArrayList<Shape> robotShapes = new ArrayList<>();
	
	DrawPanel () {
		// Adding timer which controls the timing and movement of things within the program
		Timer timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Constants.running) {
					for(Robot r: Robot.robots) {
			    		r.move();
				    }
				    
				    for(Robot r: Robot.robots) {
			    		r.logic();
				    }
				}
				repaint();
			}
		});
		timer.start();
		
		//click to remove drones
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
            	//System.out.println("Click!");
                super.mouseClicked(me);
                
                //TODO work with scaling
                /*Point p = new Point((int)(me.getX()*Constants.scale), (int)(me.getY()*Constants.scale));
                p.translate((int)Constants.translation, (int)Constants.translation);
                */
                
                for (int i = 0; i < robotShapes.size(); i++) {
                	Shape r = robotShapes.get(i);
                    if (r.contains(me.getPoint())) {//check if mouse is clicked within shape
                        Robot.robots.remove(i); //not 100% on why this works -- might have some issues with IDs in the future
                    }
                }
            }
        });
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
	/*public void autoFillDrones(Graphics g) {
		JTextField aField = new JTextField(5);
		//JTextField bField = new JTextField(5);
	
		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
	
		input.add(new JLabel("Enter Angle in Degrees:"));
		input.add(aField);
	
		input.add(Box.createVerticalStrut(15));
	
		//input.add(new JLabel("On trajectory:"));
		//input.add(bField);
	
		//input.add(Box.createVerticalStrut(15));
		
		int result = JOptionPane.showConfirmDialog(null, input, "Enter Values", JOptionPane.OK_CANCEL_OPTION);
		
		float ang = 0;
		//int id = 0;
		
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
		//	String temp2 = bField.getText();
			//Gets angle and id
			ang = Float.parseFloat(temp1);
		//	id = Integer.parseInt(temp2);
		}*/
		
		//new Robot(Trajectory.trajectories.get(id-1), (float)(Math.toRadians(ang)));	
		/*for(int i = 0; i < Trajectory.trajectories.size(); i++)
		{
			if(Trajectory.trajectories.get(i).getDir() == 1)
				new Robot(Trajectory.trajectories.get(i), (float)(Math.toRadians(ang)));	
			else
				new Robot(Trajectory.trajectories.get(i), (float)(Math.toRadians(360 - ang)));	
		}*/
		/*new Robot(Trajectory.trajectories.get(0), (float)(Math.toRadians(ang)));
		//Loop through each traj, get each neighbor, if has drone, move to next neighbor
		Trajectory a = Trajectory.trajectories.get(0);
		boolean hasBot = false;
		for(int t = 0; t < Trajectory.trajectories.size(); t++)
		{
			for(int n = 0; n < Trajectory.trajectories.get(a.getID()).neighbors.size(); n++)
			{
				float newAng = 0;
				for(int r = 0; r < Robot.robots.size(); r++)
					if(Robot.robots.get(r).getTrajectory().getID() == a.neighbors.get(n).getSecondTrajId())
					{
						hasBot = true;
						//System.out.println("has bot");
					}
				if(hasBot == false)
				{
					System.out.println(ang);
					System.out.println(360 / Math.toDegrees(a.neighbors.get(n).angle_a));
					if(Math.toDegrees(a.neighbors.get(n).angle_a) == 0)
					{
						newAng = ang*2;
					}
					else
					{
						newAng = (360 / (float)Math.toDegrees(a.neighbors.get(n).angle_a)) * ang;
						newAng += ang;
					}
					new Robot(a.neighbors.get(n).traj_b, (float)(Math.toRadians(newAng)));
					a = a.neighbors.get(n).traj_b;
				}
				hasBot = false;
			}
		}

	}*/
	
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
			float distX = (float)(Math.cos(Math.toRadians(ang)) * (Constants.trajRadius*2 + 20));
			float distY = (float)(Math.sin(Math.toRadians(ang)) * (Constants.trajRadius*2 + 20));
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
			int trajIndex = Integer.parseInt(temp1) - 1;
			Trajectory selectedTraj = Trajectory.trajectories.get(trajIndex);
			// get neighbors of trajectory that needs to removed
			for (Neighbor n : selectedTraj.neighbors) {
				System.out.println("Traj A: " + n.traj_a.getID()  + " | Traj B: " + n.traj_b.getID());
				// remove the trajectory that wants to be removed from it's neighbors' neighbor arraylist
				int trajbIndex = Trajectory.trajectories.indexOf(n.traj_b);
				for (Neighbor neigh : Trajectory.trajectories.get(trajbIndex).neighbors) {
					if (neigh.traj_b.equals(selectedTraj)) {
						Trajectory.trajectories.get(trajbIndex).neighbors.remove(neigh);
						break;
					}
				}
			}
			Trajectory.trajectories.remove(Integer.parseInt(temp1) - 1);
		}
		
	}
	
	public void showEdges() {
		showEdges = true;		
	}
	
	public void removeEdges() {
		showEdges = false;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		robotShapes.clear();
	    super.paintComponent( g ); // call superclass's paintComponent
	    Graphics2D g2 = ( Graphics2D ) g; // cast g to Graphics2D  
	    g.drawString("Drone Simulator", 50, 50);
	    g2.scale(Constants.scale, Constants.scale);
	    g2.translate(Constants.translation, .5*Constants.translation);
	    float pixelRatio;	// Creating the pixel ratio, which is the number of pixels divided by the number of units for the window size
	    pixelRatio = (float)(Math.min(getHeight(), getWidth())/800.0);
	    
	    for(Trajectory n : Trajectory.trajectories) {
	    		if (n.getDir() == 1) {
	    			g.setColor(Color.RED);
	    		} else {
	    			g.setColor(Color.BLUE);
	    		}
	    		g.drawOval((int)(n.getX()*pixelRatio + getWidth()/2 - Constants.trajRadius*pixelRatio), (int)(getHeight()/2 - n.getY()*pixelRatio - Constants.trajRadius*pixelRatio), (int)(Constants.trajRadius*2.0*pixelRatio), (int)(Constants.trajRadius*2.0*pixelRatio));
	    		g.drawString("" + (Trajectory.trajectories.indexOf(n)+1), (int)(n.getX()*pixelRatio + getWidth()/2), (int)(getHeight()/2 - n.getY()*pixelRatio));
	    }
	    
	    for(Robot r: Robot.robots) {
	    		g.setColor(Color.BLACK);
	    		g.fillOval((int)(r.getX()*pixelRatio + getWidth()/2)-10, (int)((getHeight()/2 - r.getY()*pixelRatio))-10, 20, 20);
	    		robotShapes.add(new Ellipse2D.Double((int)(r.getX()*pixelRatio + getWidth()/2)-10, (int)((getHeight()/2 - r.getY()*pixelRatio))-10, 20, 20));
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
	    
	}
	
	public void clear(){
		Robot.robots.clear();
		robotShapes.clear();
		Trajectory.trajectories.clear();
		diam = 0;
		repaint();
	}
	
}
