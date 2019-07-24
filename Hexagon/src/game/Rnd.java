package game;

import java.util.Random;

public class Rnd {

    public static Random rnd = new Random(37);

    public static int nextInt(int x){
        return rnd.nextInt(x);
    }
}
