package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.function.Predicate;

public class Item {

    public static Item stone = new Item("Stone").setProducer(Tile.stoneMine);
    public static Item wood = new Item("Wood").setProducer(Tile.tree);
    public static Item iron = new Item("Iron").setProducer(Tile.ironMine);
    public static Item fruit = new Item("Fruit").setProducer(Tile.fruitBush);
    public static Item sword = new Item("Sword");

    public String name;
    public Tile producer;
    public Image image;

    public Item(String name) {
        this.name = name;
        image = ImageLoader.load(name);
    }

    public Item setProducer(Tile producer){
        this.producer = producer;
        return this;
    }

    public Predicate droppedIsItem() {
        return d -> d instanceof Dropped && ((Dropped)d).item == this;
    }
}
