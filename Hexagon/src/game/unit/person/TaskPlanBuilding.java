package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskPlanBuilding extends Task {

    public static TaskPlanBuilding instance = new TaskPlanBuilding();

    private TaskPlanBuilding()
    {
        super(12, 1);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        return true;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;

        OTId toBeBuilt = null;
        int size = 2;
        if (Rnd.nextInt(4) == 0) {
            toBeBuilt = OTId.missingDepot;
        }
        else if (Rnd.nextInt(4) == 0) {
            toBeBuilt = OTId.missingBed;
        }
        else /*if (Rnd.nextInt(4) == 0)*/ {
            toBeBuilt = OTId.missingAnvil;
        }

        int doorCount = Rnd.nextInt(size * 6);
        for (int[] p : MapIter.of(size)) {
            int x = person.x + p[0];
            int y = person.y + p[1];
            if (Map.distance(person.x, person.y, x, y) == size) {
                if (doorCount == 0) {
                    planBuilding(OTId.missingDoor, x, y);
                } else {
                    planBuilding(OTId.missingWall, x, y);
                }
                doorCount--;
            }
        }

        if (toBeBuilt == OTId.missingDepot) {
            for (int[] p : MapIter.of(1)) {
                planBuilding(OTId.missingDepot, person.x + p[0], person.y + p[1]);
            }
        }
        else {
            planBuilding(toBeBuilt, person.x, person.y);
        }
    }

    private void planBuilding(OTId missingDoor, int x, int y) {
        OverTile overTile = new OverTile(missingDoor, x, y);
        Map.overTile[x][y] = overTile;
        Map.queueExecutable(overTile, OTIdMissingBuilding.timeToBeForgot);
    }
}
