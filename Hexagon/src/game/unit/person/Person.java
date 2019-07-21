package game.unit.person;

import game.*;
import game.unit.Unit;
import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Person extends Unit {

    public static final int maxLife = 100;
    public static final int maxFood = 80;

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

    @Override
    public void initExecute(){
        if(leader != null && !leader.alive) {
            leader = null;
        }

        checkFood();
        setTasks();
        checkBuilding();

        if(food > maxFood / 2 && Rnd.nextInt(150) == 0) {
            Map.addUnit(new Person(x, y));
        }

    }

    private void checkFood() {
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
    }

    private void setTasks() {
        tasks.clear();
        tasks.add(TaskFight.instance);
        tasks.add(TaskSleep.instance);
        tasks.add(TaskReactToPerson.instance);

        int amountStone = Collections.frequency(carrying, Item.stone);
        if(amountStone < 2) {
            tasks.add(TaskCollect.taskCollectStone);
        }
        if(amountStone > 0) {
            tasks.add(TaskBuild.taskBuildStoneThings);
        }
        else{
            tasks.add(TaskPickUp.taskPickUpStone);
        }

        int amountWood = Collections.frequency(carrying, Item.wood);
        if(amountWood < 2) {
            tasks.add(TaskCollect.taskCollectWood);
        }
        if(amountWood > 0) {
            tasks.add(TaskBuild.taskBuildWoodThings);
        }
        else{
            tasks.add(TaskPickUp.taskPickUpWood);
        }

        int amountIron = Collections.frequency(carrying, Item.iron);
        if(amountIron < 2) {
            tasks.add(TaskCollect.taskCollectIron);
        }
        if(amountIron > 0) {
            tasks.add(TaskBuild.taskBuildIronThings);
            tasks.add(TaskCreateWeapon.instance);
        }
        else{
            tasks.add(TaskPickUp.taskPickUpIron);
        }

        int amountFruit = Collections.frequency(carrying, Item.fruit);
        if(amountFruit < 2) {
            tasks.add(TaskCollect.taskCollectFruit);
        }
        if(amountFruit == 0) {
            tasks.add(TaskPickUp.taskPickUpFruit);
        }

        int amountSword = Collections.frequency(carrying, Item.sword);
        if(amountSword == 0) {
            tasks.add(TaskPickUp.taskPickUpSword);
        }

        if(amountFruit > 1 || amountIron > 1 || amountStone > 1 || amountWood > 1 || amountSword > 1) {
            tasks.add(TaskStore.instance);
        }
    }

    private void checkBuilding() {
        if(carrying.contains(Item.stone) && Rnd.nextInt(50) == 0) {
            int[] position = pickUpPosition();
            if (position != null) {
                int doorCount = Rnd.nextInt(12);
                for (int[] p : MapIter.of(2)) {
                    int x = position[0] + p[0];
                    int y = position[1] + p[1];
                    if (Map.distance(position[0], position[1], x, y) == 2) {
                        if (doorCount == 0) {
                            planBuilding(OTId.missingDoor, x, y);
                        } else {
                            planBuilding(OTId.missingWall, x, y);
                        }
                        doorCount--;
                    }
                }
                if (Rnd.nextInt(4) == 0) {
                    for (int[] p : MapIter.of(1)) {
                        planBuilding(OTId.missingDepot, position[0] + p[0], position[1] + p[1]);
                    }
                } else if (Rnd.nextInt(4) == 0) {
                    planBuilding(OTId.missingBed, position[0], position[1]);
                } else {
                    planBuilding(OTId.missingAnvil, position[0], position[1]);
                }
            }
        }
    }

    private void planBuilding(OTId missingDoor, int x, int y) {
        OverTile overTile = new OverTile(missingDoor, x, y);
        Map.overTile[x][y] = overTile;
        Map.queueExecutable(overTile, OTIdMissingBuilding.timeToBeForgot);
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

    public void causeDamage(Unit unit) {
        if(carrying.contains(Item.sword)){
            unit.receiveDamage(25);
            carrying.remove(Item.sword);
        }
        else{
            unit.receiveDamage(6);
        }
    }
}
