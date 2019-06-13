package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Unit extends Executable{

    public static Image image = ImageLoader.load("Unit");

    public static final int pathfindingDistanceLimit = 25;

    public static final int checkedTilesSize = pathfindingDistanceLimit * 2 + 1;

    public static boolean[][] checkedTiles = new boolean[checkedTilesSize][checkedTilesSize];

    public int x;
    public int y;

    public int usualX;
    public int usualY;

    public Unit next;

    public List<Item> carrying;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
        carrying = new ArrayList<>();
        usualX = x;
        usualY = y;
    }

    public void execute() {
        if(!build()) {
            goTo(Tile.bed);
        }
        usualX += (x - usualX) / 10;
        usualY += (y - usualY) / 10;
        Map.queueExecutable(this, 1);
    }

    private boolean build() {
        if(checkThereIsClose(Tile.missingWall)) {
            if (carrying.contains(Item.stone)) {
                if (Map.overTile(x, y) == Tile.missingWall) {
                    Map.overTile[x][y] = Tile.wall;
                    carrying.remove(Item.stone);
                    return true;
                } else {
                    return goTo(Tile.missingWall);
                }
            } else {
                return extract(Tile.stone);
            }
        }
        else if(checkThereIsClose(Tile.missingDoor)) {
            if (carrying.contains(Item.wood)) {
                if (Map.overTile(x, y) == Tile.missingDoor) {
                    Map.overTile[x][y] = Tile.door;
                    carrying.remove(Item.stone);
                    return true;
                } else {
                    return goTo(Tile.missingDoor);
                }
            } else {
                return extract(Tile.tree);
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
        if(position != null) {
            int doorCount = Rnd.nextInt(12);
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    if (Map.distance(position[0], position[1], position[0] + i, position[1] + j) == 2) {
                        if(doorCount == 0) {
                            Map.overTile[position[0] + i][position[1] + j] = Tile.missingDoor;
                        }
                        else {
                            Map.overTile[position[0] + i][position[1] + j] = Tile.missingWall;
                        }
                        doorCount--;
                    }
                }
            }
        }
        return true;
    }

    private int[] pickUpPosition() {
        for(int i = 0;i<3;i++){
            int var = 6 + i * 2;
            int rndX = usualX + Rnd.nextInt(var * 2 + 1) - var;
            int rndY = usualY + Rnd.nextInt(var * 2 + 1) - var;
            if(checkPickedPosition(rndX, rndY)){
                int[] position = {rndX, rndY};
                return position;
            }
        }
        return null;
    }

    private boolean checkPickedPosition(int pickedX, int pickedY) {
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                if (Map.distance(0, 0, i, j) <= 3) {
                    if(Map.underTile(pickedX + i, pickedY + j) != Tile.grass ||
                            Map.overTile(pickedX + i, pickedY + j) != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean extract(Tile tileToGetItem) {
        if(Map.underTile(x, y) == tileToGetItem) {
            Map.underTile[x][y] = tileToGetItem.depletedVersion;
            Map.queueExecutable(new ResourceReplenish(x, y), 10);
            carrying.add(tileToGetItem.providesItem);
            return true;
        }
        else {
            return goTo(tileToGetItem);
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
        next = Map.unit[x][y];
        Map.unit[x][y] = this;
    }

    private void removeFromTile() {
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
}
