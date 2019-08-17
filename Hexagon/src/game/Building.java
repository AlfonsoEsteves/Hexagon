package game;

public class Building {

    public int x;
    public int y;

    // The amount of placed tiles
    public int placed;

    public Building(int x, int y) {
        this.x = x;
        this.y = y;
        placed = 1;
    }
}
