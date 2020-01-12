package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;
import gui.MainPanel;

public class TaskPlanBuilding extends Task {

    public static int positionVariation = 6;

    public static TaskPlanBuilding instance = new TaskPlanBuilding();

    private TaskPlanBuilding()
    {
        super(12,-1, 3);
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        if(person.memoryBuildings[Person.roomBuilding] == null || person.memoryBuildings[Person.jobBuilding] == null) {
            if(person.carrying.contains(Item.stone)) {
                if(person.buildingPosition == null) {
                    int rndX = Rnd.nextInt(positionVariation * 2 + 1) - positionVariation;
                    int rndY = Rnd.nextInt(positionVariation * 2 + 1) - positionVariation;
                    person.buildingPosition = new MemoryStaticPoint(person.x + rndX, person.y + rndY);
                }
                // Bear in mind that the unit will get closer until it reaches the wall, and then it will build it
                // If the unit is already beyond (inside) the wall, then he would build a wall in an incorrect place
                if(Map.distance(person.x - person.buildingPosition.getX(), person.y - person.buildingPosition.getX()) > 1) {
                    if (checkPickedPosition(person.buildingPosition.getX(), person.buildingPosition.getY(), 2)) {
                        return true;
                    }
                    else{
                        person.buildingPosition = null;
                    }
                }
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
            if(x == 0 && person.memoryBuildings[Person.roomBuilding] == null) {
                toBeBuilt = OTId.missingBed;
                person.memoryBuildings[Person.roomBuilding] = new MemoryBuilding(person.goalMemory.getX(), person.goalMemory.getY(), 0, 1, 6);
                break;
            }
            else if(x == 1 && person.memoryBuildings[Person.jobBuilding] == null) {
                toBeBuilt = person.job.missingVersion;
                int neededIron = person.job == OTId.anvil ? 1 : 0;
                int neededWood = person.job == OTId.carpentry ? 1 : 0;
                int missingStone = person.job == OTId.depot ? 18 : 11;
                person.memoryBuildings[Person.jobBuilding] = new MemoryBuilding(person.goalMemory.getX(), person.goalMemory.getY(), neededIron, neededWood, missingStone);
                break;
            }
        }
        person.buildingPosition = null;

        Debug.check(person.carrying.contains(Item.stone));
        Debug.check(toBeBuilt != null);

        Building building = new Building(person.goalMemory.getX(), person.goalMemory.getY(), person);

        int doorCount = Rnd.nextInt(size * 6 - 1);
        for (int[] p : MapIter.of(size)) {
            int x = person.goalMemory.getX() + p[0];
            int y = person.goalMemory.getY() + p[1];
            if (Map.distance(person.goalMemory.getX() - x, person.goalMemory.getY() - y) == size) {
                if(Map.distance(person.x - x, person.y - y) == 1) {
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
                Map.overTile[person.goalMemory.getX() + p[0]][person.goalMemory.getY() + p[1]] = new OverTile(OTId.missingDepot, person.goalMemory.getX() + p[0], person.goalMemory.getY() + p[1], building);
            }
        }
        else {
            Map.overTile[person.goalMemory.getX()][person.goalMemory.getY()] = new OverTile(toBeBuilt, person.goalMemory.getX(), person.goalMemory.getY(), building);
        }

        unit.cancelTask();
    }

    @Override
    public Memory getDestination(Unit unit) {
        Person person = (Person)unit;
        return person.buildingPosition;
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