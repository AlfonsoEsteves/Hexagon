package game.unit;

public abstract class Task {

    public double maxPriorityPossible;

    public Task(double maxPriorityPossible) {
        this.maxPriorityPossible = maxPriorityPossible;
    }

    public abstract boolean applies(Unit unit);

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
