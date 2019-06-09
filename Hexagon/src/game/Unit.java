package game;

import java.util.Arrays;
import java.util.LinkedList;

public class Unit {

    public int x;
    public int y;

    public Unit next;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void execute() {
        int minDistance = Integer.MAX_VALUE;
        int destX = -1;
        int destY = -1;
        for (int i = -5; i <= 5; i++) {
            for (int j = 5; j <= 5; j++) {
                if (Map.tile(i + x, j + y) == Tile.fertileGround) {
                    int distance = Math.abs(i) + Math.abs(j);
                    if (distance < minDistance) {
                        minDistance = distance;
                        destX = i + x;
                        destY = j + y;
                    }
                }
            }
        }

        removeFromTile();
        int dir = directionTowardsDestination(Tile.fertileGround);
        x += Map.getX(dir);
        y += Map.getY(dir);
        addToTile();
        System.out.println(x + "   " + y);
    }

    private int directionTowardsDestination(Tile destination) {
        LinkedList<Integer> queueX = new LinkedList<>();
        LinkedList<Integer> queueY = new LinkedList<>();
        LinkedList<Integer> queueInitialDir = new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            int newX = x + Map.getX(i);
            int newY = y + Map.getY(i);
            if (Map.tile(newX, newY) == destination) {
                return i;
            }
            if (Map.steppable(newX, newY)) {
                queueX.addLast(newX);
                queueY.addLast(newY);
                queueInitialDir.addLast(i);
            }
        }
        int iterations = 0;
        while (!queueInitialDir.isEmpty()) {
            int currentX = queueX.removeFirst();
            int currentY = queueY.removeFirst();
            int currentInitialDir = queueInitialDir.removeFirst();
            for (int i = 0; i < 6; i++) {
                int newX = currentX + Map.getX(i);
                int newY = currentY + Map.getY(i);
                if(Map.tile(newX, newY) == destination){
                    return currentInitialDir;
                }
                if(Map.steppable(newX, newY)) {
                    queueX.add(newX);
                    queueY.add(newY);
                    queueInitialDir.add(currentInitialDir);
                }
            }
            if(iterations == 1000) {
                break;
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
