package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

import java.util.Collections;

public class TaskStore extends Task {

    public static TaskStore instance = new TaskStore();

    // If I introduce multithreading this var needs to be separated by thread
    private Item spareItem;

    private TaskStore() {
        super(3, 1,0);
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        if(person.job == OTId.depot &&
                person.memoryBuildings[Person.jobBuilding] != null &&
                person.memoryBuildings[Person.jobBuilding].missingStone == 0 &&
                person.memoryBuildings[Person.jobBuilding].stored < 7){
            spareItem = spareItem(person);
            return spareItem != null;
        }
        return false;
    }

    @Override
    public Memory appliesInTile(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        Debug.check(spareItem != null);
        OverTile depot = Map.has(tileX, tileY, OTId.depot.overTileIs);
        if (depot != null && ((Building)depot.state).placed == 19 && Map.dropped(tileX, tileY) == null) {
            int placedStones = ((Building)depot.state).placed;
            if(placedStones == 19){
                if(Map.dropped(tileX, tileY) == null) {
                    return new MemoryStaticPoint(tileX, tileY);
                }
            }
            else {
                person.memoryBuildings[Person.jobBuilding].missingStone = 19 - placedStones;
            }
        }
        return null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        OverTile depot = Map.has(person.x, person.y, OTId.depot.overTileIs);
        Debug.check(spareItem != null);
        Debug.check(depot != null);
        Debug.check(Map.dropped(unit.x, unit.y) == null);
        Debug.check(person.memoryBuildings[Person.jobBuilding].stored < 7);
        person.carrying.remove(spareItem);
        Map.dropped[unit.x][unit.y] = new Dropped(spareItem);
        person.memoryBuildings[Person.jobBuilding].stored++;
        unit.cancelTask();
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

    @Override
    public Memory getDestination(Unit unit) {
        Person person = (Person)unit;
        return person.memoryBuildings[Person.jobBuilding];
    }
}
