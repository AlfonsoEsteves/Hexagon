package game;

public class ResourceReplenish extends Executable {

    public int x, y;

    public ResourceReplenish(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        Map.tiles[x][y] = Tile.stone;
    }
}
