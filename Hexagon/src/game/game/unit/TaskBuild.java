package game.game.unit;

import game.Item;
import game.Map;
import game.Tile;

public class TaskBuild extends Task{

    public static TaskBuild instance = new TaskBuild();

    private TaskBuild() {
        super(5);
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
        if (Map.has(unit.x, unit.y, Tile.missingWall) != null) {
            person.carrying.remove(Item.stone);
            Map.overTile[person.x][person.y] = Tile.wall;
        }
        else if (Map.has(unit.x, unit.y, Tile.missingAnvil) != null) {
            person.carrying.remove(Item.iron);
            Map.overTile[person.x][person.y] = Tile.anvil;
        }
        else if (Map.has(unit.x, unit.y, Tile.missingBed) != null) {
            person.carrying.remove(Item.wood);
            Map.overTile[person.x][person.y] = Tile.bed;
        }
    }
}
