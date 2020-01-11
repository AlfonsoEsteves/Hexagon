package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskReactToPerson extends Task {

    public static TaskReactToPerson instance = new TaskReactToPerson();

    private TaskReactToPerson() {
        super(5.5, Person.visionRange, 3);
    }

    @Override
    public Memory appliesInTile(Unit unit, int tileX, int tileY) {
        Person foundPerson = Map.has(tileX, tileY, Person.is.and(x -> ((Person)x).leader == null));
        if(foundPerson != null) {
            return new MemoryUnit(foundPerson);
        }
        return null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Person leader = person.getSuperLeader();
        for(int[] p : MapIter.of(executionRange)) {
            Person other = (Person)Map.has(unit.x + p[0], unit.y + p[1], Person.is.and(x -> ((Person)x).leader == null));
            if (other != null) {
                other.leader = person;
                if(person.leader == null) {
                    person.leader = person;
                }
            }
        }
        unit.cancelTask();
    }
}
