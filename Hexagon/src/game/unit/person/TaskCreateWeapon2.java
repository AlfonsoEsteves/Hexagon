package game.unit.person;

import game.Map;
import game.OTId;
import game.unit.TaskScan;
import game.unit.TaskTravel;
import game.unit.Unit;

import java.util.Collections;

public class TaskCreateWeapon2 extends TaskTravel {

    public static TaskCreateWeapon2 createAnvilWeapon = new TaskCreateWeapon2(OTId.anvil);
    public static TaskCreateWeapon2 createCarpentryWeapon = new TaskCreateWeapon2(OTId.carpentry);

    private OTId workshop;

    private TaskCreateWeapon2(OTId workshop) {
        super(4, 0);
        this.workshop = workshop;
    }

    @Override
    public boolean applies(Unit unit) {






        return false;
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        person.carrying.remove(workshop.tranformsItem);
        person.carrying.add(workshop.providesItem);
    }

    @Override
    public void findDestination(Unit unit) {
        Person person = (Person)unit;
        destinationX = person.blacksmithPosition[0];
        destinationY = person.blacksmithPosition[1];

    }
}
