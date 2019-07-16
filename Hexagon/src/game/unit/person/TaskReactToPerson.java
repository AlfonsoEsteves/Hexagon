package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskReactToPerson extends Task {

    public static TaskReactToPerson instance = new TaskReactToPerson();

    private TaskReactToPerson() {
        super(5.5, 3);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if (Map.has(tileX, tileY, Person.is.and(x -> ((Person)x).getSuperLeader() != ((Person)unit).getSuperLeader())) != null) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Person leader = person.getSuperLeader();
        for(int[] p : MapIter.of(range)) {
            Person other = (Person)Map.has(unit.x + p[0], unit.y + p[1], Person.is.and(x -> ((Person)x).getSuperLeader() != ((Person)unit).getSuperLeader()));
            if (other != null) {
                Person otherLeader = other.getSuperLeader();
                if(leader != otherLeader) {
                    if(leader == person) {
                        person.leader = otherLeader;
                    }
                    else{
                        if(otherLeader == other) {
                            other.leader = leader;
                        }
                        else{
                            other.damage(person.getDamage());
                        }
                    }
                    break;
                }
            }
        }
    }
}
