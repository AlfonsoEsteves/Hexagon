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
        super(5, 1);
        this.material = material;
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(person.carrying.contains(material)){
            if (Map.has(tileX, tileY, Tile.makeWith(material)) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        Tile missing = null;
        int[] position = null;
        for(int[] p : MapIter.of(range)) {
            position = p;
            missing = Map.has(unit.x + p[0], unit.y + p[1], Tile.makeWith(material));
            if (missing != null) {
                person.carrying.remove(material);
                Map.overTile[person.x + position[0]][person.y + position[1]] = missing.completedVersion;
                break;
            }
        }
    }
}
