package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskPickUp extends Task {

    public static TaskPickUp taskPickUpStone = new TaskPickUp(Item.stone);
    public static TaskPickUp taskPickUpIron = new TaskPickUp(Item.iron);
    public static TaskPickUp taskPickUpWood = new TaskPickUp(Item.wood);
    public static TaskPickUp taskPickUpFruit = new TaskPickUp(Item.fruit);
    public static TaskPickUp taskPickUpHoney = new TaskPickUp(Item.honey);
    public static TaskPickUp taskPickUpMushroom = new TaskPickUp(Item.mushroom);
    public static TaskPickUp taskPickUpCarrot = new TaskPickUp(Item.carrot);
    public static TaskPickUp taskPickUpSword = new TaskPickUp(Item.sword);
    public static TaskPickUp taskPickUpBow = new TaskPickUp(Item.bow);

    public Item item;

    private TaskPickUp(Item item) {
        super(1.5, Person.visionRange, 0);
        this.item = item;
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        return !person.carrying.contains(item);
    }

    @Override
    public Memory appliesInTile(Unit unit, int tileX, int tileY) {
        if(Map.has(tileX, tileY, item.droppedIsItem) != null){
            return new MemoryStaticPoint(tileX, tileY);
        }
        return null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Dropped found = Map.has(person.x, person.y, item.droppedIsItem);

        Debug.check(found != null);

        person.carrying.add(found.item);
        if(Map.dropped[person.x][person.y] == found) {
            Map.dropped[person.x][person.y] = found.next;
        }
        else{
            Dropped current = Map.dropped[person.x][person.y];
            while(current.next != found) {
                current = current.next;
            }
            current.next = found.next;
        }
        unit.cancelTask();
    }
}
