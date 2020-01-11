package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.demon.Demon;
import game.unit.Unit;

public class TaskFight extends Task {

    public static TaskFight taskFightMelee = new TaskFight(9.9, 1);
    public static TaskFight taskFightLongDistance = new TaskFight(10, 3);

    private TaskFight(double maxPriorityPossible, int range) {
        super(maxPriorityPossible, Person.visionRange, range);
    }

    @Override
    public boolean applies(Unit unit) {
        return scanRange == 1 || ((Person)unit).carrying.contains(Item.bow);
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
