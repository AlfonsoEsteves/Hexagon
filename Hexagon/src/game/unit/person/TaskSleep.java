package game.unit.person;

import game.Map;
import game.Tile;
import game.unit.Task;
import game.unit.Unit;

public class TaskSleep extends Task {

    public static TaskSleep instance = new TaskSleep();

    private TaskSleep() {
        super(6, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(person.life < Person.maxLife && person.food > 0) {
            if (Map.has(tileX, tileY, Tile.bed.is()) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        person.life += 10;
        if(person.life >= Person.maxLife && person.food > 0) {
            person.life = Person.maxLife;
            person.priorityTask = null;
        }
    }
}
