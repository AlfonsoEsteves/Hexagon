package game.game.unit;

import game.Item;
import game.Map;
import game.Rnd;
import gui.ImageLoader;

import java.awt.*;

public class Demon extends Unit {

    public static Object demonIdentity = new Object();
    public static Image imageDemon = ImageLoader.load("Demon");

    public Demon(int x, int y) {
        super(x, y);
        life = 160;
        tasks.add(TaskHuntPerson.instance);
    }

    @Override
    public Image image() {
        return imageDemon;
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
