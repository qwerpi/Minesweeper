import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class MinesGame extends Canvas implements MouseListener, KeyListener {

	int[][] revealed;
	int[][] board;
	int size = 30;
	boolean firstClick = true;
	int backup = 0;
	boolean lose = false;
	int width, height, mines, marked;
	int pressedX, pressedY;

	public MinesGame(int width, int height, int mines) throws IllegalArgumentException {
		this.width = width;
		this.height = height;
		this.mines = mines;
		if (mines > width * height - 1)
			throw new IllegalArgumentException("Too many mines specified.");
		else if (width < 0 || height < 0)
			throw new IllegalArgumentException("Board cannot have negative size.");
		else if (mines < 0)
			throw new IllegalArgumentException("Cannot have negative mines.");

		initArrays(width, height, mines);

		setPreferredSize(new Dimension(width * size, height * size + 50));
		addMouseListener(this);
		addKeyListener(this);
	}

	public void initArrays(int width, int height, int mines) {
		board = new int[width][height];
		revealed = new int[width][height];

		lose = false;
		firstClick = true;

		int[] coords = new int[width * height];
		for (int i = 0; i < coords.length; i++) {
			coords[i] = i;
		}
		for (int i = 0; i < coords.length; i++) {
			int j = (int) (Math.random() * coords.length);
			int temp = coords[i];
			coords[i] = coords[j];
			coords[j] = temp;
		}

		for (int i = 0; i < mines; i++) {
			board[coords[i] % width][coords[i] / width] = -1;
		}
		backup = coords[mines];

		for (int i = 0; i < revealed.length; i++) {
			for (int j = 0; j < revealed[i].length; j++) {
				// revealed[i][j] = true;
			}
		}
	}

	public int neighborMines(int x, int y) {
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

	public int neighborsMarked(int x, int y) {
		int num = 0;
		if (x > 0) {
			if (y > 0)
				num += (revealed[x - 1][y - 1] == -1) ? 1 : 0;
			num += (revealed[x - 1][y] == -1) ? 1 : 0;
			if (y < revealed[0].length - 1)
				num += (revealed[x - 1][y + 1] == -1) ? 1 : 0;
		}
		if (y > 0)
			num += (revealed[x][y - 1] == -1) ? 1 : 0;
		if (y < revealed[0].length - 1)
			num += (revealed[x][y + 1] == -1) ? 1 : 0;
		if (x < revealed.length - 1) {
			if (y > 0)
				num += (revealed[x + 1][y - 1] == -1) ? 1 : 0;
			num += (revealed[x + 1][y] == -1) ? 1 : 0;
			if (y < revealed[0].length - 1)
				num += (revealed[x + 1][y + 1] == -1) ? 1 : 0;
		}
		return num;
	}

	public void reveal(int x, int y) {
		if (x < 0 || y < 0 || x >= board.length || y >= board[0].length || revealed[x][y] != 0)
			return;
		revealed[x][y] = 1;
		if (board[x][y] == -1 && !firstClick) {
			lose = true;
			return;
		} else if (board[x][y] == -1) {
			board[x][y] = 0;
			board[backup % board.length][backup / board.length] = -1;
			reveal(x, y);
		}
		board[x][y] = neighborMines(x, y);
		if (board[x][y] == 0) {
			reveal(x - 1, y - 1);
			reveal(x - 1, y);
			reveal(x - 1, y + 1);
			reveal(x, y - 1);
			reveal(x, y + 1);
			reveal(x + 1, y - 1);
			reveal(x + 1, y);
			reveal(x + 1, y + 1);
		}
	}

	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		g.drawString("Total Mines: " + mines, 20, 40);
		g.drawString("Mines Marked: " + marked, getWidth() / 2, 40);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				g.setColor(Color.black);
				g.drawRoundRect(i * size, j * size + 50, size, size, 6, 6);
				if (revealed[i][j] == 1) {
					g.setColor(new Color(190, 190, 190));
					g.fillRect(i * size + 1, j * size + 51, size - 1, size - 1);
					if (board[i][j] > 0) {
						g.setColor(Color.black);
						switch(board[i][j]) {
						case 1:
							g.setColor(Color.BLUE);
							break;
						case 2:
							g.setColor(new Color(0, 128, 0));
							break;
						case 3:
							g.setColor(Color.RED);
							break;
						case 4:
							g.setColor(new Color(128, 0, 128));
							break;
						case 5:
							g.setColor(new Color(128, 0, 0));
							break;
						case 6:
							g.setColor(new Color(64, 224, 208));
							break;
						case 7:
							g.setColor(Color.black);
							break;
						case 8:
							g.setColor(Color.gray);
							break;
						}
						drawStringCentered(g, board[i][j] + "", i * size + 1, j * size + 51, size - 1, size - 1);
					}
				} else if (revealed[i][j] == -1) {
					if (lose && board[i][j] != -1)
						g.setColor(new Color(120, 0, 0));
					else
						g.setColor(Color.red);
					g.fillRect(i * size + 1, j * size + 51, size - 1, size - 1);
				}
				if (lose && board[i][j] < 0 && revealed[i][j] != -1) {
					g.setColor(Color.black);
					g.fillRect(i * size + 1, j * size + 51, size - 1, size - 1);
				}
			}
		}
	}

	public void drawStringCentered(Graphics2D g, String s, int x, int y, int w, int h) {
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, h - 8));
		FontRenderContext frc = g.getFontRenderContext();
		Rectangle2D bounds = g.getFont().getStringBounds(s, frc);
		int dx = (int) ((w - bounds.getWidth()) / 2);
		int dy = (int) ((h + bounds.getHeight()) / 2);
		g.drawString(s, x + dx, y + dy);
	}

	public void paint(Graphics g) {
		g.setColor(new Color(240, 240, 240));
		g.fillRect(0, 0, getWidth(), getHeight());
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paint(g2);
	}

	public void update(Graphics g) {
		Graphics offgc;
		Image offscreen = null;
		Dimension d = getSize();
		offscreen = createImage(d.width, d.height);
		offgc = offscreen.getGraphics();
		offgc.setColor(getBackground());
		offgc.fillRect(0, 0, d.width, d.height);
		offgc.setColor(getForeground());
		paint(offgc);
		g.drawImage(offscreen, 0, 0, this);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (lose)
			return;
		int x = (e.getX() / size);
		int y = ((e.getY() - 50) / size);
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (revealed[x][y] == -1)
				return;
			if (revealed[x][y] == 1 && board[x][y] == neighborsMarked(x, y)) {
				reveal(x - 1, y - 1);
				reveal(x - 1, y);
				reveal(x - 1, y + 1);
				reveal(x, y - 1);
				reveal(x, y + 1);
				reveal(x + 1, y - 1);
				reveal(x + 1, y);
				reveal(x + 1, y + 1);
			} else {
				reveal(x, y);
			}
			firstClick = false;
		} else {
			if (revealed[x][y] == 0) {
				revealed[x][y] = -1;
				marked++;
			} else if (revealed[x][y] == -1) {
				revealed[x][y] = 0;
				marked--;
			}
		}
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F5) {
			initArrays(width, height, mines);
			repaint();
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent e) {
	}
}
