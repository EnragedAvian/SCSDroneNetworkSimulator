import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {
	
	DrawPanel () {
		
	}
	 
	@Override
	public void paintComponent(Graphics g) {

        super.paintComponent( g ); // call superclass's paintComponent  
        
        //Graphics2D g2 = ( Graphics2D ) g; // cast g to Graphics2D  

        g.setColor(Color.BLUE);
        g.drawArc(getWidth() / 10, getHeight() / 10, getWidth()/5, getHeight() / 5, 0, 90);
        g.setColor(Color.RED);
        g.drawArc(getWidth() / 10, getHeight() / 10, getWidth()/5, getHeight() / 5, 90, 90);
    }
}
