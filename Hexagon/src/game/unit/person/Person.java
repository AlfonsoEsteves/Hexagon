package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;
import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Person extends Unit {

    public static final int maxLife = 100;
    public static final int maxFood = 80;
    public static final int visionRange = 15;

    public static final int roomBuilding = 0;
    public static final int jobBuilding = 1;

    public static Image imagePerson = ImageLoader.load("Person");
    public static Image imagePersonWithSword = ImageLoader.load("Person with sword");
    public static Image imagePersonWithBow = ImageLoader.load("Person with bow");

    public static Predicate is = x -> (x instanceof Person);

    public List<Item> carrying;

    // If a unit is his own leader it means it can not be conquered
    public Person leader;

    public int gold;

    public int[] itemValue;

    public int food;

    public static int foods = 4;
    public int lastFoodIndex = 0;
    public Item[] lastFood = new Item[foods];

    /*public int usualX;
    public int usualY;
    public boolean goingBack;*/


    public OTId job;

    public MemoryBuilding[] memoryBuildings;

    public MemoryStaticPoint buildingPosition;

    public Person(int x, int y) {
        super(x, y);

        carrying = new ArrayList<>();
        life = maxLife;

        gold = 0;
        itemValue = new int[Item.itemTypes];
        for(int i = 0; i<Item.itemTypes;i++) {
            itemValue[i] = 6;
        }

        food = maxFood / 2;

        memoryBuildings = new MemoryBuilding[2];

        int r = Rnd.nextInt(3);
        if(r == 0) {
            job = OTId.depot;
        }
        else if(r == 1) {
            job = OTId.anvil;
            addTask(TaskCreateWeapon.createAnvilWeapon);
        }
        else if(r == 2) {
            job = OTId.carpentry;
            addTask(TaskCreateWeapon.createCarpentryWeapon);
        }

        addTask(TaskSleep.instance);

        addTask(TaskPlanBuilding.instance);

        addTask(TaskBuild.taskBuildRoom);
        addTask(TaskBuild.taskBuildJob);

        addTask(TaskFight.taskFightLongDistance);
        addTask(TaskFight.taskFightMelee);

        addTask(TaskReactToPerson.instance);

        addTask(TaskCollect.taskCollectStone);
        addTask(TaskCollect.taskCollectWood);
        addTask(TaskCollect.taskCollectIron);
        addTask(TaskCollect.taskCollectCarrot);
        addTask(TaskCollect.taskCollectFruit);
        addTask(TaskCollect.taskCollectHoney);
        addTask(TaskCollect.taskCollectMushroom);

        addTask(TaskPickUp.taskPickUpBow);
        addTask(TaskPickUp.taskPickUpSword);
        addTask(TaskPickUp.taskPickUpCarrot);
        addTask(TaskPickUp.taskPickUpFruit);
        addTask(TaskPickUp.taskPickUpHoney);
        addTask(TaskPickUp.taskPickUpMushroom);
        addTask(TaskPickUp.taskPickUpIron);
        addTask(TaskPickUp.taskPickUpWood);
        addTask(TaskPickUp.taskPickUpStone);

        addTask(TaskStore.instance);
    }

    @Override
    public Image image() {
        if(carrying.contains(Item.bow) ) {
            return imagePersonWithBow;
        }
        else if(carrying.contains(Item.sword) ) {
            return imagePersonWithSword;
        }
        else{
            return imagePerson;
        }
    }

    @Override
    public void initExecute(){
        if(leader != null && !leader.alive) {
            leader = this;
        }

        checkFood();
        checkItemValues();

        if(food > maxFood * 0.9 && Rnd.nextInt(200) == 0) {
            int totalFood = Collections.frequency(carrying, Item.fruit) + Collections.frequency(carrying, Item.mushroom) + Collections.frequency(carrying, Item.honey);
            if(totalFood > 2) {
                Map.addUnit(new Person(x, y));
            }
        }

    }

    private void checkItemValues() {
        if((Map.time + id) % 100 == 0) {
            for (Item item : Item.itemsList) {
                int amount = Collections.frequency(carrying, item);
                if (amount == 0) {
                    itemValue[item.id]++;
                }
                if (amount == 2 && itemValue[item.id] > 3) {
                    itemValue[item.id]--;
                }
            }
        }
    }

    private void checkFood() {
        if(food > 0) {
            food--;
        }
        if(food < maxFood) {
            Item foodType = null;
            int foodValue = 0;
            if(carrying.contains(Item.fruit)) {
                foodValue = calculateFoodValue(Item.fruit);
                foodType = Item.fruit;
            }
            if(carrying.contains(Item.honey)) {
                int newFoodValue = calculateFoodValue(Item.honey);
                if(newFoodValue > foodValue) {
                    foodType = Item.honey;
                    foodValue = newFoodValue;
                }
            }
            if(carrying.contains(Item.mushroom)) {
                int newFoodValue = calculateFoodValue(Item.mushroom);
                if(newFoodValue > foodValue) {
                    foodType = Item.mushroom;
                    foodValue = newFoodValue;
                }
            }
            if(carrying.contains(Item.carrot)) {
                int newFoodValue = calculateFoodValue(Item.carrot);
                if(newFoodValue > foodValue) {
                    foodType = Item.carrot;
                    foodValue = newFoodValue;
                }
            }
            if(foodType != null) {
                lastFoodIndex = (lastFoodIndex + 1) % foods;
                carrying.remove(foodType);
                food += foodValue;
            }
        }
        if(food == 0) {
            life--;
            if(life <= 0) {
                removeFromTileAndDestroy();
            }
        }
    }

    private int calculateFoodValue(Item food) {
        if(lastFood[lastFoodIndex] == food) {
            return maxFood * 25;
        }
        else if(lastFood[(lastFoodIndex + 1 ) % foods] == food) {
            return maxFood * 50;
        }
        else if(lastFood[(lastFoodIndex + 2 ) % foods] == food) {
            return maxFood * 75;
        }
        else {
            return maxFood;
        }
    }

    private void addTask(Task task) {
        int position = 0;
        while(position < tasks.size()) {
            if(((Task)tasks.get(position)).maxPriorityPossible <= task.maxPriorityPossible) {
                break;
            }
            position++;
        }
        ((ArrayList)tasks).add(position, task);
    }

    public Person getSuperLeader() {
        if(leader == null) {
            return this;
        }
        else if(leader == this) {
            return this;
        }
        else{
            return leader.getSuperLeader();
        }
    }

    public void causeDamage(Unit unit, int range) {
        if (range == 1) {
            if(carrying.contains(Item.sword)){
                unit.receiveDamage(30);
                carrying.remove(Item.sword);
            }
            else{
                unit.receiveDamage(6);
            }
        }
        else if(range < 3){
            if(carrying.contains(Item.bow)){
                unit.receiveDamage(25);
                carrying.remove(Item.bow);
            }
        }
    }
}
