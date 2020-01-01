package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import game.*;
import game.unit.Unit;
import game.unit.demon.Demon;
import game.unit.person.Person;

public class MainPanel extends JPanel implements MouseInputListener, KeyListener {

	public static Person selectedUnit;
	public static int viewSize = 20;
	public static int viewX = Map.size / 2;
	public static int viewY = Map.size / 2;
	public static int tileWidth = 20;
	public static int tileHeight = 10;

	public MainPanel(int x, int y, int width, int height) {
		setBounds(x, y, width, height);
		setLayout(null);

		MainFrame.instance.addMouseListener(this);
		MainFrame.instance.addKeyListener(this);
	}

	@Override
	public void paint(Graphics graphics) {
		//try {
			if (selectedUnit != null) {
				viewX = selectedUnit.x;
				viewY = selectedUnit.y;
			}

			graphics.setColor(Color.blue);
			graphics.fillRect(0, 0, MainFrame.width, MainFrame.height);
			for (int[] p : MapIter.of(viewSize)) {
				int x = viewX + p[0];
				int y = viewY + p[1];
				int screenX = MainFrame.width / 2 + p[0] * 10 + p[1] * 10;
				int screenY = MainFrame.height / 2 + p[0] * 20 - p[1] * 20;
				if (Map.overTile(x, y) == null) {
					graphics.drawImage(Map.underTile(x, y).image, screenX, screenY, 20, 20, this);
				} else {
					graphics.drawImage(Map.overTile(x, y).id.image, screenX, screenY, 20, 20, this);
				}

				if (Map.dropped(x, y) != null) {
					graphics.drawImage(Map.dropped(x, y).item.image, screenX, screenY, 20, 20, this);
				}
			}
			for (int[] p : MapIter.of(viewSize)) {
				int x = viewX + p[0];
				int y = viewY + p[1];
				int screenX = MainFrame.width / 2 + p[0] * 10 + p[1] * 10;
				int screenY = MainFrame.height / 2 + p[0] * 20 - p[1] * 20;
				Unit unit = Map.unit(x, y);
				if (unit != null) {
					graphics.drawImage(unit.image(), screenX, screenY, 15, 15, this);
					graphics.setColor(Color.white);
					graphics.drawString("N" + unit.id, screenX - 20, screenY - 5);

					if (unit.next == null) {
						graphics.setColor(Color.blue);
						graphics.drawString("" + unit.life, screenX, screenY + 20);
					} else {
						int count = 1;
						while (unit.next != null) {
							count++;
							unit = unit.next;
						}
						graphics.setColor(Color.red);
						graphics.drawString("*" + count, screenX, screenY + 20);
					}
				}
				Person person = Map.has(x, y, Person.is);
				if (person != null) {
					float[] hsb = new float[3];
					Color.RGBtoHSB(255, 0, 255, hsb);
					graphics.setColor(Color.getHSBColor((person.getSuperLeader().id % 100) / 100.0f, 1.0f, 1.0f));
					graphics.drawOval(screenX - 5, screenY - 5, 30, 30);
					graphics.drawOval(screenX - 4, screenY - 4, 28, 28);
					if (person.leader != null && person.leader.alive) {
						if (Map.distance(viewX - person.leader.x, viewY - person.leader.y) < viewSize) {
							int screenX2 = MainFrame.width / 2 + (person.leader.x - viewX) * 10 + (person.leader.y - viewY) * 10;
							int screenY2 = MainFrame.height / 2 + (person.leader.x - viewX) * 20 - (person.leader.y - viewY) * 20;
							graphics.drawLine(screenX, screenY, screenX2, screenY2);
						}
					}
				}
			}
			graphics.setColor(Color.white);
			graphics.drawString("" + Map.time, 10, 10);
			if (selectedUnit != null) {
				graphics.drawString("Unit:   N" + selectedUnit.id, 10, 30);
				graphics.drawString("Leader: N" + selectedUnit.getSuperLeader().id, 10, 50);
				graphics.drawString("Food: " + selectedUnit.food, 10, 70);
				graphics.drawString("Gold: " + selectedUnit.gold, 10, 90);

				if(selectedUnit.currentTask != null) {
					graphics.drawString("Task: " + selectedUnit.currentTask.getClass().getSimpleName(), 10, 110);
				}

				int y = 150;
				for (Item item : selectedUnit.carrying) {
					graphics.drawString(item.name, 10, y);
					y += 15;
				}
				for (int i = 0; i < Item.itemTypes; i++) {
					graphics.drawString(selectedUnit.itemValue[i] + " " + Item.itemsList.get(i).name, 10, 30 + y + i * 15);
				}
			}

			for (int i = 0; i < Map.executableQueueSize; i++) {
				for (int j = 0; j < Map.executableQueue[i].size(); j++) {
					Executable e = (Executable) Map.executableQueue[i].get(j);
					if (e instanceof Person) {
						Person p = (Person) e;
						int x = (p.x + p.y) * MainFrame.height / Map.size / 6;
						int y = (p.x - p.y) * 2 * MainFrame.height / Map.size / 6 + MainFrame.height / 2;
						graphics.setColor(Color.white);
						graphics.drawLine(x, y, x + 1, y);
					} else if (e instanceof Demon) {
						Demon p = (Demon) e;
						int x = (p.x + p.y) * MainFrame.height / Map.size / 6;
						int y = (p.x - p.y) * 2 * MainFrame.height / Map.size / 6 + MainFrame.height / 2;
						graphics.setColor(Color.red);
						graphics.drawLine(x, y, x + 1, y);
					}
				}
			}
			int sX = (viewX + viewY) * MainFrame.height / Map.size / 6;
			int sY = (viewX - viewY) * 2 * MainFrame.height / Map.size / 6 + MainFrame.height / 2;
			graphics.setColor(Color.yellow);
			graphics.drawLine(sX - 1, sY - 1, sX + 2, sY - 1);
			graphics.drawLine(sX - 1, sY, sX + 2, sY);
			graphics.drawLine(sX - 1, sY + 1, sX + 2, sY + 1);
		/*}
		catch(Exception e) {
			e.printStackTrace();
		}*/
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
			int screenX = MainFrame.width / 2 + p[0] * 10 + p[1] * 10;
			int screenY = MainFrame.height / 2 + p[0] * 20 - p[1] * 20;
			int diffX = screenX - e.getX();
			int diffY = screenY - e.getY();
			if(person != null) {
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
	public void keyPressed(KeyEvent e) {
		viewX = Map.size / 2;
		viewY = Map.size / 2;
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
