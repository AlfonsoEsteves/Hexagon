package game.game.unit;

import game.Item;
import game.Map;
import game.Rnd;
import game.Tile;

public class TaskHuntPerson extends Task{

    public static TaskHuntPerson instance = new TaskHuntPerson();

    private TaskHuntPerson() {
        super(10);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if (Map.has(tileX, tileY, Person.personIdentity) != null) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)Map.has(unit.x, unit.y, Person.personIdentity);
        if(person != null) {
            unit.life -= (10 + Rnd.nextInt(30)) * (person.carrying.contains(Item.sword) ? 2 : 1);
            person.life -= 10 + Rnd.nextInt(30);
            if(person.life <= 0) {
                person.removeFromTileAndDestroy();
            }
        }
        unit.life--;
        if(unit.life <= 0) {
            unit.removeFromTileAndDestroy();
        }
    }
}
