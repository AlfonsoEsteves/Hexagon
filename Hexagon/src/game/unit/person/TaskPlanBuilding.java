package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;
import gui.MainPanel;

public class TaskPlanBuilding extends Task {

    public static TaskPlanBuilding instance = new TaskPlanBuilding();

    private TaskPlanBuilding()
    {
        super(12, 2);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(tileX == person.planX && tileY == person.planY) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;

        OTId toBeBuilt = null;
        int size = 2;
        if (Rnd.nextInt(4) == 0) {
            toBeBuilt = OTId.missingDepot;
        }
        else if (Rnd.nextInt(3) == 0) {
            toBeBuilt = OTId.missingBed;
        }
        else if (Rnd.nextInt(2) == 0) {
            toBeBuilt = OTId.missingAnvil;
        }
        else {
            toBeBuilt = OTId.missingCarpentry;
        }

        Building building;
        if(toBeBuilt == OTId.missingDepot) {
            building = new BuildingStorage(person.planX, person.planY, person);
        }
        else {
            building = new Building(person.planX, person.planY);
        }

        int doorCount = Rnd.nextInt(size * 6 - 1);
        for (int[] p : MapIter.of(size)) {
            int x = person.planX + p[0];
            int y = person.planY + p[1];
            if (Map.distance(person.planX, person.planY, x, y) == size) {
                if(x == person.x && y == person.y) {
                    Map.overTile[x][y] = new OverTile(OTId.wall, x, y, building);
                }
                else {
                    if (doorCount == 0) {
                        Map.overTile[x][y] = new OverTile(OTId.missingDoor, x, y, building);
                    } else {
                        Map.overTile[x][y] = new OverTile(OTId.missingWall, x, y, building);
                    }
                    doorCount--;
                }
            }
        }

        if (toBeBuilt == OTId.missingDepot) {
            for (int[] p : MapIter.of(1)) {
                Map.overTile[person.planX + p[0]][person.planY + p[1]] = new OverTile(OTId.missingDepot, person.planX + p[0], person.planY + p[1], building);
            }
        }
        else {
            Map.overTile[person.planX][person.planY] = new OverTile(toBeBuilt, person.planX, person.planY, building);
        }

        person.planning = false;
    }

    /*private void planBuilding(OTId missingDoor, int x, int y) {
        OverTile overTile = new OverTile(missingDoor, x, y);
        Map.overTile[x][y] = overTile;
        Map.queueExecutable(overTile, OTIdMissingBuilding.timeToBeForgot);
    }*/
}
