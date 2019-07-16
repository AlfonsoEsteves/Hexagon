package game.unit.person;

import game.Dropped;
import game.Item;
import game.Map;
import game.Tile;
import game.unit.Task;
import game.unit.Unit;

public class TaskCreateWeapon extends Task {

    public static TaskCreateWeapon instance = new TaskCreateWeapon();

    private TaskCreateWeapon() {
        super(4, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(!person.carrying.contains(Item.sword)) {
            if (person.carrying.contains(Item.iron)) {
                if (Map.has(tileX, tileY, Tile.anvil.is()) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        person.carrying.remove(Item.iron);
        person.carrying.add(Item.sword);
    }
}