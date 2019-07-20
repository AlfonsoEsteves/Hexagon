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

    public static final int checkedTilesSize = pathfindingDistanceLimit * 2 + 1;

    public static boolean[][] checkedTiles = new boolean[checkedTilesSize][checkedTilesSize];

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
    public double priority;
    public Task priorityTask;
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

    @Override
    public void execute() {



        /*if(id == 1443) {
            MainPanel.viewX = x;
            MainPanel.viewY = y;
            if (Map.time >= 615) {
                System.out.println();
            }
        }*/



        Log.log("UNIT", toString());
        priority = 0;
        priorityTask = null;
        initExecute();
        if(alive) {
            scanForTasks();
            if (priorityTask != null) {
                if (Map.distance(x, y, destinationX, destinationY) > priorityTask.range) {
                    goTo(destinationX, destinationY);
                } else {
                    priorityTask.execute(this);
                }
            }
            else{
                moveRandomly();
            }
        }
        if(alive) {
            Map.queueExecutable(this, 1);
        }
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
        for(int i = 0;i<checkedTilesSize;i++) {
            for(int j = 0;j<checkedTilesSize;j++) {
                checkedTiles[i][j] = false;
            }
        }
        LinkedList<Integer> queueX = new LinkedList<>();
        LinkedList<Integer> queueY = new LinkedList<>();
        queueX.add(x);
        queueY.add(y);
        int distance = 1; // The currently checked distance, it starts at one to avoid divisions by 0
        int currentIteration = 0; //The iteration number for the currently checked distance
        int nextDistanceIteration = 1; //The iteration where the unit starts considering the next distance path
        while (!queueX.isEmpty()) {
            int currentX = queueX.removeFirst();
            int currentY = queueY.removeFirst();
            processTile(currentX, currentY, distance);
            if (Map.steppable(currentX, currentY)) {
                for (int i = 0; i < 6; i++) {
                    int newX = currentX + Map.getX(i);
                    int newY = currentY + Map.getY(i);
                    if(!checkedTiles[newX - x + checkedTilesSize / 2][newY - y + checkedTilesSize / 2]) {
                        queueX.add(newX);
                        queueY.add(newY);
                        checkedTiles[newX - x + checkedTilesSize / 2][newY - y + checkedTilesSize / 2] = true;
                    }
                }
            }
            currentIteration++;
            if(currentIteration == nextDistanceIteration){
                distance++;
                if(distance >= pathfindingDistanceLimit) {
                    break;
                }
                else if(tasks.get(0).priority / distance <= priority) {
                    break;
                }
                else{
                    currentIteration = 0;
                    nextDistanceIteration = queueX.size();
                }
            }
        }
    }

    private void processTile(int tileX, int tileY, int distance) {
        for(Task task : tasks) {
            if (task.priority / distance <= priority) {
                // This means that all the subsequent tasks don't need to be
                // chacked because they have lower maxPriorityPossible
                break;
            }
            if(task.applies(this, tileX, tileY)) {
                priorityTask = task;
                destinationX = tileX;
                destinationY = tileY;
                priority = task.priority / distance;
                break;
            }
        }
    }

    public void goTo(int destinationX, int destinationY) {
        int dir;
        if(Map.distance(x, y, destinationX, destinationY) < pathfindingDistanceLimit) {
            dir = directionTowardsDestination(destinationX, destinationY);
        }
        else {
            dir = Map.closestDirection(destinationX - x, destinationY - y);
            if(!Map.steppable(x + Map.getX(dir), y + Map.getY(dir))){
                dir = -1;
            }
        }
        if(dir != -1) {
            removeFromTile();
            x += Map.getX(dir);
            y += Map.getY(dir);
            addToTile();
        }
    }

    public int directionTowardsDestination(int destinationX, int destinationY) {
        for(int i = 0;i<checkedTilesSize;i++) {
            for(int j = 0;j<checkedTilesSize;j++) {
                checkedTiles[i][j] = false;
            }
        }
        LinkedList<Integer> queueX = new LinkedList<>();
        LinkedList<Integer> queueY = new LinkedList<>();
        LinkedList<Integer> queueInitialDir = new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            int newX = x + Map.getX(i);
            int newY = y + Map.getY(i);
            if(newX == destinationX && newY == destinationY){
                return i;
            }
            if (Map.steppable(newX, newY)) {
                queueX.addLast(newX);
                queueY.addLast(newY);
                queueInitialDir.addLast(i);
                checkedTiles[newX - x + checkedTilesSize / 2][newY - y + checkedTilesSize / 2] = true;
            }
        }
        int distance = 1;
        int currentIteration = 0; //The iteration number for the currently checked distance
        int nextDistanceIteration = queueX.size(); //The iteration where the unit starts considering the next distance path
        while (!queueX.isEmpty()) {
            int currentX = queueX.removeFirst();
            int currentY = queueY.removeFirst();
            int currentInitialDir = queueInitialDir.removeFirst();
            for (int i = 0; i < 6; i++) {
                int newX = currentX + Map.getX(i);
                int newY = currentY + Map.getY(i);
                if(newX == destinationX && newY == destinationY){
                    return currentInitialDir;
                }
                if(!checkedTiles[newX - x + checkedTilesSize / 2][newY - y + checkedTilesSize / 2]) {
                    if (Map.steppable(newX, newY)) {
                        queueX.add(newX);
                        queueY.add(newY);
                        queueInitialDir.add(currentInitialDir);
                        checkedTiles[newX - x + checkedTilesSize / 2][newY - y + checkedTilesSize / 2] = true;
                    }
                }
            }
            currentIteration++;
            if(currentIteration == nextDistanceIteration){
                distance++;
                if(distance >= pathfindingDistanceLimit) {
                    break;
                }
                else{
                    currentIteration = 0;
                    nextDistanceIteration = queueX.size();
                }
            }
        }
        return -1;
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
