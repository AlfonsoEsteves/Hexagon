package game.unit.person;

import game.Building;
import game.Map;
import game.OverTile;
import game.unit.Unit;

public class MemoryBuilding extends Memory {

    public int x;
    public int y;
    public int missingIron;
    public int missingWood;
    public int missingStone;

    public MemoryBuilding(int x, int y, int missingIron, int missingWood, int missingStone) {
        this.x = x;
        this.y = y;
        this.missingIron = missingIron;
        this.missingWood = missingWood;
        this.missingStone = missingStone;
    }

    @Override
    public boolean shouldBeForgoten(Unit owner) {
        if(Map.distance(owner.x - x, owner.y - y) <= Person.visionRange) {
            OverTile overTile = Map.overTile(x, y);
            if(overTile != null &&
                    overTile.state != null &&
                    overTile.state instanceof Building &&
                    ((Building)overTile.state).owner == owner) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
