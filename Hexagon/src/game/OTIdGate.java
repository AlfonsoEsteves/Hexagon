package game;

import game.unit.demon.Demon;

public class OTIdGate extends OTId {

    public OTIdGate(String name){
        super(name);
    }

    @Override
    public void execute(OverTile overTile) {
        Map.addUnit(new Demon(overTile.x, overTile.y));
        Map.queueExecutable(overTile, Rnd.nextInt(80) + 80);
    }
}
