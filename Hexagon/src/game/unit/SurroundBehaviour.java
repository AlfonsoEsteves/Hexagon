package game.unit;

import game.Rnd;

public class SurroundBehaviour {

    public static int startingMaxTurns = 2;

    public boolean surrounding;
    public int orientation;
    public int direction;
    public int turns;
    public int maxTurns;
    public int closestDistance;

    public SurroundBehaviour() {
        maxTurns = startingMaxTurns;
    }

    public void startSurrounding(int dirToDestination, int distanceToDestination) {
        surrounding = true;
        orientation = Rnd.nextInt(2) * 2 - 1;
        direction = dirToDestination + orientation;
        maxTurns = maxTurns * 3 / 2;
        closestDistance = distanceToDestination;
    }

    public void surround(Unit unit, int distanceToDestination) {
        for (int i = -1; i < 3; i++) {
            if (unit.moveInDirection((direction + i * orientation + 6) % 6)) {
                direction = direction + i * orientation;
                break;
            }
        }
       turns ++;
        if(turns >= maxTurns) {
            surrounding = false;
        }
        if(distanceToDestination < closestDistance) {
            surrounding = false;
            maxTurns = startingMaxTurns;
        }
    }
}
