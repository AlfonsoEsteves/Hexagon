package game.unit;

public abstract class TaskTravel extends Task {

    public TaskTravel(double maxPriorityPossible) {
        super(maxPriorityPossible, Unit.pathfindingDistanceLimit);
    }

    public abstract int[] findDestination();
}
