package game.game.unit.game.unit.person;

import game.*;
import game.game.unit.Demon;
import game.game.unit.Task;
import game.game.unit.Unit;

public class TaskFight extends Task {

    public static TaskFight instance = new TaskFight();

    private TaskFight() {
        super(10, 3);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if(Map.has(tileX, tileY, Demon.demonIdentity) != null){
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        for(int[] p : MapIter.of(range)) {
            Searchable searchable = Map.has(unit.x + p[0], unit.y + p[1], Demon.demonIdentity);
            if (searchable != null) {
                Demon demon = (Demon) searchable;
                demon.damage(person.carrying.contains(Item.sword) ? 10 : 5);
                return;
            }
        }
    }
}
