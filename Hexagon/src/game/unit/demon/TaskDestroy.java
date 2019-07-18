package game.unit.demon;

import game.*;
import game.unit.Task;
import game.unit.Unit;

public class TaskDestroy extends Task {

    public static TaskDestroy instance = new TaskDestroy();

    private TaskDestroy() {
        super(5, 1);
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
                    overTile.id = overTile.id.missingVersion;
                }
                return;
            }
        }
    }
}
