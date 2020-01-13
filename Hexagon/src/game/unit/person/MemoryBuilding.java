package game.unit.person;

import game.*;
import game.unit.Unit;

public class MemoryBuilding extends Memory {

    public int x;
    public int y;
    public int buildingIndex;
    public int missingIron;
    public int missingWood;
    public int missingStone;

    public MemoryBuilding(int x, int y, int buildingIndex, int missingIron, int missingWood, int missingStone) {
        this.x = x;
        this.y = y;
        this.buildingIndex = buildingIndex;
        this.missingIron = missingIron;
        this.missingWood = missingWood;
        this.missingStone = missingStone;
    }

    @Override
    public boolean forgetIfNeeded(Unit owner) {
        if(Map.distance(owner.x - x, owner.y - y) <= Person.visionRange) {
            OverTile overTile = Map.overTile(x, y);
            if(overTile != null &&
                    overTile.state != null &&
                    overTile.state instanceof Building &&
                    ((Building)overTile.state).owner == owner) {
                updateMissingMaterial();
                return false;
            }
            ((Person)owner).memoryBuildings[buildingIndex] = null;
            return true;
        }
        return false;
    }

    private void updateMissingMaterial() {
        missingStone = 0;
        missingWood = 0;
        missingIron = 0;
        for(int[] p : MapIter.of(2)) {
            OverTile overTile = Map.overTile(x + p[0], y + p[1]);
            if(overTile != null) {
                if (overTile.id == OTId.missingWall || overTile.id == OTId.missingDepot) {
                    missingStone++;
                }
                else if (overTile.id == OTId.missingDoor || overTile.id == OTId.missingBed || overTile.id == OTId.missingCarpentry) {
                    missingWood++;
                }
                else if (overTile.id == OTId.missingAnvil) {
                    missingIron++;
                }
            }
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void addMaterial(Item makeWith) {
        if(makeWith == Item.stone) {
            missingStone --;
        }
        else if(makeWith == Item.wood) {
            missingWood --;
        }
        else if(makeWith == Item.iron) {
            missingIron --;
        }
    }
}
