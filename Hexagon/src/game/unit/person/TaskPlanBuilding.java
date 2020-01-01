package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.TaskScan;
import game.unit.TaskTravel;
import game.unit.Unit;
import gui.MainPanel;

public class TaskPlanBuilding extends TaskTravel {

    public static int positionVariation = 6;

    public static TaskPlanBuilding instance = new TaskPlanBuilding();

    private TaskPlanBuilding()
    {
        super(12, 2);
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        if(person.roomPosition == null || person.blacksmithPosition == null || person.carpentryPosition == null || person.storagePosition == null) {
            if(person.carrying.contains(Item.stone) && Map.distance(person.x - person.getSuperLeader().usualX, person.y - person.getSuperLeader().usualY) < person.goingBackDistance / 2) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;

        OTId toBeBuilt = null;
        int size = 2;
        int r = Rnd.nextInt(4);
        for(int i = 0; i < 4; i++) {
            int x = (i + r) % 4;
            if(x == 0 && person.roomPosition == null) {
                toBeBuilt = OTId.missingBed;
                person.roomPosition = new int[]{person.destinationX, person.destinationY};
                break;
            }
            else if(x == 1 && person.blacksmithPosition == null) {
                toBeBuilt = OTId.missingAnvil;
                person.blacksmithPosition = new int[]{person.destinationX, person.destinationY};
                break;
            }
            else if(x == 2 && person.carpentryPosition == null) {
                toBeBuilt = OTId.missingCarpentry;
                person.carpentryPosition = new int[]{person.destinationX, person.destinationY};
                break;
            }
            else if(x == 3 && person.storagePosition == null) {
                toBeBuilt = OTId.missingDepot;
                person.storagePosition = new int[]{person.destinationX, person.destinationY};
                break;
            }
        }

        Debug.check(person.carrying.contains(Item.stone));
        Debug.check(toBeBuilt != null);

        Building building  = new Building(person.destinationX, person.destinationY, person);

        int doorCount = Rnd.nextInt(size * 6 - 1);
        for (int[] p : MapIter.of(size)) {
            int x = person.destinationX + p[0];
            int y = person.destinationY + p[1];
            if (Map.distance(person.destinationX - x, person.destinationY - y) == size) {
                if(x == person.x && y == person.y) {
                    Map.overTile[x][y] = new OverTile(OTId.wall, x, y, building);
                    person.carrying.remove(Item.stone);
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
                Map.overTile[person.destinationX + p[0]][person.destinationY + p[1]] = new OverTile(OTId.missingDepot, person.destinationX + p[0], person.destinationY + p[1], building);
            }
        }
        else {
            Map.overTile[person.destinationX][person.destinationY] = new OverTile(toBeBuilt, person.destinationX, person.destinationY, building);
        }
    }

    @Override
    public void findDestination(Unit unit) {
        applies = false;
        int rndX = Rnd.nextInt(positionVariation * 2 + 1) - positionVariation;
        int rndY = Rnd.nextInt(positionVariation * 2 + 1) - positionVariation;
        // Bear in mind that the unit will get closer until it reaches the wall, and then it will build it
        // if the unit is already beyond (inside) the wall, then he would build a wall in an incorrect place
        if(Map.distance(rndX, rndY) > 1) {
            destinationX = unit.x + rndX;
            destinationY = unit.y + rndY;
            if (checkPickedPosition(destinationX, destinationY, 2)) {
                applies = true;
                priority = maxPriorityPossible;
            }
        }
    }

    private boolean checkPickedPosition(int pickedX, int pickedY, int size) {
        for(int[] p : MapIter.of(size + 1)) {
            if(Map.underTile(pickedX + p[0], pickedY + p[1]) != Tile.grass ||
                    Map.overTile(pickedX + p[0], pickedY + p[1]) != null) {
                return false;
            }
        }
        return true;
    }
}