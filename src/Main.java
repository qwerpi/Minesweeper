import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;


public class Main {
	
	public static boolean display = true;

	public static void main(String[] args) {
		//MinesGame game = new MinesGame(9, 9, 10);
		MinesGame game = new MinesGame(16, 16, 40);
		//MinesGame game = new MinesGame(30, 16, 99);
		MinesAI ai = new MinesAI(game);
		
		if (display) {
			JFrame frame = new JFrame("Minesweeper");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(game);
			frame.pack();
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			//frame.setLocation((screen.width - frame.getWidth()) / 2, (screen.height - frame.getHeight()) / 2);
			frame.setVisible(true);
		}
		
		int wins = 0;
		for (int i = 0; i < 10000; i++) {
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
