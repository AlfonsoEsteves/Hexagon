package game;

import game.unit.demon.Demon;

public class OTIdGate extends OTId {

    public static int maxDelay = 150;

    public OTIdGate(String name){
        super(name);
    }

    @Override
    public void execute(OverTile overTile) {
        Map.addUnit(new Demon(overTile.x, overTile.y));
        Map.queueExecutable(overTile, 1 + Rnd.nextInt(maxDelay));
    }
}
