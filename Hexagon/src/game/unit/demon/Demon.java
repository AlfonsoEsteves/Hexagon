package game.unit.demon;

import game.unit.Unit;
import gui.ImageLoader;

import java.awt.*;
import java.util.function.Predicate;

public class Demon extends Unit {

    public static Image imageDemon = ImageLoader.load("Demon");

    public static Predicate is = x -> (x instanceof Demon);

    public Demon(int x, int y) {
        super(x, y);
        life = 125;
        tasks.add(TaskHuntPerson.instance);
        tasks.add(TaskDestroy.instance);
    }

    @Override
    public Image image() {
        return imageDemon;
    }

    @Override
    public void initExecute(){
        life --;
        if(life <= 0){
            removeFromTileAndDestroy();
        }
    }

}
