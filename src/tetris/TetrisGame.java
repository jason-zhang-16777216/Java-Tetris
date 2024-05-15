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
	static public int[][] board = {  //Initializes the board's starting state.
		{0,0,0,0,0,0,0,0,0,0},//21 index 0
		{0,0,0,0,0,0,0,0,0,0},//20 index 1
		{0,0,0,0,0,0,0,0,0,0},//19 index 2
		{0,0,0,0,0,0,0,0,0,0},//18 index 3
		{0,0,0,0,0,0,0,0,0,0},//17 index 4
		{0,0,0,0,0,0,0,0,0,0},//16 index 5
		{0,0,0,0,0,0,0,0,0,0},//15 index 6
		{0,0,0,0,0,0,0,0,0,0},//14 index 7
		{0,0,0,0,0,0,0,0,0,0},//13 index 8
		{0,0,0,0,0,0,0,0,0,0},//12 index 9
		{0,0,0,0,0,0,0,0,0,0},//11 index 10
		{0,0,0,0,0,0,0,0,0,0},//10 index 11
		{0,0,0,0,0,0,0,0,0,0},//9  index 12
		{0,0,0,0,0,0,0,0,0,0},//8  index 13
		{0,0,0,0,0,0,0,0,0,0},//7  index 14
		{0,0,0,0,0,0,0,0,0,0},//6  index 15
		{0,0,0,0,0,0,0,0,0,0},//5  index 16
		{0,0,0,0,0,0,0,0,0,0},//4  index 17
		{0,0,0,0,0,0,0,0,0,0},//3  index 18
		{0,0,0,0,0,0,0,0,0,0},//2  index 19
		{0,0,0,0,0,0,0,0,0,0} //1  index 20 
	};
	
	//time
	static int time;
	Timer clock = new Timer();
	int t;
	
	//pos, size, velocity of blocks

	static int x = 140;
	static int y = 30;
	static int w = 30;
	static int l = 30;
	static int v = 30;
	
	static int score = 0;
	
	//block being controlled by player
	Rectangle block;
	
	//obstacle list
 	//ArrayList<Rectangle> ob = new ArrayList<Rectangle>(){{
 //		add(block);
 	//}};
	
	//keyHandling object to detect key presses
	static KeyHandling kHandle = new KeyHandling();
	
	static Random r = new Random();
	
	//constructor
	public TetrisGame(){
		time = 0;
		t = 1;
		clock.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				actionPerformed(null);
			}
		}, 0, 10);
		
	}

	
	public void paintComponent(Graphics g){
		
		Graphics2D graphics = (Graphics2D) g;
	
		// draw space for game
		graphics.setColor(Color.WHITE);
	 	graphics.fillRect(20, 60, 300, 600); 
	 	graphics.setColor(Color.GRAY);
	 	graphics.fillRect(20, 30, 300, 30);
		// draw block
	 	graphics.setColor(Color.BLACK);
		Rectangle block = new Rectangle(x, y, w, l); 
	    graphics.fill(block);
	    
	    
	    //update game state
	    for (int i = 20; i >= 1; i--) { //Iterates over rows.
	    	for (int j = 9; j >= 0; j--) { // Iterates over columns
	    		if (board[i][j] == 1) {
	    			graphics.fillRect(20+30*j, 30+30*i, w, l); //locks the block at (i,j)
	    			
	    		}
	    		else if (board[i][j] == 2) {
	    			graphics.fillRect(20+30*j, 30+30*i, w, l); //fills the block at (i,j) (not locked)
	    			
	    		}
	    		if(checkLine(i)) {
	    			clearLine(i);
	    		}
	    	}
		}
	    

	}
	public boolean checkLine(int index) { // Checks for filled lines
		for (var i = 0; i < 10; i++) {
			if (board[index][i] == 0) {
				return false;
			}
		}
		return true;
	}
	public void clearLine(int index) { // Handles line clearing
		for (var i = index; i > 0; i--) { // Moves each line above down by 1
			for (var j = 0; j < 10; j++) {
				board[i][j] = board[i-1][j];
			}
		}
		
		for (var i = 0;i<10;i++) { // Clears the top line
			board[0][i] = 0;
			score++;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
		time ++;
		
		//block moves down slowly
		if (time % 50 == 0) {
			y += v;
		}
		
	    //check if block reached the bottom
	    if (y == 630 || board[(y-30)/l + 1][(x-20)/l] == 1) {
	    	
	    	//make block remain on top of stacked block for enough time before stacking
	    	t++; 
	    	if (t % 50 == 0) {
	    		board[(y-30)/30][(x-20)/30] = 1;
				x = 140;
				y = 30;
				time = 0;
				t = 1;
	    	}
	    }
		
		//Game-end condition.
		for (int i = 0; i <= 9; i++) {
			if (board[0][i] == 1) { //Ends game if a piece is placed above the board.
				System.out.println(score);
				System.out.print("LOSE!!!");
				System.exit(0); 
			}
		}
		repaint();
	}
	
	public static final class KeyHandling implements KeyListener{
		
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
	    			int k = (x-20) / 30;
	    			
	    			for (int i = 0; i <= 20; i++) {
	    				if (board[i][k] == 1) {
	    					board[i-1][k] = 1;
	    					x = 140;
	    	    			y = 30;
	    	    			time = 0;
	    	    			break;
	    				}
	    				else if (i == 20 && board[i][k] == 0) {
	    					board[20][k] = 1;
	    					x = 140;
	    	    			y = 30;
	    	    			time = 0;
	    	    			break;
	    				}
	    			}
	    			break;
	    			
	    		// drop	
	    		case KeyEvent.VK_DOWN:
	    			int j = (x-20) / 30;
	    			
	    			for (int i = 0; i <= 20; i++) {
	    				if (board[i][j] == 1) {
	    					board[i-1][j] = 1;
	    					x = 140;
	    	    			y = 30;
	    	    			time = 0;
	    	    			break;
	    				}
	    				else if (i == 20 && board[i][j] == 0) {
	    					board[20][j] = 1;
	    					x = 140;
	    	    			y = 30;
	    	    			time = 0;
	    	    			break;
	    				}
	    			}
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
		window.setBounds(350, 150, 650, 750); //size
		window.setResizable(false); //no resize
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close
		window.setVisible(true); //visibility
		
		//add key listener for key presses
		window.addKeyListener(kHandle);
		
		

	}
}