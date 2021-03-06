package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Item {

    public static int itemTypes = 0;

    public static List<Item> itemsList = new ArrayList<>();

    public static Item stone = new Item("Stone");
    public static Item wood = new Item("Wood");
    public static Item iron = new Item("Iron");
    public static Item fruit = new Item("Fruit");
    public static Item mushroom = new Item("Mushroom");
    public static Item honey = new Item("Honey");
    public static Item carrot = new Item("Carrot");
    public static Item sword = new Item("Sword");
    public static Item bow = new Item("Bow");
    public static Item meat = new Item("Meat");

    public String name;
    public OTId producer;
    public Image image;
    public int id;

    public Predicate droppedIsItem = d -> d instanceof Dropped && ((Dropped)d).item == this;
    public Predicate makeWithItem = x -> x instanceof OverTile && ((OverTile) x).id.makeWith == this;

    public Item(String name) {
        this.name = name;
        image = ImageLoader.load(name);
        id = itemTypes;
        itemTypes++;
        itemsList.add(this);
    }

    public Item setProducer(OTId producer){
        this.producer = producer;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

}
