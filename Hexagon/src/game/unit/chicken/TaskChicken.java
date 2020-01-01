package game.unit.chicken;

import game.Map;
import game.OTId;
import game.OverTile;
import game.Rnd;
import game.unit.Task;
import game.unit.TaskScan;
import game.unit.Unit;
import game.unit.person.Person;
import gui.Main;
import gui.MainPanel;

public class TaskChicken extends TaskScan {

    public static TaskChicken instance = new TaskChicken();

    private TaskChicken() {
        super(10, 0);
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        if (Map.has(tileX, tileY, OTId.richGrass.overTileIs) != null) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Chicken chicken = (Chicken) unit;
        if (Map.has(chicken.x, chicken.y, OTId.richGrass.overTileIs) != null) {
            Map.overTile[chicken.x][chicken.y].id = OTId.richGrass.depletedVersion;
            Map.queueExecutable(Map.overTile[chicken.x][chicken.y], 150 + Rnd.nextInt(150));
            chicken.food += 30;
            if (chicken.food >= 100) {
                chicken.food = 100;
                chicken.grown = true;
            }
        }
    }
}
