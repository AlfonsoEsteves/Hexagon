package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

import java.util.Collections;

public class TaskGoAndSell extends Task {

    public static TaskGoAndSell instance = new TaskGoAndSell();

    // If I introduce multithreading this var needs to be separated by thread
    private Item spareItem;

    private TaskGoAndSell() {
        super(2.7, Person.visionRange,1);
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        spareItem = spareItem(person);
        return spareItem != null;
    }

    @Override
    public Memory appliesInTile(Unit unit, int tileX, int tileY) {
        if(Map.has(tileX, tileY, Person.is.and(x -> ((Person)x).selling)) != null) {
            return new MemoryStaticPoint(tileX, tileY);
        }
        return null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Person buyer = Map.has(person.goalMemory.getX(), person.goalMemory.getY(), Person.is.and(x -> ((Person)x).selling));
        Debug.check(buyer != null);
        buyer.carrying.add(spareItem);
        buyer.gold -= buyer.itemValue[spareItem.id];
        person.gold += buyer.itemValue[spareItem.id];
        person.carrying.remove(spareItem);


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
