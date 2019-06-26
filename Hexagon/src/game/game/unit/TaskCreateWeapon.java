package game.game.unit;

import game.Item;
import game.Map;
import game.Tile;

public class TaskCreateWeapon extends Task{

    public static TaskCreateWeapon instance = new TaskCreateWeapon();

    private TaskCreateWeapon() {
        super(4);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(!person.carrying.contains(Item.sword)) {
            if (person.carrying.contains(Item.iron)) {
                if (Map.has(tileX, tileY, Tile.anvil) != null) {
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
