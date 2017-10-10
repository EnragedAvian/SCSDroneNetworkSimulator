//	Robot class which handles the creation and motion of robots on screen

import java.util.ArrayList;
import java.lang.Math;

public class Robot {
	private Trajectory t;
	private float angle;
	private float radius;
	private int ID;
	private boolean transitioningIn;
	private boolean transitioningOut;
	
	public ArrayList<Robot> robots;
	
	Robot(Trajectory traj, float ang){
		angle = ang;
		t = traj;
		// TODO add logic so that robot is given ID of trajectory if the program hasn't started, but is assigned the next value in the sequence if the program has already started
		ID = t.getID();	// Current logic won't work if robots are created while the simulation is running.
		transitioningIn = false;
		transitioningOut = false;
		radius = Constants.trajRadius;	// TODO fix so that constants still work
		robots.add(this);
	}
	
	boolean checkNeighbor(Trajectory traj) {	// Checks the range between robot and neighbor in specified trajectory
		// TODO Add logic keeping track of how many times neighbor has been passed
		for (int i = 0; i<robots.size(); i++) {
			if (robots.get(i).getID()!=this.getID()) {
				if (robots.get(i).getTrajectory()==traj) {
					float xDist = Math.abs(this.getX() - robots.get(i).getX());
					float yDist = Math.abs(this.getY() - robots.get(i).getY());
					float dist = (float)Math.sqrt((double)(xDist*xDist + yDist*yDist));
					if (dist <= Constants.wifiRange) {	// TODO fix so that constants still work
						return true;
					}
				}
			}
		}
		return false;
	}
	
	void move() {
		angle += Constants.robotSpeed;
		//TO BE UPDATED LATER
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
		result = (float)(this.getTrajectory().getX() + radius * Math.sin((double)angle));
		return result;
	}
}