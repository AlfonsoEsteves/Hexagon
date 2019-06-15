package game;

public class CreateDemon extends Executable {

    public int x, y;

    public CreateDemon(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        Map.addUnit(new Demon(x, y));
        Map.queueExecutable(this, Rnd.nextInt(50) + 50);
    }
}