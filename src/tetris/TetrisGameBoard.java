package tetris;
import javax.swing.JPanel;

public class TetrisGameBoard extends JPanel{
	public int[][] board = {  
			{1,0,0,0,0,0,0,0,0,0},//20 index 0
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
			{0,0,0,0,0,1,1,0,0,0},//4  index 16
			{0,1,0,1,0,0,0,0,0,0},//3  index 17
			{1,0,0,0,0,0,0,0,0,0},//2  index 18
			{1,1,1,1,1,1,1,1,1,1} //1  index 19
	};
	public TetrisGameBoard() {
		//draw the game board here
	}
	public void clearLine(int line) {
		for (var i = 20-line; i > 0; i--) {
			for (var j = 0; j < 10; j++) {
				board[i][j] = board[i-1][j];
			}
		}
		for (var i = 0;i<10;i++) {
			board[0][i] = 0;
		}
	}

}
