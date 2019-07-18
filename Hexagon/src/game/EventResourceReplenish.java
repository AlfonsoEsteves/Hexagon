package game;

public class EventResourceReplenish implements Executable {

    public int x, y;
    public Tile tile;

    public EventResourceReplenish(int x, int y, Tile tile){
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    @Override
    public void execute() {
        Map.underTile[x][y] = tile;
    }
}
