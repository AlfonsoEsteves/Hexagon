package game.unit.person;

import game.Map;
import game.OTId;
import game.unit.Task;
import game.unit.TaskScan;
import game.unit.Unit;

public class TaskSleep extends TaskScan {

    public static TaskSleep instance = new TaskSleep();

    private TaskSleep() {
        super(6, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(person.life < Person.maxLife && person.food > 0) {
            if (Map.has(tileX, tileY, OTId.bed.overTileIs) != null) {
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
            person.currentTask = null;
        }
    }
}
