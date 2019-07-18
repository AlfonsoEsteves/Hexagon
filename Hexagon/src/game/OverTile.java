package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.function.Predicate;

public class OverTile implements Executable {

	public OTId id;
	public int x;
	public int y;

	public OverTile(OTId id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public static Predicate makeWith(Item item){
		return x -> x instanceof OverTile && ((OverTile)x).id.makeWith == item;
	}
	public static Predicate isDestroyable = x -> x instanceof OverTile && ((OverTile)x).id.missingVersion != null;

	@Override
	public void execute() {
		id.execute(this);
	}
}
