package game;

public class Log {

    public static String typeUnit = "UNIT";
    public static String typeTime = "TIME";

    public static String currentType = typeTime;

    public static long eventCount = 0;
    public static long breakPointEventCount = 76079;

    public static void log(String type, String message) {
        eventCount ++;
        if(eventCount == breakPointEventCount) {
            System.out.println();
        }
        if(type == currentType) {
            System.out.println(message);
        }
    }
}
