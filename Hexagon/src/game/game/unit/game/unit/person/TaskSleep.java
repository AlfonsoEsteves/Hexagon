package game.game.unit.game.unit.person;

import game.Map;
import game.Tile;
import game.game.unit.Task;
import game.game.unit.Unit;

public class TaskSleep extends Task {

    public static TaskSleep instance = new TaskSleep();

    private TaskSleep() {
        super(6, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if(unit.life < Person.maxLife) {
            if (Map.has(tileX, tileY, Tile.bed.predicate()) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        person.life += 10;
        if(person.life >= Person.maxLife) {
            person.life = Person.maxLife;
            person.priorityTask = null;
        }
    }
}
