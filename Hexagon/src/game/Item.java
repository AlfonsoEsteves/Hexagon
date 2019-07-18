package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.function.Predicate;

public class Item {

    public static Item stone = new Item("Stone").setProducer(OverTile.stoneMine);
    public static Item wood = new Item("Wood").setProducer(OverTile.tree);
    public static Item iron = new Item("Iron").setProducer(OverTile.ironMine);
    public static Item fruit = new Item("Fruit").setProducer(OverTile.fruitBush);
    public static Item sword = new Item("Sword");

    public String name;
    public OverTile.Id producer;
    public Image image;

    public Item(String name) {
        this.name = name;
        image = ImageLoader.load(name);
    }

    public Item setProducer(OverTile.Id producer){
        this.producer = producer;
        return this;
    }

    public Predicate droppedIsItem() {
        return d -> d instanceof Dropped && ((Dropped)d).item == this;
    }
}
