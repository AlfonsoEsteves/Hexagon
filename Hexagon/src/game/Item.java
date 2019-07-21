package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.function.Predicate;

public class Item {

    public static Item stone = new Item("Stone").setProducer(OTId.stoneMine);
    public static Item wood = new Item("Wood").setProducer(OTId.tree);
    public static Item iron = new Item("Iron").setProducer(OTId.ironMine);
    public static Item fruit = new Item("Fruit").setProducer(OTId.fruitBush);
    public static Item sword = new Item("Sword");
    public static Item meat = new Item("Meat");

    public String name;
    public OTId producer;
    public Image image;

    public Predicate droppedIsItem = d -> d instanceof Dropped && ((Dropped)d).item == this;
    public Predicate makeWithItem = x -> x instanceof OverTile && ((OverTile) x).id.makeWith == this;

    public Item(String name) {
        this.name = name;
        image = ImageLoader.load(name);
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
