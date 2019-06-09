package gui;

import game.Game;
import game.Map;

public class Main {
	
	public static final long frameDuration = 500000000;

	public static void main(String[] args) {
		ImageLoader.loadImages();
		Game.instantiate();
		MainFrame.instantiate();
		
		long time = System.nanoTime();
		while(true) {
			if(System.nanoTime() > time) {
				time += frameDuration;
				Map.execute();
				MainFrame.instance.repaint();
			}
		}
	}

}
