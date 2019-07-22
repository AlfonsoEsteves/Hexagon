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
	public static OTId eatenRichGrass = new OTIdDepletedResource("Eaten rich grass");
	public static OTId richGrass = new OTId("Rich grass").setDepletedVersion(eatenRichGrass);
	public static OTId gate = new OTIdGate("Gate");

	// BUILDABLE:
	public static OTId wall = new OTId("Wall").setSteppableFalse();
	public static OTId missingWall = new OTIdMissingBuilding("Missing wall").setMakeWith(Item.stone).setCompletedVersion(wall);
	public static OTId door = new OTId("Door");
	public static OTId missingDoor = new OTIdMissingBuilding("Missing door").setMakeWith(Item.wood).setCompletedVersion(door);
	public static OTId bed = new OTId("Bed");
	public static OTId missingBed = new OTIdMissingBuilding("Missing bed").setMakeWith(Item.wood).setCompletedVersion(bed);
	public static OTId anvil = new OTId("Anvil");
	public static OTId missingAnvil = new OTIdMissingBuilding("Missing anvil").setMakeWith(Item.iron).setCompletedVersion(anvil);
	public static OTId henHouse = new OTId("Hen house");
	public static OTId missingHenHouse = new OTIdMissingBuilding("Missing hen house").setMakeWith(Item.wood).setCompletedVersion(henHouse);
	public static OTId depot = new OTId("Depot");
	public static OTId missingDepot = new OTIdMissingBuilding("Missing depot").setMakeWith(Item.stone).setCompletedVersion(depot);

	public String name;
	public boolean steppable;
	public OTId depletedVersion;
	public OTId renewedVersion;
	public OTId completedVersion;
	public OTId missingVersion;
	public Item providesItem;
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

	public void execute(OverTile overTile) {
	}

}
