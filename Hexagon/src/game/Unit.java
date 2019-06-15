package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Unit implements Executable, Searchable{

    public static final int pathfindingDistanceLimit = 25;

    public static final int checkedTilesSize = pathfindingDistanceLimit * 2 + 1;

    public static boolean[][] checkedTiles = new boolean[checkedTilesSize][checkedTilesSize];

    public int x;
    public int y;

    public Unit next;

    public Image image;

    public boolean alive;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
        alive = true;
    }

    @Override
    public boolean alive(){
        return alive;
    }

    public boolean goTo(Object destinationIdentity) {
        int dir = directionTowardsDestination(destinationIdentity);
        if(dir != -1) {
            removeFromTile();
            x += Map.getX(dir);
            y += Map.getY(dir);
            addToTile();
            return true;
        }
        else{
            return false;
        }
    }

    public int directionTowardsDestination(Object destinationIdentity) {
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
            if(Map.has(newX, newY, destinationIdentity) != null){
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
                if(Map.has(newX, newY, destinationIdentity) != null){
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
            if(unit == null){
                Log.log(null, null);
            }
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
}
