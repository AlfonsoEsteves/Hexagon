package game;

import gui.ImageLoader;

import java.awt.Image;
import java.util.function.Predicate;

public class Tile {

	public static Tile grass = new Tile("Grass", true);
	public static Tile water = new Tile("Water", false);

	public String name;
	public boolean steppable;
	public Image image;
	public Predicate is;

	public Tile(String name, boolean steppable) {
		this.name = name;
		this.steppable = steppable;
		image = ImageLoader.load(name);
		is = x -> x == this;
	}

}
