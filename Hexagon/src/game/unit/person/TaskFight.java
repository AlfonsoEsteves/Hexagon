package game.unit.person;

import game.*;
import game.unit.demon.Demon;
import game.unit.Task;
import game.unit.Unit;
import gui.MainPanel;

public class TaskFight extends Task {

    public static TaskFight taskFightMelee = new TaskFight(1);
    public static TaskFight taskFightLongDistance = new TaskFight(3);

    private TaskFight(int range) {
        super(10, range);
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
                person.causeDamage(demon, range);
                return;
            }
        }
    }
}
