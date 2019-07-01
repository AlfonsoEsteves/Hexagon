package game.game.unit;

import game.*;
import game.game.unit.game.unit.person.Person;

public class TaskDestroy extends Task{

    public static TaskDestroy instance = new TaskDestroy();

    private TaskDestroy() {
        super(5, 1);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if (Map.overTile(tileX, tileY) == Tile.wall) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        for(int[] p : MapIter.of(range)) {
            if (Map.overTile(unit.x + p[0], unit.y + p[1]) == Tile.wall) {
                if(Rnd.nextInt(5) == 0) {
                    Map.overTile[unit.x + p[0]][unit.y + p[1]] = Tile.missingWall;
                }
                return;
            }
        }
    }
}
