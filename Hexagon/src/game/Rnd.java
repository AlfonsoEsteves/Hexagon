package game;

import java.util.Random;

public class Rnd {

    public static Random rnd = new Random(31);

    public static int nextInt(int x){
        return rnd.nextInt(x);
    }
}
