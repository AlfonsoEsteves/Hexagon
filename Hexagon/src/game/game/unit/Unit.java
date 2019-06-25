package game.game.unit;

import game.Executable;
import game.Map;
import game.Searchable;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Unit implements Executable, Searchable {

    public static final double maxKeepingSelectedTaskPriorityBonus = 1.5;

    public static final double aproxDistanceIncrement = 0.2;

    public static final int pathfindingDistanceLimit = 25;

    public static final int checkedTilesSize = pathfindingDistanceLimit * 2 + 1;

    public static boolean[][] checkedTiles = new boolean[checkedTilesSize][checkedTilesSize];

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
    public double keepingSelectedTaskPriorityBonus;

    public class Record {
        Object identity;
        int x;
        int y;
        double aproxDistance;

        public Record(Object identity, int x, int y, double aproxDistance){
            this.identity = identity;
            this.x = x;
            this.y = y;
            this.aproxDistance = aproxDistance;
        }
    }

    public List<Record> records;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
        alive = true;
        records = new ArrayList<>();
    }

    public abstract Image image();


    cosas para considerar destinos que se mueven, como enemigos

    esta quedando medio complicado el algoritmo

    si la unidad tiene un goal de poca prioridad (ej, construir muro)
          entonces se la va a pasar escaneando en busqueda de enemigos




    ME PARECE QUE LA POSTA ES NO ESCANEAR NUNCA!

    implementar "The awareness V line"
    La idea es que todas las unidades son concientes de las cosas que tienen up to 20 tiles
    entonces, cuando Pepe se mueve:
        pepe notifica es notificado de que aparecio en su zona de awareness los objetos ques estan en la V line
        y a su ves los objetos que estan en la V line, son notificadoes de que pepe aparecio en su zona de awareness

    cuando un objeto es creado, tiene que ser agregado a todas las awareness lists de los unidades alrededor

    una vez que una objeto es registrado dentro de los awareOf:
       si no es relevante se olvida
       si se alejo mas de X distancia se olvida

    entonces, cuando una unidad se mueve en una direcion puntual, la V line seria los tiles que entraron en su vision


    entonces, en cada frame, se ejecuta el algoritmo de liniasYBifurcaciones
    en caso de que el algoritmo alcance el objetivo de manera Dummy, se registra
    para que la unidad continue moviendose de manera Dummy



    @Override
    public void execute() {
        records.stream().forEach(r -> r.aproxDistance += aproxDistanceIncrement);
        priority = priorityTask.priority(this, destinationX, destinationY, distance);
        keepingSelectedTaskPriorityBonus = maxKeepingSelectedTaskPriorityBonus;
        scan();
        if(x != destinationX || y != destinationY) {
            goTo(destinationX, destinationY);
        }
        else {
            priorityTask.execute(this);
        }



        Map.queueExecutable(this, 1);
    }

    @Override
    public boolean alive(){
        return alive;
    }

    private void scan() {
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
            for (int i = 0; i < 6; i++) {
                int newX = currentX + Map.getX(i);
                int newY = currentY + Map.getY(i);
                if(!checkedTiles[newX - x + checkedTilesSize / 2][newY - y + checkedTilesSize / 2]) {
                    if (Map.steppable(newX, newY)) {
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
                else if(tasks.get(0).maxPriorityPossible / distance <= selectedTaskPriority()) {
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
            if (task.maxPriorityPossible / distance <= selectedTaskPriority()) {
                // This means that all the subsequent tasks don't need to be
                // chacked because they have lower masPriorityPossible
                break;
            }
            double currentPriority = task.priority(this, tileX, tileY, distance);
            if(currentPriority > selectedTaskPriority()) {
                priorityTask = task;
                destinationX = tileX;
                destinationY = tileY;
                priority = currentPriority;
                keepingSelectedTaskPriorityBonus = 1;
                break;
            }
        }
    }

    private double selectedTaskPriority() {
        return priority * keepingSelectedTaskPriorityBonus;
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
}
