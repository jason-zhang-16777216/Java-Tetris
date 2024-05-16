package tetris;
import java.util.ArrayList;
import java.util.Arrays;
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
	static int t;
	
	//pos, size, velocity of blocks

	//static int x = 140;
	//static int y = 30;
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
		board[0][5] = 2;
		time = 1;
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
	 	graphics.setColor(Color.BLACK);
	    
	    
	    //update game state
	    for (int i = 0; i <= 20; i++) { //Iterates over rows.
	    	for (int j = 9; j >= 0; j--) { // Iterates over columns
	    		if (board[i][j] == 1) {
	    			graphics.fillRect(20+30*j, 30+30*i, w, l); //locks the block at (i,j)
	    			
	    		}
	    		else if (board[i][j] == 2) {
	    			graphics.fillRect(20+30*j, 30+30*i, w, l); //fills the block at (i,j) (not locked)
	    			
	    		}
	    		if(checkLine(i)) {
	    			clearLine(i);
	    			board[0][5] = 2;
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
	public static void endGame() { //Game-end condition
		for (int ii = 0; ii <= 9; ii++) {
			if (board[0][ii] == 1) { //Ends game if a piece is placed above the board.

				System.out.println(score);
				System.out.print("LOSE!!!");
				System.exit(0); 
			}
		}
	}
	public static void checkReachBottom() { //check if block reached the bottom
		
		for (int i = 0; i < 20; i++) { //Iterates over rows (top to bottom)
			for (int j = 9; j >= 0; j--) { // Iterates over columns (right to left)
				
				if (board[i][j] == 2 && board[i + 1][j] == 1) { //if block is stacking on a locked block
			    	    	
					//make block remain on top of stacked block for enough time before locking 
					t++;
			    	if (t % 50 == 0) {
			    		for (int ii = 0; ii <= 20; ii++) { //Iterates over rows (top to bottom)
			    			for (int jj = 9; jj >= 0; jj--) { // Iterates over columns (right to left)
			    				if (board [ii][jj] == 2) {
			    					board[ii][jj] = 1;
			    	    	    }
			    	    	}
			    	    }
			    	    endGame();
			    	    board[0][5] = 2; //draw new block
			    	    time = 1;
			    	    t = 1;
			    	}
			    } 
				else if (board[20][j] == 2 && i == 0) {//if block is at the bottom
					t++;
					//System.out.println(t);
			    	if (t % 50 == 0) {
			    		//System.out.println(t);
			    		for (int ii = 0; ii <= 20; ii++) { //Iterates over rows (top to bottom)
			    			for (int jj = 9; jj >= 0; jj--) { // Iterates over columns (right to left)
			    				if (board [ii][jj] == 2) {
			    					board[ii][jj] = 1;
			    	    	    }
			    	    	}
			    	    }
			    		endGame();
			    	    board[0][5] = 2; //draw new block
			    	    time = 1;
			    	    t = 1;
			    	    		
			    	}
			    }
			    			
			}
		}
		
	}
	public static boolean checkComplexShape(int j, int i) { //check if shape is complex
		//check if current block is surrounded by any unlocked block
		if (((j <= 0 || i <= 0)|| board[i-1][j-1] != 2) && (i <= 0 || board[i-1][j] != 2) && ((j >= 9 || i <= 0)|| board[i-1][j+1] != 2) &&
    		(j <= 0 || board[i][j-1]!= 2)                                &&                             (j >= 9|| board[i][j+1] != 2) &&
    		((j <= 0 || i >= 20)|| board[i+1][j-1] != 2) && (i >= 20|| board[i+1][j] != 2) && ((j >= 9 || i >= 20)|| board[i+1][j+1] != 2)) {
			return true; 
		}
		return false;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		time ++;
		checkReachBottom();
		
		//block moves down slowly
		if (time % 50 == 0) {
			for (int i = 20; i >= 0; i--) { //Iterates over rows (bottom to top)
		    	for (int j = 9; j >= 0; j--) { // Iterates over columns (right to left)
		    		if (board[i][j] == 2) {
		    			board[i][j] = 0;
		    			try {
		    				board[i+1][j] = 2;
		    			}
		    			catch (ArrayIndexOutOfBoundsException ex) { //if block is at bottom (index out of range)
		    				checkReachBottom();
		    			}
		    		}
		    	}
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
	    			int testL = 0; //check to see if shape is touching boundary
	    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
	    					
	    					if (board[i][j] == 2) { // check if it's moving shape
	    						
	    						// for 1 by 1
	    						if (checkComplexShape(j, i)) {  
	    							if (j != 0  && board[i][j-1] != 1) { // check if shape is touching leftmost boundary
	    								board[i][j] = 0;
	    								board[i][j-1] = 2;
	    							}
	    			    		}
	    						
	    						// for complex shapes
	    	    				else {
	    	    					if (j != 0 && board[i][j-1] != 1) { // check if shape is touching 
	    	    						testL ++;
	    	    					}
	    	    					else {
	    	    						testL--;
	    	    					}
	    						}
	    					}
	    				}
	    			}
	    			if (testL < 0) {
	    				for (int i = 0; i < 20; i++) { //Iterates over rows (top to bottom)
		    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
		    					if (board[i][j] == 2 && j!=0) {
		    						board[i][j] = 0;
    								board[i][j-1] = 2;
		    					}
		    				}
	    				}
	    			}
	    			break;
	    			
	    		case KeyEvent.VK_RIGHT:
	    			int testR = 0;
	    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
	    					
	    					if (board[i][j] == 2) { // check if it's moving shape
	    						
	    						// for 1 by 1
	    						if (checkComplexShape(j, i)) { 
	    							if (j != 9  && board[i][j+1] != 1) { // check if shape is touching rightmost boundary
	    								board[i][j] = 0;
	    								board[i][j+1] = 2;
	    							}
	    			    		}
	    						
	    						// for complex shapes
	    	    				else {
	    	    					if (j != 9 && board[i][j+1] != 1) { // check if shape is touching rightmost boundary
	    	    						testR ++;
	    	    					}
	    	    					else {
	    	    						testR--;
	    	    					}
	    						}
	    					}
	    				}
	    			}
	    			if (testR < 0) {
	    				for (int i = 0; i < 20; i++) { //Iterates over rows (top to bottom)
		    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
		    					if (board[i][j] == 2 && j!=0) {
		    						board[i][j] = 0;
    								board[i][j+1] = 2;
		    					}
		    				}
	    				}
	    			}
	    			break;
	    			
	    		
	    			
	    		case KeyEvent.VK_A:
	    			int testLl = 0;
	    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
	    					
	    					if (board[i][j] == 2) { // check if it's moving shape
	    						
	    						// for 1 by 1
	    						if (checkComplexShape(j, i)) {
	    							if (j != 0 && board[i][j-1] != 1) { // check if shape is touching leftmost boundary
	    								board[i][j] = 0;
	    								board[i][j-1] = 2;
	    							}
	    			    		}
	    						
	    						// for complex shapes
	    	    				else {
	    	    					if (j != 0 && board[i][j-1] != 1) { // check if shape is touching leftmost boundary
	    	    						testLl ++;
	    	    					}
	    	    					else {
	    	    						testLl--;
	    	    					}
	    						}
	    					}
	    				}
	    			}
	    			if (testLl < 0) {
	    				for (int i = 0; i < 20; i++) { //Iterates over rows (top to bottom)
		    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
		    					if (board[i][j] == 2 && j!=0) {
		    						board[i][j] = 0;
    								board[i][j-1] = 2;
		    					}
		    				}
	    				}
	    			}
	    			break;
	    			
	    		case KeyEvent.VK_D:
	    			int testRr = 0;
	    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
	    					
	    					if (board[i][j] == 2) { // check if it's moving shape
	    						
	    						// for 1 by 1
	    						if (checkComplexShape(j, i)) { 
	    							if (j != 9  && board[i][j+1] != 1) { // check if shape is touching rightmost boundary
	    								board[i][j] = 0;
	    								board[i][j+1] = 2;
	    							}
	    			    		}
	    						
	    						// for complex shapes
	    	    				else {
	    	    					if (j != 9 && board[i][j+1] != 1) { // check if shape is touching rightmost boundary
	    	    						testRr ++;
	    	    					}
	    	    					else {
	    	    						testRr--;
	    	    					}
	    						}
	    					}
	    				}
	    			}
	    			if (testRr < 0) {
	    				for (int i = 0; i < 20; i++) { //Iterates over rows (top to bottom)
		    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
		    					if (board[i][j] == 2 && j!=0) {
		    						board[i][j] = 0;
    								board[i][j+1] = 2;
		    					}
		    				}
	    				}
	    			}
	    			break;
	    		
	    		// drop	
	    		case KeyEvent.VK_S:
	    			int d = 0; //distance to move block down by
	    			int all_d[] = {100};
	    			for (int i = 20; i >= 0; i--) { //Iterates over rows (bottom to top)
	    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
	    					
	    					if (board[i][j] == 2) { //find the distance from movable block to nearest locked in block below
	    						for (int ii = i+1; ii < 21 && board[ii][j] != 1; ii++) { 
	    							d++;
	    						}
	    						// add distance to list
	    						int[] newArray = Arrays.copyOf(all_d, all_d.length + 1);
	    						newArray[newArray.length - 1] = d;
	    				        all_d = newArray;
	    					}

	    				}
	    			}
	    			
	    			// find the smallest value in the list of distances
	    			int min = all_d[0];
	    		    for(int i=0; i<all_d.length; i++) { 
	    		    	if(min > all_d[i]){
	    		           min = all_d[i];
	    		        }
	    		    }
	    		    // move all movable blocks down by min
	    			for (int i = 20; i >= 0; i--) { 
	    				for (int j = 9; j >= 0; j--) {
	    					if (board[i][j] == 2) {
	    						board[i][j] = 0;
	    						board[i+min][j] = 2;
	    						time = 1;
	    					}
	    				}
	    			}
	    			checkReachBottom();
	    			break;
	    			
	    		// drop	
	    		case KeyEvent.VK_DOWN:
	    			int di = 0; //distance to move block down by
	    			int all_di[] = {100};
	    			for (int i = 20; i >= 0; i--) { //Iterates over rows (bottom to top)
	    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
	    					
	    					if (board[i][j] == 2) { //find the distance from movable block to nearest locked in block below
	    						for (int ii = i+1; ii < 21 && board[ii][j] != 1; ii++) { 
	    							di++;
	    						}
	    						// add distance to list
	    						int[] newArray = Arrays.copyOf(all_di, all_di.length + 1);
	    						newArray[newArray.length - 1] = di;
	    				        all_di = newArray;
	    					}

	    				}
	    			}
	    			
	    			// find the smallest value in the list of distances
	    			int minu = all_di[0];
	    		    for(int i=0; i<all_di.length; i++) { 
	    		    	if(minu > all_di[i]){
	    		           minu = all_di[i];
	    		        }
	    		    }
	    		    // move all movable blocks down by min
	    			for (int i = 20; i >= 0; i--) { 
	    				for (int j = 9; j >= 0; j--) {
	    					if (board[i][j] == 2) {
	    						board[i][j] = 0;
	    						board[i+minu][j] = 2;
	    						time = 1;
	    					}
	    				}
	    			}
	    			checkReachBottom();
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