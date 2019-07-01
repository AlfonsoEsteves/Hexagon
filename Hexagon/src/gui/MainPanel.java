package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import game.Map;
import game.MapIter;
import game.game.unit.Unit;
import game.game.unit.game.unit.person.Person;

public class MainPanel extends JPanel implements MouseInputListener, KeyListener {

	public static Unit selectedUnit;
	public static int viewSize = 20;
	public static int viewX = 0;
	public static int viewY = 0;

	public MainPanel(int x, int y, int width, int height) {
		setBounds(x, y, width, height);
		setLayout(null);

		MainFrame.instance.addMouseListener(this);
		MainFrame.instance.addKeyListener(this);
	}

	@Override
	public void paint(Graphics graphics) {
		if(selectedUnit != null) {
			viewX = selectedUnit.x;
			viewY = selectedUnit.y;
		}

		graphics.setColor(Color.blue);
		graphics.fillRect(0, 0, MainFrame.width, MainFrame.height);
		for(int[] p : MapIter.of(viewSize)){
			int x = viewX + p[0];
			int y = viewY + p[1];
			int screenX = MainFrame.width / 2 + p[0] * 10 + p[1] * 10;
			int screenY = MainFrame.height / 2 + p[0] * 20 - p[1] * 20;
			if(Map.overTile(x, y) == null) {
				graphics.drawImage(Map.underTile(x, y).image, screenX, screenY, 20, 20, this);
			}
			else{
				graphics.drawImage(Map.overTile(x, y).image, screenX, screenY, 20, 20, this);
			}
			Unit unit = Map.unit(x, y);
			if(unit != null) {
				graphics.drawImage(unit.image(), screenX, screenY, 15, 15, this);
				graphics.setColor(Color.blue);
				graphics.drawString("" +unit.life, screenX, screenY + 20);
				int count = 0;
				while (unit != null) {
					unit = unit.next;
					count++;
				}
				graphics.setColor(Color.white);
				graphics.drawString("" +count, screenX, screenY);
			}
		}
		graphics.setColor(Color.white);
		graphics.drawString("" + Map.time, 10,10);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		selectedUnit = null;
		double minDistance = 250;
		for(int[] p : MapIter.of(viewSize)){
			int x = viewX + p[0];
			int y = viewY + p[1];
			Unit unit = (Unit)Map.has(x, y, Person.personIdentity);
			if(unit != null) {
				int screenX = MainFrame.width / 2 + p[0] * 10 + p[1] * 10;
				int screenY = MainFrame.height / 2 + p[0] * 20 - p[1] * 20;
				int diffX = screenX - e.getX();
				int diffY = screenX - e.getY();
				if (Math.sqrt(diffX * diffX + diffY * diffY) < minDistance) {
					selectedUnit = unit;
					minDistance = Math.sqrt(diffX * diffX + diffY * diffY);
				}
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}
