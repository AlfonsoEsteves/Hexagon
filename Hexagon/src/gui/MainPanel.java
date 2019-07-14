package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import game.Map;
import game.MapIter;
import game.unit.Unit;
import game.unit.person.Person;

public class MainPanel extends JPanel implements MouseInputListener, KeyListener {

	public static Person selectedUnit;
	public static int viewSize = 20;
	public static int viewX = Map.size / 2;
	public static int viewY = Map.size / 2;

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
		}
		for(int[] p : MapIter.of(viewSize)) {
			int x = viewX + p[0];
			int y = viewY + p[1];
			int screenX = MainFrame.width / 2 + p[0] * 10 + p[1] * 10;
			int screenY = MainFrame.height / 2 + p[0] * 20 - p[1] * 20;
			Unit unit = Map.unit(x, y);
			if(unit != null) {
				graphics.drawImage(unit.image(), screenX, screenY, 15, 15, this);
				graphics.setColor(Color.white);
				graphics.drawString("N" + unit.id, screenX - 20, screenY - 5);

				if(unit.next == null) {
					graphics.setColor(Color.blue);
					graphics.drawString("" + unit.life, screenX, screenY + 20);
				}
				else{
					int count = 1;
					while(unit.next != null){
						count++;
						unit = unit.next;
					}
					graphics.setColor(Color.red);
					graphics.drawString("*" + count, screenX, screenY + 20);
				}
			}
			Person person = Map.has(x, y, Person.is);
			if(person != null) {
				float[] hsb = new float[3];
				Color.RGBtoHSB(255, 0, 255, hsb);
				graphics.setColor(Color.getHSBColor((person.getSuperLeader().id % 100) / 100.0f, 1.0f, 1.0f));
				graphics.drawOval(screenX - 5, screenY - 5, 30, 30);
				graphics.drawOval(screenX - 4, screenY - 4, 28, 28);
				if (person.leader != null && person.leader.alive) {
					if (Map.distance(viewX, viewY, person.leader.x, person.leader.y) < viewSize) {
						int screenX2 = MainFrame.width / 2 + (person.leader.x - viewX) * 10 + (person.leader.y - viewY) * 10;
						int screenY2 = MainFrame.height / 2 + (person.leader.x - viewX) * 20 - (person.leader.y - viewY) * 20;
						graphics.drawLine(screenX, screenY, screenX2, screenY2);
					}
				}
			}
		}
		graphics.setColor(Color.white);
		graphics.drawString("" + Map.time, 10,10);
		if(selectedUnit != null) {
			graphics.drawString("Unit:   N" + selectedUnit.id, 10, 30);
			graphics.drawString("Leader: N" + selectedUnit.getSuperLeader().id, 10, 50);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		selectedUnit = null;
		double minDistance = 250;
		for(int[] p : MapIter.of(viewSize)){
			int x = viewX + p[0];
			int y = viewY + p[1];
			Person person = Map.has(x, y, Person.is);
			if(person != null) {
				int screenX = MainFrame.width / 2 + p[0] * 10 + p[1] * 10;
				int screenY = MainFrame.height / 2 + p[0] * 20 - p[1] * 20;
				int diffX = screenX - e.getX();
				int diffY = screenY - e.getY();
				if (Math.sqrt(diffX * diffX + diffY * diffY) < minDistance) {
					selectedUnit = person;
					minDistance = Math.sqrt(diffX * diffX + diffY * diffY);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

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
