import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Window extends JFrame implements ActionListener{
	private ButtonPanel buttons;
	private MenuBar menu;
	private DrawPanel draw;
	private List<Trajectory> tList = new ArrayList();
	
	public Window(){
		buttons = new ButtonPanel();
		menu = new MenuBar();
		draw = new DrawPanel();
		
		//add menu bar
		setJMenuBar(menu);
		
		//add panels
		add(buttons, BorderLayout.WEST);
		add(draw);
		pack();
		draw.setBackground(Color.WHITE);
				
		//window settings
		setSize(500, 300);
		setExtendedState(JFrame.MAXIMIZED_BOTH); //fullscreen
		setDefaultCloseOperation(EXIT_ON_CLOSE); //terminate program when closed
		setVisible(true);
		setTitle("Drone Simulator");
		
		buttons.addDrone.addActionListener(this);
		buttons.addTraj.addActionListener(this);
		buttons.autofill.addActionListener(this);
		buttons.autoGrid.addActionListener(this);
		buttons.removeEdges.addActionListener(this);
		buttons.removeTraj.addActionListener(this);
		buttons.showEdges.addActionListener(this);
		buttons.start.addActionListener(this);
		
		menu.load.addActionListener(this);
		menu.newGraph.addActionListener(this);
		menu.save.addActionListener(this);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == buttons.addDrone){
			draw.createDrone(getGraphics());
			repaint();
		}
		
		if(e.getSource() == buttons.addTraj){
			// add drone to arraylist
			// call redraw in drawPanel
			draw.createTraj(getGraphics());
			repaint();
		}
		
		if(e.getSource() == buttons.autofill){
			//draw.autoFillDrones(getGraphics());
			new Robot(Trajectory.trajectories.get(0), 0);
			Trajectory.trajectories.get(0).populateNeighbors();
			repaint();
		}
		
		if(e.getSource() == buttons.autoGrid){
			draw.clear();
			draw.createGrid(getGraphics());
			repaint();
		}
		
		if(e.getSource() == buttons.removeTraj){
			draw.removeTraj();
			repaint();
		}
		
		if(e.getSource() == buttons.showEdges){
			draw.showEdges();
			repaint();
		}
		
		if(e.getSource() == buttons.removeEdges){
			draw.removeEdges();
			repaint();
		}
		
		if(e.getSource() == buttons.start){
			if (Constants.running) {
				Constants.running = false;
				buttons.start.setText("Start");
			} else {
				Constants.running = true;
				buttons.start.setText("Stop");
			}
		}

		if(e.getSource() == menu.load){
			//see line 960 in ScreenWindow of old sim
		}
		
		if(e.getSource() == menu.newGraph){
			draw.clear();
		}
		
		if(e.getSource() == menu.save){
			//see line 906 in ScreenWindow of old sim
		}
		
	}

}
