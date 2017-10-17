import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {
	private List<Trajectory> trajList = new ArrayList();
	
	DrawPanel (List<Trajectory> t) {
		trajList = t;
	}
	 
	//@Override
	public void paintComponent(Graphics g) {

	    super.paintComponent( g ); // call superclass's paintComponent  
	    
	    //Graphics2D g2 = ( Graphics2D ) g; // cast g to Graphics2D  

	   //Make Trajectories
	    //Trees
	    //Make initial Trajectory
	    int radius = getWidth()/10;
	    int distBetweenTraj = getWidth()/100;
	    if(trajList.size() > 0)
	    {
	    	g.drawOval((int)(getWidth()/2 - getWidth()/20), (int)(getHeight()/2 - radius), radius, radius);
	    	//g.drawOval((int)(getWidth()/2 + Math.cos(Math.toRadians(180)) * distBetweenTraj), (int)(getHeight()/2 + Math.sin(Math.toRadians(180) * distBetweenTraj)), (int)(getWidth()/10), (int)(getWidth()/10));
	//    	trajList.add(new Trajectory((float)(getWidth()/2 - getWidth()/20), (float)(getHeight()/2 - radius)));
	    }
	    //Make next trajectories
	    if(trajList.size() > 2){
	    	Trajectory traj = trajList.get(0);
	    	g.drawOval((int)(getWidth()/2 + Math.cos(Math.toRadians(traj.getAng())) * distBetweenTraj), (int)(getHeight()/2 + Math.sin(Math.toRadians(trajList.get(0).getAng())) * distBetweenTraj), (int)(getWidth()/10), (int)(getWidth()/10));
	    }
	    
	    //Make grid
	    
	    
	    //Make Robots
	    
	    
	    //Drawing Trajectories
	    
	    //Drawing Robots
	}
}
