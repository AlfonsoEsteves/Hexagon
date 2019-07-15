package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskCollect extends Task {

    public static TaskCollect instance = new TaskCollect();

    private TaskCollect() {
        super(4, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(!person.carrying.contains(Item.stone)){
            if (Map.has(tileX, tileY, Tile.stoneMine.is().or(Tile.tree.is()).or(Tile.ironMine.is())) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        if(Map.has(unit.x, unit.y, Tile.stoneMine.is()) != null) {
            person.carrying.add(Item.stone);
            Map.underTile[person.x][person.y] = Tile.depletedStoneMine;
            Map.queueExecutable(new ResourceReplenish(unit.x, unit.y, Tile.stoneMine), 100);
        }
        else if(Map.has(unit.x, unit.y, Tile.tree.is()) != null) {
            person.carrying.add(Item.wood);
            Map.underTile[person.x][person.y] = Tile.cutTree;
            Map.queueExecutable(new ResourceReplenish(unit.x, unit.y, Tile.tree), 100);
        }
        else if(Map.has(unit.x, unit.y, Tile.ironMine.is()) != null) {
            person.carrying.add(Item.iron);
            Map.underTile[person.x][person.y] = Tile.depletedIronMine;
            Map.queueExecutable(new ResourceReplenish(unit.x, unit.y, Tile.ironMine), 100);
        }
    }
}
