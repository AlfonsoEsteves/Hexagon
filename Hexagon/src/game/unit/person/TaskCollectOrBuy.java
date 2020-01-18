package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

import java.util.Collections;

public class TaskCollectOrBuy extends Task {

    public static TaskCollectOrBuy taskCollectStone = new TaskCollectOrBuy(Item.stone);
    public static TaskCollectOrBuy taskCollectIron = new TaskCollectOrBuy(Item.iron);
    public static TaskCollectOrBuy taskCollectWood = new TaskCollectOrBuy(Item.wood);
    public static TaskCollectOrBuy taskCollectFruit = new TaskCollectOrBuy(Item.fruit);
    public static TaskCollectOrBuy taskCollectMushroom = new TaskCollectOrBuy(Item.mushroom);
    public static TaskCollectOrBuy taskCollectHoney = new TaskCollectOrBuy(Item.honey);
    public static TaskCollectOrBuy taskCollectCarrot = new TaskCollectOrBuy(Item.carrot);

    public Item resource;

    private TaskCollectOrBuy(Item resource) {
        super(4, Person.visionRange, 0);
        this.resource = resource;
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        return Collections.frequency(person.carrying, resource) < 2;
    }

    @Override
    public Memory appliesInTile(Unit unit, int tileX, int tileY) {
        if(Map.has(tileX, tileY, resource.producer.overTileIs) != null) {
            return new MemoryStaticPoint(tileX, tileY);
        }
        if(Map.has(tileX, tileY, Person.is.and(x -> ((Person)x).selling && ((Person)x).carrying.contains(resource))) != null) {
            return new MemoryStaticPoint(tileX, tileY);
        }
        return null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        if(Map.has(unit.x, unit.y, resource.producer.overTileIs) != null) {
            person.carrying.add(resource);
            Map.overTile[person.x][person.y].id = resource.producer.depletedVersion;


            sirve pa algo esto, ta bien?
            Map.queueExecutable(Map.overTile[person.x][person.y], 100 + Rnd.nextInt(200));



        }
        else {
            Person seller = Map.has(person.goalMemory.getX(), person.goalMemory.getY(), Person.is.and(x -> ((Person)x).selling && ((Person)x).carrying.contains(resource)));
            Debug.check(seller != null);
            seller.carrying.remove(resource);
            person.carrying.add(resource);
            seller.gold += seller.itemValue[resource.id];
            person.gold -= seller.itemValue[resource.id];
        }
        unit.cancelTask();
    }
}
