package game.unit.demon;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskDestroy extends Task {

    public static TaskDestroy instance = new TaskDestroy();

    private TaskDestroy() {
        super(1, 1);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if (Map.has(tileX, tileY, OverTile.isDestroyable) != null) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        for(int[] p : MapIter.of(range)) {
            OverTile overTile = Map.has(unit.x + p[0], unit.y + p[1], OverTile.isDestroyable);
            if (overTile != null) {
                if(Rnd.nextInt(5) == 0) {
                    // I need to create a new overtile, it can not just reset the overtile id
                    // Because if an overtile is destroyed, it could imediatele be forgotten
                    OverTile newOverTile = new OverTile(overTile.id.missingVersion, overTile.x, overTile.y);
                    Map.overTile[newOverTile.x][newOverTile.y] = newOverTile;
                    Map.queueExecutable(newOverTile, OTIdMissingBuilding.timeToBeForgot);
                }
                return;
            }
        }
    }
}
