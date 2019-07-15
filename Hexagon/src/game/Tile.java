package game;

import gui.ImageLoader;

import java.awt.Image;
import java.util.function.Predicate;

public class Tile {

	// UNDERTILES:
	public static Tile grass = new Tile("Grass", true);
	public static Tile water = new Tile("Water", false);
	public static Tile fertileGround = new Tile("Fertile ground", true);
	public static Tile depletedStoneMine = new Tile("Depleted stone mine", true);
	public static Tile stoneMine = new Tile("Stone mine", true, depletedStoneMine, Item.stone);
	public static Tile depletedIronMine = new Tile("Depleted iron mine", true);
	public static Tile ironMine = new Tile("Iron mine", true, depletedIronMine, Item.iron);
	public static Tile cutTree = new Tile("Cut tree", true);
	public static Tile tree = new Tile("Tree", true, cutTree, Item.wood);
	public static Tile gate = new Tile("Gate", true);

	// OVERTILES:
	public static Tile wall = new Tile("Wall", false);
	public static Tile missingWall = new Tile("Missing wall", true);
	public static Tile door = new Tile("Door", true);
	public static Tile missingDoor = new Tile("Missing door", true);
	public static Tile bed = new Tile("Bed", true);
	public static Tile missingBed = new Tile("Missing bed", true);
	public static Tile anvil = new Tile("Anvil", true);
	public static Tile missingAnvil = new Tile("Missing anvil", true);
	public static Tile depot = new Tile("Depot", true);
	public static Tile missingDepot = new Tile("Missing depot", true);

	public String name;
	public boolean steppable;
	public Tile depletedVersion;
	public Item providesItem;
	public Image image;

	public Tile(String name, boolean steppable) {
		this.name = name;
		this.steppable = steppable;
		image = ImageLoader.load(name);
	}

	public Tile(String name, boolean steppable, Tile depletedVersion, Item providesItem) {
		this.name = name;
		this.steppable = steppable;
		this.depletedVersion = depletedVersion;
		this.providesItem = providesItem;
		image = ImageLoader.load(name);
	}

	public Predicate is(){
		return x -> x == this;
	}
}
