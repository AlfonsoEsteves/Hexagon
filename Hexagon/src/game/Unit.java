package game;

import java.util.Arrays;
import java.util.LinkedList;

public class Unit extends Executable{

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
        if(carrying == itemStone){
            return;
        }
        if(Map.tile(x, y) == Tile.stone) {
            Map.tiles[x][y] = Tile.stoneDepleted;
            Map.queueExecutable(new ResourceReplenish(x, y), 10);
            carrying = itemStone;
        }
        else {
            removeFromTile();
            int dir = directionTowardsDestination(Tile.stone);
            x += Map.getX(dir);
            y += Map.getY(dir);
            addToTile();
        }

        Map.queueExecutable(this, 1);
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
