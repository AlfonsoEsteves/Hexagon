package game.game.unit.game.unit.person;

import game.Item;
import game.Map;
import game.ResourceReplenish;
import game.Tile;
import game.game.unit.Task;
import game.game.unit.Unit;

public class TaskCollect extends Task {

    public static TaskCollect instance = new TaskCollect();

    private TaskCollect() {
        super(4, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(!person.carrying.contains(Item.stone)){
            if (Map.has(tileX, tileY, Tile.stone.is().or(Tile.tree.is()).or(Tile.iron.is())) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        if(Map.has(unit.x, unit.y, Tile.stone.is()) != null) {
            person.carrying.add(Item.stone);
            Map.underTile[person.x][person.y] = Tile.depletedStone;
            Map.queueExecutable(new ResourceReplenish(unit.x, unit.y, Tile.stone), 100);
        }
        else if(Map.has(unit.x, unit.y, Tile.tree.is()) != null) {
            person.carrying.add(Item.wood);
            Map.underTile[person.x][person.y] = Tile.cutTree;
            Map.queueExecutable(new ResourceReplenish(unit.x, unit.y, Tile.tree), 100);
        }
        else if(Map.has(unit.x, unit.y, Tile.iron.is()) != null) {
            person.carrying.add(Item.iron);
            Map.underTile[person.x][person.y] = Tile.depletedIron;
            Map.queueExecutable(new ResourceReplenish(unit.x, unit.y, Tile.iron), 100);
        }
    }
}
