package game.unit.person;

import game.unit.Unit;

public class MemoryStaticPoint extends Memory {

    public int x;
    public int y;

    public MemoryStaticPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean shouldBeForgoten(Unit owner) {
        return false;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
