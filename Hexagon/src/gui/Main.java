package gui;

import game.Game;
import game.Log;
import game.Map;
import game.MapIter;

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
					Log.log( "TIME", "Delay: " + (currentTime - nextStepTime) / 1000000);
				}
				Map.execute();
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
