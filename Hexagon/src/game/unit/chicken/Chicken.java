package game.unit.chicken;

import game.Map;
import game.Rnd;
import game.unit.Unit;
import game.unit.person.Person;
import gui.ImageLoader;

import java.awt.*;
import java.util.function.Predicate;

public class Chicken extends Unit {

    public static Image imageChicken = ImageLoader.load("Chicken");
    public static Image imageLittleChicken = ImageLoader.load("Little chicken");

    public static Predicate is = x -> (x instanceof Chicken);

    public int food;
    public boolean grown;

    public Chicken(int x, int y) {
        super(x, y);
        grown = false;
        food = 30;
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

    @Override
    public void initExecute(){
        food --;
        if(food <= 0){
            removeFromTileAndDestroy();
        }
        else {
            tasks.clear();
            if(food < (grown ? 70 : 100)) {
                tasks.add(TaskChicken.instance);
            }
        }

        if(grown && food > 30 && Rnd.nextInt(50) == 0) {
            Map.addUnit(new Chicken(x, y));
        }
    }

    @Override
    public int delay(){
        return 3;
    }
}
