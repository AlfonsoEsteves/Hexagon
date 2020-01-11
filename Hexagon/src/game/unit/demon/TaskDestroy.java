package game.unit.demon;

import game.*;
import game.unit.Task;
import game.unit.Unit;
import game.unit.person.Memory;
import game.unit.person.MemoryStaticPoint;
import game.unit.person.Person;

public class TaskDestroy extends Task {

    public static TaskDestroy instance = new TaskDestroy();

    private TaskDestroy() {
        super(1, Person.visionRange, 1);
    }

    @Override
    public Memory appliesInTile(Unit unit, int tileX, int tileY) {
        if(Map.has(tileX, tileY, OverTile.isDestroyable) != null) {
            return new MemoryStaticPoint(tileX, tileY);
        }
        return null;
    }

    @Override
    public void execute(Unit unit) {
        for(int[] p : MapIter.of(executionRange)) {
            OverTile overTile = Map.has(unit.x + p[0], unit.y + p[1], OverTile.isDestroyable);
            if (overTile != null) {
                if(Rnd.nextInt(5) == 0) {
                    overTile.id = overTile.id.missingVersion;
                    Building building = (Building)overTile.state;
                    building.placed --;
                    if(building.placed == 0) {
                        for(int[] p2 : MapIter.of(2)) {
                            Map.overTile[building.x + p2[0]][building.y + p2[1]] = null;
                        }
                    }
                }
                return;
            }
        }
    }
}
