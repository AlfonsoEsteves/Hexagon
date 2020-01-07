package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.demon.Demon;
import game.unit.Unit;

public class TaskFight extends Task {

    public static TaskFight taskFightMelee = new TaskFight(1);
    public static TaskFight taskFightLongDistance = new TaskFight(3);

    private TaskFight(int range) {
        super(10, Person.visionRange, range);
    }

    @Override
    public boolean appliesInTile(Unit unit, int tileX, int tileY) {
        return Map.has(tileX, tileY, Demon.is) != null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        for(int[] p : MapIter.of(executionRange)) {
            Demon demon = Map.has(unit.x + p[0], unit.y + p[1], Demon.is);
            if (demon != null) {
                person.causeDamage(demon, executionRange);
                return;
            }
        }
    }
}
