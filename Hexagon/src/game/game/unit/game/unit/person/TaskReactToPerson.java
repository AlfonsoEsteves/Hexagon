package game.game.unit.game.unit.person;

import game.*;
import game.game.unit.Demon;
import game.game.unit.Task;
import game.game.unit.Unit;

import java.util.List;
import java.util.function.Predicate;

public class TaskReactToPerson extends Task {

    public static TaskReactToPerson instance = new TaskReactToPerson();

    private TaskReactToPerson() {
        super(4, 3);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if (Map.has(tileX, tileY, Person.predicate.and(x -> ((Person)x).getAlliance() != ((Person)unit).getAlliance())) != null) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Person leader = person.getAlliance();
        for(int[] p : MapIter.of(range)) {
            Person other = (Person)Map.has(unit.x + p[0], unit.y + p[1], Person.predicate.and(x -> ((Person)x).getAlliance() != ((Person)unit).getAlliance()));
            if (other != null) {
                Person otherLeader = other.getAlliance();
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
                }
            }
        }
    }
}
