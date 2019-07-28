package game.unit.person;

import game.Dropped;
import game.Item;
import game.Map;
import game.OTId;
import game.unit.Task;
import game.unit.Unit;

import java.util.Collections;

public class TaskGoBackToBase extends Task {

    public static TaskGoBackToBase instance = new TaskGoBackToBase();

    private TaskGoBackToBase() {
        super(11, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        Person superLeader = person.getSuperLeader();
        int distance = Map.distance(person.x, person.y, superLeader.usualX, superLeader.usualY);
        if (distance > Map.distance(tileX, tileY, superLeader.usualX, superLeader.usualY)) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
    }
}
