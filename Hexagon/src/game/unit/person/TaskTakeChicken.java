package game.unit.person;

import game.Dropped;
import game.Item;
import game.Map;
import game.Rnd;
import game.unit.Task;
import game.unit.TaskScan;
import game.unit.Unit;
import game.unit.chicken.Chicken;

import java.util.Collections;

public class TaskTakeChicken extends TaskScan {

    public static TaskTakeChicken instance = new TaskTakeChicken();

    private TaskTakeChicken() {
        super(4, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(Collections.frequency(person.carrying, Item.meat) < 2){
            if (Map.has(tileX, tileY, Chicken.is) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Chicken chicken = Map.has(unit.x, unit.y, Chicken.is);
        if(chicken != null) {
            person.carrying.add(Item.meat);
            chicken.removeFromTileAndDestroy();
        }
    }
}
