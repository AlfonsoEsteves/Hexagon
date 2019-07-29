package game.unit.person;

import game.*;
import game.unit.Task;
import game.unit.Unit;
import gui.ImageLoader;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;








/*
la siguiente idea es:
  borrar la clase OTIdMissingBuilding
  para crear un edificio, hay que colocar el primer muro (o cualquier otra parte)
  cuando se coloca la primer parte, se crea un objeto edificio que cuenta la cantidad de partes colocadas
  a medida que se van colocando partes o quitando, se reajusta la cuenta
  si la cantidad de partes colocadas llega a 0, el edificio entero es olvidado
  entonces, cada parte (y missing parte) va a tener una referencia a su objeto edificio
    este objeto edificio no necesita estar guardado en ningun otro lado


*/


public class Person extends Unit {

    public static final int maxLife = 100;
    public static final int maxFood = 80;
    public static final int goingBackDistance = 50;

    public static Image imagePerson = ImageLoader.load("Person");
    public static Image imagePersonWithSword = ImageLoader.load("Person with sword");

    public static Predicate is = x -> (x instanceof Person);

    public List<Item> carrying;

    // If a unit is his own leader it means it can not be conquered
    public Person leader;

    public int food;

    public int credit = 0;

    public static int foods = 4;
    public int lastFoodIndex = 0;
    public Item[] lastFood = new Item[foods];

    public int usualX;
    public int usualY;
    public boolean goingBack;

    public Person(int x, int y) {
        super(x, y);

        carrying = new ArrayList<>();
        life = maxLife;

        food = maxFood / 2;

        usualX = x;
        usualY = y;
        goingBack = false;
    }

    @Override
    public Image image() {
        return carrying.contains(Item.sword) ? imagePersonWithSword : imagePerson;
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
        int distance = Map.distance(x, y, superLider.usualX, superLider.usualY);
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
        setTasks();

        if(food > maxFood * 0.9 && Rnd.nextInt(150) == 0) {
            int totalFood = Collections.frequency(carrying, Item.fruit) + Collections.frequency(carrying, Item.mushroom) + Collections.frequency(carrying, Item.honey);
            if(totalFood > 2) {
                Map.addUnit(new Person(x, y));
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

    private void setTasks() {
        tasks.clear();

        if(goingBack) {
            addTask(TaskGoBackToBase.instance);
        }

        if(carrying.contains(Item.stone) && Rnd.nextInt(50) == 0 && Map.distance(x, y, getSuperLeader().usualX, getSuperLeader().usualY) < goingBackDistance / 2) {

            if (checkPickedPosition(x, y, 2)) {
                addTask(TaskPlanBuilding.instance);
            }
        }

        addTask(TaskFight.instance);
        addTask(TaskSleep.instance);
        addTask(TaskReactToPerson.instance);

        int amountStone = Collections.frequency(carrying, Item.stone);
        if(amountStone < 2) {
            addTask(TaskCollect.taskCollectStone);
        }
        if(amountStone > 0) {
            addTask(TaskBuild.taskBuildStoneThings);
        }
        else{
            addTask(TaskPickUp.taskPickUpStone);
        }

        int amountWood = Collections.frequency(carrying, Item.wood);
        if(amountWood < 2) {
            addTask(TaskCollect.taskCollectWood);
        }
        if(amountWood > 0) {
            addTask(TaskBuild.taskBuildWoodThings);
        }
        else{
            addTask(TaskPickUp.taskPickUpWood);
        }

        int amountIron = Collections.frequency(carrying, Item.iron);
        if(amountIron < 2) {
            addTask(TaskCollect.taskCollectIron);
        }
        if(amountIron > 0) {
            addTask(TaskBuild.taskBuildIronThings);
            addTask(TaskCreateWeapon.instance);
        }
        else{
            addTask(TaskPickUp.taskPickUpIron);
        }

        int amountFruit = Collections.frequency(carrying, Item.fruit);
        if(amountFruit < 2) {
            addTask(TaskCollect.taskCollectFruit);
        }
        if(amountFruit == 0) {
            addTask(TaskPickUp.taskPickUpFruit);
        }

        int amountHoney = Collections.frequency(carrying, Item.honey);
        if(amountHoney < 2) {
            addTask(TaskCollect.taskCollectHoney);
        }
        if(amountHoney == 0) {
            addTask(TaskPickUp.taskPickUpHoney);
        }

        int amountMushroom = Collections.frequency(carrying, Item.mushroom);
        if(amountMushroom < 2) {
            addTask(TaskCollect.taskCollectMushroom);
        }
        if(amountMushroom == 0) {
            addTask(TaskPickUp.taskPickUpMushroom);
        }

        int amountCarrot = Collections.frequency(carrying, Item.carrot);
        if(amountCarrot < 2) {
            addTask(TaskCollect.taskCollectCarrot);
        }
        if(amountCarrot == 0) {
            addTask(TaskPickUp.taskPickUpCarrot);
        }

        int amountSword = Collections.frequency(carrying, Item.sword);
        if(amountSword == 0) {
            addTask(TaskPickUp.taskPickUpSword);
        }

        if(amountFruit > 1 || amountHoney > 1 || amountMushroom > 1  || amountCarrot > 1 || amountIron > 1 || amountStone > 1 || amountWood > 1 || amountSword > 1) {
            addTask(TaskStore.instance);
        }
    }

    private void addTask(Task task) {
        int position = 0;
        while(position < tasks.size()) {
            if(tasks.get(position).priority <= task.priority) {
                break;
            }
            position++;
        }
        ((ArrayList)tasks).add(position, task);
    }

    /*private int[] pickUpPosition(int size) {
        int var = 6;
        int rndX = x + Rnd.nextInt(var * 2 + 1) - var;
        int rndY = y + Rnd.nextInt(var * 2 + 1) - var;
        if(checkPickedPosition(rndX, rndY, size)){
            int[] position = {rndX, rndY};
            return position;
        }
        else {
            return null;
        }
    }*/

    private boolean checkPickedPosition(int pickedX, int pickedY, int size) {
        for(int[] p : MapIter.of(size + 1)) {
            if(Map.underTile(pickedX + p[0], pickedY + p[1]) != Tile.grass ||
                    Map.overTile(pickedX + p[0], pickedY + p[1]) != null) {
                return false;
            }
        }
        return true;
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

    public void causeDamage(Unit unit) {
        if(carrying.contains(Item.sword)){
            unit.receiveDamage(30);
            carrying.remove(Item.sword);
        }
        else{
            unit.receiveDamage(6);
        }
    }
}
