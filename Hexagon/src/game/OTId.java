package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.function.Predicate;

// OverTile ID
public class OTId {

	// NATURAL:
	public static OTId depletedStoneMine = new OTIdDepletedResource("Depleted stone mine");
	public static OTId stoneMine = new OTId("Stone mine").setDepletedVersion(depletedStoneMine).setProvidesItem(Item.stone);
	public static OTId depletedIronMine = new OTIdDepletedResource("Depleted iron mine");
	public static OTId ironMine = new OTId("Iron mine").setDepletedVersion(depletedIronMine).setProvidesItem(Item.iron);
	public static OTId cutTree = new OTIdDepletedResource("Cut tree");
	public static OTId tree = new OTId("Tree").setDepletedVersion(cutTree).setProvidesItem(Item.wood);
	public static OTId bush = new OTIdDepletedResource("Bush");
	public static OTId fruitBush = new OTId("Fruit bush").setDepletedVersion(bush).setProvidesItem(Item.fruit);
	public static OTId cutMushrooms = new OTIdDepletedResource("Cut mushrooms");
	public static OTId mushrooms = new OTId("Mushrooms").setDepletedVersion(cutMushrooms).setProvidesItem(Item.mushroom);
	public static OTId takenBeeNest = new OTIdDepletedResource("Taken bee nest");
	public static OTId beeNest = new OTId("Bee nest").setDepletedVersion(takenBeeNest).setProvidesItem(Item.honey);
	public static OTId takenCarrots = new OTIdDepletedResource("Taken carrots");
	public static OTId carrots = new OTId("Carrots").setDepletedVersion(takenCarrots).setProvidesItem(Item.carrot);
	public static OTId eatenRichGrass = new OTIdDepletedResource("Eaten rich grass");
	public static OTId richGrass = new OTId("Rich grass").setDepletedVersion(eatenRichGrass);
	public static OTId gate = new OTIdGate("Gate");

	// BUILDABLE:
	public static OTId wall = new OTId("Wall").setSteppableFalse();
	public static OTId missingWall = new OTId("Missing wall").setMakeWith(Item.stone).setCompletedVersion(wall);
	public static OTId door = new OTId("Door");
	public static OTId missingDoor = new OTId("Missing door").setMakeWith(Item.wood).setCompletedVersion(door);
	public static OTId bed = new OTId("Bed");
	public static OTId missingBed = new OTId("Missing bed").setMakeWith(Item.wood).setCompletedVersion(bed);
	public static OTId anvil = new OTId("Anvil").tranformsItemInto(Item.iron, Item.sword);
	public static OTId missingAnvil = new OTId("Missing anvil").setMakeWith(Item.iron).setCompletedVersion(anvil);
	public static OTId carpentry = new OTId("Carpentry").tranformsItemInto(Item.wood, Item.bow);
	public static OTId missingCarpentry = new OTId("Missing Carpentry").setMakeWith(Item.wood).setCompletedVersion(carpentry);
	public static OTId henHouse = new OTId("Hen house");
	public static OTId missingHenHouse = new OTId("Missing hen house").setMakeWith(Item.wood).setCompletedVersion(henHouse);
	public static OTId depot = new OTId("Depot");
	public static OTId missingDepot = new OTId("Missing depot").setMakeWith(Item.stone).setCompletedVersion(depot);

	public String name;
	public boolean steppable;
	public OTId depletedVersion;
	public OTId renewedVersion;
	public OTId completedVersion;
	public OTId missingVersion;
	public Item providesItem;
	public Item tranformsItem;
	public Item makeWith;
	public Event event;
	public Image image;

	public Predicate overTileIs = x -> x instanceof OverTile && ((OverTile)x).id == this;

	public OTId(String name) {
		this.name = name;
		image = ImageLoader.load(name);
		this.steppable = true;
	}

	public OTId setSteppableFalse() {
		steppable = false;
		return this;
	}

	public OTId setProvidesItem(Item providesItem) {
		this.providesItem = providesItem;
		providesItem.producer = this;
		return this;
	}

	public OTId setMakeWith(Item makeWith) {
		this.makeWith = makeWith;
		return this;
	}

	public OTId setDepletedVersion(OTId depletedVersion) {
		this.depletedVersion = depletedVersion;
		depletedVersion.renewedVersion = this;
		return this;
	}

	public OTId setCompletedVersion(OTId completedVersion) {
		this.completedVersion = completedVersion;
		completedVersion.missingVersion = this;
		return this;
	}

	private OTId tranformsItemInto(Item from, Item into) {
		this.tranformsItem = from;
		this.providesItem = into;
		return this;
	}

	public void execute(OverTile overTile) {
	}

}
