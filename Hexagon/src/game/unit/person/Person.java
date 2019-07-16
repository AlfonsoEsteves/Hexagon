package game.unit.person;

import game.*;
import game.unit.Unit;
import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Person extends Unit {

    public static final int maxLife = 100;

    public static Object personIdentity;
    public static Image imagePerson = ImageLoader.load("Person");
    public static Image imagePersonWithSword = ImageLoader.load("Person with sword");

    public static Predicate is = x -> (x instanceof Person);

    public List<Item> carrying;

    public Person leader;

    public Person(int x, int y) {
        super(x, y);

        carrying = new ArrayList<>();
        life = maxLife;
    }

    @Override
    public Image image() {
        return carrying.contains(Item.sword) ? imagePersonWithSword : imagePerson;
    }

    public boolean is(Object identity) {
        return identity == personIdentity;
    }

    @Override
    public void initExecute(){
        if(leader != null && !leader.alive){
            leader = null;
        }

        tasks.clear();
        tasks.add(TaskFight.instance);
        tasks.add(TaskSleep.instance);
        tasks.add(TaskReactToPerson.instance);
        if(carrying.contains(Item.stone)) {
            tasks.add(TaskBuild.taskBuildStoneThings);
        }
        else{
            tasks.add(TaskCollect.taskCollectStone);
        }
        if(carrying.contains(Item.wood)) {
            tasks.add(TaskBuild.taskBuildWoodThings);
        }
        else{
            tasks.add(TaskCollect.taskCollectWood);
        }
        if(carrying.contains(Item.iron)) {
            tasks.add(TaskBuild.taskBuildIronThings);
            tasks.add(TaskCreateWeapon.instance);
        }
        else{
            tasks.add(TaskCollect.taskCollectIron);
        }
        if(carrying.size() > 1) {
            tasks.add(TaskStore.instance);
        }

        if(Rnd.nextInt(20) == 0) {
            int[] position = pickUpPosition();
            if (position != null) {
                int doorCount = Rnd.nextInt(12);
                for (int[] p : MapIter.of(2)) {
                    if (Map.distance(position[0], position[1], position[0] + p[0], position[1] + p[1]) == 2) {
                        if (doorCount == 0) {
                            Map.overTile[position[0] + p[0]][position[1] + p[1]] = Tile.missingDoor;
                        } else {
                            Map.overTile[position[0] + p[0]][position[1] + p[1]] = Tile.missingWall;
                        }
                        doorCount--;
                    }
                }
                if (Rnd.nextInt(4) == 0) {
                    for (int[] p : MapIter.of(1)) {
                        Map.overTile[position[0] + p[0]][position[1] + p[1]] = Tile.missingDepot;
                    }
                } else if (Rnd.nextInt(4) == 0) {
                    Map.overTile[position[0]][position[1]] = Tile.missingBed;
                } else {
                    Map.overTile[position[0]][position[1]] = Tile.missingAnvil;
                }
            }
        }
    }

    private int[] pickUpPosition() {
        int var = 6;
        int rndX = x + Rnd.nextInt(var * 2 + 1) - var;
        int rndY = y + Rnd.nextInt(var * 2 + 1) - var;
        if(checkPickedPosition(rndX, rndY)){
            int[] position = {rndX, rndY};
            return position;
        }
        else {
            return null;
        }
    }

    private boolean checkPickedPosition(int pickedX, int pickedY) {
        for(int[] p : MapIter.of(3)) {
            if(Map.underTile(pickedX + p[0], pickedY + p[1]) != Tile.grass ||
                    Map.overTile(pickedX + p[0], pickedY + p[1]) != null) {
                return false;
            }
        }
        return true;
    }

    public Person getSuperLeader() {
        if(leader == null) {
            return this;
        }
        else{
            return leader.getSuperLeader();
        }
    }

    public int getDamage() {
        return carrying.contains(Item.sword) ? 10 : 5;
    }
}
