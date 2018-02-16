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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

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
	//private List<Trajectory> trajList = new ArrayList();
	//private List<Trajectory> tempTrajList = new ArrayList();
	//private List<Robot> droneList = new ArrayList();
	//private Graphics g;
	//private float diam;
	//private int distBetweenTraj;
	private boolean showEdges = false;
	private ArrayList<Shape> robotShapes = new ArrayList<>();
	
	public static ArrayList<Data> generatedData = new ArrayList<Data>();					//to keep all the generated data
	public static ArrayList<Data> fetchedData = new ArrayList<Data>();						//to keep the fetched data
	public static ArrayList<Data> deliveredData = new ArrayList<Data>();					//to keep the delivered data
	
	public static ArrayList<Trajectory> trajectoryList = new ArrayList<Trajectory>();		//to keep the trajectories list
	public static ArrayList<Robot> robotList = new ArrayList<Robot>();						//to keep the robots list
	
	
	DrawPanel () {
		// Adding timer which controls the timing and movement of things within the program
		Timer timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Constants.running) {
					for(Robot r: robotList) {
			    		r.move();
			    		r.fetchData();
			    		deliveredData.addAll(r.deliveryData());
				    }
					
					// if the current robot is within the detection range of the 4 compass points
					if(Math.abs(robotList.get(0).getAngle()-0) < 0.01 || Math.abs(robotList.get(0).getAngle()-(Math.PI/2)) < 0.01 || Math.abs(robotList.get(0).getAngle() - Math.PI) < 0.01 || Math.abs(robotList.get(0).getAngle() - (3*Math.PI/2)) < 0.01 || Math.abs(robotList.get(0).getAngle() - (2*Math.PI)) < 0.01) {
						//System.out.println("Robot switched from trajectory: " + checking.traj_a.getID() + " to trajectory: " + t.getID());
						System.out.println("Occupied Trajectory ID's & Angles ");
						Map<Integer, Integer> occupiedTrajs = new TreeMap<Integer, Integer>(); // <trajID, linkePointNum> corresponding to angle
						for (int i=0; i<DrawPanel.robotList.size(); i++) {
							int linkPointNum = -1;
							float droneAngle = DrawPanel.robotList.get(i).getAngle();
							if (Math.abs(droneAngle-0) < 0.01 || Math.abs(droneAngle - (2*Math.PI)) < 0.01) {
								linkPointNum = 1; // angle = 0
							}
							else if (Math.abs(droneAngle-(Math.PI/2)) < 0.01) {
								linkPointNum = 2; // angle = PI/2
							}
							else if (Math.abs(droneAngle-Math.PI) < 0.01) {
								linkPointNum = 3; // angle = PI
							}
							else if (Math.abs(droneAngle-(3*Math.PI/2)) < 0.01) {
								linkPointNum = 4; // angle = 3PI/2
							}
							occupiedTrajs.put(DrawPanel.robotList.get(i).getTrajectory().getID()-1, linkPointNum);
						}
						Set set = occupiedTrajs.entrySet();
						Iterator iterator = set.iterator();
						int snapshot = 0;
						while(iterator.hasNext()) {
							Map.Entry me = (Map.Entry)iterator.next();
							System.out.print(me.getKey() + ": ");
							System.out.println(me.getValue());
							snapshot += Math.pow(10, Double.parseDouble(me.getKey()+"")) * Double.parseDouble(me.getValue()+""); // the place value = trajectory id & the digit = angle on the trajectory 
						}
						System.out.println("Snapshot: " + snapshot); // integer representing the drones in trajectories and the angles in the trajectories
						Experiments.addToLog(snapshot);
						Experiments.printLog();
						System.out.println();
					}
				    
					int tempTotalDeliveredData = deliveredData.size(); 			//to check if any new data is delivered or not
				    for(Robot r: robotList) {
			    		r.logic();
				    }   
				    
				    
				    if(deliveredData.size() != tempTotalDeliveredData){
				    	System.out.println("Total Generated Data: "+generatedData.size());
				    	System.out.println("Total Delivered Data: "+deliveredData.size());
				    	System.out.println("Delivery Ratio: "+(double)deliveredData.size()/(double)generatedData.size());
				    	long fetchingDelay = 0;
				    	for(Data data:fetchedData){
				    		fetchingDelay += data.getFetchingTime() - data.getCreationTime();
				    	}
				    	System.out.println("Average Fetching Delay: "+ ((double)fetchingDelay/fetchedData.size()));
				    	long deliveryDelay = 0;
				    	for(Data data:deliveredData){
				    		deliveryDelay += data.getDeliveryTime() - data.getFetchingTime();
				    	}
				    	System.out.println("Average Delivery Delay: "+ ((double)deliveryDelay/deliveredData.size()));
				    	long totalDelay = 0;
				    	for(Data data:deliveredData){
				    		totalDelay += data.getDeliveryTime() - data.getCreationTime();
				    	}
				    	System.out.println("Average Total Delay: "+ ((double)totalDelay/deliveredData.size()));
				    }
				    
				    //for randomly creation of data with probability of 1%
				    Random rand = new Random();
					if(rand.nextDouble() > 0.99){
						int dataSource = rand.nextInt(trajectoryList.size());
						Data newData = new Data(trajectoryList.get(dataSource),trajectoryList.get(trajectoryList.size()-1),System.currentTimeMillis()); 						//data from a random traj destined for the last traj
						//Data newData = new Data(trajectoryList.get(dataSource),trajectoryList.get(rand.nextInt(trajectoryList.size())),System.currentTimeMillis())			//data from a random traj destined for a random traj
						trajectoryList.get(dataSource).addData(newData);
						generatedData.add(newData);
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
                        robotList.remove(i); //not 100% on why this works -- might have some issues with IDs in the future
                        Experiments.clear();
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
		
		new Robot(trajectoryList.get(id-1), (float)(Math.toRadians(ang)));
		
		//Robot r = new Robot(trajList.get(id - 1), ang);
		//droneList.add(r);
		
	}
	
	public void createTraj(Graphics g) {
		
		//Adds first trajectory to list once
		if(trajectoryList.size() == 0)
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
			float newX = trajectoryList.get(id-1).getX() + distX;
			float newY = trajectoryList.get(id-1).getY() + distY;
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
		float anchorX = -(cols-1)*(Constants.trajRadius*2 + 20)/2;
		float anchorY = -(rows-1)*(Constants.trajRadius*2 + 20)/2;
		
		
	    for(int r = 0; r < rows; r++)
	    {
	    	for(int c = 0; c < cols; c++)
	    	{
	    		new Trajectory(anchorX + c*(Constants.trajRadius*2 + Constants.trajPadding), anchorY + r*(Constants.trajRadius*2 + Constants.trajPadding));
	    	}
	    }
	    
	    Experiments.clear();
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
				
				for(int i = 0; i < trajectoryList.size(); i++)
				{
					Trajectory traj = trajectoryList.get(i);
					bw.write(String.valueOf(traj.getX()));
					bw.newLine();
					bw.write(String.valueOf(traj.getY()));
					bw.newLine();
				}
				//bw.newLine();
				bw.write("R");
				bw.newLine();
				for(int j = 0; j < robotList.size(); j++)
				{
					Robot r = robotList.get(j);
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
						Trajectory t = trajectoryList.get(trajID - 1);
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
			Trajectory selectedTraj = trajectoryList.get(trajIndex);
			// get neighbors of trajectory that needs to removed
			for (Neighbor n : selectedTraj.neighbors) {
				System.out.println("Traj A: " + n.traj_a.getID()  + " | Traj B: " + n.traj_b.getID());
				// remove the trajectory that wants to be removed from it's neighbors' neighbor arraylist
				int trajbIndex = trajectoryList.indexOf(n.traj_b);
				for (Neighbor neigh : trajectoryList.get(trajbIndex).neighbors) {
					if (neigh.traj_b.equals(selectedTraj)) {
						trajectoryList.get(trajbIndex).neighbors.remove(neigh);
						break;
					}
				}
			}
			trajectoryList.remove(Integer.parseInt(temp1) - 1);
		}
		
	}
	
	public void showEdges() {
		showEdges = true;		
	}
	
	public void removeEdges() {
		showEdges = false;
	}
	
		
	public void clear(){
		robotList.clear();
		robotShapes.clear();
		trajectoryList.clear();
		Experiments.clear();
		//diam = 0;
		repaint();
	}

	//for adding data manually
	public void addData(){
		JTextField aField = new JTextField(5);
		JTextField bField = new JTextField(5);
		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

		input.add(new JLabel("Enter the source trajectory ID"));
		input.add(aField);
		input.add(new JLabel("Enter the destination trajectory ID"));
		input.add(bField);
		
		int result = JOptionPane.showConfirmDialog(null, input, "Add Data", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			String temp2 = bField.getText();
			Trajectory source = null;
			Trajectory destination = null;
			for(Trajectory t:trajectoryList){
				if(t.getID() == Integer.parseInt(temp1)){
					source = t;
				}	
				if(t.getID() == Integer.parseInt(temp2)){
					destination = t;
				}
			}
			if(source != null && destination != null){
				Data newData = new Data(source,destination,System.currentTimeMillis()); 						////data from the source traj to the destination traj
				source.addData(newData);
				generatedData.add(newData);
			} else {
				JPanel error = new JPanel();
				input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
				if(source == null){
					error.add(new JLabel("Wrong source"));
				} else {
					error.add(new JLabel("Wrong destination"));
				}
				JOptionPane.showConfirmDialog(null, error, "Add Data Error", JOptionPane.CLOSED_OPTION);
			}
		}
		
		
	}
	
	/*public void addData(){
		JTextField aField = new JTextField(5);
		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

		input.add(new JLabel("Enter robot ID to add data to"));
		input.add(aField);
		
		int result = JOptionPane.showConfirmDialog(null, input, "Add Data", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			for(Robot r:robotList){
				if(r.getID() == Integer.parseInt(temp1)){
					r.setData(true);
				}
			}
		}
	}*/
	
	/*public void removeData(){
		JTextField aField = new JTextField(5);
		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

		input.add(new JLabel("Enter robot ID to remove data from"));
		input.add(aField);
		
		int result = JOptionPane.showConfirmDialog(null, input, "Remove Data", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String temp1 = aField.getText();
			for(Robot r:robotList){
				if(r.getID() == Integer.parseInt(temp1)){
					r.setData(false);
				}
			}
		}
	}*/
	
	@Override
	public void paintComponent(Graphics g) {
		robotShapes.clear();
	    super.paintComponent( g ); // call superclass's paintComponent
	    Graphics2D g2 = ( Graphics2D ) g; // cast g to Graphics2D
	    
	    g.drawString("Drone Simulator", 50, 50);
	    g.drawString("Period: " + Experiments.period, 50, 60);
	    g.drawString("Snapshot: " + Experiments.snap, 50, 70);
	    g.drawString("Increments: " + Experiments.log.size(), 50, 80);

	    //g2.scale(Constants.scale, Constants.scale);
	    //g2.translate(Constants.translation, .5*Constants.translation);
	    float pixelRatio;	// Creating the pixel ratio, which is the number of pixels divided by the number of units for the window size
	    pixelRatio = (float)(Math.min(getHeight(), getWidth())/800.0);
	    
	    for(Trajectory n : trajectoryList) {
	    		if (n.getDir() == 1) {
	    			g.setColor(Color.RED);
	    		} else {
	    			g.setColor(Color.BLUE);
	    		}
	    		g.drawOval((int)(n.getX()*pixelRatio + getWidth()/2 - Constants.trajRadius*pixelRatio), (int)(getHeight()/2 - n.getY()*pixelRatio - Constants.trajRadius*pixelRatio), (int)(Constants.trajRadius*2.0*pixelRatio), (int)(Constants.trajRadius*2.0*pixelRatio));
	    		
	    		if (n.dataSize() > 1)
	    			g.setColor(Color.GREEN);
	    		g.drawString("" + (n.getID()), (int)(n.getX()*pixelRatio + getWidth()/2), (int)(getHeight()/2 - n.getY()*pixelRatio));
	    }
	    
	    for(Robot r: robotList) {
	    		if(r.getData().size()>0){
	    			g.setColor(Color.BLUE);
	    		}
	    		else{
	    			g.setColor(Color.BLACK);
	    		}
	    		g.fillOval((int)(r.getX()*pixelRatio + getWidth()/2-Constants.scale*Constants.trajPadding/2), (int)(getHeight()/2 - r.getY()*pixelRatio-Constants.scale*Constants.trajPadding/2), (int)(Constants.scale*Constants.trajPadding), (int)(Constants.scale*Constants.trajPadding));
	    		robotShapes.add(new Ellipse2D.Double((int)(r.getX()*pixelRatio + getWidth()/2-Constants.scale*Constants.trajPadding/2), (int)(getHeight()/2 - r.getY()*pixelRatio-Constants.scale*Constants.trajPadding/2), (int)(Constants.scale*Constants.trajPadding), (int)(Constants.scale*Constants.trajPadding)));
	    }
	    
	    // show/hide edges
	    if (showEdges) {
	    	g.setColor(Color.BLACK);
	    	for(Trajectory trajectory : trajectoryList) {
	    		for(Neighbor neighbor : trajectory.neighbors) {
	    			//System.out.println("TRAJ_A: " + neighbor.traj_a.getID() + " | TRAJ_B: " + neighbor.traj_b.getID());
	    			g.drawLine((int)(neighbor.traj_a.getX()*pixelRatio + getWidth()/2), (int)(getHeight()/2 - neighbor.traj_a.getY()*pixelRatio), (int)(neighbor.traj_b.getX()*pixelRatio + getWidth()/2), (int)(getHeight()/2 - neighbor.traj_b.getY()*pixelRatio));
	    		}
			}
	    }
	    
	}

}