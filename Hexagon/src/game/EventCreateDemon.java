package game;

import game.unit.demon.Demon;

public class EventCreateDemon implements Executable {

    public int x, y;

    public EventCreateDemon(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        Map.addUnit(new Demon(x, y));
        Map.queueExecutable(this, Rnd.nextInt(80) + 80);
    }
}
