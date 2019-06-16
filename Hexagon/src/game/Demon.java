package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Demon extends Unit {

    public static Object demonIdentity = new Object();
    public static Image imageDemon = ImageLoader.load("Demon");

    public Demon(int x, int y) {
        super(x, y);
        image = imageDemon;
        life = 8;
    }

    public boolean is(Object identity) {
        return identity == demonIdentity;
    }

    public void execute() {
        Unit person = (Person)Map.has(x, y, Person.personIdentity);
        if(person == null) {
            goTo(Person.personIdentity);
        }

        // The demon could have reached a person
        person = (Person)Map.has(x, y, Person.personIdentity);
        if(person != null) {
            life -= 1 + Rnd.nextInt(3);
            if(life <= 0) {
                removeFromTile();
            }
            person.life -= 1 + Rnd.nextInt(3);
            if(person.life <= 0) {
                person.removeFromTileAndDestroy();
            }
        }
        if(life > 0) {
            Map.queueExecutable(this, 1);
        }
    }

}
