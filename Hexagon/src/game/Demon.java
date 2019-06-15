package game;

import gui.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Demon extends Unit {

    public static Image imageDemon = ImageLoader.load("Demon");


    public Demon(int x, int y) {
        super(x, y);
        image = imageDemon;
    }

    public void execute() {
    }

}
