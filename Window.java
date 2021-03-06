import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame implements ActionListener{
	private ButtonPanel buttons;
	private MenuBar menu;
	private DrawPanel draw;
	private List<Trajectory> tList = new ArrayList();
	private JButton zoomIn, zoomOut;
	
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
		
		//zoom in and out buttons
		zoomIn = new JButton("+");
		zoomOut = new JButton("-");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(zoomIn);
		panel.add(zoomOut);
		add(panel, BorderLayout.SOUTH);
		
		
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
		buttons.addData.addActionListener(this);
		buttons.removeData.addActionListener(this);
		buttons.autoTest.addActionListener(this);
		
		menu.load.addActionListener(this);
		menu.newGraph.addActionListener(this);
		menu.save.addActionListener(this);
		
		zoomIn.addActionListener(this);
		zoomOut.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == buttons.addDrone){
			draw.createRobot(getGraphics());
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
			DrawPanel.robotList.clear();
			new Robot(DrawPanel.trajectoryList.get(0), 0);
			DrawPanel.trajectoryList.get(0).populateNeighbors();
			Experiments.clear();
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
		
		if(e.getSource() == buttons.addData)
		{
			draw.addData();
			//TODO need show robot ID method
		}
		
//		if(e.getSource() == buttons.removeData)
//		{
//			draw.removeData();
//		}
		
		if(e.getSource() == buttons.autoTest)
		{
			draw.test(1);
			Constants.running = true;
			if(Experiments.period != -1){
				Constants.running = false;
				DrawPanel.robotList.clear();
				DrawPanel.trajectoryList.clear();
				draw.test(4);
			}
			repaint();
		}

		if(e.getSource() == menu.load){
			draw.loadGrid();
			repaint();
		}
		
		if(e.getSource() == menu.newGraph){
			draw.clear();
		}
		
		if(e.getSource() == menu.save){
			draw.saveGrid();
			repaint();
		}
		
		if(e.getSource() == zoomIn){
			Constants.setScale((float)1.1);
			for(Trajectory t:DrawPanel.trajectoryList){
				t.setX((float)(t.getX()*Constants.scale));
				t.setY((float)(t.getY()*Constants.scale));
			}
			for(Robot r:DrawPanel.robotList){
				r.setRadius((float)(r.getRadius()*Constants.scale));
			}
		}
		
		if(e.getSource() == zoomOut){
			Constants.setScale((float)(1/1.1));
			for(Trajectory t:DrawPanel.trajectoryList){
				t.setX((float)(t.getX()*Constants.scale));
				t.setY((float)(t.getY()*Constants.scale));
			}
			for(Robot r:DrawPanel.robotList){
				r.setRadius((float)(r.getRadius()*Constants.scale));
			}
		}
		
		//TODO: Default scaling
	}

}
