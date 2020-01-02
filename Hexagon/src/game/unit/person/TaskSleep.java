package game.unit.person;

import game.unit.TaskTravel;
import game.unit.Unit;

public class TaskSleep extends TaskTravel {

    public static TaskSleep instance = new TaskSleep();

    private TaskSleep() {
        super(6, 0);
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        return person.life < Person.maxLife && person.food > 0 && person.roomPosition != null;
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
    public int[] getDestination(Unit unit) {
        return ((Person)unit).roomPosition;
    }
}
