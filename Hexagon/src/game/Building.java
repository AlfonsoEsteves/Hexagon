package game;

import game.unit.person.Person;

public class Building {

    public int x;
    public int y;

    public Person owner;

    // The amount of placed tiles
    public int placed;

    public Building(int x, int y, Person owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        placed = 1;
    }
}
