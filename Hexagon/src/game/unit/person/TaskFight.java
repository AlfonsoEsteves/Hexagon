package game.unit.person;

import game.*;
import game.unit.demon.Demon;
import game.unit.Task;
import game.unit.Unit;

public class TaskFight extends Task {

    public static TaskFight instance = new TaskFight();

    private TaskFight() {
        super(10, 3);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if(Map.has(tileX, tileY, Demon.is) != null){
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        for(int[] p : MapIter.of(range)) {
            Demon demon = Map.has(unit.x + p[0], unit.y + p[1], Demon.is);
            if (demon != null) {
                person.causeDamage(demon);
                return;
            }
        }
    }
}
