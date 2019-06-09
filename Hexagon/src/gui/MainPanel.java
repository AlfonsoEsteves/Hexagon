package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import game.Map;

public class MainPanel extends JPanel implements MouseInputListener, KeyListener {
	
	public static int viewSize = 30;

	private BufferedImage bufferedImage;
	private Graphics2D graphics;

	public MainPanel(int x, int y, int width, int height) {
		setBounds(x, y, width, height);
		setLayout(null);

		MainFrame.instance.addMouseListener(this);
		MainFrame.instance.addKeyListener(this);

		bufferedImage = (BufferedImage) new BufferedImage(MainFrame.width, MainFrame.width, BufferedImage.TYPE_INT_RGB);
		graphics = bufferedImage.createGraphics();
		setDoubleBuffered(false);
	}

	@Override
	public void paint(Graphics g) {
		for(int i = 0;i<viewSize; i++) {
			for(int j = 0;j<viewSize; j++) {
			    if(Map.things[i][j] == null) {
                    graphics.drawImage(Map.tiles[i][j].image, 20 + i * 10 + j * 10, MainFrame.height / 2 + i * 20 - j * 20, 20, 20, this);
                }
			    else{
                    graphics.drawImage(Map.things[i][j].image, 20 + i * 10 + j * 10, MainFrame.height / 2 + i * 20 - j * 20, 20, 20, this);
                }
				if(Map.units[i][j] != null) {
					graphics.drawImage(ImageLoader.unitImage, 20 + i * 10 + j * 10, MainFrame.height / 2 + i * 20 - j * 20, 15, 15, this);
				}
			}
		}
		g.drawImage(bufferedImage, 0, 0, this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
