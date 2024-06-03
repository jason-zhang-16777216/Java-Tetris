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
import java.util.Collections;
import java.util.List;
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
	
	// Time handlings
	Timer clock = new Timer();  
	static int time;
	static int fallTime;
	static int lockTime;
	
	//block basics
	static int w = 28; //Width and height. 
	static int h = 28;
	static int score = 0; // Score == Lines cleared × 10.
	static char type; //Type of block. (A, I, O, T, S, Z, L, J).
	static boolean blockChecked = false; //make sure soft drop lock fallTime is consistent
	
	static KeyHandling kHandle = new KeyHandling();//Key-press detection. 
	
	//rotation settings
	static int[] x_values = {100}; //all x values of block
	static int[] y_values = {100}; //all y values of block
	static int rotationNum = 1;
	static int oppo_rotationNum = 1;
	
	// gameover handlings (i.e. animations)
	static boolean gameOver = false;
	static int killScreenLine = 0; //the line in the kill screen (ending animation)
	
	//bag randomizer
	static char[] buddyTypes = {'A', 'I', 'O', 'T', 'S', 'Z', 'L', 'J'};
	static char[] blockTypes = {'I', 'O', 'T', 'S', 'Z', 'L', 'J'};
	static ArrayList<Character> bag = new ArrayList<Character>(); 
	static ArrayList<Character> nextBag = new ArrayList<Character>(); 
	static int bagRandom = 0;

	//colors
	static Color Z = new Color(207, 54, 22);
	static Color S = new Color(138, 234, 40);
	static Color J = new Color(0, 0, 240);
	static Color L = new Color(221, 164, 34);
	static Color O = new Color(241, 239, 47);
	static Color T = new Color(136, 44, 237);
	static Color I = new Color(0, 240, 240);
	static Color A = new Color(0,0,0);
	static Color currColor = new Color(0,0,0);
	static Color queueColor = new Color(220,220,220);
	
	static boolean hard = false; //hard drop
	
	/*
	 * little buddy is the single block, and it is not commonly included in Tetris
	 * if little buddy disrupts Tetris' normal game play
	 * we decided for the users to determine whether they want the single block present in their game
	*/
	static boolean buddyMode = false; // later on add JButton for user to control whether little buddy appears of not
	
	//constructor
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
	    	graphics.setColor(Color.LIGHT_GRAY);
	    	graphics.fillRect(40, 55+h*i, 10*w, 1); //Horizontal grid rows
	    	for (int j = 9; j >= 0; j--) { // Vertical grid columns
	    		graphics.setColor(Color.LIGHT_GRAY);
	    		graphics.fillRect(40+w*j, 57+h, 1, 20*h-2);
	    		
	    		if (board[i][j]!=0) {
	    			switch(Math.abs(board[i][j])) {
	    			//normal blocks
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
		    			
		    		// ghost blocks
		    		case 11:
		    			currColor = Color.GRAY;
		    			break;
		    		case 12:
		    			currColor = (I.darker()).darker();
		    			break;
		    		case 13:
		    			currColor = (O.darker()).darker();
		    			break;
		    		case 14:
		    			currColor = (T.darker()).darker();
		    			break;
		    		case 15:
		    			currColor = (S.darker()).darker();
		    			break;
		    		case 16:
		    			currColor = (Z.darker()).darker();
		    			break;
		    		case 17:
		    			currColor = (L.darker()).darker();
		    			break;
		    		case 18:
		    			currColor = (J.darker()).darker();
		    			break;
		    		}
	    			
	    		 	graphics.setColor(currColor);
	    		 	graphics.fillRect(40+w*j, 55+h*i, w, h); //locks the block at (i,j)
	    			graphics.setColor(Color.WHITE);
	    		 	graphics.fillRect(42+w*j, 57+h*i, w-3, h-3);
	    			graphics.setColor(currColor);
	    			graphics.fillRect(43+w*j, 58+h*i, w-5, h-5);

	    		}
	    		
		   		if(checkLine(i)) {
		    		clearLine(i);
		    	}
		   		
	    	}
	    	
		}
	    //Queue
    	for (int i = 0 ;i < 5;i++) {
    		graphics.setColor(Color.BLACK);
    		graphics.fillRect(348, 28+90*i, 164, 89);
    		graphics.setColor(queueColor);
    		graphics.fillRect(350, 30+90*i, 160, 85);
    		switch(bag.get(i)) {
			case 'A':
				graphics.setColor(A);
				graphics.fillRect(410, 55+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(411, 56+90*i, 28, 28);
				graphics.setColor(A);
				graphics.fillRect(412, 57+90*i, 26, 26);
				break;
			case 'O':
				graphics.setColor(O);
				graphics.fillRect(400, 40+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(401, 41+90*i, 28, 28);
				graphics.setColor(O);
				graphics.fillRect(402, 42+90*i, 26, 26);
				
				graphics.setColor(O);
				graphics.fillRect(430, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(431, 71+90*i, 28, 28);
				graphics.setColor(O);
				graphics.fillRect(432, 72+90*i, 26, 26);
				
				graphics.setColor(O);
				graphics.fillRect(430, 40+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(431, 41+90*i, 28, 28);
				graphics.setColor(O);
				graphics.fillRect(432, 42+90*i, 26, 26);
				
				graphics.setColor(O);
				graphics.fillRect(400, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(401, 71+90*i, 28, 28);
				graphics.setColor(O);
				graphics.fillRect(402, 72+90*i, 26, 26);
				break;
			case 'I':
				graphics.setColor(I);
				graphics.fillRect(370, 55+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(371, 56+90*i, 28, 28);
				graphics.setColor(I);
				graphics.fillRect(372, 57+90*i, 26, 26);
				
				graphics.setColor(I);
				graphics.fillRect(400, 55+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(401, 56+90*i, 28, 28);
				graphics.setColor(I);
				graphics.fillRect(402, 57+90*i, 26, 26);
				
				graphics.setColor(I);
				graphics.fillRect(430, 55+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(431, 56+90*i, 28, 28);
				graphics.setColor(I);
				graphics.fillRect(432, 57+90*i, 26, 26);
				
				graphics.setColor(I);
				graphics.fillRect(460, 55+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(461, 56+90*i, 28, 28);
				graphics.setColor(I);
				graphics.fillRect(462, 57+90*i, 26, 26);
				break;
			case 'T':
				graphics.setColor(T);
				graphics.fillRect(385, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(386, 71+90*i, 28, 28);
				graphics.setColor(T);
				graphics.fillRect(387, 72+90*i, 26, 26);
				
				graphics.setColor(T);
				graphics.fillRect(415, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(416, 71+90*i, 28, 28);
				graphics.setColor(T);
				graphics.fillRect(417, 72+90*i, 26, 26);
				
				graphics.setColor(T);
				graphics.fillRect(445, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(446, 71+90*i, 28, 28);
				graphics.setColor(T);
				graphics.fillRect(447, 72+90*i, 26, 26);
				
				graphics.setColor(T);
				graphics.fillRect(415, 40+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(416, 41+90*i, 28, 28);
				graphics.setColor(T);
				graphics.fillRect(417, 42+90*i, 26, 26);
				break;
			case 'S':
				graphics.setColor(S);
				graphics.fillRect(440, 45+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(441, 46+90*i, 28, 28);
				graphics.setColor(S);
				graphics.fillRect(442, 47+90*i, 26, 26);
				
				graphics.setColor(S);
				graphics.fillRect(410, 75+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(411, 76+90*i, 28, 28);
				graphics.setColor(S);
				graphics.fillRect(412, 77+90*i, 26, 26);
				
				graphics.setColor(S);
				graphics.fillRect(410, 45+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(411, 46+90*i, 28, 28);
				graphics.setColor(S);
				graphics.fillRect(412, 47+90*i, 26, 26);
				
				graphics.setColor(S);
				graphics.fillRect(380, 75+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(381, 76+90*i, 28, 28);
				graphics.setColor(S);
				graphics.fillRect(382, 77+90*i, 26, 26);
				break;
			case 'Z':
				graphics.setColor(Z);
				graphics.fillRect(380, 45+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(381, 46+90*i, 28, 28);
				graphics.setColor(Z);
				graphics.fillRect(382, 47+90*i, 26, 26);
				
				graphics.setColor(Z);
				graphics.fillRect(410, 75+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(411, 76+90*i, 28, 28);
				graphics.setColor(Z);
				graphics.fillRect(412, 77+90*i, 26, 26);
				
				graphics.setColor(Z);
				graphics.fillRect(410, 45+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(411, 46+90*i, 28, 28);
				graphics.setColor(Z);
				graphics.fillRect(412, 47+90*i, 26, 26);
				
				graphics.setColor(Z);
				graphics.fillRect(440, 75+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(441, 76+90*i, 28, 28);
				graphics.setColor(Z);
				graphics.fillRect(442, 77+90*i, 26, 26);
				break;
			case 'J':
				graphics.setColor(J);
				graphics.fillRect(385, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(386, 71+90*i, 28, 28);
				graphics.setColor(J);
				graphics.fillRect(387, 72+90*i, 26, 26);
				
				graphics.setColor(J);
				graphics.fillRect(415, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(416, 71+90*i, 28, 28);
				graphics.setColor(J);
				graphics.fillRect(417, 72+90*i, 26, 26);
				
				graphics.setColor(J);
				graphics.fillRect(445, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(446, 71+90*i, 28, 28);
				graphics.setColor(J);
				graphics.fillRect(447, 72+90*i, 26, 26);
				
				graphics.setColor(J);
				graphics.fillRect(385, 40+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(386, 41+90*i, 28, 28);
				graphics.setColor(J);
				graphics.fillRect(387, 42+90*i, 26, 26);
				break;
			case 'L':
				graphics.setColor(L);
				graphics.fillRect(385, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(386, 71+90*i, 28, 28);
				graphics.setColor(L);
				graphics.fillRect(387, 72+90*i, 26, 26);
				
				graphics.setColor(L);
				graphics.fillRect(415, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(416, 71+90*i, 28, 28);
				graphics.setColor(L);
				graphics.fillRect(417, 72+90*i, 26, 26);
				
				graphics.setColor(L);
				graphics.fillRect(445, 70+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(446, 71+90*i, 28, 28);
				graphics.setColor(L);
				graphics.fillRect(447, 72+90*i, 26, 26);
				
				graphics.setColor(L);
				graphics.fillRect(445, 40+90*i, 30, 30);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(446, 41+90*i, 28, 28);
				graphics.setColor(L);
				graphics.fillRect(447, 42+90*i, 26, 26);
				break;
    				
    		}
    		
    	}
	    
	    // print score
	    graphics.setColor(Color.WHITE);
	    graphics.setFont(new Font("Arial", Font.BOLD, 50));
	    for (int xo = -2; xo <= 2; xo++) {
            for (int yo = -2; yo <= 2; yo++) {
                if (xo != 0 || yo != 0) {
                	graphics.drawString("SCORE: " + score, 550 + xo, 75 + yo);//outline
                }
            }
        }
	    graphics.setColor(Color.BLACK);
	    graphics.drawString("SCORE: " + score, 550, 75); //draw text
	    
	    // print time
	    graphics.setColor(Color.WHITE);
	    for (int xo = -2; xo <= 2; xo++) {
            for (int yo = -2; yo <= 2; yo++) {
                if (xo != 0 || yo != 0) {
                	graphics.drawString("TIME: " + time/100, 550 + xo, 175 + yo);//outline
                }
            }
        }
	    graphics.setColor(Color.BLACK);
	    graphics.drawString("TIME: " + time/100, 550, 175); //draw text
	    
	    // check if little buddy mode is on and inform user
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
	    	//System.out.println('t');
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
			
			if (board[0][i] > 0 || board[0][i] < -10 ) { //Ends game if a piece is placed above the board.
				
				gameOver = true;
			}
		}
	}
	public static void getBlock() { // Chooses a random block type and then adds it to the board.
		if(gameOver == true) {
			return;
		}
		
		rotationNum = 1; // Sets the rotation state of the current block to the default state.
		oppo_rotationNum = 1; 
		
		type = bag.get(0);
		bag.remove(0);
		try {
			bag.add(nextBag.get(0));
			nextBag.remove(0);
		}
		catch(Exception e) {
			if(!buddyMode) {
				for (char block : blockTypes) {
		            nextBag.add(block);
		        }
				Collections.shuffle(nextBag);
			} else {
				for (char block : buddyTypes) {
		            nextBag.add(block);
				}
				Collections.shuffle(nextBag);
			}
			bag.add(nextBag.get(0));
			nextBag.remove(0);
		}
		
		
		switch(type) { ///////// value of number controls color, sign (pos or neg) controls locked-ness.
			case('A'):
				board[0][5] = -1; // □□□□□■□□□□ 
				board[0+down()][5] = -11; // □□□□□■□□□□ 
				break;
			case('I'):
				board[0][3] = -2; // □□□■■■■□□□
				board[0][4] = -2; // □□□□□□□□□□
				board[0][5] = -2;
				board[0][6] = -2;
				
				board[0+down()][3] = -12; // □□□■■■■□□□
				board[0+down()][4] = -12; // □□□□□□□□□□
				board[0+down()][5] = -12;
				board[0+down()][6] = -12;
				break;
			
			case('O'):
				board[1][4] = -3; // □□□□■■□□□□
				board[0][4] = -3; // □□□□■■□□□□
				board[0][5] = -3;
				board[1][5] = -3;
				
				board[1+down()][4] = -13; // □□□□■■□□□□
				board[0+down()][4] = -13; // □□□□■■□□□□
				board[0+down()][5] = -13;
				board[1+down()][5] = -13;
				
				break;
				
			case('T'):
				board[0][4] = -4; // □□□□■□□□□□
				board[1][3] = -4; // □□□■■■□□□□
				board[1][4] = -4;
				board[1][5] = -4;
				
				board[0+down()][4] = -14; // □□□□□■□□□□
				board[1+down()][3] = -14; // □□□□■■■□□□
				board[1+down()][4] = -14;
				board[1+down()][5] = -14;
				
				break;
				
			case('S'):
				board[0][5] = -5; // □□□□■■□□□□
				board[0][4] = -5; // □□□■■□□□□□
				board[1][4] = -5;
				board[1][3] = -5;
				
				board[0+down()][5] = -15; // □□□□■■□□□□
				board[0+down()][4] = -15; // □□□■■□□□□□
				board[1+down()][4] = -15;
				board[1+down()][3] = -15;
				
				break;
			
			case('Z'):
				board[1][5] = -6; // □□□■■□□□□□
				board[1][4] = -6; // □□□□■■□□□□
				board[0][4] = -6;
				board[0][3] = -6;
				
				board[1+down()][5] = -16; // □□□■■□□□□□
				board[1+down()][4] = -16; // □□□□■■□□□□
				board[0+down()][4] = -16;
				board[0+down()][3] = -16;
				
				break;
				
			case('L'):
				board[0][5] = -7; // □□□□□■□□□□
				board[1][3] = -7; // □□□■■■□□□□
				board[1][4] = -7;
				board[1][5] = -7;
				
				board[0+down()][5] = -17; // □□□□□■□□□□
				board[1+down()][3] = -17; // □□□■■■□□□□
				board[1+down()][4] = -17;
				board[1+down()][5] = -17;
				
				break;
				
			case('J'):
				board[0][3] = -8; // □□□■□□□□□□
				board[1][3] = -8; // □□□■■■□□□□
				board[1][4] = -8;
				board[1][5] = -8;
				
				board[0+down()][3] = -18; // □□□■□□□□□□
				board[1+down()][3] = -18; // □□□■■■□□□□
				board[1+down()][4] = -18;
				board[1+down()][5] = -18;
		}
	}
	public static void checkReachBottom() { //check if block reached the bottom and lock piece
		
		for (int i = 0; i < 20; i++) { //Iterates over rows (top to bottom)
			for (int j = 9; j >= 0; j--) { // Iterates over columns (right to left)
				
				if ((board[i][j] < 0 && board[i][j] > -10 && board[i + 1][j] > 0) || (board[20][j] < 0 && board[20][j] > -10 && i == 0)) { //if block is stacking on a locked block or at the bottom
			    	
					if (blockChecked == false) { //make sure soft drop lock fallTime is consistent
						lockTime++;
						blockChecked = true;
					}
			    	if ((lockTime % 50 == 0)||(hard)) {
			    		for (int ii = 0; ii <= 20; ii++) { //Iterates over rows (top to bottom)
			    			for (int jj = 9; jj >= 0; jj--) { // Iterates over columns (right to left)
			    				if (board[ii][jj] < 0 && board[ii][jj] > -10) {
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
	public static int down() {
		int d = 0; //distance to move block down by
		int all_d[] = {100};
		for (int i = 20; i >= 0; i--) { //Iterates over rows (bottom to top)
			for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)
					
				if (board[i][j] < 0 && board[i][j] > -10) { //find the distance from movable block to nearest locked in block below
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
		return min;
	}
	public static void moveGhostBlock() { // move ghost blocks
		for (int i = 20; i >= 0; i--) { 
			for (int j = 9; j >= 0; j--) {
				if (board[i][j] < -10 && i >= 1) {
					board[i][j] = 0;
				}
			}
		}
		for (int i = 20; i >= 0; i--) { 
			for (int j = 9; j >= 0; j--) {
				if (board[i][j] < 0 && board[i][j] > -10 && i+down() >= 1) {
					if (board[i+down()][j] == 0) {
						board[i+down()][j] = board[i][j] - 10;
					}
				}
			}
		}
	}
	public void actionPerformed(ActionEvent e) {
		fallTime ++;
		time++;	
		checkReachBottom();
		System.out.println(fallTime + " " + lockTime + " " + hard);
		
		//block moves down slowly
		if (fallTime % 50 == 0) {

			for (int i = 20; i >= 0; i--) { //Iterates over rows (bottom to top)
		    	for (int j = 9; j >= 0; j--) { // Iterates over columns (right to left)
		    		checkReachBottom();
		    		
		    		if (board[i][j] < 0 && board[i][j] > -10 ) {
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
		endGame();
		repaint();
	}
	
	public static final class KeyHandling implements KeyListener{
		
		@Override //movements based on key presses
		public void keyPressed(KeyEvent event) {
	    	
			// add little buddy
			if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				if (buddyMode) {
					buddyMode = false;
				}
				else {
					buddyMode = true;
				}
			}
			
			//left
    		if ((event.getKeyCode() == KeyEvent.VK_A) || (event.getKeyCode() == KeyEvent.VK_LEFT)) {
    			int testLl = 0;
    			for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
    				for (int j = 0; j <= 9; j++) { //Iterates over columns (right to left)
    					
    					if (board[i][j] < 0 && board[i][j] > -10) { // check if it's moving shape
    						
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

	    					if (board[i][j] < 0 && board[i][j] > -10) {
	    						
								board[i][j-1] = board[i][j];
								board[i][j] = 0;
	    					}
	    				}
    				}
    			}
    			// move ghost blocks
    			moveGhostBlock();
    		}
	    	
    		//right
    		else if ((event.getKeyCode() == KeyEvent.VK_D)||(event.getKeyCode() == KeyEvent.VK_RIGHT)) {
	    		int testRr = 0;
	    		for (int i = 0; i <= 20; i++) { //Iterates over rows (top to bottom)
	    			for (int j = 9; j >= 0; j--) { //Iterates over columns (right to left)

	    				if (board[i][j] < 0 && board[i][j] > -10) { // check if it's moving shape
	    						
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
		    				if (board[i][j] < 0 && board[i][j] > -10) {
		    					
    							board[i][j+1] = board[i][j];
    							board[i][j] = 0;
		    				}
		    			}
	    			}
	    		}
	    		
	    		// move ghost blocks
    			moveGhostBlock();
    		}
	    		
	    	//hard drops
    		else if ((event.getKeyCode() == KeyEvent.VK_UP) || (event.getKeyCode() == KeyEvent.VK_SPACE)){
    			
    			int min = down();
    			
    			// move all movable blocks down by min
    			for (int i = 20; i >= 0; i--) { 
    				for (int j = 9; j >= 0; j--) {
    					if (board[i][j] < 0 && board[i][j] > -10) {
    						board[i+min][j] = board[i][j];
    						if (min != 0) {
    							board[i][j] = 0;
    						}
    						fallTime = 1;
    					}
    				}
    			}
    			hard = true;
    		}
    		//soft drops
    		else if ((event.getKeyCode() == KeyEvent.VK_S)||(event.getKeyCode() == KeyEvent.VK_DOWN)) {
    			int min = down();
    			
    			// move all movable blocks down by min
    			for (int i = 20; i >= 0; i--) {
    				for (int j = 9; j >= 0; j--) {
    					if (board[i][j] < 0 && board[i][j] > -10) {
    						board[i+min][j] = board[i][j];
    						//System.out.println(down());
    						if (min != 0) {
    							board[i][j] = 0;
    						}
    						fallTime = 1;
    					}
    				}
    			}
	    	}
	    			
    		// rotation counterclockwise (helllllllllp)
	    	else if ((event.getKeyCode() == KeyEvent.VK_Z) || (event.getKeyCode() == KeyEvent.VK_W)) {
	    		
	    		boolean testx = true;
	    		boolean testy = true;
	    		for (int i = 20; i >= 0; i--) { 
	    			for (int j = 9; j >= 0; j--) {
	    					
	    				if (board[i][j] < 0 && board[i][j] > -10) { //find the distance from movable block to nearest locked in block below
	    						
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
	    			if (y_values[1]-2 >= 0 && y_values[1]-1 >= 0 && y_values[1]+1 <= 20 && board[y_values[1]-2][x_values[3]] <= 0 && board[y_values[1]-1][x_values[3]] <= 0 && board[y_values[1]+1][x_values[3]] <= 0) {
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
	    			if (y_values[1]+2 <= 20 && y_values[1]-1 >= 0 && y_values[1]+1 <= 20 && board[y_values[1]+2][x_values[2]] <= 0 && board[y_values[1]-1][x_values[2]] <= 0 && board[y_values[1]+1][x_values[2]] <= 0) {
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
	    				if (x_values[1]+2 <= 9 && x_values[1]-1 >= 0 && x_values[1]+1 <= 9 && board[y_values[2]][x_values[1]-1] <= 0 && board[y_values[2]][x_values[1]+1] <= 0 && board[y_values[2]][x_values[1]+2] <= 0) {
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
	    				if (x_values[1]-2 >= 0 && x_values[1]-1 >= 0 && x_values[1]+1 <= 9 && board[y_values[3]][x_values[1]+1] <= 0 && board[y_values[3]][x_values[1]-1] <= 0 && board[y_values[3]][x_values[1]-2] <= 0) {
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
				
				// move ghost blocks
    			moveGhostBlock();
	    	}
	    	
	    	// rotation clockwise (helllllllllp)
	    	else if ((event.getKeyCode() == KeyEvent.VK_X) || (event.getKeyCode() == KeyEvent.VK_E)) {
	    		boolean testx = true;
	    		boolean testy = true;
	    		for (int i = 20; i >= 0; i--) { 
	    			for (int j = 9; j >= 0; j--) {
	    					
	    				if (board[i][j] < 0 && board[i][j] > -10) { //find the distance from movable block to nearest locked in block below
	    						
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
	    			if (y_values[1]-2 >= 0 && y_values[1]-1 >= 0 && y_values[1]+1 <= 20 && board[y_values[1]-2][x_values[3]] <= 0 && board[y_values[1]-1][x_values[3]] <= 0 && board[y_values[1]+1][x_values[3]] <= 0) {
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
	    			if (y_values[1]+2 <= 20 && y_values[1]-1 >= 0 && y_values[1]+1 <= 20 && board[y_values[1]+2][x_values[2]] <= 0 && board[y_values[1]-1][x_values[2]] <= 0 && board[y_values[1]+1][x_values[2]] <= 0) {
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
	    				if (x_values[1]+2 <= 9 && x_values[1]-1 >= 0 && x_values[1]+1 <= 9 && board[y_values[2]][x_values[1]-1] <= 0 && board[y_values[2]][x_values[1]+1] <= 0 && board[y_values[2]][x_values[1]+2] <= 0) {
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
	    				if (x_values[1]-2 >= 0 && x_values[1]-1 >= 0 && x_values[1]+1 <= 9 && board[y_values[3]][x_values[1]+1] <= 0 && board[y_values[3]][x_values[1]-1] <= 0 && board[y_values[3]][x_values[1]-2] <= 0) {
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
				
				// move ghost blocks
    			moveGhostBlock();
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

	public static void start() {
		for (char block : blockTypes) {
            bag.add(block);
            nextBag.add(block);
            Collections.shuffle(bag);
            Collections.shuffle(nextBag);
        }
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
		window.setBounds(200, 5, 900, 720); //size
		window.setResizable(false); //no resize
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close
		window.setVisible(true); //visibility
		
		
		//add key listener for key presses
		window.addKeyListener(kHandle);

	}
}