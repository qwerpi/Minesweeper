import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import javax.swing.JFrame;


public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new MinesGame(30, 16, 99));
		frame.pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		//frame.setLocation((screen.width - frame.getWidth()) / 2, (screen.height - frame.getHeight()) / 2);
		frame.setVisible(true);
	}
	
}
