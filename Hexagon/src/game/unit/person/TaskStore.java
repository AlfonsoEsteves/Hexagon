package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

import java.util.Collections;

public class TaskStore extends Task {

    public static TaskStore instance = new TaskStore();

    private TaskStore() {
        super(3, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(!person.carrying.isEmpty()){
            if (Map.has(tileX, tileY, OTId.depot.overTileIs) != null && Map.dropped(tileX, tileY) == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        if(Map.has(person.x, person.y, OTId.depot.overTileIs) != null && Map.dropped(unit.x, unit.y) == null) {
            Item item = null;
            if(Collections.frequency(person.carrying, Item.sword) > 1) {
                item = Item.sword;
            }
            else if(Collections.frequency(person.carrying, Item.iron) > 1) {
                item = Item.iron;
            }
            else if(Collections.frequency(person.carrying, Item.wood) > 1) {
                item = Item.wood;
            }
            else if(Collections.frequency(person.carrying, Item.stone) > 1) {
                item = Item.stone;
            }
            else if(Collections.frequency(person.carrying, Item.fruit) > 1) {
                item = Item.fruit;
            }

            person.carrying.remove(item);
            Map.dropped[unit.x][unit.y] = new Dropped(item);
        }
    }
}
