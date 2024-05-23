package tetris;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;

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
	static int time;
	static int fallTime;
	Timer clock = new Timer();
	static int lockTime;
	static int w = 30; //Width and length. 
	static int l = 30;
	static int score = 0;
	static KeyHandling kHandle = new KeyHandling();//Key-press detection. 
	static Random r = new Random();
	static char type; //type of block
	static int check = 1; //make sure soft drop lock fallTime is consistent
	static int[] x_values = {100}; //all x values of block
	static int[] y_values = {100}; //all y values of block
	static int rotationNum = 1; 
	static boolean gameOver = false;
	static int killScreenLine = 0; //the line in the kill screen (ending animation)
	
	public TetrisGame(){
		time = 0;
		fallTime = 0;
		lockTime = 1;
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
	    		if ((board[i][j] == 1)||(board[i][j] == 2)) {
	    			graphics.fillRect(20+30*j, 30+30*i, w, l); //locks the block at (i,j)
	    			graphics.setColor(Color.WHITE);
	    			graphics.fillRect(22+30*j, 32+30*i, w-4, l-4);
	    			graphics.setColor(Color.BLACK);
	    			graphics.fillRect(24+30*j, 34+30*i, w-8, l-8);
	    			
	    		}
	    		if(checkLine(i)) {
	    			clearLine(i);
	    		}
	    	}
		}
	    
	    //prints game over animation
	    if(gameOver == true) {
	    	if(fallTime % 10 == 0) {
	    		for(var i = 0; i < 10; i++) {
	    			if(fallTime % 10 == 0) {
	    				board[20-killScreenLine][i] = 1;
	    			}
	    		}
	    		if(killScreenLine < 20) {
	    			killScreenLine++;
	    		}else{
	    			System.out.println("SCORE: " + score);
					System.out.println("YOU LOSE!!! HAHAHAHAHAHAHA! (ahem ahem... sorry, you wann try again..? :>)");
	    			System.exit(0);	    		
	    		}
	    	}
	    }
	}
	
	public boolean checkLine(int index) { // Checks for filled lines
		if(gameOver == true) {
			return(false);
		}
		for (var i = 0; i < 10; i++) {
			if (board[index][i] != 1) {
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
	public static void endGame(){ //Game-end condition
		
		for (int i = 0; i <= 9; i++) {
			if (board[0][i] == 1) { //Ends game if a piece is placed above the board.
				gameOver = true;
			}
		}
	}
	public static void getBlock() { // Chooses a random block type and then adds it to the board. s
		if(gameOver == true) {
			return;
		}
		rotationNum = 1;
		char[] blockTypes = {'A', 'I', 'O', 'T', 'S', 'Z', 'L','J'}; 
		type = blockTypes[r.nextInt(blockTypes.length)]; //r.nextInt(blockTypes.length)
		
		switch(type) {
			case('I'):
				board[0][3] = 2; // □□□■■■■□□□
				board[0][4] = 2; // □□□□□□□□□□
				board[0][5] = 2;
				board[0][6] = 2;
				break;
			
			case('O'):
				board[1][4] = 2; // □□□□■■□□□□
				board[0][4] = 2; // □□□□■■□□□□
				board[0][5] = 2;
				board[1][5] = 2;
				break;
				
			case('T'):
				board[0][5] = 2; // □□□□□■□□□□
				board[1][4] = 2; // □□□□■■■□□□
				board[1][5] = 2;
				board[1][6] = 2;
				break;
				
			case('S'):
				board[0][5] = 2; // □□□□■■□□□□
				board[0][4] = 2; // □□□■■□□□□□
				board[1][4] = 2;
				board[1][3] = 2;
				break;
			
			case('Z'):
				board[1][5] = 2; // □□□■■□□□□□
				board[1][4] = 2; // □□□□■■□□□□
				board[0][4] = 2;
				board[0][3] = 2;
				break;
				
			case('L'):
				board[0][5] = 2; // □□□□□■□□□□
				board[1][3] = 2; // □□□■■■□□□□
				board[1][4] = 2;
				board[1][5] = 2;
				break;
				
			case('J'):
				board[0][3] = 2; // □□□■□□□□□□
				board[1][3] = 2; // □□□■■■□□□□
				board[1][4] = 2;
				board[1][5] = 2;
				break;
				
			case('A'):
				board[0][5] = 2; // □□□□□■□□□□
		}
		
	}
	public static void checkReachBottom() { //check if block reached the bottom and lock piece
		
		for (int i = 0; i < 20; i++) { //Iterates over rows (top to bottom)
			for (int j = 9; j >= 0; j--) { // Iterates over columns (right to left)
				
				if ((board[i][j] == 2 && board[i + 1][j] == 1) || (board[20][j] == 2 && i == 0)) { //if block is stacking on a locked block or at the bottom
			    	
					if (check == 1) { //make sure soft drop lock fallTime is consistent
						lockTime++;
						check ++;
					}
			    	if (lockTime % 50 == 0) {
			    		for (int ii = 0; ii <= 20; ii++) { //Iterates over rows (top to bottom)
			    			for (int jj = 9; jj >= 0; jj--) { // Iterates over columns (right to left)
			    				if (board[ii][jj] == 2) {
			    					board[ii][jj] = 1; //locking all pieces
			    	    	    }
			    	    	}
			    	    }
			    		endGame();
			    	    getBlock(); //draw new block
			    	    fallTime =1;
			    	    lockTime = 1;
			    	    		
			    	}
			    } 			
			}
		}
		check = 1;
		
	}
	public static boolean checkComplexShape(int j, int i) { //check if shape is complex
		//check if current block is surrounded by any unlocked block
		if (((j <= 0 || i <= 0)|| board[i-1][j-1] != 2) && (i <= 0 || board[i-1][j] != 2) && ((j >= 9 || i <= 0)|| board[i-1][j+1] != 2) &&
    		(j <= 0 || board[i][j-1]!= 2)                                  &&                              (j >= 9|| board[i][j+1] != 2) &&
    		((j <= 0 || i >= 20)|| board[i+1][j-1] != 2) && (i >= 20|| board[i+1][j] != 2) && ((j >= 9 || i >= 20)|| board[i+1][j+1] != 2)) {
			return true; 
		}
		return false;
	}
	
	public void actionPerformed(ActionEvent e) {
		fallTime ++;
		time++;
		checkReachBottom();
		
		//block moves down slowly
		if (fallTime % 50 == 0) {
			for (int i = 20; i >= 0; i--) { //Iterates over rows (bottom to top)
		    	for (int j = 9; j >= 0; j--) { // Iterates over columns (right to left)
		    		checkReachBottom();
		    		
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
	    	
    		case KeyEvent.VK_A:
    			int testLl = 0;
    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
    					
    					if (board[i][j] == 2) { // check if it's moving shape
    						
    						// for 1 by 1
    						if (checkComplexShape(j, i)) {
    							testLl = 1;
    							if (j != 0 && board[i][j-1] != 1) { // check if shape is touching leftmost boundary
    								board[i][j] = 0;
    								board[i][j-1] = 2;
    							}
    			    		}
    						
    						// for complex shapes
    	    				else {
    	    					if (j != 0 && board[i][j-1] != 1) { // check if shape is touching leftmost boundary
    	    						testLl += 0;
    	    					}
    	    					else {
    	    						testLl--;
    	    					}
    						}
    					}
    				}
    			}
    			if (testLl == 0) {
    				for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
	    					if (board[i][j] == 2) {
	    						board[i][j] = 0;
								board[i][j-1] = 2;
	    					}
	    				}
    				}
    			}
    			break;
	    	
	    		case KeyEvent.VK_LEFT:
	    			int testL = 0;
	    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
	    					
	    					if (board[i][j] == 2) { // check if it's moving shape
	    						
	    						// for 1 by 1
	    						if (checkComplexShape(j, i)) {
	    							testL = 1;
	    							if (j != 0 && board[i][j-1] != 1) { // check if shape is touching leftmost boundary
	    								board[i][j] = 0;
	    								board[i][j-1] = 2;
	    							}
	    			    		}
	    						
	    						// for complex shapes
	    	    				else {
	    	    					if (j != 0 && board[i][j-1] != 1) { // check if shape is touching leftmost boundary
	    	    						testL += 0;
	    	    					}
	    	    					else {
	    	    						testL--;
	    	    					}
	    						}
	    					}
	    				}
	    			}
	    			if (testL == 0) {
	    				for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
		    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
		    					if (board[i][j] == 2) {
		    						board[i][j] = 0;
    								board[i][j-1] = 2;
		    					}
		    				}
	    				}
	    			}
	    			checkReachBottom();
	    			break;
	    		
	    		case KeyEvent.VK_D:
	    			int testRr = 0;
	    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
	    					
	    					if (board[i][j] == 2) { // check if it's moving shape
	    						
	    						// for 1 by 1
	    						if (checkComplexShape(j, i)) {
	    							testRr = 1;
	    							if (j != 9 && board[i][j+1] != 1) { // check if shape is touching leftmost boundary
	    								board[i][j] = 0;
	    								board[i][j+1] = 2;
	    							}
	    			    		}
	    						
	    						// for complex shapes
	    	    				else {
	    	    					if (j != 9 && board[i][j+1] != 1) { // check if shape is touching leftmost boundary
	    	    						testRr += 0;
	    	    					}
	    	    					else {
	    	    						testRr--;
	    	    					}
	    						}
	    					}
	    				}
	    			}
	    			if (testRr == 0) {
	    				for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
		    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
		    					if (board[i][j] == 2) {
		    						board[i][j] = 0;
    								board[i][j+1] = 2;
		    					}
		    				}
	    				}
	    			}
	    			checkReachBottom();
	    			break;
	    		
	    		case KeyEvent.VK_RIGHT:
	    			int testR = 0;
	    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
	    					
	    					if (board[i][j] == 2) { // check if it's moving shape
	    						
	    						// for 1 by 1
	    						if (checkComplexShape(j, i)) {
	    							testR = 1;
	    							if (j != 9 && board[i][j+1] != 1) { // check if shape is touching leftmost boundary
	    								board[i][j] = 0;
	    								board[i][j+1] = 2;
	    							}
	    			    		}
	    						
	    						// for complex shapes
	    	    				else {
	    	    					if (j != 9 && board[i][j+1] != 1) { // check if shape is touching leftmost boundary
	    	    						testR += 0;
	    	    					}
	    	    					else {
	    	    						testR--;
	    	    					}
	    						}
	    					}
	    				}
	    			}
	    			if (testR == 0) {
	    				for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
		    				for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
		    					if (board[i][j] == 2) {
		    						board[i][j] = 0;
    								board[i][j+1] = 2;
		    					}
		    				}
	    				}
	    			}
	    			checkReachBottom();
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
	    				        d = 0;
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
	    						fallTime = 1;
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
	    				        di = 0;
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
	    						fallTime = 1;
	    					}
	    				}
	    			}
	    			checkReachBottom();
	    			break;
	    			
	    		// rotation counterclockwise (helllllllllp)
	    		case KeyEvent.VK_Z:
	    			boolean testx = true;
	    			boolean testy = true;
	    			for (int i = 20; i >= 0; i--) { 
	    				for (int j = 9; j >= 0; j--) {
	    					
	    					if (board[i][j] == 2) { //find the distance from movable block to nearest locked in block below
	    						
	    						// make list of all coodinates of block
	    						for (int ii = 1; ii < x_values.length; ii++) {
	    							if (x_values[ii] == j) {
	    								testx = false;
	    							}
	    						}
	    						
	    						for (int ii = 1; ii < y_values.length; ii++) {
	    							if (y_values[ii] == i) {
	    								testy = false;
	    							}
	    						}
	    						
	    						if (testx) {
	    							int[] newArray = Arrays.copyOf(x_values, x_values.length + 1);
		    						newArray[newArray.length - 1] = j;
		    						x_values = newArray;
	    						}
	    						if (testy) {
	    							int[] newArray = Arrays.copyOf(y_values, y_values.length + 1);
		    				        newArray[newArray.length - 1] = i;
		    				        y_values = newArray;
	    						}
	    						testx = true;
	    						testy = true;
	    					}
	    				}
	    			}
	    			if (x_values.length == 4) {
	    				switch(type) {
	    					case('T'):
	    						if(y_values[1]+1 <= 20 && board[y_values[1]+1][x_values[2]] != 1) {
	    							if (rotationNum == 1) {
	    								board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[1]+1][x_values[2]] = 2;
	    								rotationNum++;
	    							}
	    						}
	    						if(y_values[2]-1 >= 0 && board[y_values[2]-1][x_values[1]] != 1) {
	    							if (rotationNum == 3) {
	    								board[y_values[2]][x_values[3]] = 0;
	    								board[y_values[2]-1][x_values[1]] = 2;
	    								rotationNum++;
	    							}
	    						}
	    						
	    						break;
	    						
	    					case('S'):
	    						if(y_values[1]+1 <= 20 && board[y_values[2]][x_values[2]] != 1 && board[y_values[1]+1][x_values[1]] != 1) {
	    							if (rotationNum == 1) {
	    								board[y_values[2]][x_values[1]] = 0;
	    								board[y_values[2]][x_values[3]] = 0;
	    								board[y_values[2]][x_values[2]] = 2;
	    								board[y_values[1]+1][x_values[1]] = 2;
	    								rotationNum++;
	    							}
	    						}
	    						if(y_values[2]-1 >= 0 && board[y_values[1]][x_values[3]] != 1 && board[y_values[2]-1][x_values[1]] != 1) {
	    							if (rotationNum == 3) {
	    								board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[1]][x_values[2]] = 0;
	    								board[y_values[1]][x_values[3]] = 2;
	    								board[y_values[2]-1][x_values[1]] = 2;
	    								rotationNum++;
	    							}
	    						}
	    						break;
	    						
	    					case('Z'):
	    						if(y_values[1]+1 <= 20 && board[y_values[1]][x_values[3]] != 1 && board[y_values[1]+1][x_values[3]] != 1) {
	    							if (rotationNum == 1) {
	    								board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[2]][x_values[3]] = 0;
	    								board[y_values[1]][x_values[3]] = 2;
	    								board[y_values[1]+1][x_values[3]] = 2;
	    								rotationNum++;
	    							}
	    						}
	    						if(y_values[2]-1 >= 0 && board[y_values[2]][x_values[1]] != 1 && board[y_values[2]-1][x_values[1]] != 1) {
	    							if (rotationNum == 3) {
	    								board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[2]][x_values[3]] = 0;
	    								board[y_values[2]][x_values[1]] = 2;
	    								board[y_values[2]-1][x_values[1]] = 2;
	    								rotationNum++;
	    							}
	    						}
	    						break;
	    					
	    					case('L'):
	    						if(y_values[1]+1 <= 20 && board[y_values[2]][x_values[2]] != 1 && board[y_values[2]][x_values[3]] != 1 && board[y_values[1]+1][x_values[2]] != 1) {
	    							if (rotationNum == 1) {
	    								board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[2]][x_values[1]] = 0;
	    								board[y_values[1]][x_values[3]] = 0;
	    								board[y_values[2]][x_values[2]] = 2;
	    								board[y_values[2]][x_values[3]] = 2;
	    								board[y_values[1]+1][x_values[2]] = 2;
	    								rotationNum++;
	    							}
	    						}
	    						if(y_values[2]-1 >= 0 && board[y_values[1]][x_values[2]] != 1 && board[y_values[1]][x_values[3]] != 1 && board[y_values[2]-1][x_values[3]] != 1) {
	    							if (rotationNum == 3) {
	    								board[y_values[2]][x_values[2]] = 0;
	    								board[y_values[2]][x_values[1]] = 0;
	    								board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[1]][x_values[2]] = 2;
	    								board[y_values[1]][x_values[3]] = 2;
	    								board[y_values[2]-1][x_values[3]] = 2;
	    								rotationNum++;
	    							}
	    						}
	    						break;
	    					
	    					case('J'):
	    						if(y_values[1]+1 <= 20 && board[y_values[2]][x_values[2]] != 1 && board[y_values[1]+1][x_values[2]]!= 1 && board[y_values[1]+1][x_values[3]] != 1) {
	    							if (rotationNum == 1) {
	    								board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[1]][x_values[3]] = 0;
	    								board[y_values[2]][x_values[3]] = 0;
	    								board[y_values[2]][x_values[2]] = 2;
	    								board[y_values[1]+1][x_values[2]] = 2;
	    								board[y_values[1]+1][x_values[3]] = 2;
    									rotationNum++;
	    							}
	    						}
	    						if(y_values[2]-1 >= 0 && board[y_values[1]][x_values[2]] != 1 && board[y_values[2]-1][x_values[2]] != 1 && board[y_values[2]-1][x_values[1]] != 1) {
	    							if (rotationNum == 3) {
	    								board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[2]][x_values[1]] = 0;
	    								board[y_values[2]][x_values[3]] = 0;
	    								board[y_values[1]][x_values[2]] = 2;
	    								board[y_values[2]-1][x_values[2]] = 2;
	    								board[y_values[2]-1][x_values[1]] = 2;
	    								rotationNum++;
	    							}
	    						}
	    						break;
	    				}
	    			}
	    			else if (y_values.length == 4) {
	    				switch(type) {
	    				
    						case('T'):
    							if(x_values[1]+1 <= 9 && board[y_values[2]][x_values[1]+1]!=1) {
    								if (rotationNum == 2) {
    									board[y_values[3]][x_values[1]] = 0;
    									board[y_values[2]][x_values[1]+1] = 2;
    									rotationNum++;
    								}
    							}
    							if(x_values[1]-1 >= 0 && board[y_values[2]][x_values[1]-1]!=1) {
    								if (rotationNum == 4) {
    									board[y_values[1]][x_values[1]] = 0;
    									board[y_values[2]][x_values[1]-1] = 2;
    									rotationNum = 1;
    								}
    							}
    							
    							break;
    						
    						case('S'):
    							if(x_values[1]+1 <= 9 && board[y_values[1]][x_values[2]] != 1 && board[y_values[2]][x_values[1]+1] != 1) {
    								if (rotationNum == 2) {
    									board[y_values[2]][x_values[2]] = 0;
	    								board[y_values[3]][x_values[2]] = 0;
	    								board[y_values[1]][x_values[2]] = 2;
	    								board[y_values[2]][x_values[1]+1] = 2;
    									rotationNum++;
    								}
    							}
    							if(x_values[2]-1 >= 0 && board[y_values[2]][x_values[2]-1] != 1 && board[y_values[3]][x_values[1]] != 1) {
    								if (rotationNum == 4) {
    									board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[2]][x_values[1]] = 0;
	    								board[y_values[2]][x_values[2]-1] = 2;
	    								board[y_values[3]][x_values[1]] = 2;
    									rotationNum = 1;
    								}
    							}
    							break;
    							
    						case('Z'):
    							if(x_values[2]+1 <= 9 && board[y_values[1]][x_values[2]+1] != 1 && board[y_values[1]][x_values[2]] != 1) {
    								if (rotationNum == 2) {
    									board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[3]][x_values[2]] = 0;
	    								board[y_values[1]][x_values[2]+1] = 2;
	    								board[y_values[1]][x_values[2]] = 2;
    									rotationNum++;
    								}
    							}
    							if(x_values[1]-1 >= 0 && board[y_values[3]][x_values[1]-1] != 1 && board[y_values[3]][x_values[1]] != 1) {
    								if (rotationNum == 4) {
    									board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[3]][x_values[2]] = 0;
	    								board[y_values[3]][x_values[1]-1] = 2;
	    								board[y_values[3]][x_values[1]] = 2;
    									rotationNum = 1;
    								}
    							}
    							break;
    				
    						case('L'):
    							if(x_values[1]+1 <= 9 && board[y_values[2]][x_values[2]] != 1 && board[y_values[1]][x_values[2]] != 1 && board[y_values[2]][x_values[1]+1] != 1) {
    								if (rotationNum == 2) {
    									board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[3]][x_values[1]] = 0;
	    								board[y_values[3]][x_values[2]] = 0;
	    								board[y_values[2]][x_values[2]] = 2;
	    								board[y_values[1]][x_values[2]] = 2;
	    								board[y_values[2]][x_values[1]+1] = 2;
    									rotationNum++;
    								}
    							}
    							if(x_values[2]-1 >= 0 && board[y_values[2]][x_values[1]] != 1 && board[y_values[2]][x_values[2]-1] != 1 && board[y_values[3]][x_values[1]] != 1) {
    								if (rotationNum == 4) {
    									board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[1]][x_values[2]] = 0;
	    								board[y_values[3]][x_values[2]] = 0;
	    								board[y_values[2]][x_values[1]] = 2;
	    								board[y_values[2]][x_values[2]-1] = 2;
	    								board[y_values[3]][x_values[1]] = 2;
    									rotationNum = 1;
    								}
    							}
    							break;
    						
    						case('J'):
    							if(x_values[1]+1 <= 9 && board[y_values[2]][x_values[2]] != 1 && board[y_values[2]][x_values[1]+1] != 1 && board[y_values[1]][x_values[1]+1] != 1) {
    								if (rotationNum == 2) {
    									board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[1]][x_values[2]] = 0;
	    								board[y_values[3]][x_values[1]] = 0;
	    								board[y_values[2]][x_values[2]] = 2;
	    								board[y_values[2]][x_values[1]+1] = 2;
	    								board[y_values[1]][x_values[1]+1] = 2;
    									rotationNum++;
    								}
    							}
    							if(x_values[1]-1 >= 0 && board[y_values[2]][x_values[1]-1] != 1 && board[y_values[2]][x_values[2]] != 1 && board[y_values[3]][x_values[1]-1] != 1) {
    								if (rotationNum == 4) {
    									board[y_values[1]][x_values[1]] = 0;
	    								board[y_values[3]][x_values[1]] = 0;
	    								board[y_values[3]][x_values[2]] = 0;
	    								board[y_values[2]][x_values[1]-1] = 2;
	    								board[y_values[3]][x_values[1]-1] = 2;
	    								board[y_values[2]][x_values[2]] = 2;
    									rotationNum = 1;
    								}
    							}
    							break;
	    				}
	    			}
	    			else if (x_values.length == 5) {
	    				if (y_values[1]-2 >= 0 && y_values[1]-1 >= 0 && y_values[1]+1 <= 20 && board[y_values[1]-2][x_values[3]] !=1 && board[y_values[1]-1][x_values[3]] !=1 && board[y_values[1]+1][x_values[3]] !=1) {
	    					if (rotationNum == 1) {
		    					board[y_values[1]-2][x_values[3]] = 2;
			    				board[y_values[1]-1][x_values[3]] = 2;
			    				board[y_values[1]+1][x_values[3]] = 2;
			    				board[y_values[1]][x_values[1]] = 0;
			    				board[y_values[1]][x_values[2]] = 0;
			    				board[y_values[1]][x_values[4]] = 0;
			    				rotationNum ++;
		    				}
	    				}
	    				if (y_values[1]+2 <= 20 && y_values[1]-1 >= 0 && y_values[1]+1 <= 20 && board[y_values[1]+2][x_values[2]] !=1 && board[y_values[1]-1][x_values[2]] !=1 && board[y_values[1]+1][x_values[2]] !=1) {
	    					if (rotationNum == 3) {
	    						board[y_values[1]+2][x_values[2]] = 2;
	    						board[y_values[1]-1][x_values[2]] = 2;
	    						board[y_values[1]+1][x_values[2]] = 2;
	    						board[y_values[1]][x_values[1]] = 0;
	    						board[y_values[1]][x_values[3]] = 0;
	    						board[y_values[1]][x_values[4]] = 0;
	    						rotationNum ++;
	    					}
	    				}
	    			}
	    			else if (y_values.length == 5) {
	    				if (rotationNum == 2) {
	    					if (x_values[1]+2 <= 9 && x_values[1]-1 >= 0 && x_values[1]+1 <= 9 && board[y_values[2]][x_values[1]-1] !=1 && board[y_values[2]][x_values[1]+1] !=1 && board[y_values[2]][x_values[1]+2] !=1) {
	    						board[y_values[2]][x_values[1]-1] = 2;
			    				board[y_values[2]][x_values[1]+1] = 2;
			    				board[y_values[2]][x_values[1]+2] = 2;
			    				board[y_values[1]][x_values[1]] = 0;
			    				board[y_values[3]][x_values[1]] = 0;
			    				board[y_values[4]][x_values[1]] = 0;
			    				rotationNum ++;
	    					}
	    					
	    				}
	    				else if (rotationNum == 4) {
	    					if (x_values[1]-2 >= 0 && x_values[1]-1 >= 0 && x_values[1]+1 <= 9 && board[y_values[3]][x_values[1]+1] !=1 && board[y_values[3]][x_values[1]-1] !=1 && board[y_values[3]][x_values[1]-2] !=1) {
	    						board[y_values[3]][x_values[1]+1] = 2;
			    				board[y_values[3]][x_values[1]-1] = 2;
			    				board[y_values[3]][x_values[1]-2] = 2;
			    				board[y_values[1]][x_values[1]] = 0;
			    				board[y_values[2]][x_values[1]] = 0;
			    				board[y_values[4]][x_values[1]] = 0;
			    				rotationNum = 1;
	    					}
	    				}
	    			}
	    			int[] newArray = {100};
					x_values = newArray;
					y_values = newArray;
	    			break;
	    		
	    		// rotation (helllllllllp)
	    		case KeyEvent.VK_UP:
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
		game.getBlock();
		Container c = window.getContentPane();
		c.add(game);
		
		//draw  bg which changes color every fallTime it's run
		Color bg = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		window.setBackground(bg); 
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