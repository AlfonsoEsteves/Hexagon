package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public abstract class TaskBuild extends Task {

    public static TaskBuild taskBuildRoom = new TaskBuild() {
        @Override
        protected MemoryBuilding getBuilding(Person person) {
            return person.roomMemory;
        }
    };

    public static TaskBuild taskBuildJob = new TaskBuild() {
        @Override
        protected MemoryBuilding getBuilding(Person person) {
            return person.jobMemory;
        }
    };

    private TaskBuild()
    {
        super(5, Person.visionRange, 1);
    }

    protected abstract MemoryBuilding getBuilding(Person person);

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        MemoryBuilding memoryBuilding = getBuilding(person);
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
    public boolean appliesInTile(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        return Map.has(tileX, tileY, OverTile.personCarriesMaterial(person).and(x -> ((Building)((OverTile)x).state).owner == person)) != null;
    }

    @Override
    public int[] getDestination(Unit unit) {
        MemoryBuilding memoryBuilding = getBuilding((Person)unit);
        return new int[]{memoryBuilding.x, memoryBuilding.y};
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        OverTile missing = null;
        int[] position = null;
        for(int[] p : MapIter.of(executionRange)) {
            position = p;
            missing = Map.has(unit.x + p[0], unit.y + p[1], OverTile.personCarriesMaterial(person).and(x -> ((Building)((OverTile)x).state).owner == person));
            if (missing != null) {
                person.carrying.remove(missing.id.makeWith);
                missing.id = missing.id.completedVersion;
                ((Building)missing.state).placed++;
                break;
            }
        }
    }
}
