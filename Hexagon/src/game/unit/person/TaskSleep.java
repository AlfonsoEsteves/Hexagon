package game.unit.person;

import game.unit.Task;
import game.unit.Unit;

public class TaskSleep extends Task {

    public static TaskSleep instance = new TaskSleep();

    private TaskSleep() {
        super(6, -1,0);
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        return person.life < Person.maxLife && person.food > 0 && person.roomMemory != null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        if (person.food > 0) {
            person.life += 10;
            if(person.life >= Person.maxLife) {
                person.life = Person.maxLife;
            }
        }
    }

    @Override
    public Memory getDestination(Unit unit) {
        Person person = (Person)unit;
        return person.roomMemory;
    }
}
