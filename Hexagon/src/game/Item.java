package game;

public class Item {

    public static Item stone = new Item("Stone");
    public static Item wood = new Item("Wood");

    public String name;

    public Item(String name) {
        this.name = name;
    }
}
