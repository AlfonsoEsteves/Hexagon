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
    public static final int maxFood = 80;

    public static Object personIdentity;
    public static Image imagePerson = ImageLoader.load("Person");
    public static Image imagePersonWithSword = ImageLoader.load("Person with sword");

    public static Predicate is = x -> (x instanceof Person);

    public List<Item> carrying;

    public Person leader;

    public int food;

    public Person(int x, int y) {
        super(x, y);

        carrying = new ArrayList<>();
        life = maxLife;

        food = maxFood;
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
        if(!carrying.contains(Item.fruit)) {
            tasks.add(TaskCollect.taskCollectFruit);
        }
        if(carrying.size() > 1) {
            tasks.add(TaskStore.instance);
        }
        else if(carrying.size() == 0) {
            tasks.add(TaskPickUp.taskPickUpStone);
            tasks.add(TaskPickUp.taskPickUpWood);
            tasks.add(TaskPickUp.taskPickUpIron);
        }
        if(!carrying.contains(Item.fruit) && food < Person.maxFood / 2) {
            tasks.add(TaskPickUp.taskPickUpFruit);
        }

        if(Rnd.nextInt(20) == 0) {
            int[] position = pickUpPosition();
            if (position != null) {
                int doorCount = Rnd.nextInt(12);
                for (int[] p : MapIter.of(2)) {
                    int x = position[0] + p[0];
                    int y = position[1] + p[1];
                    if (Map.distance(position[0], position[1], x, y) == 2) {
                        if (doorCount == 0) {
                            Map.overTile[x][y] = new OverTile(OTId.missingDoor, x, y);
                        } else {
                            Map.overTile[x][y] = new OverTile(OTId.missingWall, x, y);
                        }
                        doorCount--;
                    }
                }
                if (Rnd.nextInt(4) == 0) {
                    for (int[] p : MapIter.of(1)) {
                        Map.overTile[position[0] + p[0]][position[1] + p[1]] = new OverTile(OTId.missingDepot, position[0] + p[0], position[1] + p[1]);
                    }
                } else if (Rnd.nextInt(4) == 0) {
                    Map.overTile[position[0]][position[1]] = new OverTile(OTId.missingBed, position[0], position[1]);
                } else {
                    Map.overTile[position[0]][position[1]] = new OverTile(OTId.missingAnvil, position[0], position[1]);
                }
            }
        }

        if(food > 0) {
            food--;
        }
        if(food < maxFood / 2) {
            if(carrying.contains(Item.fruit)) {
                carrying.remove(Item.fruit);
                food += maxFood;
            }
        }
        if(food == 0) {
            life--;
            if(life <= 0) {
                removeFromTileAndDestroy();
            }
        }

        if(food > maxFood / 2 && Rnd.nextInt(150) == 0) {
            Map.addUnit(new Person(x, y));
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
