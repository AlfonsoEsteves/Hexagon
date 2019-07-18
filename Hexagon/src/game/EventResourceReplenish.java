package game;

public class EventResourceReplenish implements Executable {

    public OverTile tile;

    public EventResourceReplenish(OverTile tile){
        this.tile = tile;
    }

    @Override
    public void execute() {
        tile.id = tile.id.renewedVersion;
    }
}
