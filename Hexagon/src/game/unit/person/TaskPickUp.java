package game.unit.person;

import game.Dropped;
import game.Item;
import game.Map;
import game.unit.Task;
import game.unit.Unit;

public class TaskPickUp extends Task {

    public static TaskPickUp taskPickUpStone = new TaskPickUp(Item.stone);
    public static TaskPickUp taskPickUpIron = new TaskPickUp(Item.iron);
    public static TaskPickUp taskPickUpWood = new TaskPickUp(Item.wood);
    public static TaskPickUp taskPickUpFruit = new TaskPickUp(Item.fruit);
    public static TaskPickUp taskPickUpMeat = new TaskPickUp(Item.meat);
    public static TaskPickUp taskPickUpSword = new TaskPickUp(Item.sword);

    public Item item;

    private TaskPickUp(Item item) {
        super(1.5, 0);
        this.item = item;
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(!person.carrying.contains(item)){
            if (Map.has(tileX, tileY, item.droppedIsItem) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Dropped found = Map.has(person.x, person.y, item.droppedIsItem);
        if(found != null) {
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
        }
    }
}
