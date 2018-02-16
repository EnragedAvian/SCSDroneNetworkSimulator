//  Robot class which handles the creation and motion of robots on screen.

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Math;

public class Robot {
	private Trajectory t;
	private float angle;
	private float radius;
	private int ID;
	private Neighbor checking;	// ID of the trajectory we want to be checking
	private boolean hasData = false;
		
	/*private Thread thread;
	private String threadName;*/
	
	boolean transitioningIn;
	boolean transitioningOut;
	boolean detected;  // checks to see whether or not robot has been detected in desired range.
	boolean checked;   // boolean stating SOMETHING
	boolean swapped;
	int rangeState;  // Integer determining what range the robot is in, affecting the logic
  
	private ArrayList<Data> dataList;           //for keeping data
//	public static ArrayList<Robot> robots = new ArrayList<Robot>();
  
	Robot(Trajectory traj, float ang){
		dataList = new ArrayList<Data>();                      //for keeping data

		angle = ang;
		t = traj;
		// TODO add logic so that robot is given ID of trajectory if the program hasn't started, but is assigned the next value in the sequence if the program has already started
		ID = t.getID();  // Current logic won't work if robots are created while the simulation is running.
		transitioningIn = false;
		transitioningOut = false;
		radius = Constants.trajRadius;
		DrawPanel.robotList.add(this);
		/*threadName = "Robot " + ID;*/
		
		/*if (Constants.running) {
			this.start();
		}*/
		/*Timer timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Constants.running) {
					move();
					logic();
				}
			}
		});
		timer.start();*/
		
	}
  
	float getRadius(){
		return radius;
	}
	
	void setRadius(float rval){
		radius = rval;
	}
	
	boolean checkNeighbor(Trajectory traj) {  // Checks the range between robot and neighbor in specified trajectory
		// TODO Add logic keeping track of how many times neighbor has been passed
		//System.out.println("checkNeighbor function called");
		for (int i = 0; i<DrawPanel.robotList.size(); i++) {  // For every robot
			if ((DrawPanel.robotList.get(i).getID()!=this.getID())&&(DrawPanel.robotList.get(i).getTrajectory().getID() == traj.getID())) {  // If the robot is not the same as the other robot
				//System.out.println("Found another robot!");
				float xDist = Math.abs(this.getX() - DrawPanel.robotList.get(i).getX());
				float yDist = Math.abs(this.getY() - DrawPanel.robotList.get(i).getY());
				float dist = (float)Math.sqrt((double)(xDist*xDist + yDist*yDist));
				if (dist <= (Constants.wifiRange + 5)) {	// added small buffer to ensure that robots are detected properly if they are within range
					return true;
				}
			}
		}
		return false;
		//return true;
	}
  
	/*public void run () {
		System.out.println("Run function called.");
		while (Constants.running) {
			try {
				move();
				logic();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("Error on thread!");
			}
		}
	}*/
	
	void move() {
		if (t.getDir() == 1) {
			angle += Constants.robotSpeed;  // Incrementing robot angle by constant value per tick
			angle = Constants.normalizeAngle(angle);  // normalizing angle so robot always falls within 0-2pi
		} else {
			angle -= Constants.robotSpeed;
			angle = Constants.normalizeAngle(angle);
		}
	}
  
	void logic() {
		if (t.getDir() == 1) {  // Checking direction of robot on trajectory
    
			// Adding new definition of rangeState, which allows easy determination of what range the robot is in
			
			rangeState = -1;
			for (int i = 0; i < t.neighbors.size(); i++) {
				// Creating temporary values for all the parameters of the neighbor class to aid with calculations later.
				float tempDetectIn = t.neighbors.get(i).detectIn_a;
				float tempTranIn = t.neighbors.get(i).transitionIn_a;
				float tempAngleRef = t.neighbors.get(i).angle_a;
				float tempTranOut = t.neighbors.get(i).transitionOut_a;
        
				// Checking to see if the robot lies within a detectIn range
				if (((tempDetectIn > tempTranIn) && (angle > tempDetectIn || angle < tempTranIn)) || ((tempDetectIn < tempTranIn) && (angle > tempDetectIn && angle < tempTranIn))) {  // Testing to see if robot falls within the detect range
					rangeState = 1;
					checking = t.neighbors.get(i);
				} else if (((tempTranIn > tempTranOut) && (angle > tempTranIn || angle < tempTranOut)) || ((tempTranIn < tempTranOut) && (angle > tempTranIn && angle < tempTranOut))) {  // Testing to see if robot falls within transition range
					if (transitioningIn) {	// Going through cases to check to see whether or not the robot is transitioning in or out in the transition range as a whole
						rangeState = 2;
						checking = t.neighbors.get(i);
					} else if (transitioningOut) {
						rangeState = 3;
						checking = t.neighbors.get(i);
					}
				} else if (rangeState == -1) {
					rangeState = 0;
				}
			}
			
			switch (rangeState) {
			case 0:
				checked = false;
				transitioningIn = false;
				transitioningOut = false;
				radius = Constants.trajRadius;
				break;
			case 1:
				if(!checked) {

					int secondTrajID = checking.getSecondTraj().getID();
					Robot toSendRobot = null;
					for(Robot robots:DrawPanel.robotList){
						if(robots.getTrajectory().getID() == secondTrajID)
							toSendRobot = robots;
					}
					if(toSendRobot != null){
						sendData(toSendRobot);					
						DrawPanel.deliveredData.addAll(toSendRobot.deliveryData());
					}
					detected = checkNeighbor(checking.traj_b);
					if (!detected) {
						transitioningIn = true;
					}
					checked = true;
				}
				break;
			case 2:
				radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(angle-checking.transitionIn_a)));
				
				float tempAngleRef = checking.angle_a;
				float tempTranOut = checking.transitionOut_a;
				if (((tempAngleRef > tempTranOut) && (angle > tempAngleRef || angle < tempTranOut)) || ((tempAngleRef < tempTranOut) && (angle > tempAngleRef && angle < tempTranOut))) {	// Checking to see if the robot is within the transitionOut range
					transitioningIn = false;
					transitioningOut = true;
					angle = Constants.normalizeAngle(checking.angle_b-(Constants.normalizeAngle(angle-checking.angle_a)));
					//System.out.println("Old Trajectory is: " + t.getID());
					t = checking.traj_b;
					DrawPanel.deliveredData.addAll(this.deliveryData());
					//System.out.println("New Trajectory is: " + t.getID());
				}
				checked = false;
				break;
			
			case 3:
				radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(checking.transitionOut_a-angle)));
				checked = false;
				break;
			}
		}
    
		if (t.getDir() == -1) {  // Checking direction of robot on trajectory, moving clockwise
			rangeState = -1;
			for (int i = 0; i < t.neighbors.size(); i++) {
				// Creating temporary values for all the parameters of the neighbor class to aid with calculations later.
				float tempDetectIn = t.neighbors.get(i).detectIn_a;
				float tempTranIn = t.neighbors.get(i).transitionIn_a;
				float tempAngleRef = t.neighbors.get(i).angle_a;
				float tempTranOut = t.neighbors.get(i).transitionOut_a;
		        
		        // Checking to see if the robot lies within a detectIn range
		        if (((tempDetectIn < tempTranIn) && (angle < tempDetectIn || angle > tempTranIn)) || ((tempDetectIn > tempTranIn) && (angle < tempDetectIn && angle > tempTranIn))) {  // Testing to see if robot falls within the detect range
		        		rangeState = 1;
		        		checking = t.neighbors.get(i);
		        } else if (((tempTranIn < tempTranOut) && (angle < tempTranIn || angle > tempTranOut)) || ((tempTranIn > tempTranOut) && (angle < tempTranIn && angle > tempTranOut))) {  // Testing to see if robot falls within transition range
		        		if (transitioningIn) {  // Going through cases to check to see whether or not the robot is transitioning in or out in the transition range as a whole
		        			rangeState = 2;
		        			checking = t.neighbors.get(i);
		        		} else if (transitioningOut) {
		        			rangeState = 3;
		        			checking = t.neighbors.get(i);
		        		}
		        } else if (rangeState == -1) {
		        		rangeState = 0;
		        }
			}
		      
			switch (rangeState) {
			case 0:
				checked = false;
				transitioningIn = false;
				transitioningOut = false;
		        radius = Constants.trajRadius;
		        break;
			case 1:
				if(!checked) {
					int secondTrajID = checking.getSecondTraj().getID();
					Robot toSendRobot = null;
					for(Robot robots:DrawPanel.robotList){
						if(robots.getTrajectory().getID() == secondTrajID)
							toSendRobot = robots;
					}
					if(toSendRobot != null){
						sendData(toSendRobot);					
						DrawPanel.deliveredData.addAll(toSendRobot.deliveryData());
					}
					detected = checkNeighbor(checking.traj_b);
					if (!detected) {
						transitioningIn = true;
					}
					checked = true;
				}
				break;
			case 2:
				radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(angle-checking.transitionIn_a)));
		        
				float tempAngleRef = checking.angle_a;
				float tempTranOut = checking.transitionOut_a;
				if (((tempAngleRef < tempTranOut) && (angle < tempAngleRef || angle > tempTranOut)) || ((tempAngleRef > tempTranOut) && (angle < tempAngleRef && angle > tempTranOut))) {  // Checking to see if the robot is within the transitionOut range
					transitioningIn = false;
					transitioningOut = true;
					angle = Constants.normalizeAngle(checking.angle_b-(Constants.normalizeAngle(angle-checking.angle_a)));
					//System.out.println("Old Trajectory is: " + t.getID());
					t = checking.traj_b;
					DrawPanel.deliveredData.addAll(this.deliveryData());
					//System.out.println("New Trajectory is: " + t.getID());
				}
				checked = false;
				break;
			case 3:
				radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(checking.transitionOut_a-angle)));
				checked = false;
				break;
			}
		}
		
		//System.out.println("Logic function for robot on traj: " + t.getID() + " with angle: " + angle);
		
		
		
//		// if the current robot is within the detection range of the 4 compass points
//		if(Math.abs(angle-0) < 0.01 || Math.abs(angle-(Math.PI/2)) < 0.01 || Math.abs(angle - Math.PI) < 0.01 || Math.abs(angle - (3*Math.PI/2)) < 0.01 || Math.abs(angle - (2*Math.PI)) < 0.01) {
//			//System.out.println("Robot switched from trajectory: " + checking.traj_a.getID() + " to trajectory: " + t.getID());
//			System.out.println("Occupied Trajectory ID's & Angles ");
//			Map<Integer, Integer> occupiedTrajs = new TreeMap<Integer, Integer>(); // <trajID, linkePointNum> corresponding to angle
//			for (int i=0; i<DrawPanel.robotList.size(); i++) {
//				int linkPointNum = -1;
//				float droneAngle = DrawPanel.robotList.get(i).getAngle();
//				if (Math.abs(droneAngle-0) < 0.01 || Math.abs(droneAngle - (2*Math.PI)) < 0.01) {
//					linkPointNum = 1; // angle = 0
//				}
//				else if (Math.abs(droneAngle-(Math.PI/2)) < 0.01) {
//					linkPointNum = 2; // angle = PI/2
//				}
//				else if (Math.abs(droneAngle-Math.PI) < 0.01) {
//					linkPointNum = 3; // angle = PI
//				}
//				else if (Math.abs(droneAngle-(3*Math.PI/2)) < 0.01) {
//					linkPointNum = 4; // angle = 3PI/2
//				}
//				occupiedTrajs.put(DrawPanel.robotList.get(i).getTrajectory().getID()-1, linkPointNum);
//			}
//			Set set = occupiedTrajs.entrySet();
//			Iterator iterator = set.iterator();
//			int snapshot = 0;
//			while(iterator.hasNext()) {
//				Map.Entry me = (Map.Entry)iterator.next();
//				System.out.print(me.getKey() + ": ");
//				System.out.println(me.getValue());
//				snapshot += Math.pow(10, Double.parseDouble(me.getKey()+"")) * Double.parseDouble(me.getValue()+""); // the place value = trajectory id & the digit = angle on the trajectory 
//			}
//			System.out.println("Snapshot: " + snapshot); // integer representing the drones in trajectories and the angles in the trajectories
//			Experiments.addToLog(snapshot);
//			Experiments.printLog();
//			System.out.println();
//		}
	}
  
	int getID() {
		return ID;
	}
  
	Trajectory getTrajectory() {
		return t;
	}
  
	float getX() {
		float result;
		result = (float)(this.getTrajectory().getX() + radius * Math.cos((double)angle));
		return result;
	}
  
	float getY() {
		float result;
		result = (float)(this.getTrajectory().getY() + radius * Math.sin((double)angle));
		return result;
	}
	
	float getAngle() {
		return angle;
	}
  
	/*void setData(boolean b){
		hasData = b;
	}*/
	
	/*boolean hasData(){
		return hasData;
	}*/
	
	//fetching data from trajectory
	void fetchData(){
		if(this.getTrajectory().dataSize() > 0){
			ArrayList<Data> dataFromTraj = this.getTrajectory().sendData();
			for(Data data:dataFromTraj){
				data.setFetchingTime(System.currentTimeMillis());
				data.setFetchedStatus(true);
			}
			dataList.addAll(dataFromTraj);
			DrawPanel.fetchedData.addAll(dataFromTraj);
		}
		DrawPanel.deliveredData.addAll(deliveryData());
	}
	
	//returning all data that drone carries
	ArrayList<Data> getData(){
		return dataList;
	}
	
	//reset the data
	void setData(ArrayList<Data> data){
		 dataList.clear();
		 dataList.addAll(data);
	}
	
	//check for delivery
	ArrayList<Data> deliveryData(){
		ArrayList<Data> delivered = new ArrayList<Data>();
		int i=0;
		//int delivered = 0;
		while(i < dataList.size())
			if(dataList.get(i).getDestTraj() == this.getTrajectory()){
				dataList.get(i).setDeliveryTime(System.currentTimeMillis());
				delivered.add(dataList.remove(i));
			}
			else
				i++;		
		return delivered;
	}
	
	//clear the data list
	void clearData(){
		dataList.clear();
	}
	
	//receive data from another drone
	void receiveData(ArrayList<Data> data){
		this.dataList.addAll(data);
	}
	
	//send data to another drone
	void sendData(Robot secondRobot){
		ArrayList<Data> dataToSend = new ArrayList<Data>();
		if(secondRobot.getTrajectory().getX() > this.getTrajectory().getX()){
			int i=0;
			while(i<dataList.size()){
				if(dataList.get(i).getDestTraj().getX() > this.getTrajectory().getX()){
					dataToSend.add(dataList.remove(i));
				} else {
					i++;
				}
			}
		} else if(secondRobot.getTrajectory().getX() < this.getTrajectory().getX()){
			int i=0;
			while(i<dataList.size()){
				if(dataList.get(i).getDestTraj().getX() < this.getTrajectory().getX()){
					dataToSend.add(dataList.remove(i));
				} else {
					i++;
				}
			}
		} else if(secondRobot.getTrajectory().getY() > this.getTrajectory().getY()){
			int i=0;
			while(i<dataList.size()){
				if(dataList.get(i).getDestTraj().getY() > this.getTrajectory().getY()){
					dataToSend.add(dataList.remove(i));
				} else {
					i++;
				}
			}
		} else {
			int i=0;
			while(i<dataList.size()){
				if(dataList.get(i).getDestTraj().getX() < this.getTrajectory().getX()){
					dataToSend.add(dataList.remove(i));
				} else {
					i++;
				}
			}
		}
		secondRobot.receiveData(dataToSend);
	}
	
	
}