// Constants class contains constant items that can be changed later (wifi range, traj radius, etc..)

public class Constants {
	public static float wifiRange = 50;
	public static float trajRadius = 50;
	public static float trajPadding = 10;
	public static float robotSpeed = (float)0.02;
	public static boolean running = false;
	public static float scale = 1;
  
	public static float normalizeAngle(float angle) {  // Function which places an angle between range of 0-2pi. Replaced old one in Neighbor class
		float newAngle = angle;
		while(newAngle>(float)(2*Math.PI)) {  // Making sure that angle_a is within the range of 0-2pi (decrementing)
			newAngle -= (float)(2*Math.PI);
		}
		while(newAngle<0) {  // Making sure that angle_a is within the range of 0-2pi (incrementing)
			newAngle += (float)(2*Math.PI);
		}
		return newAngle;
	}
	
	public static void setScale(float sc){
		scale = sc;
		trajRadius = scale*trajRadius;
		trajPadding = scale*trajPadding;
		wifiRange = scale*wifiRange;
	}
}