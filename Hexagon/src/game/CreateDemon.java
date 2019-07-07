package game;

import game.game.unit.Demon;

public class CreateDemon implements Executable {

    public int x, y;

    public CreateDemon(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        Map.addUnit(new Demon(x, y));
        Map.queueExecutable(this, Rnd.nextInt(60) + 60);
    }
}
