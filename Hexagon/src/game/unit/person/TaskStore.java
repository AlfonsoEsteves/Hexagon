package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.TaskScan;
import game.unit.Unit;

import java.util.Collections;

public class TaskStore extends TaskScan {

    public static TaskStore instance = new TaskStore();

    private TaskStore() {
        super(3, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(spareItem(person) != null){
            if (Map.has(tileX, tileY, OTId.depot.overTileIs) != null && Map.dropped(tileX, tileY) == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        OverTile depot = Map.has(person.x, person.y, OTId.depot.overTileIs);
        if(depot != null && Map.dropped(unit.x, unit.y) == null) {
            Item item = spareItem(person);

            Debug.check(item != null);

            person.carrying.remove(item);
            Map.dropped[unit.x][unit.y] = new Dropped(item);
        }
    }

    private Item spareItem(Person person) {
        Item item = null;
        if(Collections.frequency(person.carrying, Item.sword) > 1) {
            item = Item.sword;
        }
        else if(Collections.frequency(person.carrying, Item.bow) > 1) {
            item = Item.bow;
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
        else if(Collections.frequency(person.carrying, Item.honey) > 1) {
            item = Item.honey;
        }
        else if(Collections.frequency(person.carrying, Item.mushroom) > 1) {
            item = Item.mushroom;
        }
        else if(Collections.frequency(person.carrying, Item.carrot) > 1) {
            item = Item.carrot;
        }
        return item;
    }
}
