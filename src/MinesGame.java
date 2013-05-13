import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MinesGame extends Canvas implements MouseListener {

	boolean[][] revealed;
	int[][] mines;
	
	public MinesGame(int width, int height, int mines) throws IllegalArgumentException {
		if (mines > width * height)
			throw new IllegalArgumentException("Too many mines specified.");
		else if (width < 0 || height < 0)
			throw new IllegalArgumentException("Board cannot have negative size.");
		else if (mines < 0)
			throw new IllegalArgumentException("Cannot have negative mines.");
		
		initArrays(width, height, mines);
		
		setPreferredSize(new Dimension(800, 600));
	}
	
	public void initArrays(int width, int height, int mines) {
		
	}
	
	public void paint(Graphics2D g) {
		
	}
	
	public void paint(Graphics g) {
		paint((Graphics2D)g);
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

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
}
