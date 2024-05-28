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
	JButton startgame = new JButton("START GAME");;
	
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

	}
	
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == startgame) {
			this.dispose();
            game.start(); // Start the tutorial screen
        }
	}
	
	//main function
	public static void main(String[] args) {
		 
		InstructionManuel tutorial = new InstructionManuel();
		
		// window settings
		tutorial.setBounds(300, 200, 800, 400);
		tutorial.setResizable(false); //no resize
		tutorial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tutorial.setVisible(true);
	}
}
