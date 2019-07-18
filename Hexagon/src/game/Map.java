package game;

import game.unit.person.Person;
import game.unit.Unit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Map {

	public static final int size = 350;

	public static Tile[][] underTile = new Tile[size][size];
    public static OverTile[][] overTile = new OverTile[size][size];
	public static Unit[][] unit = new Unit[size][size];
	public static Dropped[][] dropped = new Dropped[size][size];

	public static final int executableQueueSize = 500;

	public static LinkedList<Executable>[] executableQueue = new LinkedList[executableQueueSize];

	public static int time = 0;

	static {
	    for(int i = 0;i < executableQueueSize;i++){
            executableQueue[i] = new LinkedList();
        }

	    List<Integer> richPointX = new ArrayList<>();
	    List<Integer> richPointY = new ArrayList<>();
	    List<OverTile.Id> richPointOverTileIds = new ArrayList<>();

	    double factor = size * size / 7500;
		createRichPoint(richPointX, richPointY, richPointOverTileIds, factor * 1.2, OverTile.stoneMine);
		createRichPoint(richPointX, richPointY, richPointOverTileIds, factor, OverTile.fruitBush);
		createRichPoint(richPointX, richPointY, richPointOverTileIds, factor, OverTile.tree);
		createRichPoint(richPointX, richPointY, richPointOverTileIds, factor, OverTile.ironMine);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				underTile[i][j] = Tile.grass;
				for(int k = 0;k<richPointOverTileIds.size();k++){
					int x = richPointX.get(k);
					int y = richPointY.get(k);
					if(Rnd.nextInt(180) > distance(i, j, x, y) + 160){
						overTile[i][j] = new OverTile(richPointOverTileIds.get(k), i, j);
						break;
					}
				}

				if (underTile[i][j] == Tile.grass && overTile[i][j] == null){
					if(Rnd.nextInt(3000) == 0) {
						overTile[i][j] = new OverTile(OverTile.gate, i, j);
						EventCreateDemon createDemon = new EventCreateDemon(i, j);
						queueExecutable(createDemon, 1);
					}
					else if (Rnd.nextInt(300) == 0) {
						addUnit(new Person(i, j));
					}
				}
			}
		}
	}

	private static void createRichPoint(List<Integer> richPointX, List<Integer> richPointY, List<OverTile.Id> richPointOverTileIds, double amount, OverTile.Id overTileId) {
		for(int i = 0;i<amount;i++) {
			richPointX.add(Rnd.nextInt(size));
			richPointY.add(Rnd.nextInt(size));
			richPointOverTileIds.add(overTileId);
		}
	}

	public static void addUnit(Unit unit) {
		if(Map.unit[unit.x][unit.y] == null) {
			Map.unit[unit.x][unit.y] = unit;
		}
		else{
			Unit last = Map.unit[unit.x][unit.y];
			while(last.next != null) {
				last = last.next;
			}
			last.next = unit;
		}
		queueExecutable(unit, 1);
	}
	
	public static void execute() {
		LinkedList<Executable> currentQueue = executableQueue[time % executableQueueSize];
		while(!currentQueue.isEmpty()) {
			Executable executable = currentQueue.removeFirst();
			if(executable.alive()) {
				executable.execute();
			}
		}
		time++;
	}

	public static void queueExecutable(Executable executable, int delay) {
        executableQueue[(time + delay) % executableQueueSize].addLast(executable);
    }
	
	public static Tile underTile(int x, int y) {
		if(x >= 0 && x < size && y >= 0 && y < size) {
			return underTile[x][y];
		}
		else {
			return Tile.water;
		}
	}

    public static OverTile overTile(int x, int y) {
        if(x >= 0 && x < size && y >= 0 && y < size) {
            return overTile[x][y];
        }
        else {
            return null;
        }
    }

    public static Unit unit(int x, int y) {
		if(x >= 0 && x < size && y >= 0 && y < size) {
			return unit[x][y];
		}
		else {
			return null;
		}
	}

	public static Dropped dropped(int x, int y) {
		if(x >= 0 && x < size && y >= 0 && y < size) {
			return dropped[x][y];
		}
		else {
			return null;
		}
	}

	public static <T> T has(int x, int y, Predicate filter) {
		if(filter.test(underTile(x, y))) {
			return (T)underTile(x, y);
		}
		if(filter.test(overTile(x, y))) {
			return (T)overTile(x, y);
		}
		Unit unit = unit(x, y);
		while(unit != null) {
			if(filter.test(unit)) {
				return (T)unit;
			}
			unit = unit.next;
		}
		Dropped dropped = dropped(x, y);
		while(dropped != null) {
			if(filter.test(dropped)) {
				return (T)dropped;
			}
			dropped = dropped.next;
		}
		return null;
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

	public static int closestDirection(int x, int y) {
		double z = (double)(y) / (double)(x);
		if (x + y >= 0) {
			if(y * 2 < x) {
				return 0;
			}
			else if(y < x * 2) {
				return 1;
			}
			else{
				return 2;
			}
		}
		else{
			if(y * 2 > x) {
				return 3;
			}
			else if(y > x * 2) {
				return 4;
			}
			else{
				return 5;
			}
		}
	}
	
	public static boolean steppable(int x, int y) {
		if (underTile(x, y).steppable && (overTile(x, y) == null || overTile(x, y).id.steppable)) {
			return true;
		} else {
			return false;
		}
	}

	public static int distance(int x1, int y1, int x2, int y2) {
		int diffX = x2 - x1;
		int diffY = y2 - y1;
		if (diffX * diffY > 0) {
			return Math.max(Math.abs(diffX), Math.abs(diffY));
		}
		else{
			return Math.abs(diffX) + Math.abs(diffY);
		}
	}
}
