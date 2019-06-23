package game.game.unit;

public abstract class Task {

    public double maxPriorityPossible;

    public Task(double maxPriorityPossible) {
        this.maxPriorityPossible = maxPriorityPossible;
    }

    public abstract double priority(Unit unit, int tileX, int tileY, int distance);

    public abstract void execute(Unit unit);
}
