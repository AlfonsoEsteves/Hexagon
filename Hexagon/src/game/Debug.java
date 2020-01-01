package game;

public class Debug {

    public static String typeUnit = "UNIT";
    public static String typeTime = "TIME";
    public static String typeDebug = "DEBUG";

    public static String currentType = typeTime;

    public static long eventCount = 0;
    public static long breakPointEventCount = 13817;

    public static void log(String type, String message) {
        eventCount ++;
        if(eventCount == breakPointEventCount) {
            System.out.println();
        }
        if(type == currentType) {
            System.out.println(message);
        }
    }

    public static void check(boolean condition) {
        log(typeDebug, "Checking condition");
        if(!condition) {
            throw new RuntimeException("ERROR AT BREAK POINT " + currentType);
        }
    }
}
