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

public class InstructionManuel extends JFrame implements ActionListener{
	
	private TetrisGame game;
	static Random r = new Random();
	
	// create buttons for different use
	JButton startgame = new JButton("START GAME");
	
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
	public InstructionManuel() {
		
		//accessing title attribute of JFrame
		super("TETRIS");
		
		//set container object to draw
		Container c = getContentPane();
		c.setBackground(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        
        // Add buttons to the container
        c.add(startgame);
        startgame.setBounds(80, 75, 150, 50);
        startgame.addActionListener(this);  
		
        // draw key buttons
        c.add(a);
        a.setBounds(60, 255, 30, 30); //left (a)
        c.add(s);
        s.setBounds(90, 255, 30, 30); //soft drop (s)
        c.add(d);
        d.setBounds(120, 255, 30, 30); //right (d)
        c.add(w);
        w.setBounds(85, 225, 30, 30); //counterclockwise (w)
        c.add(e);
        e.setBounds(115, 225, 30, 30); //clockwise (e)
        c.add(z);
        z.setBounds(75, 285, 30, 30); //counterclockwise (z)
        c.add(x);
        x.setBounds(105, 285, 30, 30); //clockwise (x)
        c.add(up);
        up.setBounds(290, 255, 30, 30); //left (up)
        c.add(down);
        down.setBounds(290, 285, 30, 30); //soft drop (down)
        c.add(left);
        left.setBounds(260, 285, 30, 30); //left (left)
        c.add(right);
        right.setBounds(320, 285, 30, 30); //right (right)
        c.add(space);
        space.setBounds(135, 315, 125, 20); //hard drop (space)
        
		//set Layout
		c.setLayout(null);

	}

	public void paint(Graphics g){
		
		super.paint(g);
	    
	    // print title
	    g.setFont(new Font("Arial", Font.BOLD, 100));
	    g.setColor(Color.BLACK);
	    g.drawString("TETRIS", 350, 170); //draw shadow
	    g.setColor(Color.WHITE);
        g.drawString("TETRIS", 345, 165); // actual title
        
        g.setColor(Color.BLACK);
        g.fillRect(400, 210, 475, 180);
        g.setColor(new Color(240, 240, 240));
        g.fillRect(390, 200, 475, 180);
        
        // print instruction
	    g.setColor(Color.BLACK);
	    g.setFont(new Font("Arial", Font.PLAIN, 15));
	    g.drawString("Press E to X to rotate clockwise", 400, 230); 
	    g.drawString("Press W or Z to rotate counterclockwise", 400, 255); 
	    g.drawString("Press A or < to move left", 400, 280); 
	    g.drawString("Press D or > to move right", 400, 305);
	    g.drawString("Press S or V to soft drop (block can still be moved after dropping)", 400, 330);
	    g.drawString("Press SPACE or ^ to hard drop (block can't be moved after dropping)", 400, 355);

	}
	
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == startgame) {
			this.dispose();
            game.start(); // Start game!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! :>
        }
	}
	
	//main function
	public static void main(String[] args) {
		 
		InstructionManuel tutorial = new InstructionManuel();
		
		// window settings
		tutorial.setBounds(300, 200, 900, 400);
		tutorial.setResizable(false); //no resize
		tutorial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tutorial.setVisible(true);
	}
}
