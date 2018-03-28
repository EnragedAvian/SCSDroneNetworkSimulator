import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ButtonPanel extends JPanel{
	protected JButton start, autoGrid, addTraj, removeTraj, addDrone, autofill, showEdges, removeEdges, addData, removeData, autoTest;
	
	public ButtonPanel(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		start = new JButton("Start");
		autoGrid = new JButton("Create Grid");
		addTraj = new JButton("Add Trajectory");
		removeTraj = new JButton("Remove Trajectory");
		addDrone = new JButton("Add Drone");
		autofill = new JButton("Autofill Drones");
		showEdges = new JButton("Show Edges");
		removeEdges = new JButton("Hide Edges"); // rename to "Hide Edges" for the time being. If we want "Remove Edge" functionality, rename it back.
		//removeEdges = new JButton("Remove Edges");
		addData = new JButton ("Add Data");
		removeData = new JButton ("Remove Data");
		autoTest = new JButton("Test");
		
		//set panel to GridLayout
		GridLayout layout = new GridLayout(5,2);
		layout.setHgap(5);
		layout.setVgap(5);
		setLayout(layout);
		add(start);
		add(autoGrid);
		add(addTraj);
		add(removeTraj);
		add(addDrone);
		add(autofill);
		add(showEdges);
		add(removeEdges);
		add(addData);
		add(removeData);
		add(autoTest);
		
	}
}
