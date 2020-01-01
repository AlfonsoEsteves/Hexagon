package game;

public class Debug {

    public static int count = 0;
    public static int breakPointCount = 285;

    public static void breakPoint() {
        count ++;
        if(count == breakPointCount) {
            System.out.println("BREAK POINT");
        }
    }

    public static void check(boolean condition) {
        breakPoint();
        if(!condition) {
            throw new RuntimeException("ERROR AT BREAK POINT " + count);
        }
    }
}
