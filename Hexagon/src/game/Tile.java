package game;

import gui.ImageLoader;

import java.awt.Image;

public class Tile {

	// UNDERTILES:
	public static Tile grass = new Tile("Grass", true);
	public static Tile water = new Tile("Water", false);
	public static Tile fertileGround = new Tile("Fertile ground", true);
	public static Tile stone = new Tile("Stone", true);
	public static Tile depletedStone = new Tile("Depleted stone", true);

	// OVERTILES:
	public static Tile wall = new Tile("Wall", false);
	public static Tile missingWall = new Tile("Missing wall", true);
	public static Tile bed = new Tile("Bed", true);

	public String name;
	public boolean steppable;
	public Image image;

	public Tile(String name, boolean steppable) {
		this.name = name;
		this.steppable = steppable;
		image = ImageLoader.load(name);
	}

}
