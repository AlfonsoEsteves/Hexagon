package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskCollect extends Task {

    public static TaskCollect taskCollectStone = new TaskCollect(Item.stone);
    public static TaskCollect taskCollectIron = new TaskCollect(Item.iron);
    public static TaskCollect taskCollectWood = new TaskCollect(Item.wood);

    public Item resource;

    private TaskCollect(Item resource) {
        super(4, 0);
        this.resource = resource;
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(!person.carrying.contains(resource)){
            if (Map.has(tileX, tileY, resource.producer.is()) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        if(Map.has(unit.x, unit.y, resource.producer.is()) != null) {
            person.carrying.add(resource);
            Map.underTile[person.x][person.y] = resource.producer.depletedVersion;
            Map.queueExecutable(new ResourceReplenish(unit.x, unit.y, resource.producer), 100);
        }
    }
}
