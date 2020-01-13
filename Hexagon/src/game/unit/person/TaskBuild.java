package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskBuild extends Task {

    public static TaskBuild taskBuildRoom = new TaskBuild(Person.roomBuilding);
    public static TaskBuild taskBuildJob = new TaskBuild(Person.jobBuilding);

    public int buildingIndex = 0;

    private TaskBuild(int buildingIndex) {
        super(5, Person.visionRange, 1);
        this.buildingIndex = buildingIndex;
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        MemoryBuilding memoryBuilding = person.memoryBuildings[buildingIndex];
        if(memoryBuilding != null) {
            if (memoryBuilding.missingStone > 0 && person.carrying.contains(Item.stone)) {
                return true;
            }
            if (memoryBuilding.missingWood > 0 && person.carrying.contains(Item.wood)) {
                return true;
            }
            if (memoryBuilding.missingIron > 0 && person.carrying.contains(Item.iron)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Memory appliesInTile(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(Map.has(tileX, tileY, OverTile.personCarriesMaterial(person).and(x -> ((Building)((OverTile)x).state).x == person.memoryBuildings[buildingIndex].x && ((Building)((OverTile)x).state).y == person.memoryBuildings[buildingIndex].y)) != null) {
            return new MemoryStaticPoint(tileX, tileY);
        }
        return null;
    }

    @Override
    public Memory getDestination(Unit unit) {
        return ((Person)unit).memoryBuildings[buildingIndex];
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        OverTile missing = null;
        int[] position = null;
        for(int[] p : MapIter.of(executionRange)) {
            position = p;
            missing = Map.has(unit.x + p[0], unit.y + p[1], OverTile.personCarriesMaterial(person).and(x -> ((Building)((OverTile)x).state).x == person.memoryBuildings[buildingIndex].x && ((Building)((OverTile)x).state).y == person.memoryBuildings[buildingIndex].y));
            if (missing != null) {
                person.memoryBuildings[buildingIndex].addMaterial(missing.id.makeWith);
                person.carrying.remove(missing.id.makeWith);
                missing.id = missing.id.completedVersion;
                ((Building)missing.state).placed++;
                break;
            }
        }
        unit.cancelTask();
    }
}
