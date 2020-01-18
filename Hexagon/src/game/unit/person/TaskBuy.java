package game.unit.person;

import game.Debug;
import game.Dropped;
import game.Item;
import game.Map;
import game.unit.Task;
import game.unit.Unit;

public class TaskBuy extends Task {

    public static TaskBuy taskPickUpStone = new TaskBuy(Item.stone);
    public static TaskBuy taskPickUpIron = new TaskBuy(Item.iron);
    public static TaskBuy taskPickUpWood = new TaskBuy(Item.wood);
    public static TaskBuy taskPickUpFruit = new TaskBuy(Item.fruit);
    public static TaskBuy taskPickUpHoney = new TaskBuy(Item.honey);
    public static TaskBuy taskPickUpMushroom = new TaskBuy(Item.mushroom);
    public static TaskBuy taskPickUpCarrot = new TaskBuy(Item.carrot);
    public static TaskBuy taskPickUpSword = new TaskBuy(Item.sword);
    public static TaskBuy taskPickUpBow = new TaskBuy(Item.bow);

    public Item item;

    private TaskBuy(Item item) {
        super(1.5, Person.visionRange, 1);
        this.item = item;
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        return !person.carrying.contains(item);
    }

    @Override
    public Memory appliesInTile(Unit unit, int tileX, int tileY) {
        if(Map.has(tileX, tileY, Person.is.and(x -> ((Person)x).selling && ((Person)x).carrying.contains(item))) != null) {
            return new MemoryStaticPoint(tileX, tileY);
        }
        return null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Person seller = Map.has(person.goalMemory.getX(), person.goalMemory.getY(), Person.is.and(x -> ((Person)x).selling && ((Person)x).carrying.contains(item)));
        Debug.check(seller != null);
        seller.carrying.remove(item);
        seller.gold += seller.itemValue[item.id];
        person.gold -= seller.itemValue[item.id];
        person.carrying.add(item);



        unit.cancelTask();
    }
}
