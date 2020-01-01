package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.TaskScan;
import game.unit.Unit;

import java.util.Collections;

public class TaskCreateWeapon extends TaskScan {

    public static TaskCreateWeapon createAnvilWeapon = new TaskCreateWeapon(OTId.anvil);
    public static TaskCreateWeapon createCarpentryWeapon = new TaskCreateWeapon(OTId.carpentry);

    private OTId workshop;

    private TaskCreateWeapon(OTId workshop) {
        super(4, 0);
        this.workshop = workshop;
    }

    @Override
    public boolean applies(Unit unit, int tileX, int tileY) {
        Person person = (Person)unit;
        if(Collections.frequency(person.carrying, workshop.providesItem) < 2) {
            if (person.carrying.contains(workshop.tranformsItem)) {
                if (Map.has(tileX, tileY, workshop.overTileIs) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        person.carrying.remove(workshop.tranformsItem);
        person.carrying.add(workshop.providesItem);
    }
}
