package game;

import gui.ImageLoader;

import java.awt.*;

public class Item {

    public static Item stone = new Item("Stone").setProducer(Tile.stoneMine);
    public static Item wood = new Item("Wood").setProducer(Tile.tree);
    public static Item iron = new Item("Iron").setProducer(Tile.ironMine);
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
}
