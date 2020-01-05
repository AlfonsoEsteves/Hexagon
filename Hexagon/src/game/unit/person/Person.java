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
    public static final int goingBackDistance = 40;

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

    public int usualX;
    public int usualY;
    public boolean goingBack;



    public int[] roomPosition;
    public int[] storagePosition;
    public int[] blacksmithPosition;
    public int[] carpentryPosition;

    public int[] buildingPosition;



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

        usualX = x;
        usualY = y;
        goingBack = false;

        addTask(TaskPlanBuilding.instance, travelTasks);
        addTask(TaskCreateWeapon.createAnvilWeapon, travelTasks);
        addTask(TaskCreateWeapon.createCarpentryWeapon, travelTasks);
        addTask(TaskSleep.instance, travelTasks);
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
    protected void setScanTasks() {
        scanTasks.clear();

        if(goingBack) {
            addTask(TaskGoBackToBase.instance, scanTasks);
        }

        if(carrying.contains(Item.bow)) {
            addTask(TaskFight.taskFightLongDistance, scanTasks);
        }
        else{
            addTask(TaskFight.taskFightMelee, scanTasks);
        }

        addTask(TaskReactToPerson.instance, scanTasks);

        int amountStone = Collections.frequency(carrying, Item.stone);
        if(amountStone < 2) {
            addTask(TaskCollect.taskCollectStone, scanTasks);
        }
        if(amountStone > 0) {
            addTask(TaskBuild.taskBuildStoneThings, scanTasks);
        }
        else{
            addTask(TaskPickUp.taskPickUpStone, scanTasks);
        }

        int amountWood = Collections.frequency(carrying, Item.wood);
        if(amountWood < 2) {
            addTask(TaskCollect.taskCollectWood, scanTasks);
        }
        if(amountWood > 0) {
            addTask(TaskBuild.taskBuildWoodThings, scanTasks);
        }
        else{
            addTask(TaskPickUp.taskPickUpWood, scanTasks);
        }

        int amountIron = Collections.frequency(carrying, Item.iron);
        if(amountIron < 2) {
            addTask(TaskCollect.taskCollectIron, scanTasks);
        }
        if(amountIron > 0) {
            addTask(TaskBuild.taskBuildIronThings, scanTasks);
        }
        else{
            addTask(TaskPickUp.taskPickUpIron, scanTasks);
        }

        int amountFruit = Collections.frequency(carrying, Item.fruit);
        if(amountFruit < 2) {
            addTask(TaskCollect.taskCollectFruit, scanTasks);
        }
        if(amountFruit == 0) {
            addTask(TaskPickUp.taskPickUpFruit, scanTasks);
        }

        int amountHoney = Collections.frequency(carrying, Item.honey);
        if(amountHoney < 2) {
            addTask(TaskCollect.taskCollectHoney, scanTasks);
        }
        if(amountHoney == 0) {
            addTask(TaskPickUp.taskPickUpHoney, scanTasks);
        }

        int amountMushroom = Collections.frequency(carrying, Item.mushroom);
        if(amountMushroom < 2) {
            addTask(TaskCollect.taskCollectMushroom, scanTasks);
        }
        if(amountMushroom == 0) {
            addTask(TaskPickUp.taskPickUpMushroom, scanTasks);
        }

        int amountCarrot = Collections.frequency(carrying, Item.carrot);
        if(amountCarrot < 2) {
            addTask(TaskCollect.taskCollectCarrot, scanTasks);
        }
        if(amountCarrot == 0) {
            addTask(TaskPickUp.taskPickUpCarrot, scanTasks);
        }

        int amountSword = Collections.frequency(carrying, Item.sword);
        if(amountSword == 0) {
            addTask(TaskPickUp.taskPickUpSword, scanTasks);
        }

        int amountBow = Collections.frequency(carrying, Item.bow);
        if(amountBow == 0) {
            addTask(TaskPickUp.taskPickUpBow, scanTasks);
        }

        if(amountFruit > 1 || amountHoney > 1 || amountMushroom > 1  || amountCarrot > 1 || amountIron > 1 || amountStone > 1 || amountWood > 1 || amountSword > 1 || amountBow > 1) {
            addTask(TaskStore.instance, scanTasks);
        }
    }

    @Override
    public void initExecute(){
        if(leader != null && !leader.alive) {
            leader = this;
        }

        if(Rnd.nextInt(20) == 0) {
            if(usualX < x) {
                usualX ++;
            }
            if(usualX > x) {
                usualX --;
            }
            if(usualY < y) {
                usualY ++;
            }
            if(usualY > y) {
                usualY --;
            }
        }
        Person superLider = getSuperLeader();
        int distance = Map.distance(x - superLider.usualX, y - superLider.usualY);
        if(goingBack) {
            if(distance < goingBackDistance / 2) {
                goingBack = false;
            }
        }
        else{
            if(distance > goingBackDistance) {
                goingBack = true;
            }
        }

        checkFood();
        checkItemValues();

        if(food > maxFood * 0.9 && Rnd.nextInt(180) == 0) {
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

    private void addTask(Task task, List tasks) {
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
        else if(range == 3){
            if(carrying.contains(Item.bow)){
                unit.receiveDamage(25);
                carrying.remove(Item.bow);
            }
        }
    }
}
