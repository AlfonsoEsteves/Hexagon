package game;

import gui.ImageLoader;

import java.awt.Image;

public class Tile implements Searchable {

	// UNDERTILES:
	public static Tile grass = new Tile("Grass", true);
	public static Tile water = new Tile("Water", false);
	public static Tile fertileGround = new Tile("Fertile ground", true);
	public static Tile depletedStone = new Tile("Depleted stone", true);
	public static Tile stone = new Tile("Stone", true, depletedStone, Item.Identity.stone);
	public static Tile depletedIron = new Tile("Depleted iron", true);
	public static Tile iron = new Tile("Iron", true, depletedIron, Item.Identity.iron);
	public static Tile cutTree = new Tile("Cut tree", true);
	public static Tile tree = new Tile("Tree", true, cutTree, Item.Identity.wood);
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

	public String name;
	public boolean steppable;
	public Tile depletedVersion;
	public Item.Identity providesItem;
	public Image image;

	public Tile(String name, boolean steppable) {
		this.name = name;
		this.steppable = steppable;
		image = ImageLoader.load(name);
	}

	public Tile(String name, boolean steppable, Tile depletedVersion, Item.Identity providesItem) {
		this.name = name;
		this.steppable = steppable;
		this.depletedVersion = depletedVersion;
		this.providesItem = providesItem;
		image = ImageLoader.load(name);
	}

	@Override
	public boolean is(Object identity) {
		return identity == this;
	}
}
