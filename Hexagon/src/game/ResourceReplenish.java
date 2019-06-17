package game;

public class ResourceReplenish implements Executable {

    public int x, y;
    public Tile tile;

    public ResourceReplenish(int x, int y, Tile tile){
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    @Override
    public void execute() {
        Map.underTile[x][y] = tile;
    }
}
