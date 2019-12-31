package game.unit;

import game.Executable;
import game.Log;
import game.Map;
import game.Rnd;
import gui.MainPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

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

    public java.util.List<Task> tasks;
    public double currentTaskPriority;
    public Task currentTask;
    public int conserveTaskTime;
    public int dirToDestination;
    public int destinationX;
    public int destinationY;

    private int randomDirection;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
        alive = true;
        tasks = new ArrayList<>();
        id = maxId;
        maxId++;
    }

    public abstract Image image();

    @Override
    public boolean alive(){
        return alive;
    }

    public static long timeScan = 0;
    public static long timeOther = 0;
    public static long t1 = System.nanoTime();
    public static long t2 = t1;

    @Override
    public void execute() {
        /*if(id == 328) {
            MainPanel.viewX = x;
            MainPanel.viewY = y;
            if (Map.time >= 40) {
                System.out.println();
            }
        }*/

        Log.log("UNIT", toString());

        /* PSEUDO CODE:

        If I have a current task and a destiny
          If they apply
            If conserve task time is not over
              Conserve task
            Else
              Reduce the current task priority
              Scan
          Else
            Set current current task to null and task priority to 0
            Scan
        Else
          Scan
        Execute task
        */

        initExecute();
        if(alive) {
            dirToDestination = -1;

            boolean scan = true;
            if (currentTask != null) {
                if (currentTask.applies(this, destinationX, destinationY)) {
                    if (Map.time > conserveTaskTime) {
                        // Don't reset the current task
                        // Just decrease the priority in case the goal has moved farther
                        // or in case an obstacle appeared
                        resetPriority(currentTaskPriority - 1);
                    } else {
                        scan = false;
                    }
                } else {
                    currentTask = null;
                    currentTaskPriority = 0;
                }
            }

            if(scan) {
                t1 = System.nanoTime();
                timeOther += t1 - t2;

                scanForTasks();

                t2 = System.nanoTime();
                timeScan += t2 - t1;
            }

            if(currentTask != null) {
                if (Map.distance(x, y, destinationX, destinationY) > currentTask.range) {
                    if(dirToDestination == -1) {
                        dirToDestination = Map.closestDirection(destinationX - x, destinationY - y);
                    }
                    removeFromTile();
                    x += Map.getX(dirToDestination);
                    y += Map.getY(dirToDestination);
                    addToTile();
                } else {
                    currentTask.execute(this);
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
        if(Map.steppable(x + Map.getX(randomDirection), y + Map.getY(randomDirection))){
            removeFromTile();
            x += Map.getX(randomDirection);
            y += Map.getY(randomDirection);
            addToTile();
        }
        else{
            randomDirection = Rnd.nextInt(6);
        }
    }

    public void initExecute() {
    }

    private void scanForTasks() {
        LinkedList<Integer> queueX = new LinkedList<>();
        LinkedList<Integer> queueY = new LinkedList<>();
        LinkedList<Integer> queueDir = new LinkedList<>();
        queueX.add(x);
        queueY.add(y);
        queueDir.add(-1);
        int distance = 0; // The currently checked distance
        int currentIteration = 0; //The iteration number for the currently checked distance
        int nextDistanceIteration = 1; //The iteration where the unit starts considering the next distance path
        while (!queueX.isEmpty()) {
            int currentX = queueX.removeFirst();
            int currentY = queueY.removeFirst();
            int currentDir = queueDir.removeFirst();
            processTile(currentX, currentY, distance, currentDir);
            if (Map.steppable(currentX, currentY)) {
                for (int i = 0; i < 6; i++) {
                    int newX = currentX + Map.getX(i);
                    int newY = currentY + Map.getY(i);
                    if(checkedTilesUnit[newX][newY] != this || checkedTilesTime[newX][newY] != Map.time) {
                        queueX.add(newX);
                        queueY.add(newY);
                        queueDir.add(currentDir == -1 ? i : currentDir);
                        checkedTilesUnit[newX][newY] = this;
                        checkedTilesTime[newX][newY] = Map.time;
                    }
                }
            }
            currentIteration++;
            if(currentIteration == nextDistanceIteration){
                distance++;
                if(distance >= pathfindingDistanceLimit) {
                    break;
                }
                else if(calculatePriority(tasks.get(0).priority, distance) <= currentTaskPriority) {
                    break;
                }
                else{
                    currentIteration = 0;
                    nextDistanceIteration = queueX.size();
                }
            }
        }
    }

    // Priority calculation does not take into consideration the range
    // Because otherwise, it wouldn't be possible to order the tasks from most priority to least priority
    // Cause this order would depend on the distance to the goal
    protected double calculatePriority(double pri, int distance){
        if(distance == 0) {
            return pri;
        }
        else {
            return pri / distance;
        }
    }

    private void processTile(int tileX, int tileY, int distance, int dir) {
        for(Task task : tasks) {
            double pri = calculatePriority(task.priority, distance);
            if (pri <= currentTaskPriority) {
                // This means that all the subsequent tasks don't need to be
                // checked because they have lower maxPriorityPossible
                break;
            }
            if(task.applies(this, tileX, tileY)) {
                currentTask = task;
                dirToDestination = dir;
                resetPriority(pri);
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
