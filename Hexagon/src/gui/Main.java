package gui;

import game.Game;
import game.Debug;
import game.Map;
import game.unit.Unit;

import java.awt.*;

public class Main {
	
	public static final long frameDuration = 70000000;

	public static void main(String[] args) {
		Game.instantiate();
		MainFrame.instantiate();
		
		long nextStepTime = System.nanoTime();
		while(true) {
			long currentTime = System.nanoTime();
			if(currentTime > nextStepTime) {
				nextStepTime += frameDuration;
				if (currentTime > nextStepTime) {
					Debug.log( "TIME", "Delay: " + (currentTime - nextStepTime) / 1000000);
				}

				try {
					Map.execute();
				}
				catch(RuntimeException e){
					System.out.println("Error at time " + Map.time + " for unit " + Unit.lastExecutedUnit.id + " with task " + Unit.lastExecutedUnit.currentTask);
					e.printStackTrace();
					return;
				}

				MainFrame.instance.repaint();

				Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
				if(mouseLocation.getX() > MainFrame.width - 50){
					MainPanel.viewX += 2;
					MainPanel.viewY += 2;
				}
				if(mouseLocation.getX() < 50){
					MainPanel.viewX -= 2;
					MainPanel.viewY -= 2;
				}
				if(mouseLocation.getY() > MainFrame.height - 50){
					MainPanel.viewX ++;
					MainPanel.viewY --;
				}
				if(mouseLocation.getY() < 50){
					MainPanel.viewX --;
					MainPanel.viewY ++;
				}
			}
		}
	}

}
