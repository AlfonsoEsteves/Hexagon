package game.game.unit;

import game.Map;
import game.Tile;

public class TaskSleep extends Task{

    public static TaskSleep instance = new TaskSleep();

    private TaskSleep() {
        super(6);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if(unit.life < Person.maxLife) {
            if (Map.has(tileX, tileY, Tile.bed) != null) {
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
