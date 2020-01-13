package game.unit.person;

import game.Item;
import game.unit.Unit;

public abstract class Memory {
    public abstract boolean forgetIfNeeded(Unit owner);
    public abstract int getX();
    public abstract int getY();
}
