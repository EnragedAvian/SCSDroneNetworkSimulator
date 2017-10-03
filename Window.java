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
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
