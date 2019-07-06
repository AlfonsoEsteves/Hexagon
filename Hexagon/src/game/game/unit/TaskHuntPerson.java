package game.game.unit;

import game.Map;
import game.game.unit.game.unit.person.Person;

public class TaskHuntPerson extends Task{

    public static TaskHuntPerson instance = new TaskHuntPerson();

    private TaskHuntPerson() {
        super(10, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if (Map.has(tileX, tileY, Person.is) != null) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)Map.has(unit.x, unit.y, Person.is);
        if(person != null) {
            person.damage(10);
        }
        unit.damage(1);
    }
}
