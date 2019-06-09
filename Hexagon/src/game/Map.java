package game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Stream;

public class Map {

	public static final int size = 30;

	public static Tile[][] tiles = new Tile[size][size];
	public static Unit[][] units = new Unit[size][size];

	public static final int executableQueueSize = 100;

	public static LinkedList<Executable>[] executableQueue = new LinkedList[executableQueueSize];

	public static int time = 0;
	
	public static Random rnd = new Random(0);

	static {
	    for(int i = 0;i < executableQueueSize;i++){
            executableQueue[i] = new LinkedList();
        }

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (rnd.nextInt(5) == 0) {
					tiles[i][j] = Tile.water;
				}
				else {
					tiles[i][j] = Tile.grass;
				}
			}
		}
		
		addUnit(8, 7);
		
		tiles[4][4] = Tile.fertileGround;
        tiles[5][9] = Tile.stone;
	}

	public static void addUnit(int x, int y) {
		Unit unit = new Unit(x, y);
		queueExecutable(unit, 1);
		units[x][y] = unit;
	}
	
	public static void execute() {
		executableQueue[time % executableQueueSize].stream().forEach(x -> x.execute());

		time++;
	}

	public static void queueExecutable(Executable executable, int delay) {
        executableQueue[(time + delay) % executableQueueSize].addLast(executable);
    }
	
	public static Tile tile(int x, int y) {
		if(x >= 0 && x < size && y >= 0 && y < size) {
			return tiles[x][y];
		}
		else {
			return Tile.water;
		}
	}
	
	public static int getX(int dir) {
		if(dir == 0) {
			return 1;
		}
		if(dir == 1) {
			return 1;
		}
		if(dir == 3) {
			return -1;
		}
		if(dir == 4) {
			return -1;
		}
		return 0;
	}
	
	public static int getY(int dir) {
		if(dir == 1) {
			return 1;
		}
		if(dir == 2) {
			return 1;
		}
		if(dir == 4) {
			return -1;
		}
		if(dir == 5) {
			return -1;
		}
		return 0;
	}
	
	public static boolean steppable(int x, int y) {
		if(tile(x, y) == Tile.water) {
			return false;
		}
		else {
			return true;
		}
	}
	
}
