package game.unit.person;

import game.Map;
import game.unit.Unit;

public class MemoryUnit extends Memory {

    public Unit unit;

    public MemoryUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean forgetIfNeeded(Unit owner) {
        return !unit.alive || Map.distance(owner.x - unit.x, owner.y - unit.y) > Person.visionRange;
    }

    @Override
    public int getX() {
        return unit.x;
    }

    @Override
    public int getY() {
        return unit.y;
    }
}
