package game;

public class OTIdMissingBuilding extends OTId {

    public static int timeToBeForgot = 500;

    public OTIdMissingBuilding(String name){
        super(name);
    }

    @Override
    public void execute(OverTile overTile) {
        if(Map.overTile(overTile.x, overTile.y) == overTile){
            Map.overTile[overTile.x][overTile.y] = null;
        }
    }
}
