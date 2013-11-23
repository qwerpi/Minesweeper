import java.awt.Point;
import java.util.ArrayList;


public class MinesAI {
	
	private boolean debug = false;
	private MinesGame game;
	private int[][] board;
	/*
	 * Key for board representation
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
	
	// used to actually play the game
	// for now, just call the provided method in MinesGame, but this could
	// eventually use java.awt.Robot to play other minesweeper games
	public void click(int x, int y) {
		game.click(x, y);
	}

	// used to actually play the game
	// for now, just call the provided method in MinesGame, but this could
	// eventually use java.awt.Robot to play other minesweeper games
	public void flag(int x, int y) {
		game.flag(x, y);
	}
	
	// count number of neighbors flagged
	public int neighborsFlagged(int x, int y) {
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
	
	// count number of neighbors empty
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
	
	// mark all empty neighbors using flag()
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
	
	// update the board and make moves
	// if it cannot make an intelligent move, choose a random unmarked cell
	public void move() {
		updateBoard();
		
		boolean obv = false;
		
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				if (board[x][y] > 0 && board[x][y] == neighborsFlagged(x, y) && neighborsEmpty(x, y) > 0) {
					if (debug) {
						System.out.println("===Doing Actual Things===");
						System.out.printf("Expanding (%2d, %2d)\n", x, y);
					}
					click(x, y);
					updateBoard();
					obv = true;
				} else if (board[x][y] > 0 && board[x][y] == (neighborsEmpty(x, y) + neighborsFlagged(x, y)) && neighborsEmpty(x, y) > 0) {
					if (debug) {
						System.out.println("===Doing Actual Things===");
						System.out.printf("Marking empty neighbors for (%2d, %2d)\n", x, y);
						System.out.println("\tNumber = " + board[x][y]);
						System.out.println("\tEmpty = " + neighborsEmpty(x, y) + "\tMarked = " + neighborsFlagged(x, y));
						printBoard();
					}
					markAllEmptyNeighbors(x, y);
					updateBoard();
					obv = true;
				}
			}
		}
		
		if (obv)
			return;
		
		if (true)	return;
		
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
	
	// for debugging
	private void printBoard() {
		if (!debug)
			return;
		for (int j = 0; j < board[0].length; j++) {
			for (int i = 0; i < board.length; i++) {
				System.out.printf("%2d", board[i][j]);
			}
			System.out.println();
		}
	}
	
	// make moves until game ends
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
