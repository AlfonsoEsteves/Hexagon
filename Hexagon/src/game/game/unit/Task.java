package game.game.unit;

public abstract class Task {

    public double priority;

    public Task(double priority) {
        this.priority = priority;
    }

    public abstract boolean applies(Unit unit, int tileX, int tileY);

    public abstract void execute(Unit unit);
}
