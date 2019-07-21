package game.unit;

import game.unit.demon.TaskDestroy;
import game.unit.demon.TaskHuntPerson;
import gui.ImageLoader;

import java.awt.*;
import java.util.function.Predicate;

public class Chicken extends Unit {

    public static Object demonIdentity = new Object();
    public static Image imageChicken = ImageLoader.load("Chicken");
    public static Image imageLittleChicken = ImageLoader.load("Little chicken");

    public static Predicate is = x -> (x instanceof Chicken);

    public boolean grown;

    public Chicken(int x, int y) {
        super(x, y);
        grown = false;
    }

    @Override
    public Image image() {
        if(grown) {
            return imageChicken;
        }
        else{
            return imageLittleChicken;
        }
    }

    public boolean is(Object identity) {
        return identity == demonIdentity;
    }

    @Override
    public void initExecute(){
        life --;
        if(life <= 0){
            removeFromTileAndDestroy();
        }
    }

}
