package game;

import game.unit.person.Person;

public class BuildingStorage extends Building {

    public static int initialGold = 20;

    public int gold;
    public int[] itemValue;

    public BuildingStorage(int x, int y, Person person) {
        super(x, y);
        person.gold -= initialGold;
        gold = initialGold;
        itemValue = new int[Item.itemTypes];
        for(int i = 0;i<Item.itemTypes;i++) {
            itemValue[i] = person.itemValues[i];
        }
    }

}
