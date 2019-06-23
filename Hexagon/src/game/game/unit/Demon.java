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
    }

    @Override
    public Image image() {
        return imageDemon;
    }

    public boolean is(Object identity) {
        return identity == demonIdentity;
    }

    public void execute() {
        Person person = (Person) Map.has(x, y, Person.personIdentity);
        if(person == null) {
            goTo(Person.personIdentity);
        }

        // The demon could have reached a person
        person = (Person)Map.has(x, y, Person.personIdentity);
        if(person != null) {
            life -= (10 + Rnd.nextInt(30)) * (person.carrying.contains(Item.sword) ? 2 : 1);
            person.life -= 10 + Rnd.nextInt(30);
            if(person.life <= 0) {
                person.removeFromTileAndDestroy();
            }
        }
        life--;
        if(life <= 0) {
            removeFromTile();
        }
        else {
            Map.queueExecutable(this, 1);
        }
    }

}
