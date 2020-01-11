package game.unit.demon;

import game.Debug;
import game.Map;
import game.unit.Task;
import game.unit.Unit;
import game.unit.person.Person;

public class TaskHuntPerson extends Task {

    public static TaskHuntPerson instance = new TaskHuntPerson();

    private TaskHuntPerson() {
        super(10, Person.visionRange, 0);
    }

    @Override
    public boolean appliesInTile(Unit unit, int tileX, int tileY) {
        return Map.has(tileX, tileY, Person.is) != null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)Map.has(unit.x, unit.y, Person.is);
        Debug.check(person != null);
        person.receiveDamage(10);
    }
}
