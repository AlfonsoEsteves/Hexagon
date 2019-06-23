package game.game.unit;

import game.Item;
import game.Map;
import game.Searchable;
import game.Tile;
import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Person extends Unit {

    public static final int maxLife = 100;

    public static Object personIdentity;
    public static Image imagePerson = ImageLoader.load("Person");
    public static Image imagePersonWithSword = ImageLoader.load("Person with sword");

    public int usualX;
    public int usualY;

    public List<Item> carrying;

    public Person(int x, int y) {
        super(x, y);
        carrying = new ArrayList<>();
        usualX = x;
        usualY = y;
        life = maxLife;
    }

    public double remembersAtDistance(Object identity) {
        Record record = records.stream().filter(r -> r.identity == identity).findFirst().get();
        if(record != null){
            return record.aproxDistance;
        }
        else{
            return -1;
        }
    }

    public void remembersAtDistance(Object identity, int tileX, int tileY, int distance) {
        Record record = records.stream().filter(r -> r.identity == identity).findFirst().get();
        if(record != null){
            if(record.aproxDistance < distance) {
                return;
            }
        }
        records.add(new Record(identity, tileX, tileY, distance));
    }

    @Override
    public Image image() {
        return carrying.contains(Item.sword) ? imagePersonWithSword : imagePerson;
    }

    public boolean is(Object identity) {
        return identity == personIdentity;
    }

}
