package game.game.unit;

public class TaskSleep extends Task{

    public TaskSleep() {
        super(5);
    }

    @Override
    public double priority(Unit unit, int tileX, int tileY, int distance) {
        if(unit.life < Person.maxLife) {
            return maxPriorityPossible * ((double)(Person.maxLife - unit.life) / (double)(Person.maxLife)) / distance;
        }
        else{
            return 0;
        }
    }

    @Override
    public void execute(Unit unit) {
        Person person = (Person)unit;
        person.life += 10;
        if(person.life >= Person.maxLife) {
            person.life = Person.maxLife;
            person.priorityTask = null;
        }
    }
}
