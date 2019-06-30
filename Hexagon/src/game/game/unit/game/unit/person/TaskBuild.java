package game.game.unit.game.unit.person;

import game.*;
import game.game.unit.Task;
import game.game.unit.Unit;

public class TaskBuild extends Task {

    public static TaskBuild instance = new TaskBuild();

    private TaskBuild() {
        super(5, 1);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(person.carrying.contains(Item.stone)){
            if (Map.has(tileX, tileY, Tile.missingWall) != null) {
                return true;
            }
        }
        if(person.carrying.contains(Item.wood)){
            if (Map.has(tileX, tileY, Tile.missingBed) != null) {
                return true;
            }
        }
        if(person.carrying.contains(Item.iron)){
            if (Map.has(tileX, tileY, Tile.missingAnvil) != null) {
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
            if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingWall) != null) {
                resource = Item.stone;
                built = Tile.wall;
                break;
            }
            else if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingAnvil) != null) {
                resource = Item.iron;
                built = Tile.anvil;
                break;
            }
            else if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingBed) != null) {
                resource = Item.wood;
                built = Tile.bed;
                break;
            }
            else if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingDoor) != null) {
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
