package tetris;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.Font;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;

//main class
public class TetrisGame extends JPanel implements ActionListener{
	
	
	//gameboard dimensions
	static public int[][] board = {  
		{0,0,0,0,0,0,0,0,0,0},//20 index 0
		{0,0,0,0,0,0,0,0,0,0},//19 index 1
		{0,0,0,0,0,0,0,0,0,0},//18 index 2
		{0,0,0,0,0,0,0,0,0,0},//17 index 3
		{0,0,0,0,0,0,0,0,0,0},//16 index 4
		{0,0,0,0,0,0,0,0,0,0},//15 index 5
		{0,0,0,0,0,0,0,0,0,0},//14 index 6
		{0,0,0,0,0,0,0,0,0,0},//13 index 7
		{0,0,0,0,0,0,0,0,0,0},//12 index 8
		{0,0,0,0,0,0,0,0,0,0},//11 index 9
		{0,0,0,0,0,0,0,0,0,0},//10 index 10
		{0,0,0,0,0,0,0,0,0,0},//9  index 11
		{0,0,0,0,0,0,0,0,0,0},//8  index 12
		{0,0,0,0,0,0,0,0,0,0},//7  index 13
		{0,0,0,0,0,0,0,0,0,0},//6  index 14
		{0,0,0,0,0,0,0,0,0,0},//5  index 15
		{0,0,0,0,0,0,0,0,0,0},//4  index 16
		{0,0,0,0,0,0,0,0,0,0},//3  index 17
		{0,0,0,0,0,0,0,0,0,0},//2  index 18
		{0,0,0,0,0,0,0,0,0,0} //1  index 19
	};
	
	
	int time;
	Timer clock = new Timer();
	
	//pos, size of blocks
	static int x = 140;
	static int y = 90;
	static int w = 30;
	static int l = 30;

	Rectangle block;
	
	//obstacle list
 	ArrayList<Rectangle> ob = new ArrayList<Rectangle>(){{
 		add(block);
 	}};
	
	//keyHandling object to detect key presses
	static KeyHandling kHandle = new KeyHandling();
	
	static Random r = new Random();
	
	//constructor
	public TetrisGame(){
		time = 0;
		clock.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				actionPerformed(null);
			}
		}, 0, 10);
		
	}

	
	public void paintComponent(Graphics g){
		
		Graphics2D g2d = (Graphics2D) g;
	
		// draw space for game
		g2d.setColor(Color.WHITE);
	 	g2d.fillRect(20, 60, 300, 600); 
		
		// draw block
	 	g2d.setColor(Color.BLACK);
	 	// make basic block
		Rectangle block = new Rectangle(x, y, w, l); 
	    g2d.fill(block);
	    
	    for (int i = 19; i >= 0; i--) {
	    	for (int j = 9; j >= 0; j--) {
	    		if (board[i][j] == 1) {
	    			g2d.fillRect(20+30*j, 60+30*i, 30, 30);
	    		}
	    	}
		}

	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		time ++;
		repaint();

	}
	
	public static class KeyHandling implements KeyListener{
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			//movements based on key presses
	    	switch(e.getKeyCode()) {
	    			
	    		case KeyEvent.VK_LEFT:
	    			if (x == 20) {
	    				x -=0;
	    			}
	    			else {
	    				x -= 30;
	    			}
	    			break;
	    			
	    		case KeyEvent.VK_RIGHT:
	    			if (x == 290) {
	    				x +=0;
	    			}
	    			else {
	    				x += 30;
	    			}
	    			break;
	    			
	    		case KeyEvent.VK_A:
	    			if (x == 20) {
	    				x -=0;
	    			}
	    			else {
	    				x -= 30;
	    			}
	    			break;
	    			
	    		case KeyEvent.VK_D:
	    			if (x == 290) {
	    				x +=0;
	    			}
	    			else {
	    				x += 30;
	    			};
	    			break;
	    			
	    		// drop	
	    		case KeyEvent.VK_S:
	    			int j = (x-20) / 30;
	    			
	    			for (int i = 19; i >= 0; i--) {
	    				if (board[i][j] == 0) {
	    					board[i][j] = 1;
	    					i = -1;
	    				}
	    			}
	    			
	    			x = 140;
	    			y = 90;
	    			break;
	        }
	    	
	    }

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			//transform back when key released
			
		}
	}
	

	//main function
	public static void main(String[] args) {
		
		//set title
		JFrame window = new JFrame("Tetris");
		
		// set new GamePlay object to refer to
		TetrisGame game = new TetrisGame();
		Container c = window.getContentPane();
		c.add(game);
		
		//draw  bg which changes color every time it's run
		Color bg = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		game.setBackground(bg); 
		
		//window settings
		window.setBounds(300, 200, 600, 700); //size
		window.setResizable(false); //no resize
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close
		window.setVisible(true); //visibility
		
		//add key listener for key presses
		window.addKeyListener(kHandle);
		
		

	}
}