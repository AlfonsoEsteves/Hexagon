package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskPickUpOrBuy extends Task {

    public static TaskPickUpOrBuy taskPickUpStone = new TaskPickUpOrBuy(Item.stone);
    public static TaskPickUpOrBuy taskPickUpIron = new TaskPickUpOrBuy(Item.iron);
    public static TaskPickUpOrBuy taskPickUpWood = new TaskPickUpOrBuy(Item.wood);
    public static TaskPickUpOrBuy taskPickUpFruit = new TaskPickUpOrBuy(Item.fruit);
    public static TaskPickUpOrBuy taskPickUpHoney = new TaskPickUpOrBuy(Item.honey);
    public static TaskPickUpOrBuy taskPickUpMushroom = new TaskPickUpOrBuy(Item.mushroom);
    public static TaskPickUpOrBuy taskPickUpCarrot = new TaskPickUpOrBuy(Item.carrot);
    public static TaskPickUpOrBuy taskPickUpSword = new TaskPickUpOrBuy(Item.sword);
    public static TaskPickUpOrBuy taskPickUpBow = new TaskPickUpOrBuy(Item.bow);

    public Item item;

    private TaskPickUpOrBuy(Item item) {
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
        if(Map.has(tileX, tileY, Person.is.and(x -> ((Person)x).selling && ((Person)x).carrying.contains(item))) != null) {
            return new MemoryStaticPoint(tileX, tileY);
        }
        return null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Dropped dropped = Map.has(person.x, person.y, item.droppedIsItem);
        if(dropped != null) {
            if (Map.dropped[person.x][person.y] == dropped) {
                Map.dropped[person.x][person.y] = dropped.next;
            } else {
                Dropped current = Map.dropped[person.x][person.y];
                while (current.next != dropped) {
                    current = current.next;
                }
                current.next = dropped.next;
            }
        }
        else{
            Person seller = Map.has(person.goalMemory.getX(), person.goalMemory.getY(), Person.is.and(x -> ((Person)x).selling && ((Person)x).carrying.contains(item)));
            Debug.check(seller != null);
            seller.carrying.remove(item);
            seller.gold += seller.itemValue[item.id];
            person.gold -= seller.itemValue[item.id];
        }
        person.carrying.add(item);
        unit.cancelTask();
    }
}
