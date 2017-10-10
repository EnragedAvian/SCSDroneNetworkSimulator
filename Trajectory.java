// Trajectory class which contains circular trajectory object

import java.util.ArrayList;

public class Trajectory {
	private float x;	// x coordinate of vertex
	private float y;	// y coordinate of vertex
	private int trajID;	// Identification number for trajectory
	private int direction;
	private ArrayList<Neighbor> neighbors;	// Neighbors to be better defined later
	
	public ArrayList<Trajectory> trajectories;
	
	public Trajectory(float xCoord, float yCoord) {
		x = xCoord;
		y = yCoord;
		trajID = trajectories.size() + 1;
		
		if (trajectories.size() == 0) {
			direction = 1;
		} else {
			// TODO Fill in logic determining direction of trajectory based on previous trajectories as well as determining which trajectories are neighbors
		}
		
		trajectories.add(this);	// Adding trajectory to the arraylist of trajectories
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
}