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
        Tile missing;
        Tile built;
        if(person.carrying.contains(Item.stone)) {
            missing = Tile.missingWall;
            built = Tile.wall;
        }
        else if(person.carrying.contains(Item.iron)) {
            missing = Tile.missingAnvil;
            built = Tile.anvil;
        }
        else if(person.carrying.contains(Item.wood)) {
            missing = Tile.missingDoor;
            built = Tile.door;
        }asd
        for(int[] p : MapIter.of(range)) {
            if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingWall) != null) {
                person.carrying.remove(Item.stone);
                Map.overTile[person.x + p[0]][person.y + p[1]] = Tile.wall;
            }
            else if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingAnvil) != null) {
                person.carrying.remove(Item.iron);
                Map.overTile[person.x + p[0]][person.y + p[1]] = Tile.anvil;
            }
            else if (Map.has(unit.x + p[0], unit.y + p[1], Tile.missingBed) != null) {
                person.carrying.remove(Item.wood);
                Map.overTile[person.x + p[0]][person.y + p[1]] = Tile.bed;
            }
        }
    }
}
