package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.LinkedList;

public class Unit extends Executable{

    public static Image image = ImageLoader.load("Unit");

    public static final int pathfindingDistanceLimit = 15;

    public static final int checkedTilesSize = pathfindingDistanceLimit * 2 + 1;

    public static boolean[][] checkedTiles = new boolean[checkedTilesSize][checkedTilesSize];

    public static final int itemNone = -1;
    public static final int itemStone = 0;

    public int x;
    public int y;

    public Unit next;

    public int carrying;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
        carrying = itemNone;
    }

    public void execute() {
        if(!build()) {
            goTo(Tile.bed);
        }
        Map.queueExecutable(this, 1);
    }

    private boolean build() {
        if(checkThereIsClose(Tile.missingWall)) {
            if (carrying == itemStone) {
                if (Map.overTile(x, y) == Tile.missingWall) {
                    Map.overTile[x][y] = Tile.wall;
                    carrying = itemNone;
                    return true;
                } else {
                    return goTo(Tile.missingWall);
                }
            } else {
                return getItem();
            }
        }
        else {
            return planBuilding();
        }
    }

    private boolean checkThereIsClose(Tile tile) {
        for(int i = -pathfindingDistanceLimit;i<=pathfindingDistanceLimit;i++) {
            for(int j = -pathfindingDistanceLimit;j<=pathfindingDistanceLimit;j++) {
                if (Map.distance(0, 0, i, j) < pathfindingDistanceLimit) {
                    if (Map.underTile(x + i, y + j) == tile || Map.overTile(x + i, y + j) == tile) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean planBuilding() {
        int[] position = pickUpPosition();
        for (int i=-2;i<=2;i++){
            for(int j=-2;j<=2;j++){
                if (Map.distance(position[0], position[1], position[0] + i, position[1] + j) == 2) {
                    Map.overTile[position[0] + i][position[1] + j] = Tile.missingWall;
                }
            }
        }
        return true;
    }

    private int[] pickUpPosition() {
        int[] position = {9, 5};
        return position;
    }

    private boolean getItem() {
        if(Map.underTile(x, y) == Tile.stone) {
            Map.underTile[x][y] = Tile.depletedStone;
            Map.queueExecutable(new ResourceReplenish(x, y), 10);
            carrying = itemStone;
            return true;
        }
        else {
            return goTo(Tile.stone);
        }
    }

    private boolean goTo(Tile destination) {
        int dir = directionTowardsDestination(destination);
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

    private int directionTowardsDestination(Tile destination) {
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
            if (Map.underTile(newX, newY) == destination || Map.overTile(newX, newY) == destination) {
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
                if(Map.underTile(newX, newY) == destination || Map.overTile(newX, newY) == destination){
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

    private void addToTile() {
        next = Map.units[x][y];
        Map.units[x][y] = this;
    }

    private void removeFromTile() {
        if (Map.units[x][y] == this) {
            Map.units[x][y] = next;
        } else {
            Unit unit = Map.units[x][y];
            while (unit.next != this) {
                unit = unit.next;
            }
            unit.next = next;
        }
        next = null;
    }
}
