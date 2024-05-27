package tetris;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.ArrayList;
import java.lang.Math;

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
	static Random r = new Random(); //RNG object. 
	Timer clock = new Timer(); // Time handling. 
	static int time;
	static int fallTime;
	static int lockTime;
	static int w = 28; //Width and height. 
	static int h = 28;
	static int score = 0; // Score == Lines cleared × 10.
	static KeyHandling kHandle = new KeyHandling();//Key-press detection. 
	static char type; //Type of block. (A, I, O, T, S, Z, L, J).
	static boolean blockChecked = false; //make sure soft drop lock fallTime is consistent
	static int[] x_values = {100}; //all x values of block
	static int[] y_values = {100}; //all y values of block
	static int rotationNum = 1; 
	static int oppo_rotationNum = 1;
	static boolean gameOver = false;
	static int killScreenLine = 0; //the line in the kill screen (ending animation)
	static ArrayList<Character> bag = new ArrayList<Character>(); 
	static int bagRandom = 0;
	static Color Z = new Color(207, 54, 22);
	static Color S = new Color(138, 234, 40);
	static Color J = new Color(0, 0, 240);
	static Color L = new Color(221, 164, 34);
	static Color O = new Color(241, 239, 47);
	static Color T = new Color(136, 44, 237);
	static Color I = new Color(0, 240, 240);
	static Color currColor = new Color(0,0,0);
	static boolean hard = false;
	
	/*
	 * little buddy is the single block, and it is not commonly included in Tetris
	 * if little buddy disrupts Tetris' normal game play
	 * we decided for the users to determine whether they want the single block present in their game
	*/
	static boolean buddyMode = false; // later on add JButton for user to control whether little buddy appears of not
	
	
	public TetrisGame(){
		time = 0;
		fallTime = 1;
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
		graphics.setColor(Color.BLACK);
	 	graphics.fillRect(30, 45, 10*w+20, 22*h-2); 
		graphics.setColor(Color.WHITE);
	 	graphics.fillRect(40, 85, 10*w, 20*h-2); 
	 	graphics.setColor(currColor);
	 	graphics.fillRect(40, 55, 280, 28);
	    //update game state
	    for (int i = 20; i >=0 ; i--) { //Iterates over rows.
	    	
	    	for (int j = 9; j >= 0; j--) { // Iterates over columns
	    		
	    		if (board[i][j]!=0) {
	    			switch(Math.abs(board[i][j])) {
		    		case 1:
		    			currColor = Color.BLACK;
		    			break;
		    		case 2:
		    			currColor = I;
		    			break;
		    		case 3:
		    			currColor = O;
		    			break;
		    		case 4:
		    			currColor = T;
		    			break;
		    		case 5:
		    			currColor = S;
		    			break;
		    		case 6:
		    			currColor = Z;
		    			break;
		    		case 7:
		    			currColor = L;
		    			break;
		    		case 8:
		    			currColor = J;
		    			break;
		    		}
	    			
	    		 	graphics.setColor(currColor);
	    		 	graphics.fillRect(40+w*j, 55+h*i, w, h); //locks the block at (i,j)
	    			graphics.setColor(Color.WHITE);
	    		 	graphics.fillRect(41+w*j, 56+h*i, w-2, h-2);
	    			graphics.setColor(currColor);
	    			graphics.fillRect(42+w*j, 57+h*i, w-4, h-4);

	    		}
	    		
		   		if(checkLine(i)) {
		    		clearLine(i);
		    	}
	    	}
	    	
		}
	    //Queue
    	for (int i = 0 ;i < 5;i++) {
    		//graphics.setColor(Color.LIGHT_GRAY);
    		//graphics.fillRect(35, 30, 50, 30);
    	}
	    
	    // print score
	    graphics.setColor(Color.WHITE);
	    graphics.setFont(new Font("Arial", Font.BOLD, 50));
	    for (int xo = -2; xo <= 2; xo++) {
            for (int yo = -2; yo <= 2; yo++) {
                if (xo != 0 || yo != 0) {
                	graphics.drawString("SCORE: " + score, 400 + xo, 75 + yo);//outline
                }
            }
        }
	    graphics.setColor(Color.BLACK);
	    graphics.drawString("SCORE: " + score, 400, 75); //draw text
	    
	    // print time
	    graphics.setColor(Color.WHITE);
	    for (int xo = -2; xo <= 2; xo++) {
            for (int yo = -2; yo <= 2; yo++) {
                if (xo != 0 || yo != 0) {
                	graphics.drawString("TIME: " + time/100, 400 + xo, 175 + yo);//outline
                }
            }
        }
	    graphics.setColor(Color.BLACK);
	    graphics.drawString("TIME: " + time/100, 400, 175); //draw text
	    
	    // check if little buddy more is on and inform user
	    if(buddyMode) {
	    	// print buddy statement
	    	graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		    graphics.setColor(Color.WHITE);
		    for (int xo = -1; xo <= 1; xo++) {
	            for (int yo = -1; yo <= 1; yo++) {
	                if (xo != 0 || yo != 0) {
	                	graphics.drawString("Buddy Mode Enabled (press return to turn off)", 400 + xo, 650 + yo);//outline
	                }
	            }
	        }
		    graphics.setColor(Color.BLACK);
		    graphics.drawString("Buddy Mode Enabled (press return to turn off)", 400, 650); //draw text
	    }
	    else {
	    	// print buddy statement
	    	graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		    graphics.setColor(Color.WHITE);
		    for (int xo = -1; xo <= 1; xo++) {
	            for (int yo = -1; yo <= 1; yo++) {
	                if (xo != 0 || yo != 0) {
	                	graphics.drawString("Buddy Mode Not Enabled (press return to turn on)", 400 + xo, 650 + yo);//outline
	                }
	            }
	        }
		    graphics.setColor(Color.BLACK);
		    graphics.drawString("Buddy Mode Not Enabled (press return to turn on)", 400, 650); //draw text
	    }
	    
	    //prints game over animation
	    if(gameOver == true) {
	    	time--;
		    
	    	if(fallTime % 10 == 0) {
	    		for(var i = 0; i < 10; i++) {
	    			if(fallTime % 10 == 0) {
	    				board[20-killScreenLine][i] = 1;
	    			}
	    		}
	    		if(killScreenLine < 20) {
	    			killScreenLine++;
	    		}else{
	    			clock.cancel();  		
	    		}
	    	}
	    }
	}

	public boolean checkLine(int index) { // Checks for filled lines
		if(gameOver == true) {
			return(false);
		}
		for (var i = 0; i < 10; i++) {
			if (board[index][i] <= 0) {
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
			if (board[0][i] != 0 ) { //Ends game if a piece is placed above the board.
				gameOver = true;
			}
		}
	}
	public static void getBlock() { // Chooses a random block type and then adds it to the board.
		if(gameOver == true) {
			return;
		}
		rotationNum = 1;
		oppo_rotationNum = 1; 
		char[] buddyTypes = {'A', 'I', 'O', 'T', 'S', 'Z', 'L', 'J'}; 
		char[] blockTypes = {'I', 'O', 'T', 'S', 'Z', 'L', 'J'}; 
		
		if(bag.isEmpty() && ! buddyMode) {
			for (char block : blockTypes) {
	            bag.add(block);
	        }
		} 
		else if(bag.isEmpty() && buddyMode) {
			for (char block : buddyTypes) {
	            bag.add(block);
			}
		}
		
		bagRandom = r.nextInt(0,bag.size());
		type = bag.get(bagRandom);
		bag.remove(bagRandom);
		

		
		//type = blockTypes[r.nextInt(blockTypes.length)]; //r.nextInt(blockTypes.length)
		
		switch(type) { ///////// Magnitude controls color, parity controls locked-ness.
			case('A'):
				board[0][5] = -1; // □□□□□■□□□□ 
				break;
			case('I'):
				board[0][3] = -2; // □□□■■■■□□□
				board[0][4] = -2; // □□□□□□□□□□
				board[0][5] = -2;
				board[0][6] = -2;
				
				break;
			
			case('O'):
				board[1][4] = -3; // □□□□■■□□□□
				board[0][4] = -3; // □□□□■■□□□□
				board[0][5] = -3;
				board[1][5] = -3;
				
				break;
				
			case('T'):
				board[0][5] = -4; // □□□□□■□□□□
				board[1][4] = -4; // □□□□■■■□□□
				board[1][5] = -4;
				board[1][6] = -4;
				
				break;
				
			case('S'):
				board[0][5] = -5; // □□□□■■□□□□
				board[0][4] = -5; // □□□■■□□□□□
				board[1][4] = -5;
				board[1][3] = -5;
				
				break;
			
			case('Z'):
				board[1][5] = -6; // □□□■■□□□□□
				board[1][4] = -6; // □□□□■■□□□□
				board[0][4] = -6;
				board[0][3] = -6;
				
				break;
				
			case('L'):
				board[0][5] = -7; // □□□□□■□□□□
				board[1][3] = -7; // □□□■■■□□□□
				board[1][4] = -7;
				board[1][5] = -7;
				
				break;
				
			case('J'):
				board[0][3] = -8; // □□□■□□□□□□
				board[1][3] = -8; // □□□■■■□□□□
				board[1][4] = -8;
				board[1][5] = -8;
		}
	}
	public static void checkReachBottom() { //check if block reached the bottom and lock piece
		
		for (int i = 0; i < 20; i++) { //Iterates over rows (top to bottom)
			for (int j = 9; j >= 0; j--) { // Iterates over columns (right to left)
				
				if ((board[i][j] < 0  && board[i + 1][j] > 0) || (board[20][j] < 0  && i == 0)) { //if block is stacking on a locked block or at the bottom
			    	
					if (blockChecked == false) { //make sure soft drop lock fallTime is consistent
						lockTime++;
						blockChecked = true;
					}
			    	if ((lockTime % 50 == 0)||(hard)) {
			    		for (int ii = 0; ii <= 20; ii++) { //Iterates over rows (top to bottom)
			    			for (int jj = 9; jj >= 0; jj--) { // Iterates over columns (right to left)
			    				if (board[ii][jj] < 0 ) {
			    					board[ii][jj] = -board[ii][jj]; //locking all pieces
			    	    	    }
			    	    	}
			    	    }
			    		endGame();
			    	    getBlock(); //draw new block
			    	    fallTime = 1;
			    	    lockTime = 1;
			    	    		
			    	}
			    } 	
			}
		}
		blockChecked = false;
		hard = false;
	}
	public static boolean checkComplexShape(int j, int i) { //check if shape is complex
		//check if current block is surrounded by any unlocked block
		if (((j <= 0 || i <= 0)|| board[i-1][j-1] >= 0) && (i <= 0 || board[i-1][j] >= 0) && ((j >= 9 || i <= 0)|| board[i-1][j+1] >= 0) &&
    		(j <= 0 || board[i][j-1] >= 0)                                  &&                              (j >= 9|| board[i][j+1] >= 0 ) &&
    		((j <= 0 || i >= 20)|| board[i+1][j-1] >= 0) && (i >= 20|| board[i+1][j] >= 0) && ((j >= 9 || i >= 20)|| board[i+1][j+1] >= 0)) {
			return true; 
		}
		return false;
	}
	public static void down() {
		int d = 0; //distance to move block down by
		int all_d[] = {100};
		for (int i = 20; i >= 0; i--) { //Iterates over rows (bottom to top)
			for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
					
				if (board[i][j] < 0 ) { //find the distance from movable block to nearest locked in block below
					for (int ii = i+1; ii < 21 && board[ii][j] <= 0 && board[i+1][j] <= 0; ii++) { 
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
				if (board[i][j] < 0 ) {
					board[i+min][j] = board[i][j];
					if (min != 0) {
						board[i][j] = 0;
					}
					fallTime = 1;
				}
			}
		}
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
		    		
		    		if (board[i][j] < 0 ) {
				    	try {
				    		board[i+1][j] = board[i][j];
			    			board[i][j] = 0;
			    			fallTime = 1;
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
		
		@Override //movements based on key presses
		public void keyPressed(KeyEvent e) {
	    	
			// add little buddy
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (buddyMode) {
					buddyMode = false;
				}
				else {
					buddyMode = true;
				}
			}
			
			//left
    		if ((e.getKeyCode() == KeyEvent.VK_A) || (e.getKeyCode() == KeyEvent.VK_LEFT)) {
    			int testLl = 0;
    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
    					
    					if (board[i][j] < 0 ) { // check if it's moving shape
    						
    						// for 1 by 1
    						if (checkComplexShape(j, i)) {
    							testLl = 1;
    							
    							if (j != 0 && board[i][j-1] == 0) { // check if shape is touching leftmost boundary
    								
    								board[i][j-1] = board[i][j];
    								board[i][j] = 0;
    							}
    			    		}
    						
    						// for complex shapes
    	    				else {
    	    					
    	    					if (j != 0 && board[i][j-1] <= 0) { // check if shape is touching leftmost boundary
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
	    				for (int j = 0; j <= 9; j++) { //Iterates over columns (left to right)

	    					if (board[i][j] < 0 ) {
	    						
								board[i][j-1] = board[i][j];
								board[i][j] = 0;
	    					}
	    				}
    				}
    			}
    		}
	    	
    		//right
    		else if ((e.getKeyCode() == KeyEvent.VK_D)||(e.getKeyCode() == KeyEvent.VK_RIGHT)) {
	    		int testRr = 0;
	    		for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    			for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)

	    				if (board[i][j] < 0 ) { // check if it's moving shape
	    						
	    					// for 1 by 1
	    					if (checkComplexShape(j, i)) {
	    						testRr = 1;
	    						
	    						if (j != 9 && board[i][j+1] == 0) { // check if shape is touching leftmost boundary
	    							
	    							board[i][j+1] = board[i][j];
	    							board[i][j] = 0;
	    						}
	    			    	}
	    						
	    					// for complex shapes
	    	    			else {
	    	    				if (j != 9 && board[i][j+1] <= 0) { // check if shape is touching leftmost boundary
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
		    				if (board[i][j] < 0 ) {
		    					
    							board[i][j+1] = board[i][j];
    							board[i][j] = 0;
		    				}
		    			}
	    			}
	    		}
    		}
	    		
	    	
    		else if ((e.getKeyCode() == KeyEvent.VK_UP)){
    			down();
    			hard = true;
    		}
    		else if ((e.getKeyCode() == KeyEvent.VK_S)||(e.getKeyCode() == KeyEvent.VK_DOWN)) {
    			down();
	    		
	    	}
	    			
    		// rotation counterclockwise (helllllllllp)
	    	else if ((e.getKeyCode() == KeyEvent.VK_Z) || (e.getKeyCode() == KeyEvent.VK_W)) {
	    		
	    		boolean testx = true;
	    		boolean testy = true;
	    		for (int i = 20; i >= 0; i--) { 
	    			for (int j = 9; j >= 0; j--) {
	    					
	    				if (board[i][j] < 0) { //find the distance from movable block to nearest locked in block below
	    						
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
	    					if(y_values[1]+1 <= 20 && board[y_values[1]+1][x_values[2]] <= 0) {
	    						if (oppo_rotationNum == 1) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]+1][x_values[2]] = board[y_values[1]][x_values[2]];
	    							rotationNum = 4;
	    							oppo_rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[2]-1][x_values[1]] <= 0) {
	    						if (oppo_rotationNum == 3) {
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[2]-1][x_values[1]] = board[y_values[2]][x_values[1]];
	    							rotationNum = 2;
	    							oppo_rotationNum++;
	    						}
	    					}
	    						
	    					break;
	    						
	    				case('S'):
	    					if(y_values[1]+1 <= 20 && board[y_values[2]][x_values[2]] <= 0 && board[y_values[1]+1][x_values[1]] <= 0) {
	    						if (oppo_rotationNum == 1) {
	    							board[y_values[2]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[2]] = board[y_values[1]][x_values[1]];
	    							board[y_values[1]+1][x_values[1]] = board[y_values[1]][x_values[1]];
	    							rotationNum = 4;
	    							oppo_rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[1]][x_values[3]] <= 0 && board[y_values[2]-1][x_values[1]] <= 0) {
	    						if (oppo_rotationNum == 3) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[2]] = 0;
	    							board[y_values[1]][x_values[3]] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]-1][x_values[1]] = board[y_values[2]][x_values[1]];
	    							rotationNum = 2;
	    							oppo_rotationNum++;
	    						}
	    					}
	    					break;
	    						
	    				case('Z'):
	    					if(y_values[1]+1 <= 20 && board[y_values[1]][x_values[3]] <= 0 && board[y_values[1]+1][x_values[3]] <= 0) {
	    						if (oppo_rotationNum == 1) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[1]][x_values[3]] = board[y_values[1]][x_values[2]];
	    							board[y_values[1]+1][x_values[3]] = board[y_values[1]][x_values[2]];
	    							rotationNum = 4;
	    							oppo_rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[2]][x_values[1]] <= 0 && board[y_values[2]-1][x_values[1]] <= 0) {
	    						if (oppo_rotationNum == 3) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[1]] = board[y_values[2]][x_values[2]];
	    							board[y_values[2]-1][x_values[1]] = board[y_values[2]][x_values[2]];
	    							rotationNum = 2;
	    							oppo_rotationNum++;
	    						}
	    					}
	    					break;
	    					
	    				case('L'):
	    					if(y_values[1]+1 <= 20 && board[y_values[2]][x_values[2]] <= 0 && board[y_values[2]][x_values[3]] <= 0 && board[y_values[1]+1][x_values[2]] <= 0) {
	    						if (oppo_rotationNum == 1) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[2]] = board[y_values[1]][x_values[2]];
	    							board[y_values[2]][x_values[3]] = board[y_values[1]][x_values[2]];
	    							board[y_values[1]+1][x_values[2]] = board[y_values[1]][x_values[2]];
	    							rotationNum = 4;
	    							oppo_rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[1]][x_values[2]] <= 0 && board[y_values[1]][x_values[3]] <= 0 && board[y_values[2]-1][x_values[3]] <= 0) {
	    						if (oppo_rotationNum == 3) {
	    							board[y_values[2]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[2]] = board[y_values[2]][x_values[3]];
	    							board[y_values[1]][x_values[3]] = board[y_values[2]][x_values[3]];
	    							board[y_values[2]-1][x_values[3]] = board[y_values[2]][x_values[3]];
	    							rotationNum = 2;
	    							oppo_rotationNum++;
	    						}
	    					}
	    					break;
	    					
	    				case('J'):
	    					if(y_values[1]+1 <= 20 && board[y_values[2]][x_values[2]] <= 0 && board[y_values[1]+1][x_values[2]] <= 0 && board[y_values[1]+1][x_values[3]] <= 0) {
	    						if (rotationNum == 1) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[2]] = board[y_values[1]][x_values[2]];
	    							board[y_values[1]+1][x_values[2]] = board[y_values[1]][x_values[2]];
	    							board[y_values[1]+1][x_values[3]] = board[y_values[1]][x_values[2]];
	    							rotationNum = 4;
	    							oppo_rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[1]][x_values[2]] <= 0 && board[y_values[2]-1][x_values[2]] <= 0 && board[y_values[2]-1][x_values[1]] <= 0) {
	    						if (oppo_rotationNum == 3) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[1]][x_values[2]] = board[y_values[2]][x_values[2]];
	    							board[y_values[2]-1][x_values[2]] = board[y_values[2]][x_values[2]];
	    							board[y_values[2]-1][x_values[1]] = board[y_values[2]][x_values[2]];
	    							rotationNum = 2;
	    							oppo_rotationNum++;
	    						}
	    					}
	    					break;
	    			}
	    		}
	    		else if (y_values.length == 4) {
	    			switch(type) {
	    				
    					case('T'):
    						if(x_values[1]+1 <= 9 && board[y_values[2]][x_values[1]+1] <= 0) {
    							if (oppo_rotationNum == 2) {
    								board[y_values[3]][x_values[1]] = 0;
    								board[y_values[2]][x_values[1]+1] = board[y_values[2]][x_values[1]];
    								rotationNum = 3;
    								oppo_rotationNum++;
    							}
    						}
    						if(x_values[1]-1 > 0 && board[y_values[2]][x_values[1]-1] <= 0) {
    							if (oppo_rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
    								board[y_values[2]][x_values[1]-1] = board[y_values[2]][x_values[1]];
    								rotationNum = 1;
    								oppo_rotationNum = 1;
    							}
    						}
    							
    						break;
    						
    					case('S'):
    						if(x_values[1]+1 <= 9 && board[y_values[1]][x_values[2]] <= 0 && board[y_values[2]][x_values[1]+1] <= 0) {
    							if (oppo_rotationNum == 2) {
    								board[y_values[2]][x_values[2]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[1]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]][x_values[1]+1] = board[y_values[2]][x_values[1]];
	    							rotationNum = 3;
	    							oppo_rotationNum++;
    							}
    						}
    						if(x_values[2]-1 > 0 && board[y_values[2]][x_values[2]-1] <= 0 && board[y_values[3]][x_values[1]] <= 0) {
    							if (oppo_rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[2]-1] = board[y_values[2]][x_values[2]];
	    							board[y_values[3]][x_values[1]] = board[y_values[2]][x_values[2]];
	    							rotationNum = 1;
	    							oppo_rotationNum = 1;
    							}
    						}
    						break;
    							
    					case('Z'):
    						if(x_values[2]+1 <= 9 && board[y_values[1]][x_values[2]+1] <= 0 && board[y_values[1]][x_values[2]] <= 0) {
    							if (oppo_rotationNum == 2) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[1]][x_values[2]+1] = board[y_values[2]][x_values[2]];
	    							board[y_values[1]][x_values[2]] = board[y_values[2]][x_values[2]];
	    							rotationNum = 3;
	    							oppo_rotationNum++;
    							}
    						}
    						if(x_values[1]-1 > 0 && board[y_values[3]][x_values[1]-1] <= 0 && board[y_values[3]][x_values[1]] <= 0) {
    							if (oppo_rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[3]][x_values[1]-1] = board[y_values[2]][x_values[1]];
	    							board[y_values[3]][x_values[1]] = board[y_values[2]][x_values[1]];
	    							rotationNum = 1;
	    							oppo_rotationNum = 1;
    							}
    						}
    						break;
    				
    					case('L'):
    						if(x_values[1]+1 <= 9 && board[y_values[2]][x_values[2]] <= 0 && board[y_values[1]][x_values[2]] <= 0 && board[y_values[2]][x_values[1]+1] <= 0) {
    							if (oppo_rotationNum == 2) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							board[y_values[1]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]][x_values[1]+1] = board[y_values[2]][x_values[1]];
	    							rotationNum = 3;
	    							oppo_rotationNum++;
    							}
    						}
    						if(x_values[2]-1 > 0 && board[y_values[2]][x_values[1]] <= 0 && board[y_values[2]][x_values[2]-1] <= 0 && board[y_values[3]][x_values[1]] <= 0) {
    							if (oppo_rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[2]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[1]] = board[y_values[2]][x_values[2]];
	    							board[y_values[2]][x_values[2]-1] = board[y_values[2]][x_values[2]];
	    							board[y_values[3]][x_values[1]] = board[y_values[2]][x_values[2]];
	    							rotationNum = 1;
	    							oppo_rotationNum = 1;
    							}
    						}
    						break;
    						
    					case('J'):
    						if(x_values[1]+1 <= 9 && board[y_values[2]][x_values[2]] <= 0 && board[y_values[2]][x_values[1]+1] <= 0 && board[y_values[1]][x_values[1]+1] <= 0) {
    							if (oppo_rotationNum == 2) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[2]] = 0;
	    							board[y_values[3]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]][x_values[1]+1] = board[y_values[2]][x_values[1]];
	    							board[y_values[1]][x_values[1]+1] = board[y_values[2]][x_values[1]];
	    							rotationNum = 3;
	    							oppo_rotationNum++;
    							}
    						}
    						if(x_values[1]-1 > 0 && board[y_values[2]][x_values[1]-1] <= 0 && board[y_values[2]][x_values[2]] <= 0 && board[y_values[3]][x_values[1]-1] <= 0) {
    							if (oppo_rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[1]-1] = board[y_values[2]][x_values[1]];
	    							board[y_values[3]][x_values[1]-1] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							rotationNum = 1;
	    							oppo_rotationNum = 1;
    							}
    						}
    						break;
	    			}
	    		}
		    		
	    		else if (x_values.length == 5) {
	    			if (y_values[1]-2 > 0 && y_values[1]-1 > 0 && y_values[1]+1 <= 20 && board[y_values[1]-2][x_values[3]] <= 0 && board[y_values[1]-1][x_values[3]] <= 0 && board[y_values[1]+1][x_values[3]] <= 0) {
	    				if (oppo_rotationNum == 1) {
		    				board[y_values[1]-2][x_values[3]] = board[y_values[1]][x_values[3]];
			    			board[y_values[1]-1][x_values[3]] = board[y_values[1]][x_values[3]];
			    			board[y_values[1]+1][x_values[3]] = board[y_values[1]][x_values[3]];
			    			board[y_values[1]][x_values[1]] = 0;
			    			board[y_values[1]][x_values[2]] = 0;
			    			board[y_values[1]][x_values[4]] = 0;
			    			oppo_rotationNum ++;
		    			}
	    			}
	    			if (y_values[1]+2 <= 20 && y_values[1]-1 > 0 && y_values[1]+1 <= 20 && board[y_values[1]+2][x_values[2]] <= 0 && board[y_values[1]-1][x_values[2]] <= 0 && board[y_values[1]+1][x_values[2]] <= 0) {
	    				if (oppo_rotationNum == 3) {
	    					board[y_values[1]+2][x_values[2]] = board[y_values[1]][x_values[2]];
	    					board[y_values[1]-1][x_values[2]] = board[y_values[1]][x_values[2]];
	    					board[y_values[1]+1][x_values[2]] = board[y_values[1]][x_values[2]];
	    					board[y_values[1]][x_values[1]] = 0;
	    					board[y_values[1]][x_values[3]] = 0;
	    					board[y_values[1]][x_values[4]] = 0;
	    					oppo_rotationNum ++;
	    				}
	    			}
	    		}
	    		else if (y_values.length == 5) {
	    			if (oppo_rotationNum == 2) {
	    				if (x_values[1]+2 <= 9 && x_values[1]-1 > 0 && x_values[1]+1 <= 9 && board[y_values[2]][x_values[1]-1] <= 0 && board[y_values[2]][x_values[1]+1] <= 0 && board[y_values[2]][x_values[1]+2] <= 0) {
	    					board[y_values[2]][x_values[1]-1] = board[y_values[2]][x_values[1]];
			    			board[y_values[2]][x_values[1]+1] = board[y_values[2]][x_values[1]];
			    			board[y_values[2]][x_values[1]+2] = board[y_values[2]][x_values[1]];
			    			board[y_values[1]][x_values[1]] = 0;
			    			board[y_values[3]][x_values[1]] = 0;
			    			board[y_values[4]][x_values[1]] = 0;
			    			oppo_rotationNum ++;
	    				}
	    					
	    			}
	    			else if (oppo_rotationNum == 4) {
	    				if (x_values[1]-2 > 0 && x_values[1]-1 > 0 && x_values[1]+1 <= 9 && board[y_values[3]][x_values[1]+1] <= 0 && board[y_values[3]][x_values[1]-1] <= 0 && board[y_values[3]][x_values[1]-2] <= 0) {
	    					board[y_values[3]][x_values[1]+1] = board[y_values[3]][x_values[1]];
			    			board[y_values[3]][x_values[1]-1] = board[y_values[3]][x_values[1]];
			    			board[y_values[3]][x_values[1]-2] = board[y_values[3]][x_values[1]];
			    			board[y_values[1]][x_values[1]] = 0;
			    			board[y_values[2]][x_values[1]] = 0;
			    			board[y_values[4]][x_values[1]] = 0;
			    			oppo_rotationNum = 1;
	    				}
	    			}
	    		}
	    		int[] newArray = {100};
				x_values = newArray;
				y_values = newArray;
	    	}
	    	
	    	// rotation clockwise (helllllllllp)
	    	else if ((e.getKeyCode() == KeyEvent.VK_X) || (e.getKeyCode() == KeyEvent.VK_E)) {
	    		boolean testx = true;
	    		boolean testy = true;
	    		for (int i = 20; i >= 0; i--) { 
	    			for (int j = 9; j >= 0; j--) {
	    					
	    				if (board[i][j] < 0) { //find the distance from movable block to nearest locked in block below
	    						
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
	    					if(y_values[1]+1 <= 20 && board[y_values[1]+1][x_values[2]] <= 0) {
	    						if (rotationNum == 1) {
	    							board[y_values[1]][x_values[3]] = 0;
	    							board[y_values[1]+1][x_values[2]] = board[y_values[1]][x_values[2]];
	    							oppo_rotationNum = 4;
	    							rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[2]-1][x_values[1]] <= 0) {
	    						if (rotationNum == 3) {
	    							board[y_values[2]][x_values[2]] = 0;
	    							board[y_values[2]-1][x_values[1]] = board[y_values[2]][x_values[1]];
	    							oppo_rotationNum = 2;
	    							rotationNum++;
	    						}
	    					}
	    						
	    					break;
	    						
	    				case('S'):
	    					if(y_values[1]+1 <= 20 && board[y_values[1]][x_values[3]] <= 0 && board[y_values[1]+1][x_values[3]] <= 0) {
	    						if (rotationNum == 1) {
	    							board[y_values[1]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[1]][x_values[3]] = board[y_values[1]][x_values[1]];
	    							board[y_values[1]+1][x_values[3]] = board[y_values[1]][x_values[1]];
	    							oppo_rotationNum = 4;
	    							rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[2]][x_values[2]] <= 0 && board[y_values[2]-1][x_values[2]] <= 0) {
	    						if (rotationNum == 3) {
	    							board[y_values[1]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]-1][x_values[2]] = board[y_values[2]][x_values[1]];
	    							oppo_rotationNum = 2;
	    							rotationNum++;
	    						}
	    					}
	    					break;
	    						
	    				case('Z'):
	    					if(y_values[1]+1 <= 20 && board[y_values[2]][x_values[1]] <= 0 && board[y_values[1]+1][x_values[2]] <= 0) {
	    						if (rotationNum == 1) {
	    							board[y_values[2]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[1]] = board[y_values[1]][x_values[2]];
	    							board[y_values[1]+1][x_values[2]] = board[y_values[1]][x_values[2]];
	    							oppo_rotationNum = 4;
	    							rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[1]][x_values[3]] <= 0 && board[y_values[2]-1][x_values[2]] <= 0) {
	    						if (rotationNum == 3) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[2]] = 0;
	    							board[y_values[1]][x_values[3]] = board[y_values[2]][x_values[2]];
	    							board[y_values[2]-1][x_values[2]] = board[y_values[2]][x_values[2]];
	    							oppo_rotationNum = 2;
	    							rotationNum++;
	    						}
	    					}
	    					break;
	    					
	    				case('L'):
	    					if(y_values[1]+1 <= 20 && board[y_values[2]][x_values[2]] <= 0 && board[y_values[1]+1][x_values[1]] <= 0 && board[y_values[1]+1][x_values[2]] <= 0) {
	    						if (rotationNum == 1) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[2]] = board[y_values[1]][x_values[2]];
	    							board[y_values[1]+1][x_values[1]] = board[y_values[1]][x_values[2]];
	    							board[y_values[1]+1][x_values[2]] = board[y_values[1]][x_values[2]];
	    							oppo_rotationNum = 4;
	    							rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[1]][x_values[3]] <= 0 && board[y_values[2]-1][x_values[3]] <= 0 && board[y_values[2]-1][x_values[1]] <= 0) {
	    						if (rotationNum == 3) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[3]] = board[y_values[2]][x_values[3]];
	    							board[y_values[2]-1][x_values[3]] = board[y_values[2]][x_values[3]];
	    							board[y_values[2]-1][x_values[1]] = board[y_values[2]][x_values[3]];
	    							oppo_rotationNum = 2;
	    							rotationNum++;
	    						}
	    					}
	    					break;
	    					
	    				case('J'):
	    					if(y_values[1]+1 <= 9 && board[y_values[2]][x_values[1]] <= 0 && board[y_values[1]+1][x_values[2]] <= 0 && board[y_values[2]][x_values[2]] <= 0) {
	    						if (rotationNum == 1) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[2]][x_values[1]] = board[y_values[1]][x_values[2]];
	    							board[y_values[1]+1][x_values[2]] = board[y_values[1]][x_values[2]];
	    							board[y_values[2]][x_values[2]] = board[y_values[1]][x_values[2]];
	    							oppo_rotationNum = 4;
    								rotationNum++;
	    						}
	    					}
	    					if(y_values[2]-1 > 0 && board[y_values[1]][x_values[3]] <= 0 && board[y_values[2]-1][x_values[2]] <= 0 && board[y_values[1]][x_values[2]] <= 0) {
	    						if (rotationNum == 3) {
	    							board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[3]] = 0;
	    							board[y_values[1]][x_values[3]] = board[y_values[2]][x_values[2]];
	    							board[y_values[2]-1][x_values[2]] = board[y_values[2]][x_values[2]];
	    							board[y_values[1]][x_values[2]] = board[y_values[2]][x_values[2]];
	    							oppo_rotationNum = 2;
	    							rotationNum++;
	    						}
	    					}
	    					break;
	    			}
	    		}
	    		else if (y_values.length == 4) {
	    			switch(type) {
	    				
    					case('T'):
    						if(x_values[1]-1 > 0 && board[y_values[2]][x_values[1]-1] <= 0) {
    							if (rotationNum == 2) {
    								board[y_values[3]][x_values[1]] = 0;
    								board[y_values[2]][x_values[1]-1] = board[y_values[2]][x_values[1]];
    								oppo_rotationNum = 3;
    								rotationNum++;
    							}
    						}
    						if(x_values[1]+1 <= 9 && board[y_values[2]][x_values[1]+1] <= 0) {
    							if (rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
    								board[y_values[2]][x_values[1]+1] = board[y_values[2]][x_values[1]];
    								oppo_rotationNum = 1;
    								rotationNum = 1;
    							}
    						}
    							
    						break;
    						
    					case('S'):
    						if(x_values[2]-1 > 0 && board[y_values[1]][x_values[2]] <= 0 && board[y_values[1]][x_values[2]-1] <= 0) {
    							if (rotationNum == 2) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[1]][x_values[2]] = board[y_values[2]][x_values[2]];
	    							board[y_values[1]][x_values[2]-1] = board[y_values[2]][x_values[2]];
	    							oppo_rotationNum = 3;
    								rotationNum++;
    							}
    						}
    						if(x_values[1]+1 <= 9 && board[y_values[3]][x_values[1]+1] <= 0 && board[y_values[3]][x_values[1]] <= 0) {
    							if (rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[3]][x_values[1]] = board[y_values[2]][x_values[1]];
	    							board[y_values[3]][x_values[1]+1] = board[y_values[2]][x_values[1]];
	    							oppo_rotationNum = 1;
    								rotationNum = 1;
    							}
    						}
    						break;
    							
    					case('Z'):
    						if(x_values[1]-1 > 0 && board[y_values[2]][x_values[1]-1] <= 0 && board[y_values[1]][x_values[2]] <= 0) {
    							if (rotationNum == 2) {
    								board[y_values[2]][x_values[2]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[1]-1] = board[y_values[2]][x_values[1]];
	    							board[y_values[1]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							oppo_rotationNum = 3;
    								rotationNum++;
    							}
    						}
    						if(x_values[2]+1 <= 9 && board[y_values[2]][x_values[2]+1] <= 0 && board[y_values[3]][x_values[1]] <= 0) {
    							if (rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[2]+1] = board[y_values[2]][x_values[2]];
	    							board[y_values[3]][x_values[1]] = board[y_values[2]][x_values[2]];
	    							oppo_rotationNum = 1;
    								rotationNum = 1;
    							}
    						}
    						break;
    				
    					case('L'):
    						if(x_values[2]-1 > 0 && board[y_values[2]][x_values[1]] <= 0 && board[y_values[1]][x_values[2]-1] <= 0 && board[y_values[2]][x_values[1]-1] != 1) {
    							if (rotationNum == 2) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[2]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[1]] = board[y_values[2]][x_values[2]];
	    							board[y_values[1]][x_values[2]-1] = board[y_values[2]][x_values[2]];
	    							board[y_values[2]][x_values[2]-1] = board[y_values[2]][x_values[2]];
	    							oppo_rotationNum = 3;
    								rotationNum++;
    							}
    						}
    						if(x_values[2]-1 > 0 && board[y_values[2]][x_values[1]] <= 0 && board[y_values[2]][x_values[2]-1] <= 0 && board[y_values[3]][x_values[1]] <= 0) {
    							if (rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[2]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]][x_values[1]+1] = board[y_values[2]][x_values[1]];
	    							board[y_values[3]][x_values[1]+1] = board[y_values[2]][x_values[1]];
	    							oppo_rotationNum = 1;
    								rotationNum = 1;
    							}
    						}
    						break;
    						
    					case('J'):
    						if(x_values[1]-1 > 0 && board[y_values[1]][x_values[2]] <= 0 && board[y_values[2]][x_values[2]] <= 0 && board[y_values[2]][x_values[1]-1] <= 0) {
    							if (rotationNum == 2) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[3]][x_values[2]] = 0;
	    							board[y_values[3]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]][x_values[1]-1] = board[y_values[2]][x_values[1]];
	    							oppo_rotationNum = 3;
    								rotationNum++;
    							}
    						}
    						if(x_values[1]+1 > 0 && board[y_values[2]][x_values[1]+1] <= 0 && board[y_values[3]][x_values[2]] <= 0 && board[y_values[2]][x_values[2]] <= 0) {
    							if (rotationNum == 4) {
    								board[y_values[1]][x_values[1]] = 0;
	    							board[y_values[1]][x_values[2]] = 0;
	    							board[y_values[3]][x_values[1]] = 0;
	    							board[y_values[2]][x_values[1]+1] = board[y_values[2]][x_values[1]];
	    							board[y_values[3]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							board[y_values[2]][x_values[2]] = board[y_values[2]][x_values[1]];
	    							oppo_rotationNum = 1;
    								rotationNum = 1;
    							}
    						}
    						break;
	    			}
	    		}
	    		else if (x_values.length == 5) {
	    			if (y_values[1]-2 > 0 && y_values[1]-1 > 0 && y_values[1]+1 <= 20 && board[y_values[1]-2][x_values[3]] <= 0 && board[y_values[1]-1][x_values[3]] <= 0 && board[y_values[1]+1][x_values[3]] <= 0) {
	    				if (oppo_rotationNum == 1) {
		    				board[y_values[1]-2][x_values[3]] = board[y_values[1]][x_values[3]];
			    			board[y_values[1]-1][x_values[3]] = board[y_values[1]][x_values[3]];
			    			board[y_values[1]+1][x_values[3]] = board[y_values[1]][x_values[3]];
			    			board[y_values[1]][x_values[1]] = 0;
			    			board[y_values[1]][x_values[2]] = 0;
			    			board[y_values[1]][x_values[4]] = 0;
			    			oppo_rotationNum ++;
		    			}
	    			}
	    			if (y_values[1]+2 <= 20 && y_values[1]-1 > 0 && y_values[1]+1 <= 20 && board[y_values[1]+2][x_values[2]] <= 0 && board[y_values[1]-1][x_values[2]] <= 0 && board[y_values[1]+1][x_values[2]] <= 0) {
	    				if (oppo_rotationNum == 3) {
	    					board[y_values[1]+2][x_values[2]] = board[y_values[1]][x_values[2]];
	    					board[y_values[1]-1][x_values[2]] = board[y_values[1]][x_values[2]];
	    					board[y_values[1]+1][x_values[2]] = board[y_values[1]][x_values[2]];
	    					board[y_values[1]][x_values[1]] = 0;
	    					board[y_values[1]][x_values[3]] = 0;
	    					board[y_values[1]][x_values[4]] = 0;
	    					oppo_rotationNum ++;
	    				}
	    			}
	    		}
	    		else if (y_values.length == 5) {
	    			if (oppo_rotationNum == 2) {
	    				if (x_values[1]+2 <= 9 && x_values[1]-1 > 0 && x_values[1]+1 <= 9 && board[y_values[2]][x_values[1]-1] <= 0 && board[y_values[2]][x_values[1]+1] <= 0 && board[y_values[2]][x_values[1]+2] <= 0) {
	    					board[y_values[2]][x_values[1]-1] = board[y_values[2]][x_values[1]];
			    			board[y_values[2]][x_values[1]+1] = board[y_values[2]][x_values[1]];
			    			board[y_values[2]][x_values[1]+2] = board[y_values[2]][x_values[1]];
			    			board[y_values[1]][x_values[1]] = 0;
			    			board[y_values[3]][x_values[1]] = 0;
			    			board[y_values[4]][x_values[1]] = 0;
			    			oppo_rotationNum ++;
	    				}
	    					
	    			}
	    			else if (oppo_rotationNum == 4) {
	    				if (x_values[1]-2 > 0 && x_values[1]-1 > 0 && x_values[1]+1 <= 9 && board[y_values[3]][x_values[1]+1] <= 0 && board[y_values[3]][x_values[1]-1] <= 0 && board[y_values[3]][x_values[1]-2] <= 0) {
	    					board[y_values[3]][x_values[1]+1] = board[y_values[3]][x_values[1]];
			    			board[y_values[3]][x_values[1]-1] = board[y_values[3]][x_values[1]];
			    			board[y_values[3]][x_values[1]-2] = board[y_values[3]][x_values[1]];
			    			board[y_values[1]][x_values[1]] = 0;
			    			board[y_values[2]][x_values[1]] = 0;
			    			board[y_values[4]][x_values[1]] = 0;
			    			oppo_rotationNum = 1;
	    				}
	    			}
	    		}
	    		int[] newArray = {100};
				x_values = newArray;
				y_values = newArray;
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
		
		//random background color 
		Color bg = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		window.setBackground(bg); 
		game.setBackground(bg);
		
		//window settings
		window.setBounds(200, 100, 900, 720); //size
		window.setResizable(false); //no resize
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close
		window.setVisible(true); //visibility
		
		
		//add key listener for key presses
		window.addKeyListener(kHandle);

	}
}