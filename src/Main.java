import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;


public class Main {
	
	private static boolean display = true;
	private static boolean ai = true;

	public static void main(String[] args) {
		MinesGame game = new MinesGame(9, 9, 10);	// easy
		//MinesGame game = new MinesGame(16, 16, 40);	// medium
		//MinesGame game = new MinesGame(30, 16, 99);	// hard
		
		if (display) {
			JFrame frame = new JFrame("Minesweeper");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(game);
			frame.pack();
			// uncomment to center the JFrame
			// In xfce on multiple monitors, this behaves as though there were a single monitor,
			// which puts the frame in between them. It properly centers the frame in the primary
			// monitor in Windows. Not sure about others window managers.
			// Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			// frame.setLocation((screen.width - frame.getWidth()) / 2, (screen.height - frame.getHeight()) / 2);
			frame.setVisible(true);
		}

		if (ai) {
			// count wins
			MinesAI ai = new MinesAI(game);
			int wins = 0;
			for (int i = 0; i < 100; i++) {
				if (i % 100 == 0)
					System.out.println(i);
				ai.play(display ? 50 : 0);
				if (game.win)
					wins++;
				if (display) {
					try {
						Thread.sleep(500);
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
				game.reset();
			}
			System.out.println("Done playing");
			System.out.println(wins + " wins");
		}
	}
	
}
