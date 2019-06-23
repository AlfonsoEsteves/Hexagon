package game.game.unit;

import game.Item;
import game.Map;
import game.Tile;

public class TaskBuildWall extends Task{

    public TaskBuildWall() {
        super(5);
    }

    @Override
    public double priority(Unit unit, int tileX, int tileY, int distance) {
        Person person = (Person)unit;
        if(person.carrying.contains(Item.stone)){
            if (Map.has(tileX, tileY, Tile.missingWall) != null) {
                return maxPriorityPossible / distance;
            }
        }
        else {
            if (Map.has(tileX, tileY, Tile.stone) != null) {
                double missingWallDistance = person.remembersAtDistance(Tile.missingWall);
                if (missingWallDistance != -1) {
                    return maxPriorityPossible / (missingWallDistance + distance);
                }
            }
            else if (Map.has(tileX, tileY, Tile.missingWall) != null) {
                person.remembersAtDistance(Tile.missingWall, tileX, tileY, distance);
            }
            return 0;
        }
    }
}
