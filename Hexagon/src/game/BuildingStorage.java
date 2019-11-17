package game;

import game.unit.person.Person;

public class BuildingStorage extends Building {

    public int[] itemValue;

    public BuildingStorage(int x, int y, Person person) {
        super(x, y);
        itemValue = new int[Item.itemTypes];
        for(int i = 0;i<Item.itemTypes;i++) {
            itemValue[i] = person.itemValue[i];
        }
    }

}
