package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

import java.util.Collections;

public class TaskSell extends Task {

    public static TaskSell instance = new TaskSell();

    private TaskSell() {
        super(3, 2,0);
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        return person.job == OTId.depot &&
                person.memoryBuildings[Person.jobBuilding] != null &&
                person.memoryBuildings[Person.jobBuilding].missingStone == 0 &&
                person.memoryBuildings[Person.jobBuilding].stored > 0;
    }

    @Override
    public Memory appliesInTile(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        OverTile depot = Map.has(tileX, tileY, OTId.door.overTileIs.and(x -> ((Building)((OverTile)x).state).x == person.memoryBuildings[Person.jobBuilding].x && ((Building)((OverTile)x).state).y == person.memoryBuildings[Person.jobBuilding].y));
        if (depot != null && ((Building)depot.state).placed == 19) {
            return new MemoryStaticPoint(tileX, tileY);
        }
        return null;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        person.selling = true;
    }

    @Override
    public Memory getDestination(Unit unit) {
        Person person = (Person)unit;
        return person.memoryBuildings[Person.jobBuilding];
    }
}
