import java.awt.Point;
import java.util.ArrayList;


public class MinesAI {
	
	private boolean debug = false;
	private MinesGame game;
	private int[][] board;
	/*
	 * Key
	 *  -2 : unmarked
	 *  -1 : flagged
	 * 0-9 : revealed
	 */
	
	public MinesAI(MinesGame game) {
		this.game = game;
	}
	
	public void updateBoard() {
		board = game.getBoard();
	}
	
	public void click(int x, int y) {
		game.click(x, y);
	}
	
	public void flag(int x, int y) {
		game.flag(x, y);
	}
	
	public int neighborsMarked(int x, int y) {
		int num = 0;
		if (x > 0) {
			if (y > 0)
				num += (board[x - 1][y - 1] == -1) ? 1 : 0;
			num += (board[x - 1][y] == -1) ? 1 : 0;
			if (y < board[0].length - 1)
				num += (board[x - 1][y + 1] == -1) ? 1 : 0;
		}
		if (y > 0)
			num += (board[x][y - 1] == -1) ? 1 : 0;
		if (y < board[0].length - 1)
			num += (board[x][y + 1] == -1) ? 1 : 0;
		if (x < board.length - 1) {
			if (y > 0)
				num += (board[x + 1][y - 1] == -1) ? 1 : 0;
			num += (board[x + 1][y] == -1) ? 1 : 0;
			if (y < board[0].length - 1)
				num += (board[x + 1][y + 1] == -1) ? 1 : 0;
		}
		return num;
	}
	
	public int neighborsEmpty(int x, int y) {
		int num = 0;
		if (x > 0) {
			if (y > 0)
				num += (board[x - 1][y - 1] == -2) ? 1 : 0;
			num += (board[x - 1][y] == -2) ? 1 : 0;
			if (y < board[0].length - 1)
				num += (board[x - 1][y + 1] == -2) ? 1 : 0;
		}
		if (y > 0)
			num += (board[x][y - 1] == -2) ? 1 : 0;
		if (y < board[0].length - 1)
			num += (board[x][y + 1] == -2) ? 1 : 0;
		if (x < board.length - 1) {
			if (y > 0)
				num += (board[x + 1][y - 1] == -2) ? 1 : 0;
			num += (board[x + 1][y] == -2) ? 1 : 0;
			if (y < board[0].length - 1)
				num += (board[x + 1][y + 1] == -2) ? 1 : 0;
		}
		return num;
	}
	
	public void markAllEmptyNeighbors(int x, int y) {
		if (x > 0) {
			if (y > 0)
				if (board[x - 1][y - 1] == -2) flag(x - 1, y - 1);
			if (board[x - 1][y] == -2) flag(x - 1, y);
			if (y < board[0].length - 1)
				if (board[x - 1][y + 1] == -2) flag(x - 1, y + 1);
		}
		if (y > 0)
			if (board[x][y - 1] == -2) flag(x, y - 1);
		if (y < board[0].length - 1)
			if (board[x][y + 1] == -2) flag(x, y + 1);
		if (x < board.length - 1) {
			if (y > 0)
				if (board[x + 1][y - 1] == -2) flag(x + 1, y - 1);
			if (board[x + 1][y] == -2) flag(x + 1, y);
			if (y < board[0].length - 1)
				if (board[x + 1][y + 1] == -2) flag(x + 1, y + 1);
		}
	}
	
	public void move() {
		updateBoard();
		
		boolean obv = false;
		
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				if (board[x][y] > 0 && board[x][y] == neighborsMarked(x, y) && neighborsEmpty(x, y) > 0) {
					if (debug) System.out.println("===Doing Actual Things===");
					if (debug) System.out.printf("Expanding (%2d, %2d)\n", x, y);
					click(x, y);
					updateBoard();
					obv = true;
				} else if (board[x][y] > 0 && board[x][y] == (neighborsEmpty(x, y) + neighborsMarked(x, y)) && neighborsEmpty(x, y) > 0) {
					if (debug) System.out.println("===Doing Actual Things===");
					if (debug) System.out.printf("Marking empty neighbors for (%2d, %2d)\n", x, y);
					/*if (debug) System.out.println("\tNumber = " + board[x][y]);
					if (debug) System.out.println("\tEmpty = " + neighborsEmpty(x, y) + "\tMarked = " + neighborsMarked(x, y));
					printBoard();*/
					markAllEmptyNeighbors(x, y);
					updateBoard();
					obv = true;
				}
			}
		}
		
		if (obv)
			return;
		
		if (debug) System.out.println("===Random===");
		ArrayList<Point> unmarked = new ArrayList<Point>();
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				if (board[x][y] == -2)
					unmarked.add(new Point(x, y));
			}
		}
		Point p = unmarked.get((int)(Math.random()*unmarked.size()));
		int x = p.x;
		int y = p.y;
		if (debug) System.out.printf("Clicked (%2d, %2d)\n", x, y);
		click(x, y);
	}
	
	public void printBoard() {
		for (int j = 0; j < board[0].length; j++) {
			for (int i = 0; i < board.length; i++) {
				if (debug) System.out.printf("%2d", board[i][j]);
			}
			if (debug) System.out.println();
		}
	}
	
	public void play(int delay) {
		while(!game.win && !game.lose) {
			try {
				move();
				Thread.sleep(delay);
			} catch(Exception ex) {
				System.out.println(ex);
			}
		}
	}
}
