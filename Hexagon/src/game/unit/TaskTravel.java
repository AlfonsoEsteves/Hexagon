package game.unit;

public abstract class TaskTravel extends Task {

    public TaskTravel(double maxPriorityPossible, int range) {
        super(maxPriorityPossible, range);
    }

    public abstract int[] getDestination(Unit unit);
}
