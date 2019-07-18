package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.function.Predicate;

public class OverTile implements Executable {

	public static class Id {
		public String name;
		public boolean steppable;
		public Id depletedVersion;
		public Id renewedVersion;
		public Id completedVersion;
		public Id missingVersion;
		public Item providesItem;
		public Item makeWith;
		public Event event;
		public Image image;

		public Predicate is;

		public Id(String name) {
			this.name = name;
			image = ImageLoader.load(name);
			this.steppable = true;
			is = x -> x == this;
		}

		public Id setSteppableFalse(){
			steppable = false;
			return this;
		}

		public Id setProvidesItem(Item providesItem){
			this.providesItem = providesItem;
			providesItem.producer = this;
			return this;
		}

		public Id setMakeWith(Item makeWith){
			this.makeWith = makeWith;
			return this;
		}

		public Id setDepletedVersion(Id depletedVersion){
			this.depletedVersion = depletedVersion;
			depletedVersion.renewedVersion = this;
			return this;
		}

		public Id setCompletedVersion(Id completedVersion){
			this.completedVersion = completedVersion;
			completedVersion.missingVersion = this;
			return this;
		}
	}

	// NATURAL:
	public static Id depletedStoneMine = new Id("Depleted stone mine");
	public static Id stoneMine = new Id("Stone mine").setDepletedVersion(depletedStoneMine).setProvidesItem(Item.stone);
	public static Id depletedIronMine = new Id("Depleted iron mine");
	public static Id ironMine = new Id("Iron mine").setDepletedVersion(depletedIronMine).setProvidesItem(Item.iron);
	public static Id cutTree = new Id("Cut tree");
	public static Id tree = new Id("Tree").setDepletedVersion(cutTree).setProvidesItem(Item.wood);
	public static Id bush = new Id("Bush");
	public static Id fruitBush = new Id("Fruit bush").setDepletedVersion(bush).setProvidesItem(Item.fruit);
	public static Id gate = new Id("Gate");

	// BUILDABLE:
	public static Id wall = new Id("Wall").setSteppableFalse();
	public static Id missingWall = new Id("Missing wall").setMakeWith(Item.stone).setCompletedVersion(wall);
	public static Id door = new Id("Door");
	public static Id missingDoor = new Id("Missing door").setMakeWith(Item.wood).setCompletedVersion(door);
	public static Id bed = new Id("Bed");
	public static Id missingBed = new Id("Missing bed").setMakeWith(Item.wood).setCompletedVersion(bed);
	public static Id anvil = new Id("Anvil");
	public static Id missingAnvil = new Id("Missing anvil").setMakeWith(Item.iron).setCompletedVersion(anvil);
	public static Id depot = new Id("Depot");
	public static Id missingDepot = new Id("Missing depot").setMakeWith(Item.stone).setCompletedVersion(depot);

	public Id id;
	public int x;
	public int y;

	public OverTile(Id id, int x, int y) {
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
