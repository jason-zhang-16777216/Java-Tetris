package tetris;
import javax.swing.JFrame;

public class Main extends JFrame{
	static TetrisGameBoard tetris = new TetrisGameBoard();
	public Main() {
		setTitle("Tetris");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false); //CURRENTLY SET TO FALSE !!!
        setResizable(false);
        add(tetris);
	}
	public static void main(String[] args) {
		Main main = new Main();
		tetris.clearLine(20);
		for (var i = 0; i <20; i++) {
			for (var j = 0; j < 10; j++) {
				System.out.print(main.tetris.board[i][j]+" ");
			}
		System.out.println();
		}
	}

}
