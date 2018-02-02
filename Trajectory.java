// Trajectory class which contains circular trajectory object.

import java.util.ArrayList;
import java.lang.Math;


public class Trajectory {
	private float x;  // x coordinate of vertex
	private float y;  // y coordinate of vertex
	private int trajID;  // Identification number for trajectory
	private int direction;
	ArrayList<Neighbor> neighbors;  // Neighbors to be better defined later
  
	public static ArrayList<Trajectory> trajectories = new ArrayList<Trajectory>();
  
  /*public void buildNetwork() {
    for (int i = 0; i<trajectories.size(); i++) {
      
    }
  }*/
  
	public Trajectory(float xCoord, float yCoord) {
		x = xCoord;
		y = yCoord;
    
		trajectories.add(this);  // Adding trajectory to the arraylist of trajectories
    
		trajID = trajectories.size();
		System.out.println("Trajectory size: " + trajectories.size());
    
		neighbors = new ArrayList<Neighbor>();
    
    
    
		if (trajectories.size() == 1) {
			direction = 1;
			System.out.println("Created Direction for trajectory " + trajID);
		} else {
			//Knowingly creating issues that can be fixed later. Namely, that there is no checking to see if trajectories are too close to one another
			ArrayList<Trajectory> compareTrajectories = new ArrayList<Trajectory>();
      
      
			for (int i = 0; i<trajectories.size(); i++) {
				if (trajectories.get(i).getID()!=trajID) {
					if (inRange(trajectories.get(i))) {
						compareTrajectories.add(trajectories.get(i));
						System.out.println("Trajectory found within range!");
					}
				}
			}
			int oldestTraj = 999999;  // Initializing to high value in order to find smaller values than it
			int arrayID = 0;
			for (int j = 0; j<compareTrajectories.size(); j++) {  // Creating link with the oldest trajectory in range first, then making other links based off of that.
				if (compareTrajectories.get(j).getID() < oldestTraj) {
					oldestTraj = compareTrajectories.get(j).getID();
					arrayID = j;
				}        
			}
			if(compareTrajectories.size() > 0) {
				direction = -1 * compareTrajectories.get(arrayID).getDir();
				for (int k = 0; k<compareTrajectories.size(); k++) {
					if (direction != compareTrajectories.get(k).getDir()) {
						addNeighbor(compareTrajectories.get(k));
						compareTrajectories.get(k).addNeighbor(this);
						System.out.println("Added a neighbor!");
						System.out.println("Neighbor added: " + neighbors.get(0).trajID_b);
					}
				}
			} else {
				direction = 1;
			}
			
			
		}
	}
  
	int getID() {
		return trajID;
	}
  
	float getX() {
		return x;
	}
  
	float getY() {
		return y;
	}
  
	int getDir() {
		return direction;
	}
  
	void addNeighbor(Trajectory t) {
		Neighbor newNeighbor = new Neighbor(this, t);
		neighbors.add(newNeighbor);
	}
	
	void setX(float xval){
		x = xval;
	}
	
	void setY(float yval){
		y = yval;
	}
  
	boolean inRange(Trajectory t) {
		float xDist = Math.abs(x - t.getX());
		float yDist = Math.abs(y - t.getY());
		System.out.println("xDist: " + xDist + " yDist: " + yDist);
		float distance = (float)Math.sqrt((double)(xDist*xDist + yDist*yDist));
		System.out.println("distance: " + distance);
		System.out.println("Maximum distance: " + (2*Constants.trajRadius/Math.cos(Math.PI/12)));
		if (distance <= (float)(2*Constants.trajRadius/Math.cos(Math.PI/6))) {
			return true;
		} else {
			return false;
		}
	}
	
	void populateNeighbors() {	// Function to populate neighboring trajectories with synchronized robots
		float tempAngle = 0;
		float tempDiff;
		float angle;
		for(Robot r: Robot.robots) {
			if (r.getID() == trajID) {
				tempAngle = r.getAngle();
			}
		}
		for(Neighbor n: neighbors) {
			boolean filled = false;
			for(Robot r: Robot.robots) {
				if (r.getID() == n.trajID_b) {
					filled = true;
				}
			}
			if(!filled) {
				tempDiff = tempAngle - n.angle_a;
				angle = n.angle_b - tempDiff;
				new Robot(n.traj_b, angle);
				n.traj_b.populateNeighbors();
			}
		}
	}
}