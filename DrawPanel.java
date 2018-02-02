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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
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
	
	public void createRobot(Graphics g) {
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
	
	public void createTraj(Graphics g) {
		
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
		
	    //Makes each trajectory
		float anchorX = -(cols-1)*(Constants.trajRadius*2 + Constants.trajPadding)/2;
		float anchorY = -(rows-1)*(Constants.trajRadius*2 + Constants.trajPadding)/2;
		
	    for(int r = 0; r < rows; r++)
	    {
	    	for(int c = 0; c < cols; c++)
	    	{
	    		new Trajectory(anchorX + c*(Constants.trajRadius*2 + Constants.trajPadding), anchorY + r*(Constants.trajRadius*2 + Constants.trajPadding));
	    	}
	    }
	}
	
	public void saveGrid(){
		JFrame parentFrame = new JFrame();
		 
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");   
		 
		int userSelection = fileChooser.showSaveDialog(parentFrame);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			try{
				File file = fileChooser.getSelectedFile();
				String filename = file.getAbsolutePath() + ".txt";
				FileWriter fw = new FileWriter(filename, true);
				BufferedWriter bw = new BufferedWriter(fw);
				
				for(int i = 0; i < Trajectory.trajectories.size(); i++)
				{
					Trajectory traj = Trajectory.trajectories.get(i);
					bw.write(String.valueOf(traj.getX()));
					bw.newLine();
					bw.write(String.valueOf(traj.getY()));
					bw.newLine();
				}
				//bw.newLine();
				bw.write("R");
				bw.newLine();
				for(int j = 0; j < Robot.robots.size(); j++)
				{
					Robot r = Robot.robots.get(j);
					bw.write(String.valueOf(r.getTrajectory().getID()));
					bw.newLine();
					bw.write(String.valueOf(r.getAngle()));
					bw.newLine();
				}
				
				bw.close();
				System.out.println(file + ".txt");
			}
			catch(IOException ioe)
			{
				System.err.println(":(");
			}
		}
	}
	
	public void loadGrid(){
		JFrame parentFrame = new JFrame();
		JFileChooser fileChooser = new JFileChooser();
		boolean trajlist = true;
		int returnVal = fileChooser.showOpenDialog(parentFrame);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			try {
				Scanner scan = new Scanner(file);
				boolean robots = false;
				while(scan.hasNext())
				{
					String first = scan.next();
					if(first.equals("R"))
					{
						robots = true;
						first = scan.next();
					}
					if(robots == false)
					{
						float xCoord = Float.parseFloat(first);
						float yCoord = Float.parseFloat(scan.next());
			    		new Trajectory(xCoord, yCoord);
					}
					else if(robots == true)
					{
						int trajID = Integer.parseInt(first);
						Trajectory t = Trajectory.trajectories.get(trajID - 1);
						float ang = Float.parseFloat(scan.next());
			    		new Robot(t, ang);
					}
				}
			}catch (FileNotFoundException e) {
				System.out.println("File not found.");
			} catch(NumberFormatException e){
				
			}catch(NoSuchElementException e){
				
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
	
		
	public void clear(){
		Robot.robots.clear();
		robotShapes.clear();
		Trajectory.trajectories.clear();
		diam = 0;
		repaint();
	}
	
	public void addData(){
		JTextField aField = new JTextField(5);
		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

		input.add(new JLabel("Enter robot ID to add data to"));
		input.add(aField);
		
		int result = JOptionPane.showConfirmDialog(null, input, "Add Data", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			for(Robot r:Robot.robots){
				if(r.getID() == Integer.parseInt(temp1)){
					r.setData(true);
				}
			}
		}
	}
	
	public void removeData(){
		JTextField aField = new JTextField(5);
		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

		input.add(new JLabel("Enter robot ID to remove data from"));
		input.add(aField);
		
		int result = JOptionPane.showConfirmDialog(null, input, "Remove Data", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			for(Robot r:Robot.robots){
				if(r.getID() == Integer.parseInt(temp1)){
					r.setData(false);
				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		robotShapes.clear();
	    super.paintComponent( g ); // call superclass's paintComponent
	    Graphics2D g2 = ( Graphics2D ) g; // cast g to Graphics2D  
	    g.drawString("Drone Simulator", 50, 50);
	    //g2.scale(Constants.scale, Constants.scale);
	    //g2.translate(Constants.translation, .5*Constants.translation);
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
	    		if(!r.hasData()){
	    			g.setColor(Color.BLACK);
	    		}
	    		else{
	    			g.setColor(Color.BLUE);
	    		}
	    		g.fillOval((int)(r.getX()*pixelRatio + getWidth()/2-Constants.scale*Constants.trajPadding/2), (int)(getHeight()/2 - r.getY()*pixelRatio-Constants.scale*Constants.trajPadding/2), (int)(Constants.scale*Constants.trajPadding), (int)(Constants.scale*Constants.trajPadding));
	    		robotShapes.add(new Ellipse2D.Double((int)(r.getX()*pixelRatio + getWidth()/2-Constants.scale*Constants.trajPadding/2), (int)(getHeight()/2 - r.getY()*pixelRatio-Constants.scale*Constants.trajPadding/2), (int)(Constants.scale*Constants.trajPadding), (int)(Constants.scale*Constants.trajPadding)));
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

}
