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
	public static Tile stoneMine = new Tile("Stone mine", true).setDepletedVersion(depletedStoneMine).setProvidesItem(Item.stone);
	public static Tile depletedIronMine = new Tile("Depleted iron mine", true);
	public static Tile ironMine = new Tile("Iron mine", true).setDepletedVersion(depletedIronMine).setProvidesItem(Item.iron);
	public static Tile cutTree = new Tile("Cut tree", true);
	public static Tile tree = new Tile("Tree", true).setDepletedVersion(cutTree).setProvidesItem(Item.wood);
	public static Tile gate = new Tile("Gate", true);

	// OVERTILES:
	public static Tile wall = new Tile("Wall", false);
	public static Tile missingWall = new Tile("Missing wall", true).setMakeWith(Item.stone).setCompletedVersion(wall);
	public static Tile door = new Tile("Door", true);
	public static Tile missingDoor = new Tile("Missing door", true).setMakeWith(Item.wood).setCompletedVersion(door);
	public static Tile bed = new Tile("Bed", true);
	public static Tile missingBed = new Tile("Missing bed", true).setMakeWith(Item.wood).setCompletedVersion(bed);
	public static Tile anvil = new Tile("Anvil", true);
	public static Tile missingAnvil = new Tile("Missing anvil", true).setMakeWith(Item.iron).setCompletedVersion(anvil);
	public static Tile depot = new Tile("Depot", true);
	public static Tile missingDepot = new Tile("Missing depot", true).setMakeWith(Item.stone).setCompletedVersion(depot);

	public String name;
	public boolean steppable;
	public Tile depletedVersion;
	public Item providesItem;
	public Item makeWith;
	public Tile completedVersion;
	public Image image;

	public Tile(String name, boolean steppable) {
		this.name = name;
		this.steppable = steppable;
		image = ImageLoader.load(name);
	}

	public Tile setDepletedVersion(Tile depletedVersion){
		this.depletedVersion = depletedVersion;
		return this;
	}

	public Tile setProvidesItem(Item providesItem){
		this.providesItem = providesItem;
		providesItem.producer = this;
		return this;
	}

	public Tile setMakeWith(Item makeWith){
		this.makeWith = makeWith;
		return this;
	}

	public Tile setCompletedVersion(Tile completedVersion){
		this.completedVersion = completedVersion;
		return this;
	}

	public Predicate is(){
		return x -> x == this;
	}

	public static Predicate makeWith(Item item){
		return x -> x instanceof Tile && ((Tile)x).makeWith == item;
	}
}
