package gui;

import game.Game;
import game.Debug;
import game.Map;

import java.awt.*;

public class Main {
	
	public static final long frameDuration = 100000000;

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
					MainFrame.instance.repaint();
				}
				catch(Exception e){
					e.printStackTrace();
					System.out.println("Event count: " + Debug.eventCount);
					return;
				}

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
