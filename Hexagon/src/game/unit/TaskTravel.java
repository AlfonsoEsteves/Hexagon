package game.unit;

public abstract class TaskTravel extends Task {

    // If I implement multithreading, each thread should have its own set of these variables
    public boolean applies;
    public int destinationX;
    public int destinationY;
    public double priority;

    public TaskTravel(double maxPriorityPossible, int range) {
        super(maxPriorityPossible, range);
    }

    public abstract void findDestination(Unit unit);
}
