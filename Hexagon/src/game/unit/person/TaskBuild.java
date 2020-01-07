package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskBuild extends Task {

    public static TaskBuild taskBuildStoneThings = new TaskBuild(Item.stone);
    public static TaskBuild taskBuildWoodThings = new TaskBuild(Item.wood);
    public static TaskBuild taskBuildIronThings = new TaskBuild(Item.iron);

    private Item material;

    private TaskBuild(Item material)
    {
        super(5, Person.visionRange, 1);
        this.material = material;
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        return person.carrying.contains(material);
    }

    @Override
    public boolean appliesInTile(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        return Map.has(tileX, tileY, OverTile.makeWith(material).and(x -> ((Building)((OverTile)x).state).owner == person)) != null;
    }

    @Override
    public int[] getDestination(Unit unit) {
        Person person = (Person)unit;
        return person.buildingPosition;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        OverTile missing = null;
        int[] position = null;
        for(int[] p : MapIter.of(executionRange)) {
            position = p;
            missing = Map.has(unit.x + p[0], unit.y + p[1], OverTile.makeWith(material));
            if (missing != null) {
                person.carrying.remove(material);
                missing.id = missing.id.completedVersion;
                ((Building)missing.state).placed++;
                break;
            }
        }
    }
}
