package gui;

import java.awt.*;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static int width = 1000;
	public static int height = 668;

	public static MainFrame instance;

	public static void instantiate() {
		instance = new MainFrame();
		instance.initialize();
	}

	private void initialize() {
		//setUndecorated(true);
		setSize(width, height);
		setLayout(null);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(width, height));
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		MainPanel mainPanel = new MainPanel(0, 0, width, height);
		add(mainPanel);
		setVisible(true);
	}
}