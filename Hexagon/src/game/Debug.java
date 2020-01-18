package game;

import game.unit.Unit;
import gui.MainPanel;

public class Debug {

    public static String typeUnit = "UNIT";
    public static String typeTime = "TIME";
    public static String typeDebug = "DEBUG";

    public static String currentType = typeTime;

    public static void log(String type, String message) {
        if(type == currentType) {
            System.out.println(message);
        }
    }

    public static void check(boolean condition) {
        if(!condition) {
            throw new RuntimeException("Condition not met");
        }
    }

    public static void focus(Unit unit) {
        MainPanel.selectedUnit = unit;
    }
}
