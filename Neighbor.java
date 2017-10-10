// Neighbor class contains a links between the graph of trajectories

public class Neighbor {
	int trajID_a;	// IDs of the specific trajectories
	int trajID_b;
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
		
		
	}
}