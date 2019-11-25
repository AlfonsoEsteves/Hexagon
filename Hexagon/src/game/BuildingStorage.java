package game;

import game.unit.person.Person;

public class BuildingStorage extends Building implements Executable{

    public int totalStock;
    public int[] itemStock;
    public int[] itemValue;

    public BuildingStorage(int x, int y, Person person) {
        super(x, y);
        itemStock = new int[Item.itemTypes];
        itemValue = new int[Item.itemTypes];
        for(int i = 0;i<Item.itemTypes;i++) {
            itemValue[i] = person.itemValue[i];
        }
    }

    @Override
    public void execute() {
        for(Item item : Item.itemsList) {
            if(itemStock[item.id] == 0) {
                if(itemValue[item.id] <= 100) {
                    itemValue[item.id]++;
                }
            }
            else if(totalStock == 7) {
                itemValue[item.id]--;
            }
            else if(totalStock == 6 && itemValue[item.id] >= 4) {
                itemValue[item.id]--;
            }
        }

        Map.queueExecutable(this, 40);
    }

    @Override
    public boolean alive() {
        return placed > 0;
    }

    public void store(int itemId) {
        totalStock++;
        itemStock[itemId]++;
        if(itemValue[itemId] > 3) {
            itemValue[itemId]--;
        }
    }

    public void pickUp(int itemId) {
        totalStock--;
        itemStock[itemId]--;
        if(itemValue[itemId] < 100) {
            itemValue[itemId]++;
        }
    }
}