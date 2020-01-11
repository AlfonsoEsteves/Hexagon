package game.unit.person;

import game.unit.Unit;

public abstract class Memory {
    public abstract boolean shouldBeForgoten(Unit owner);
    public abstract int getX();
    public abstract int getY();
}
