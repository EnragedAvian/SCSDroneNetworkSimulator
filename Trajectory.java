// Trajectory class which contains circular trajectory object

import java.util.ArrayList;
import java.lang.Math;

public class Trajectory {
	private float x;	// x coordinate of vertex
	private float y;	// y coordinate of vertex
	private int trajID;	// Identification number for trajectory
	private int direction;
	private ArrayList<Neighbor> neighbors;	// Neighbors to be better defined later
	private float radius = 10;
	
	
	public ArrayList<Trajectory> trajectories = new ArrayList();
	
	/*public void buildNetwork() {
		for (int i = 0; i<trajectories.size(); i++) {
			
		}
	}*/
	
	public Trajectory(float xCoord, float yCoord) {
		x = xCoord;
		y = yCoord;
		trajID = trajectories.size() + 1;
		
		if (trajectories.size() == 0) {
			direction = 1;
		} else {
			//Knowingly creating issues that can be fixed later. Namely, that there is no checking to see if trajectories are too close to one another
			for (int i = 0; i<trajectories.size(); i++) {
				if (trajectories.get(i).getID()!=trajID) {
					if (inRange(trajectories.get(i))) {
						
					}
				}
			}
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
	
	boolean inRange(Trajectory t) {
		float xDist = Math.abs(x - t.getX());
		float yDist = Math.abs(y - t.getY());
		float distance = (float)Math.sqrt((double)(xDist*xDist + yDist*yDist));
		if (distance <= (float)(2*radius/Math.cos(Math.PI/12))) {
			return true;
		} else {
			return false;
		}
	}
}