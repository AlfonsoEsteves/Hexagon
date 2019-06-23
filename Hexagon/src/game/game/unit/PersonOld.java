package game.game.unit;

import game.*;
import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PersonOld extends Unit {

    public static Object personIdentity;
    public static Image imagePerson = ImageLoader.load("Person");
    public static Image imagePersonWithSword = ImageLoader.load("Person with sword");

    public int usualX;
    public int usualY;

    public List<Item> carrying;

    public PersonOld(int x, int y) {
        super(x, y);
        carrying = new ArrayList<>();
        usualX = x;
        usualY = y;
        life = 100;
    }

    @Override
    public Image image() {
        return carrying.contains(Item.sword) ? imagePersonWithSword : imagePerson;
    }

    public boolean is(Object identity) {
        return identity == personIdentity;
    }

    public void execute() {
        /*determina la lista de prioridades con sus respectivos coeficientes de distancia
                por ejemplo, si la unidad tiene poca vida curarse tendria
                mucha prioridad y un coeficiente bajo
                  de tal manera que moverse 20 tiles para curarse sea mejor que moverse
                  10 tiles para hacer otro objetive con un coeficiente mas alto
                  20*0.1 < 10*0.5, por lo tanto curarse es como si fuece "lo mas cercano/importante"


       cuando el algoritmo se topa con stone lo registra, luego si mas adelante se topa con un missing wall
                entonces (si es la mejor opcion) la unidad va a ir a recoger el stone
                   en el calculo de prioridad usa la distancia del stone
                y deja rejistrada la ubicacion de el missing wall
                  para que sea mas eficiente buscarlo
                  y por si buscando la stone se aleja mas alla de la dist max*/




        if(life < 100) {
            if(Map.has(x, y, Tile.bed) != null) {
                life += 10;
            }
            else{
                if(!goTo(Tile.bed)){
                    build();
                }
            }
        }
        else if(!carrying.contains(Item.sword)) {
            if(carrying.contains(Item.iron)) {
                if(Map.has(x,y, Tile.anvil) != null){
                    carrying.remove(Item.iron);
                    carrying.add(Item.sword);
                }
                else{
                    if(!goTo(Tile.anvil)) {
                        build();
                    }
                }
            }
            else{
                if(!extract(Tile.iron)){
                    build();
                }
            }
        }
        else {
            build();
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
                    carrying.remove(Item.wood);
                    return true;
                } else {
                    return goTo(Tile.missingDoor);
                }
            } else {
                return extract(Tile.tree);
            }
        }
        else if(checkThereIsClose(Tile.missingBed)) {
            if (carrying.contains(Item.wood)) {
                if (Map.overTile(x, y) == Tile.missingBed) {
                    Map.overTile[x][y] = Tile.bed;
                    carrying.remove(Item.wood);
                    return true;
                } else {
                    return goTo(Tile.missingBed);
                }
            } else {
                return extract(Tile.tree);
            }
        }
        else if(checkThereIsClose(Tile.missingAnvil)) {
            if (carrying.contains(Item.iron)) {
                if (Map.overTile(x, y) == Tile.missingAnvil) {
                    Map.overTile[x][y] = Tile.anvil;
                    carrying.remove(Item.iron);
                    return true;
                } else {
                    return goTo(Tile.missingAnvil);
                }
            } else {
                return extract(Tile.iron);
            }
        }
        else {
            return planBuilding();
        }
    }

    private boolean checkThereIsClose(Tile tile) {
        for(int[] p : MapIter.of(pathfindingDistanceLimit)){
            if (Map.underTile(x + p[0], y + p[1]) == tile || Map.overTile(x + p[0], y + p[1]) == tile) {
                return true;
            }
        }
        /*for(int i = -pathfindingDistanceLimit;i<=pathfindingDistanceLimit;i++) {
            for(int j = -pathfindingDistanceLimit;j<=pathfindingDistanceLimit;j++) {
                if (Map.distance(0, 0, i, j) < pathfindingDistanceLimit) {
                    if (Map.underTile(x + i, y + j) == tile || Map.overTile(x + i, y + j) == tile) {
                        return true;
                    }
                }
            }
        }*/
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
            if(life < 100) {
                Map.overTile[position[0]][position[1]] = Tile.missingBed;
            }
            else{
                Map.overTile[position[0]][position[1]] = Tile.missingAnvil;
            }
            return true;
        }
        return false;
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
        for(int[] p : MapIter.of(3)) {
            if(Map.underTile(pickedX + p[0], pickedY + p[1]) != Tile.grass ||
                    Map.overTile(pickedX + p[0], pickedY + p[1]) != null) {
                return false;
            }
        }
        /*for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                if (Map.distance(0, 0, i, j) <= 3) {
                    if(Map.underTile(pickedX + i, pickedY + j) != Tile.grass ||
                            Map.overTile(pickedX + i, pickedY + j) != null) {
                        return false;
                    }
                }
            }
        }*/
        return true;
    }

    private boolean extract(Tile tileToGetItem) {
        if(Map.underTile(x, y) == tileToGetItem) {
            Map.underTile[x][y] = tileToGetItem.depletedVersion;
            Map.queueExecutable(new ResourceReplenish(x, y, tileToGetItem), 100);
            carrying.add(tileToGetItem.providesItem);
            return true;
        }
        else {
            return goTo(tileToGetItem);
        }
    }

}
