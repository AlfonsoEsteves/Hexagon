package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

import java.util.Collections;

public class TaskCollect extends Task {

    public static TaskCollect taskCollectStone = new TaskCollect(Item.stone);
    public static TaskCollect taskCollectIron = new TaskCollect(Item.iron);
    public static TaskCollect taskCollectWood = new TaskCollect(Item.wood);
    public static TaskCollect taskCollectFruit = new TaskCollect(Item.fruit);
    public static TaskCollect taskCollectMushroom = new TaskCollect(Item.mushroom);
    public static TaskCollect taskCollectHoney = new TaskCollect(Item.honey);
    public static TaskCollect taskCollectCarrot = new TaskCollect(Item.carrot);

    public Item resource;

    private TaskCollect(Item resource) {
        super(4, 0);
        this.resource = resource;
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(Collections.frequency(person.carrying, resource) < 2){
            if (Map.has(tileX, tileY, resource.producer.overTileIs) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        if(Map.has(unit.x, unit.y, resource.producer.overTileIs) != null) {
            person.carrying.add(resource);
            Map.overTile[person.x][person.y].id = resource.producer.depletedVersion;
            Map.queueExecutable(Map.overTile[person.x][person.y], 100 + Rnd.nextInt(200));
        }
    }
}
