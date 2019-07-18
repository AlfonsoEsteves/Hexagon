package game;

public class OTIdDepletedResource extends OTId {

    public OTIdDepletedResource(String name){
        super(name);
    }

    @Override
    public void execute(OverTile overTile) {
        overTile.id = overTile.id.renewedVersion;
    }
}
