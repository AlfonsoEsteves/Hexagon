package game.unit;

import game.Executable;
import game.Debug;
import game.Map;
import game.Rnd;
import game.unit.person.Memory;
import gui.MainPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Unit implements Executable {

    public static final int pathfindingDistanceLimit = 15;

    public static final double turnsPerPriorityFactor = 1.0;

    public static Unit[][] checkedTilesUnit = new Unit[Map.size][Map.size];
    public static int[][] checkedTilesTime = new int[Map.size][Map.size];

    public static int maxId = 0;

    public static Unit lastExecutedUnit;

    public int id;

    public int x;
    public int y;

    public Unit next;

    // Indicates whether the unit should be executed or not
    // Remember that when a unit gets destroyed, it is removed
    // from the map, but not from the execution queue.
    public boolean alive;

    public int life;

    public List<Task> tasks;
    public List<Task> currentScanTasks;
    public double currentTaskPriority;
    public Task currentTask;
    public int conserveTaskTime;
    public Memory goalMemory;

    private int randomDirection;

    public SurroundBehaviour surroundBehaviour;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
        alive = true;
        tasks = new ArrayList<>();
        id = maxId;
        maxId++;
        surroundBehaviour = new SurroundBehaviour();
        currentScanTasks = new ArrayList<>();
    }

    public abstract Image image();

    @Override
    public boolean alive(){
        return alive;
    }

    public void initExecute() {}

    @Override
    public void execute() {
        lastExecutedUnit = this;

        if(id == 1139) {
            MainPanel.selectedUnit = this;
            if (Map.time >= 191) {
                System.out.println();
            }
        }

        Debug.log("UNIT", toString());

        initExecute();
        if(alive) {
            if(shouldRecheckTasks()) {
                checkNormalTasks();

                // It is important to reset the scanning tasks in each execution
                // Cause they are going to be checked for each scanned tile
                // So you don't want to have unnecessary ones
                setScanTasks();

                checkScanTasks();
            }

            if(currentTask != null) {
                int distanceToDestination = Map.distance(x - goalMemory.getX(), y - goalMemory.getY());
                if(!surroundBehaviour.surrounding) {
                    if (distanceToDestination > currentTask.executionRange) {
                        int dirToDestination = Map.closestDirection(goalMemory.getX() - x, goalMemory.getY() - y);
                        if (!moveInDirection(dirToDestination)) {
                            surroundBehaviour.startSurrounding(dirToDestination, distanceToDestination);
                        }
                    } else {
                        if(currentTask.appliesInTile(this, goalMemory.getX(), goalMemory.getY()) != null) {
                            currentTask.execute(this);
                        }
                    }
                }
                if(surroundBehaviour.surrounding) {
                    surroundBehaviour.surround(this, distanceToDestination);
                }
            }
            else {
                surroundBehaviour.maxTurns = SurroundBehaviour.startingMaxTurns;
                moveRandomly();
            }

            if(alive) {
                Map.queueExecutable(this, delay());
            }
        }
    }

    private boolean shouldRecheckTasks() {
        boolean recheckTasks = true;
        if (currentTask != null) {
            if (currentTask.applies(this)){
                if(goalMemory.forgetIfNeeded(this)) {
                    cancelTask();
                } else {
                    if (Map.time > conserveTaskTime) {
                        // To keep things more performant, I don't reset the current task
                        // Just decrease the maxPriorityPossible in case the goal has moved farther
                        // or in case an obstacle appeared and it is blocking the way
                        resetPriority(currentTaskPriority - 1);
                    } else {
                        recheckTasks = false;
                    }
                }
            } else {
                cancelTask();
            }
        }
        return recheckTasks;
    }

    protected void checkNormalTasks() {
        for(Task task : tasks){
             if(task.maxPriorityPossible <= currentTaskPriority) {
                 break;
             }
             if(task.applies(this)) {
                 Memory candidateGoal = task.getDestination(this);
                 if(candidateGoal != null) {
                     int distance = Map.distance(x - candidateGoal.getX(), y - candidateGoal.getY());
                     if(distance > task.scanRange) {
                         double priority = task.calculatePriority(distance, true);
                         if(priority > currentTaskPriority) {
                             currentTask = task;
                             currentTaskPriority = priority;
                             goalMemory = candidateGoal;
                         }
                     }
                 }
             }
        }
    }

    protected void setScanTasks() {
        currentScanTasks.clear();
        for(Task task : tasks){
            if(task.maxPriorityPossible <= currentTaskPriority) {
                break;
            }
            if(task.applies(this)) {
                Memory position = task.getDestination(this);
                if(position == null) {
                    currentScanTasks.add(task);
                }
                else{
                    int distance = Map.distance(x - position.getX(), y - position.getY());
                    // Note that this distance isn't the real distance cause there may be obstacles in the way
                    // That means I can not set this task as the curret task yet
                    if(distance <= task.scanRange) {
                        currentScanTasks.add(task);
                    }
                }
            }
        }
    }

    private void checkScanTasks() {
        if(currentScanTasks.isEmpty()) {
            return;
        }
        LinkedList<Integer> queueX = new LinkedList<>();
        LinkedList<Integer> queueY = new LinkedList<>();
        queueX.add(x);
        queueY.add(y);
        int distance = 0; // The currently checked distance
        int currentIteration = 0; //The iteration number for the currently checked distance
        int nextDistanceIteration = 1; //The iteration where the unit starts considering the next distance path
        while (!queueX.isEmpty()) {
            int currentX = queueX.removeFirst();
            int currentY = queueY.removeFirst();
            processTile(currentX, currentY, distance);
            currentIteration++;
            if(currentIteration == nextDistanceIteration){
                distance++;
                if(distance >= pathfindingDistanceLimit) {
                    break;
                }
                if(currentScanTasks.get(0).calculatePriority(distance, false) <= currentTaskPriority) {
                    break;
                }
            }
            if (Map.steppable(this, currentX, currentY)) {
                for (int i = 0; i < 6; i++) {
                    int newX = currentX + Map.getX(i);
                    int newY = currentY + Map.getY(i);
                    if(checkedTilesUnit[newX][newY] != this || checkedTilesTime[newX][newY] != Map.time) {
                        queueX.add(newX);
                        queueY.add(newY);
                        checkedTilesUnit[newX][newY] = this;
                        checkedTilesTime[newX][newY] = Map.time;
                    }
                }
            }
            if(currentIteration == nextDistanceIteration){
                currentIteration = 0;
                nextDistanceIteration = queueX.size();
            }
        }
    }

    private void processTile(int tileX, int tileY, int distance) {
        for(Task task : currentScanTasks) {
            double priority = task.calculatePriority(distance, false);
            if (priority <= currentTaskPriority) {
                // This means that all the subsequent tasks don't need to be
                // checked because they have lower maxPriorityPossible
                break;
            }
            Memory candidateGoal = task.appliesInTile(this, tileX, tileY);
            if(candidateGoal != null) {
                currentTask = task;
                resetPriority(priority);
                goalMemory = candidateGoal;


                // I could remove the task from the list of scanned task since it is not gonna find a
                // closer tile that applies


                break;
            }
        }
    }

    public void cancelTask(){
        currentTask = null;
        currentTaskPriority = 0;
        goalMemory = null;
    }

    private void resetPriority(double priority){
        currentTaskPriority = priority;
        conserveTaskTime = Map.time + 1 + (int)(priority * turnsPerPriorityFactor);
    }

    public void addToTile() {
        next = Map.unit[x][y];
        Map.unit[x][y] = this;
    }

    public boolean moveInDirection(int direction) {
        if(Map.steppable(this, x + Map.getX(direction), y + Map.getY(direction))){
            removeFromTile();
            x += Map.getX(direction);
            y += Map.getY(direction);
            addToTile();
            return true;
        }
        return false;
    }

    private void moveRandomly(){
        if(Rnd.nextInt(10) == 0) {
            randomDirection = (randomDirection + 1) % 6;
        }
        if(Rnd.nextInt(10) == 0) {
            randomDirection = (randomDirection + 5) % 6;
        }
        if(!moveInDirection(randomDirection)) {
            randomDirection = Rnd.nextInt(6);
        }
    }

    public int delay() {
        return 1;
    }

    public void removeFromTile() {
        if (Map.unit[x][y] == this) {
            Map.unit[x][y] = next;
        } else {
            Unit unit = Map.unit[x][y];
            while (unit.next != this) {
                unit = unit.next;
            }
            unit.next = next;
        }
        next = null;
    }

    public void removeFromTileAndDestroy() {
        removeFromTile();
        alive = false;
    }

    public void receiveDamage(int amount){
        life -= amount;
        if(life <= 0){
            removeFromTileAndDestroy();
        }
    }
}
