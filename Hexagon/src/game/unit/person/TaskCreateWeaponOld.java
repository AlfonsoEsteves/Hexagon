package game.unit.person;

import game.*;
import game.unit.TaskScan;
import game.unit.Unit;

import java.util.Collections;

public class TaskCreateWeaponOld extends TaskScan {

    public static TaskCreateWeaponOld createAnvilWeapon = new TaskCreateWeaponOld(OTId.anvil);
    public static TaskCreateWeaponOld createCarpentryWeapon = new TaskCreateWeaponOld(OTId.carpentry);

    private OTId workshop;

    private TaskCreateWeaponOld(OTId workshop) {
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
