package tetris;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Instruction extends JFrame implements ActionListener{
	
	static Random r = new Random();
	
	
	// draw key buttons
	JButton a = new JButton("A");
	JButton s = new JButton("S");
	JButton w = new JButton("W");
	JButton d = new JButton("D");
	JButton e = new JButton("E");
	JButton z = new JButton("Z");
	JButton x = new JButton("X");
	JButton up = new JButton("^");
	JButton down = new JButton("V");
	JButton left = new JButton("<");
	JButton right = new JButton(">");
	JButton space = new JButton("SPACE");
	
	//constructor
	public Instruction() {
		
		//accessing title attribute of JFrame
		super("TETRIS");
		
		//set container object to draw
		Container c = getContentPane();
		c.setBackground(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));

        // draw key buttons
        c.add(a);
        a.setBounds(60, 55, 30, 30); //left (a)
        c.add(s);
        s.setBounds(90, 55, 30, 30); //soft drop (s)
        c.add(d);
        d.setBounds(120, 55, 30, 30); //right (d)
        c.add(w);
        w.setBounds(85, 25, 30, 30); //counterclockwise (w)
        c.add(e);
        e.setBounds(115, 25, 30, 30); //clockwise (e)
        c.add(z);
        z.setBounds(75, 85, 30, 30); //counterclockwise (z)
        c.add(x);
        x.setBounds(105, 85, 30, 30); //clockwise (x)
        c.add(up);
        up.setBounds(290, 55, 30, 30); //left (up)
        c.add(down);
        down.setBounds(290, 85, 30, 30); //soft drop (down)
        c.add(left);
        left.setBounds(260, 85, 30, 30); //left (left)
        c.add(right);
        right.setBounds(320, 85, 30, 30); //right (right)
        c.add(space);
        space.setBounds(135, 115, 125, 20); //hard drop (space)
        
		//set Layout
		c.setLayout(null);

	}

	public void paint(Graphics g){
		
		super.paint(g);
        
        g.setColor(Color.BLACK);
        g.fillRect(60, 215, 475, 170);
        g.setColor(new Color(240, 240, 240));
        g.fillRect(50, 205, 475, 170);
        
        // print instruction
	    g.setColor(Color.BLACK);
	    g.setFont(new Font("Arial", Font.PLAIN, 15));
	    g.drawString("Press E or X to rotate clockwise", 65, 230); 
	    g.drawString("Press W or Z to rotate counterclockwise", 65, 255); 
	    g.drawString("Press A or < to move left", 65, 280); 
	    g.drawString("Press D or > to move right", 65, 305);
	    g.drawString("Press S or V to soft drop (block can still be moved after dropping)", 65, 330);
	    g.drawString("Press SPACE or ^ to hard drop (block can't be moved after dropping)", 65, 355);

	}
	
	//main function
	public static void start() {
		 
		Instruction ins = new Instruction();
		
		// window settings
		ins.setBounds(1000, 250, 550, 420);
		ins.setResizable(false); //no resize
		ins.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}