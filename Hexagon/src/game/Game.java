package game;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Game implements Serializable{
	
	public static Game instance;
	
	public static void instantiate() {
		instance = new Game();
	}
}
