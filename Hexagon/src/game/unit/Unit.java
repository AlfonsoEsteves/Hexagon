package game.unit;

import game.Executable;
import game.Debug;
import game.Map;
import game.Rnd;
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

    public int id;

    public int x;
    public int y;

    public Unit next;

    // Indicates whether the unit should be executed or not
    // Remember that when a unit gets destroyed, it is removed
    // from the map, but not from the execution queue.
    public boolean alive;

    public int life;

    public List<TaskTravel> travelTasks;
    public List<TaskScan> scanTasks;
    public double currentTaskPriority;
    public Task currentTask;
    public int conserveTaskTime;
    public int destinationX;
    public int destinationY;

    private int randomDirection;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
        alive = true;
        scanTasks = new ArrayList<>();
        travelTasks = new ArrayList<>();
        id = maxId;
        maxId++;
    }

    public abstract Image image();

    @Override
    public boolean alive(){
        return alive;
    }

    @Override
    public void execute() {
        /*if(id == 270) {
            MainPanel.viewX = x;
            MainPanel.viewY = y;
            if (Map.time >= 519) {
                System.out.println();
            }
        }*/

        Debug.log("UNIT", toString());

        /* PSEUDO CODE:

        If I have a current task and a destiny
          If they apply
            If conserve task time is not over
              Conserve task
            Else
              Reduce the current task maxPriorityPossible
              Check tasks
          Else
            Set current current task to null and task maxPriorityPossible to 0
            Check tasks
        Else
          Check tasks
        Execute task
        */

        initExecute();
        if(alive) {
            boolean recheckTasks = true;
            if (currentTask != null) {
                if (currentTask.applies(this)) {
                    if (Map.time > conserveTaskTime) {
                        // Don't reset the current task
                        // Just decrease the maxPriorityPossible in case the goal has moved farther
                        // or in case an obstacle appeared and it is blocking the way
                        resetPriority(currentTaskPriority - 1);
                    } else {
                        recheckTasks = false;
                    }
                } else {
                    currentTask = null;
                    currentTaskPriority = 0;
                }
            }

            if(recheckTasks) {
                checkNormalTasks();

                // It is important to reset the scanning tasks in each execution
                // Cause they are going to be checked for each scanned tile
                // So you don't want to have unnecessary ones
                setScanTasks();

                checkScanTasks();
            }

            if(currentTask != null) {
                if (Map.distance(x - destinationX, y - destinationY) > currentTask.range) {
                    int dirToDestination = Map.closestDirection(destinationX - x, destinationY - y);
                    if(!moveInDirection(dirToDestination)) {
                        moveRandomly();
                    }
                } else {
                    currentTask.execute(this);
                    currentTask = null;
                    currentTaskPriority = 0;
                }
            }
            else {
                moveRandomly();
            }

            if(alive) {
                Map.queueExecutable(this, delay());
            }
        }
    }

    private boolean moveInDirection(int direction) {
        if(Map.steppable(this, x + Map.getX(direction), y + Map.getY(direction))){
            removeFromTile();
            x += Map.getX(direction);
            y += Map.getY(direction);
            addToTile();
            return true;
        }
        return false;
    }

    protected abstract void setScanTasks();

    protected void checkNormalTasks() {
        for(TaskTravel task : travelTasks){
             if(task.maxPriorityPossible < currentTaskPriority) {
                 break;
             }
             if(task.applies(this)) {
                 int[] position = task.getDestination(this);
                 double priority = task.calculatePriority(Map.distance(x - position[0], y - position[1]));
                 if(priority > currentTaskPriority) {
                     currentTask = task;
                     currentTaskPriority = priority;
                     destinationX = position[0];
                     destinationY = position[1];
                 }
             }
        }
    }

    public int delay() {
        return 1;
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

    public void initExecute() {
    }

    private void checkScanTasks() {
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
                if(scanTasks.get(0).calculatePriority(distance) <= currentTaskPriority) {
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
        for(TaskScan task : scanTasks) {
            double priority = task.calculatePriority(distance);
            if (priority <= currentTaskPriority) {
                // This means that all the subsequent tasks don't need to be
                // checked because they have lower maxPriorityPossible
                break;
            }
            if(task.applies(this, tileX, tileY)) {
                currentTask = task;
                resetPriority(priority);
                destinationX = tileX;
                destinationY = tileY;
                break;
            }
        }
    }

    private void resetPriority(double pri){
        currentTaskPriority = pri;
        conserveTaskTime = Map.time + 1 + (int)(pri * turnsPerPriorityFactor);
    }

    public void addToTile() {
        next = Map.unit[x][y];
        Map.unit[x][y] = this;
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
