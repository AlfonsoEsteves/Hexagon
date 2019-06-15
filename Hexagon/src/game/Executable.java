package game;

public interface Executable {

    void execute();

    default boolean alive() {
        return true;
    }

}
