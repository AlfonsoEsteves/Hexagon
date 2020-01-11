package game.unit.person;

import game.Map;
import game.OTId;
import game.unit.Task;
import game.unit.Unit;

import java.util.Collections;

public class TaskCreateWeapon extends Task {

    public static TaskCreateWeapon createAnvilWeapon = new TaskCreateWeapon(OTId.anvil);
    public static TaskCreateWeapon createCarpentryWeapon = new TaskCreateWeapon(OTId.carpentry);

    private OTId workshop;

    private TaskCreateWeapon(OTId workshop) {
        super(4, -1, 0);
        this.workshop = workshop;
    }

    @Override
    public boolean applies(Unit unit) {
        Person person = (Person)unit;
        if(person.jobMemory != null) {
            if (person.jobMemory.missingWood == 0 && person.jobMemory.missingIron == 0) {
                if (person.carrying.contains(workshop.tranformsItem)) {
                    if (Collections.frequency(person.carrying, workshop.providesItem) < 2) {
                        return true;
                    }
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
        unit.cancelTask();
    }

    @Override
    public Memory getDestination(Unit unit) {
        Person person = (Person)unit;
        return person.jobMemory;
    }
}
