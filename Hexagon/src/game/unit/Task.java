package game.unit;

public abstract class Task {

    public double maxPriorityPossible;

    public int executionRange; // The range at which the task can be performed
    public int scanRange; // The range at which the task can be scanned for

    public Task(double maxPriorityPossible, int executionRange, int scanRange) {
        this.maxPriorityPossible = maxPriorityPossible;
        this.executionRange = executionRange;
        this.scanRange = scanRange;
    }

    public boolean applies(Unit unit) {
        return true;
    }

    public boolean appliesInTile(Unit unit, int tileX, int tileY) {
        throw new RuntimeException();
    }

    public int[] getDestination(Unit unit) {
        return null;
    }

    public abstract void execute(Unit unit);

    // I need to be able to order the scannable tasks based on priority
    // This allows me to realize when I should stop scanning
    //   I should stop scanning when I am sure that no task will be more prioritary then the current task
    // Priority calculation does not take into consideration the range
    // Because otherwise, it wouldn't be possible to order the tasks from most priority to least priority
    // Cause this order would depend on the distance to the goal
    protected double calculatePriority(int distance){
        if(distance == 0) {
            return maxPriorityPossible;
        }
        else {
            return maxPriorityPossible / distance;
        }
    }
}
