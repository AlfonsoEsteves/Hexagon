package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskBuild extends Task {

    public static TaskBuild instance = new TaskBuild();

    private TaskBuild() {
        super(5, 1);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(person.carrying.contains(Item.stone)){
            if (Map.has(tileX, tileY, Tile.missingWall.is()) != null) {
                return true;
            }
            else if (Map.has(tileX, tileY, Tile.missingDepot.is()) != null) {
                return true;
            }
        }
        if(person.carrying.contains(Item.wood)){
            if (Map.has(tileX, tileY, Tile.missingBed.is()) != null) {
                return true;
            }
        }
        if(person.carrying.contains(Item.iron)){
            if (Map.has(tileX, tileY, Tile.missingAnvil.is()) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Tile built = null;
        Item resource = null;
        int[] position = null;
        for(int[] p : MapIter.of(range)) {
            position = p;
            if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingWall.is()) != null) {
                resource = Item.stone;
                built = Tile.wall;
                break;
            }
            else if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingDepot.is()) != null) {
                resource = Item.stone;
                built = Tile.depot;
                break;
            }
            else if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingAnvil.is()) != null) {
                resource = Item.iron;
                built = Tile.anvil;
                break;
            }
            else if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingBed.is()) != null) {
                resource = Item.wood;
                built = Tile.bed;
                break;
            }
            else if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingDoor.is()) != null) {
                resource = Item.wood;
                built = Tile.door;
                break;
            }
        }
        if(built != null) {
            person.carrying.remove(resource);
            Map.overTile[person.x + position[0]][person.y + position[1]] = built;
        }
    }
}
