// Neighbor class contains a links between the graph of trajectories

import java.lang.Math;

public class Neighbor {
	int trajID_a;	// IDs of the specific trajectories
	int trajID_b;
	float trajDistance;	// Distance between the two trajectories
	float angle_a;	// Angle between positive x axis and the angle of the trajectory
	float angle_b;
	float detectIn_a;	// Beginning detection angle for trajectories
	float detectIn_b;
	float transitionIn_a;	// Beginning transition angle for trajectories
	float transitionIn_b;
	float transitionOut_a;	// Midway transition point for trajectories, point at where the robot switches trajectories
	float transitionOut_b;
	float transitionEnd_a;	// Ending transition point
	float transitionEnd_b;
	// Rework specific values held, these are just placeholder for now
	
	Neighbor(Trajectory a, Trajectory b) {
		trajID_a = a.getID();
		trajID_b = b.getID();
		
		// Calculating the distance between the two trajectories
		float xRange = Math.abs(a.getX() - b.getX());
		float yRange = Math.abs(a.getY() - b.getY());
		float distance = (float)Math.sqrt((double)(xRange*xRange + yRange*yRange));
		trajDistance = distance;
		
		// math for determining both angles a and b
		if(a.getX()<=b.getX()) {	// Testing to see if a is to the left of b, ensuring that the angle_a falls between +/- pi/2
			float xDist = b.getX()-a.getX();
			float yDist = b.getY()-a.getY();
			angle_a = (float)Math.atan((double)(yDist/xDist));
			angle_b = (float)(angle_a+Math.PI);
		} else {		// If a is to the right of b, use b as the reference point and measure angles from b
			float xDist = a.getX()-b.getX();
			float yDist = a.getY()-b.getY();
			angle_b = (float)Math.atan((double)(yDist/xDist));
			angle_a = (float)(angle_b+Math.PI);
		}
		while(angle_a>2*Math.PI) {	// Making sure that angle_a is within the range of 0-2pi (decrementing)
			angle_a -= (float)(2*Math.PI);
		}
		while(angle_a<2*Math.PI) {	// Making sure that angle_a is within the range of 0-2pi (incrementing)
			angle_a += (float)(2*Math.PI);
		}
		while(angle_b>2*Math.PI) {	// Same as above except for angle_b
			angle_a -= (float)(2*Math.PI);
		}
		while(angle_b<2*Math.PI) {
			angle_a += (float)(2*Math.PI);
		}
		
		// math for determining the detectIn angle for both a and b
	
		
		
	}
}