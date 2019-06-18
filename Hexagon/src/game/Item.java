package game;

public class Item extends Occupant {

    public enum Identity {stone, wood, iron, sword}

    public Identity identity;

    public Item(Identity identity) {
        this.identity = identity;
    }

    @Override
    public boolean is(Object identity) {
        return this.identity == identity;
    }

}
