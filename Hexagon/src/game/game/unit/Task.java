package game.game.unit;

public abstract class Task {

    public double priority;
    public int range; // The range at which the task can be performed

    public Task(double priority) {
        this.priority = priority;
        range = 1;
    }

    public abstract boolean applies(Unit unit, int tileX, int tileY);

    public abstract void execute(Unit unit);
}
