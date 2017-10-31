//  Robot class which handles the creation and motion of robots on screen.

import java.util.ArrayList;
import java.lang.Math;

public class Robot {
	private Trajectory t;
	private float angle;
	private float radius;
	private int ID;
	boolean transitioningIn;
	boolean transitioningOut;
	boolean detected;  // checks to see whether or not robot has been detected in desired range.
	boolean checked;   // boolean stating SOMETHING
	boolean swapped;
	int rangeState;  // Integer determining what range the robot is in, affecting the logic
  
	public static ArrayList<Robot> robots = new ArrayList<Robot>();
  
	Robot(Trajectory traj, float ang){
		angle = ang;
		t = traj;
		// TODO add logic so that robot is given ID of trajectory if the program hasn't started, but is assigned the next value in the sequence if the program has already started
		ID = t.getID();  // Current logic won't work if robots are created while the simulation is running.
		transitioningIn = false;
		transitioningOut = false;
		radius = Constants.trajRadius;
		robots.add(this);
	}
  
	boolean checkNeighbor() {  // Checks the range between robot and neighbor in specified trajectory
		// TODO Add logic keeping track of how many times neighbor has been passed
		System.out.println("checkNeighbor function called");
		for (int i = 0; i<robots.size(); i++) {  // For every robot
			if (robots.get(i).getID()!=this.getID()) {  // If the robot is not the same as the other robot
				System.out.println("Found another robot!");
				float xDist = Math.abs(this.getX() - robots.get(i).getX());
				float yDist = Math.abs(this.getY() - robots.get(i).getY());
				float dist = (float)Math.sqrt((double)(xDist*xDist + yDist*yDist));
				if (dist <= Constants.wifiRange) {
					return true;
				}
			}
		}
		return false;
		//return true;
	}
  
	void move() {
		angle += Constants.robotSpeed;  // Incrementing robot angle by constant value per tick
		angle = Constants.normalizeAngle(angle);  // normalizing angle so robot always falls within 0-2pi
	}
  
	void logic() {
		if (t.getDir() == 1) {  // Checking direction of robot on trajectory
    
			// Adding new definition of rangeState, which allows easy determination of what range the robot is in
    	
    	
			for (int i = 0; i < t.neighbors.size(); i++) {
				// Creating temporary values for all the parameters of the neighbor class to aid with calculations later.
				float tempDetectIn = t.neighbors.get(i).detectIn_a;
				float tempTranIn = t.neighbors.get(i).transitionIn_a;
				float tempAngleRef = t.neighbors.get(i).angle_a;
				float tempTranOut = t.neighbors.get(i).transitionOut_a;
        
				// Checking to see if the robot lies within a detectIn range
				if (((tempDetectIn > tempTranIn) && (angle > tempDetectIn || angle < tempTranIn)) || ((tempDetectIn < tempTranIn) && (angle > tempDetectIn && angle < tempTranIn))) {  // Testing to see if robot falls within the detect range
					rangeState = 1;
				} else if (((tempTranIn > tempAngleRef) && (angle > tempTranIn || angle < tempAngleRef)) || ((tempTranIn < tempAngleRef) && (angle > tempTranIn && angle < tempAngleRef))) {  // Testing to see if robot falls within transitionIn range
					rangeState = 2;
				} else if (((tempAngleRef > tempTranOut) && (angle > tempAngleRef || angle < tempTranOut)) || ((tempAngleRef < tempTranOut) && (angle > tempAngleRef && angle < tempTranOut))) {  // Testing to see if robot falls within transitiouOut range
					rangeState = 3;
				}
			}
      
      
      
      
      
      
			for (int i = 0; i < t.neighbors.size(); i++) {  // For loop iterating through all neighbors in neighbors list
				// executing checking function, looking to see if robot in neighboring trajectory is within range.
				if (t.neighbors.get(i).detectIn_a > t.neighbors.get(i).transitionIn_a ) {  // Testing case if the detecting range passes across the 0/2pi radian line 
					if ((angle > t.neighbors.get(i).detectIn_a || angle < t.neighbors.get(i).transitionIn_a)/* && !checked*/) {  // Checking to see if the robot is within the detect range, not cycling through loop if robot has already detected something.
						detected = checkNeighbor();
						System.out.println("Detected?: " + detected);
						checked = true;
					}
				} else if(t.neighbors.get(i).detectIn_a < t.neighbors.get(i).transitionIn_a) {  // Other case checking to see if the detectIn range doesn't cross the radian line
					if ((angle > t.neighbors.get(i).detectIn_a && angle < t.neighbors.get(i).transitionIn_a)/* && !checked*/) {
						detected = checkNeighbor();  // Checking to see if robot is within range in the neighboring trajectory.
						System.out.println("Detected?: " + detected);
						checked = true;
					}
				}
        
				// Logic that occurs when the robot is in the transition range. If the robot has checked something, and there is no robot to be found, it assigns the value of transitioningIn to true and resets checked to false  
				if (t.neighbors.get(i).transitionIn_a > t.neighbors.get(i).angle_a) {  // Checking to see if transitionIn range crosses over 0/2pi radian line
					if (angle > t.neighbors.get(i).transitionIn_a || angle < t.neighbors.get(i).angle_a) {  // Testing to see if the transitionIn range crosses over the 
						//System.out.println("Within transitionIn range");
						if(!detected && checked) {
							transitioningIn = true;
							checked = false;
							System.out.println("Changed to transitioningIn state");
						}
					}
				} else if (t.neighbors.get(i).transitionIn_a < t.neighbors.get(i).angle_a) {
					if (angle > t.neighbors.get(i).transitionIn_a && angle < t.neighbors.get(i).angle_a) {
						//System.out.println("Within transitionIn range");
						if(!detected && checked) {
							transitioningIn = true;
							checked = false;
							System.out.println("Changed to transitioningIn state");
						}
					}
				}
        
				// Dynamically assigning the radius of the robot based on whether or not the robot is in the transitioning phase
				if (t.neighbors.get(i).transitionIn_a > t.neighbors.get(i).angle_a) {  // Checking to see if transitionIn range crosses over 0/2pi radian line
					if ((angle > t.neighbors.get(i).transitionIn_a || angle < t.neighbors.get(i).angle_a) && transitioningIn) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(angle-t.neighbors.get(i).transitionIn_a)));
					}
				} else if (t.neighbors.get(i).transitionIn_a < t.neighbors.get(i).angle_a) {
					if ((angle > t.neighbors.get(i).transitionIn_a && angle < t.neighbors.get(i).angle_a) && transitioningIn) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(angle-t.neighbors.get(i).transitionIn_a)));
					}
				}
        
				// Checking to see if robot is with transitioningOut range of the trajectory and swapping trajectories if so.
				if (t.neighbors.get(i).angle_a > t.neighbors.get(i).transitionOut_a) {  // Checking to see if transitionIn range crosses over 0/2pi radian line
					if ((angle > t.neighbors.get(i).angle_a || angle < t.neighbors.get(i).transitionOut_a) && transitioningIn) {
						System.out.println("Swapping");
						transitioningIn = false;
						transitioningOut = true;
						// changing angle before changing neighbors
						angle = Constants.normalizeAngle(t.neighbors.get(i).angle_b-(Constants.normalizeAngle(angle-t.neighbors.get(i).angle_a)));
						System.out.println("Old Trajectory is: " + t.getID());
						t = t.neighbors.get(i).traj_b;
						System.out.println("New Trajectory is: " + t.getID());
						break;
					}
					if ((angle > t.neighbors.get(i).angle_a || angle < t.neighbors.get(i).transitionOut_a) && transitioningOut) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(t.neighbors.get(i).transitionOut_a-angle)));
					} /*else if (transitioningOut) {
						transitioningOut = false;
					}*/
				} else if (t.neighbors.get(i).angle_a < t.neighbors.get(i).transitionOut_a) {
					if ((angle > t.neighbors.get(i).angle_a && angle < t.neighbors.get(i).transitionOut_a) && transitioningIn) {
						System.out.println("Swapping");
						transitioningIn = false;
						transitioningOut = true;
						// changing angle before changing neighbors
						angle = Constants.normalizeAngle(t.neighbors.get(i).angle_b-(Constants.normalizeAngle(angle-t.neighbors.get(i).angle_a)));
						System.out.println("Old Trajectory is: " + t.getID());
						t = t.neighbors.get(i).traj_b;
						System.out.println("New Trajectory is: " + t.getID());
						break;
					}
					if ((angle > t.neighbors.get(i).angle_a && angle < t.neighbors.get(i).transitionOut_a) && transitioningOut) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(t.neighbors.get(i).transitionOut_a-angle)));
					} /*else if (transitioningOut) {
						transitioningOut = false;
					}*/
				} 
			}
		}
    
    
    
		if (t.getDir() == -1) {  // Checking direction of robot on trajectory, moving clockwise
			angle -= Constants.robotSpeed;
			angle = Constants.normalizeAngle(angle);
			for (int i = 0; i < t.neighbors.size(); i++) {
				// executing checking function, looking to see if robot in neighboring trajectory is within range.
				if (t.neighbors.get(i).detectIn_a < t.neighbors.get(i).transitionIn_a ) {  // Testing case if the detecting range passes across the 0/2pi radian line      
					if ((angle < t.neighbors.get(i).detectIn_a || angle > t.neighbors.get(i).transitionIn_a) && !checked) {  // Checking to see if the robot is within the detect range, not cycling through loop if robot has already detected something.
						detected = checkNeighbor();
						checked = true;
						System.out.println("Detected?: " + detected);
					}
				} else if(t.neighbors.get(i).detectIn_a > t.neighbors.get(i).transitionIn_a) {
					if ((angle < t.neighbors.get(i).detectIn_a && angle > t.neighbors.get(i).transitionIn_a) && !checked) {
						detected = checkNeighbor();
						checked = true;
						System.out.println("Detected?: " + detected);
					}
				}
        
				// Logic that occurs when the robot is in the transition range. If the robot has checked something, and there is no robot to be found, it assigns the value of transitioningIn to true and resets checked to false  
				if (t.neighbors.get(i).transitionIn_a < t.neighbors.get(i).angle_a) {  // Checking to see if transitionIn range crosses over 0/2pi radian line
					if (angle < t.neighbors.get(i).transitionIn_a || angle > t.neighbors.get(i).angle_a) {
						if(!detected && checked) {
							transitioningIn = true;
							checked = false;
							System.out.println("Changed to transitioningIn state");
						}
					}
				} else if (t.neighbors.get(i).transitionIn_a > t.neighbors.get(i).angle_a) {
					if (angle < t.neighbors.get(i).transitionIn_a && angle > t.neighbors.get(i).angle_a) {
						if(!detected && checked) {
							transitioningIn = true;
							checked = false;
							System.out.println("Changed to transitioningIn state");
						}
					}
				}
        
				// Dynamically assigning the radius of the robot based on whether or not the robot is in the transitioning phase
				if (t.neighbors.get(i).transitionIn_a < t.neighbors.get(i).angle_a) {  // Checking to see if transitionIn range crosses over 0/2pi radian line
					if ((angle < t.neighbors.get(i).transitionIn_a || angle > t.neighbors.get(i).angle_a) && transitioningIn) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(t.neighbors.get(i).transitionIn_a-angle)));
					}
				} else if (t.neighbors.get(i).transitionIn_a > t.neighbors.get(i).angle_a) {
					if ((angle < t.neighbors.get(i).transitionIn_a && angle > t.neighbors.get(i).angle_a) && transitioningIn) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(t.neighbors.get(i).transitionIn_a-angle)));
					}
				}
        
        
        
				// Checking to see if robot is with transitioningOut range of the trajectory and swapping trajectories if so.
				if (t.neighbors.get(i).angle_a < t.neighbors.get(i).transitionOut_a) {  // Checking to see if transitionIn range crosses over 0/2pi radian line
					if ((angle < t.neighbors.get(i).angle_a || angle > t.neighbors.get(i).transitionOut_a) && transitioningIn) {
						transitioningIn = false;
						transitioningOut = true;
						// changing angle before changing neighbors
						angle = Constants.normalizeAngle(t.neighbors.get(i).angle_b+(Constants.normalizeAngle(t.neighbors.get(i).angle_a-angle)));
						System.out.println("Swapping");
						System.out.println("Old Trajectory is: " + t.getID());
						t = t.neighbors.get(i).traj_b;
						System.out.println("New Trajectory is: " + t.getID());
						break;
            
					}
					if ((angle < t.neighbors.get(i).angle_a || angle > t.neighbors.get(i).transitionOut_a) && transitioningOut) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(angle-t.neighbors.get(i).transitionOut_a)));
					} /*else if (transitioningOut) {
						transitioningOut = false;
					}*/
				} else if (t.neighbors.get(i).angle_a > t.neighbors.get(i).transitionOut_a) {  // Checking to see if transitionIn range does not cross 0 radian line
					if ((angle < t.neighbors.get(i).angle_a && angle > t.neighbors.get(i).transitionOut_a) && transitioningIn) {
						System.out.println("Swapping");
						transitioningIn = false;
						transitioningOut = true;
						// changing angle before changing neighbors
						angle = Constants.normalizeAngle(t.neighbors.get(i).angle_b+(Constants.normalizeAngle(t.neighbors.get(i).angle_a-angle)));
						System.out.println("Old Trajectory is: " + t.getID());
						t = t.neighbors.get(i).traj_b;
						System.out.println("New Trajectory is: " + t.getID());
						break;
					}
					if ((angle < t.neighbors.get(i).angle_a && angle > t.neighbors.get(i).transitionOut_a) && transitioningOut) {
						radius = (float)(Constants.trajRadius/Math.cos((double)Constants.normalizeAngle(angle-t.neighbors.get(i).transitionOut_a)));
					} /*else if (transitioningOut) {
						transitioningOut = false;
					}*/
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
		result = (float)(this.getTrajectory().getY() + radius * Math.sin((double)angle));
		return result;
	}
  
}