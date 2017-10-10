import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class Window extends JFrame implements ActionListener{
	private ButtonPanel buttons;
	private MenuBar menu;
	private DrawPanel draw;
	
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
		
		//window settings
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
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == buttons.addDrone){
			
		}
		if(e.getSource() == buttons.addTraj){
			
		}
		if(e.getSource() == buttons.autofill){
			
		}
		if(e.getSource() == buttons.autoGrid){
			
		}
		if(e.getSource() == buttons.removeEdges){
			
		}
		if(e.getSource() == buttons.removeTraj){
			
		}
		if(e.getSource() == buttons.showEdges){
			
		}
		if(e.getSource() == buttons.start){
			
		}

		
	}

}
