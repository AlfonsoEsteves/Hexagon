package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskStore extends Task {

    public static TaskStore instance = new TaskStore();

    private TaskStore() {
        super(3, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(!person.carrying.isEmpty()){
            if (Map.overTile(tileX, tileY) == Tile.depot && Map.dropped(tileX, tileY) == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        if(Map.overTile(unit.x, unit.y) == Tile.depot && Map.dropped(unit.x, unit.y) == null) {
            Item item = person.carrying.remove(0);
            Map.dropped[unit.x][unit.y] = new Dropped(item);
        }
    }
}
