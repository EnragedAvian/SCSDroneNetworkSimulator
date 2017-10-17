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
	private boolean detected;	// checks to see whether or not robot has been detected in desired range.
	private boolean checked;
	private boolean swapped;
	
	public ArrayList<Robot> robots;
	
	Robot(Trajectory traj, float ang){
		angle = ang;
		t = traj;
		// TODO add logic so that robot is given ID of trajectory if the program hasn't started, but is assigned the next value in the sequence if the program has already started
		ID = t.getID();	// Current logic won't work if robots are created while the simulation is running.
		transitioningIn = false;
		transitioningOut = false;
		radius = Constants.trajRadius;
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
					if (dist <= Constants.wifiRange) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	void move() {
		if (t.getDir() == 1) {	// Checking direction of robot on trajectory
			angle += Constants.robotSpeed;
			angle = Neighbor.normalizeAngle(angle);
			for (int i = 0; i < t.neighbors.size(); i++) {
				// executing checking function, looking to see if robot in neighboring trajectory is within range.
				if (t.neighbors.get(i).detectIn_a > t.neighbors.get(i).transitionIn_a ) {	// Testing case if the detecting range passes across the 0/2pi radian line			
					if ((angle > t.neighbors.get(i).detectIn_a || angle < t.neighbors.get(i).transitionIn_a) && !detected) {	// Checking to see if the robot is within the detect range, not cycling through loop if robot has already detected something.
						detected = checkNeighbor(t.neighbors.get(i).traj_b);
					}
					checked = true;
				} else if(t.neighbors.get(i).detectIn_a < t.neighbors.get(i).transitionIn_a) {
					if ((angle > t.neighbors.get(i).detectIn_a && angle < t.neighbors.get(i).transitionIn_a) && !detected) {
						detected = checkNeighbor(t.neighbors.get(i).traj_b);
					}
					checked = true;
				}
				
				// Logic that occurs when the robot is in the transition range. If the robot has checked something, and there is no robot to be found, it assigns the value of transitioningIn to true and resets checked to false	
				if (t.neighbors.get(i).transitionIn_a > t.neighbors.get(i).angle_a) {	// Checking to see if transitionIn range crosses over 0/2pi radian line
					if (angle > t.neighbors.get(i).transitionIn_a || angle < t.neighbors.get(i).angle_a) {
						if(!detected && checked) {
							transitioningIn = true;
							checked = false;
						}
					}
				} else if (t.neighbors.get(i).transitionIn_a < t.neighbors.get(i).angle_a) {
					if (angle > t.neighbors.get(i).transitionIn_a && angle < t.neighbors.get(i).angle_a) {
						if(!detected && checked) {
							transitioningIn = true;
							checked = false;
						}
					}
				}
				
				// Dynamically assigning the radius of the robot based on whether or not the robot is in the transitioning phase
				if (t.neighbors.get(i).transitionIn_a > t.neighbors.get(i).angle_a) {	// Checking to see if transitionIn range crosses over 0/2pi radian line
					if ((angle > t.neighbors.get(i).transitionIn_a || angle < t.neighbors.get(i).angle_a) && transitioningIn) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Neighbor.normalizeAngle(angle-t.neighbors.get(i).transitionIn_a)));
					}
				} else if (t.neighbors.get(i).transitionIn_a < t.neighbors.get(i).angle_a) {
					if ((angle > t.neighbors.get(i).transitionIn_a && angle < t.neighbors.get(i).angle_a) && transitioningIn) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Neighbor.normalizeAngle(angle-t.neighbors.get(i).transitionIn_a)));
					}
				}
				
				// Checking to see if robot is with transitioningOut range of the trajectory and swapping trajectories if so.
				if (t.neighbors.get(i).angle_a > t.neighbors.get(i).transitionOut_a) {	// Checking to see if transitionIn range crosses over 0/2pi radian line
					if ((angle > t.neighbors.get(i).angle_a || angle < t.neighbors.get(i).transitionOut_a) && transitioningIn) {
						transitioningIn = false;
						transitioningOut = true;
						// changing angle before changing neighbors
						angle = Neighbor.normalizeAngle(t.neighbors.get(i).angle_b-(Neighbor.normalizeAngle(angle-t.neighbors.get(i).angle_a)));
						t = t.neighbors.get(i).traj_b;
						
					}
					if ((angle > t.neighbors.get(i).angle_a || angle < t.neighbors.get(i).transitionOut_a) && transitioningOut) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Neighbor.normalizeAngle(t.neighbors.get(i).transitionOut_a-angle)));
					} else if (transitioningOut) {
						transitioningOut = false;
					}
				} else if (t.neighbors.get(i).angle_a < t.neighbors.get(i).transitionOut_a) {
					if ((angle > t.neighbors.get(i).transitionIn_a && angle < t.neighbors.get(i).angle_a) && transitioningIn) {
						transitioningIn = false;
						transitioningOut = true;
						// changing angle before changing neighbors
						angle = Neighbor.normalizeAngle(t.neighbors.get(i).angle_b-(Neighbor.normalizeAngle(angle-t.neighbors.get(i).angle_a)));
						t = t.neighbors.get(i).traj_b;
					}
					if ((angle > t.neighbors.get(i).angle_a && angle < t.neighbors.get(i).transitionOut_a) && transitioningOut) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Neighbor.normalizeAngle(t.neighbors.get(i).transitionOut_a-angle)));
					} else if (transitioningOut) {
						transitioningOut = false;
					}
				}
				
				
			}
		}
		
		if (t.getDir() == -1) {	// Checking direction of robot on trajectory, moving clockwise
			angle -= Constants.robotSpeed;
			angle = Neighbor.normalizeAngle(angle);
			for (int i = 0; i < t.neighbors.size(); i++) {
				// executing checking function, looking to see if robot in neighboring trajectory is within range.
				if (t.neighbors.get(i).detectIn_a < t.neighbors.get(i).transitionIn_a ) {	// Testing case if the detecting range passes across the 0/2pi radian line			
					if ((angle < t.neighbors.get(i).detectIn_a || angle > t.neighbors.get(i).transitionIn_a) && !detected) {	// Checking to see if the robot is within the detect range, not cycling through loop if robot has already detected something.
						detected = checkNeighbor(t.neighbors.get(i).traj_b);
					}
					checked = true;
				} else if(t.neighbors.get(i).detectIn_a > t.neighbors.get(i).transitionIn_a) {
					if ((angle < t.neighbors.get(i).detectIn_a && angle > t.neighbors.get(i).transitionIn_a) && !detected) {
						detected = checkNeighbor(t.neighbors.get(i).traj_b);
					}
					checked = true;
				}
				
				// Logic that occurs when the robot is in the transition range. If the robot has checked something, and there is no robot to be found, it assigns the value of transitioningIn to true and resets checked to false	
				if (t.neighbors.get(i).transitionIn_a < t.neighbors.get(i).angle_a) {	// Checking to see if transitionIn range crosses over 0/2pi radian line
					if (angle < t.neighbors.get(i).transitionIn_a || angle > t.neighbors.get(i).angle_a) {
						if(!detected && checked) {
							transitioningIn = true;
							checked = false;
						}
					}
				} else if (t.neighbors.get(i).transitionIn_a > t.neighbors.get(i).angle_a) {
					if (angle < t.neighbors.get(i).transitionIn_a && angle > t.neighbors.get(i).angle_a) {
						if(!detected && checked) {
							transitioningIn = true;
							checked = false;
						}
					}
				}
				
				// Dynamically assigning the radius of the robot based on whether or not the robot is in the transitioning phase
				if (t.neighbors.get(i).transitionIn_a < t.neighbors.get(i).angle_a) {	// Checking to see if transitionIn range crosses over 0/2pi radian line
					if ((angle < t.neighbors.get(i).transitionIn_a || angle > t.neighbors.get(i).angle_a) && transitioningIn) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Neighbor.normalizeAngle(t.neighbors.get(i).transitionIn_a-angle)));
					}
				} else if (t.neighbors.get(i).transitionIn_a > t.neighbors.get(i).angle_a) {
					if ((angle < t.neighbors.get(i).transitionIn_a && angle > t.neighbors.get(i).angle_a) && transitioningIn) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Neighbor.normalizeAngle(t.neighbors.get(i).transitionIn_a-angle)));
					}
				}
				
				
				
				// Checking to see if robot is with transitioningOut range of the trajectory and swapping trajectories if so.
				if (t.neighbors.get(i).angle_a < t.neighbors.get(i).transitionOut_a) {	// Checking to see if transitionIn range crosses over 0/2pi radian line
					if ((angle < t.neighbors.get(i).angle_a || angle > t.neighbors.get(i).transitionOut_a) && transitioningIn) {
						transitioningIn = false;
						transitioningOut = true;
						// changing angle before changing neighbors
						angle = Neighbor.normalizeAngle(t.neighbors.get(i).angle_b+(Neighbor.normalizeAngle(t.neighbors.get(i).angle_a-angle)));
						t = t.neighbors.get(i).traj_b;
						
					}
					if ((angle < t.neighbors.get(i).angle_a || angle > t.neighbors.get(i).transitionOut_a) && transitioningOut) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Neighbor.normalizeAngle(angle-t.neighbors.get(i).transitionOut_a)));
					} else if (transitioningOut) {
						transitioningOut = false;
					}
				} else if (t.neighbors.get(i).angle_a > t.neighbors.get(i).transitionOut_a) {
					if ((angle < t.neighbors.get(i).transitionIn_a && angle > t.neighbors.get(i).angle_a) && transitioningIn) {
						transitioningIn = false;
						transitioningOut = true;
						// changing angle before changing neighbors
						angle = Neighbor.normalizeAngle(t.neighbors.get(i).angle_b+(Neighbor.normalizeAngle(t.neighbors.get(i).angle_a-angle)));
						t = t.neighbors.get(i).traj_b;
					}
					if ((angle < t.neighbors.get(i).angle_a && angle > t.neighbors.get(i).transitionOut_a) && transitioningOut) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Neighbor.normalizeAngle(angle-t.neighbors.get(i).transitionOut_a)));
					} else if (transitioningOut) {
						transitioningOut = false;
					}
				}
				
				
			}
		}
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