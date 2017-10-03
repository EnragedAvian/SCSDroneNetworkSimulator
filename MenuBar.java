import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuBar extends JMenuBar implements ActionListener{
	private JMenu options;
	private JMenu help;
	private JMenuItem newGraph;
	private JMenuItem save;
	private JMenuItem load;
	private JMenuItem quit;
	private JMenuItem viewHelp;
	private JMenuItem about;
	
	public MenuBar(){
		//menu categories
		options = new JMenu("Options");
		help = new JMenu("Help");
		
		//options
		newGraph = new JMenuItem("New Graph");
		save = new JMenuItem("Save to Text File");
		load = new JMenuItem("Load from Text File");
		quit = new JMenuItem("Quit");
		options.add(newGraph);
		options.add(save);
		options.add(load);
		options.add(quit);
		
		//help
		viewHelp = new JMenuItem("View Help");
		about = new JMenuItem("About Simulator");
		help.add(viewHelp);
		help.add(about);
		
		add(options);
		add(help);
		
		newGraph.addActionListener(this);
		save.addActionListener(this);
		load.addActionListener(this);
		quit.addActionListener(this);
		
		viewHelp.addActionListener(this);
		about.addActionListener(this);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == quit) {
			JOptionPane.showConfirmDialog(null, "Are you sure?", "Are you sure?", JOptionPane.YES_NO_OPTION);
		}
		if(e.getSource() == viewHelp){
			JOptionPane helpPane = new JOptionPane("Help documentation goes here");
			JDialog helpDialog = helpPane.createDialog((JFrame)null, "Help");
			helpDialog.setLocation(20,20);
			helpDialog.setVisible(true);
		}
		if(e.getSource() == about){
			JOptionPane aboutPane = new JOptionPane("Created 2017 for Synchronized Drones project");
			JDialog aboutDialog = aboutPane.createDialog((JFrame)null, "About");
			aboutDialog.setLocation(20,20);
			aboutDialog.setVisible(true);
		}
	}

}
