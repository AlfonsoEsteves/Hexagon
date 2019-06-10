package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.LinkedList;

public class Unit extends Executable{

    public static final int pathfindingDistanceLimit = 15;

    public static final int itemNone = -1;
    public static final int itemStone = 0;

    public int x;
    public int y;

    public Unit next;

    public int carrying;

    public static Image image = ImageLoader.load("Unit");;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
        carrying = itemNone;
    }

    public void execute() {

        /*
        search for bed
          go to bed
        else
          build one
         */

        if(!goTo(Tile.bed)) {
            build();
        }





        Map.queueExecutable(this, 1);
    }

    private void build() {
        if(carrying == itemStone){
            return;
        }
        else{
            getItem();
        }
    }

    private void getItem() {
        if(Map.underTile(x, y) == Tile.stone) {
            Map.underTile[x][y] = Tile.depletedStone;
            Map.queueExecutable(new ResourceReplenish(x, y), 10);
            carrying = itemStone;
        }
        else {
            goTo(Tile.stone);
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


        agregar a este script una matriz para no chequear 2 veces el mismo tile
                mmm
                        capas en ves de hacer una del tamanio del mapa hacer una del tamanio del rango de la unidad
                para que no sea tan grande



        LinkedList<Integer> queueX = new LinkedList<>();
        LinkedList<Integer> queueY = new LinkedList<>();
        LinkedList<Integer> queueInitialDir = new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            int newX = x + Map.getX(i);
            int newY = y + Map.getY(i);
            if (Map.underTile(newX, newY) == destination) {
                return i;
            }
            if (Map.steppable(newX, newY)) {
                queueX.addLast(newX);
                queueY.addLast(newY);
                queueInitialDir.addLast(i);
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
                if(Map.underTile(newX, newY) == destination){
                    return currentInitialDir;
                }
                if(Map.steppable(newX, newY)) {
                    queueX.add(newX);
                    queueY.add(newY);
                    queueInitialDir.add(currentInitialDir);
                }
            }
            currentIteration++;
            if(currentIteration == nextDistanceIteration){
                distance++;
                if(distance > pathfindingDistanceLimit) {
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
