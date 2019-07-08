package game.game.unit.game.unit.person;

import game.*;
import game.game.unit.Unit;
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

    public List<Person> alliance;
    public List<Double> shouldTrust;
    public List<Double> shouldBeTrusted;

    public Person(int x, int y) {
        super(x, y);

        carrying = new ArrayList<>();
        life = maxLife;

        alliance = new ArrayList<>();
        shouldTrust = new ArrayList<>();
        shouldBeTrusted = new ArrayList<>();

        tasks.add(TaskFight.instance);
        tasks.add(TaskSleep.instance);
        tasks.add(TaskReactToPerson.instance);
        tasks.add(TaskBuild.instance);
        tasks.add(TaskCollect.instance);
        tasks.add(TaskCreateWeapon.instance);
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
        if(Rnd.nextInt(20) == 0) {
            int[] position = pickUpPosition();
            if (position != null) {
                int doorCount = Rnd.nextInt(12);
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (Map.distance(position[0], position[1], position[0] + i, position[1] + j) == 2) {
                            if (doorCount == 0) {
                                Map.overTile[position[0] + i][position[1] + j] = Tile.missingDoor;
                            } else {
                                Map.overTile[position[0] + i][position[1] + j] = Tile.missingWall;
                            }
                            doorCount--;
                        }
                    }
                }
                if (life < 100) {
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
