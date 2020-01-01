package game;

import java.util.Random;

public class Rnd {

    public static Random rnd = new Random(71);

    public static int nextInt(int x){
        return rnd.nextInt(x);
    }
}
