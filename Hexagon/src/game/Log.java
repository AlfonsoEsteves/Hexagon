package game;

public class Log {

    public static String currentType = "TIME";

    public static void log(String type, String message) {
        if(type == currentType) {
            System.out.println(message);
        }
    }
}
