package game;

import java.util.LinkedList;

public class Map {

	public static final int size = 650;

	public static Tile[][] underTile = new Tile[size][size];
    public static Tile[][] overTile = new Tile[size][size];
	public static Unit[][] unit = new Unit[size][size];

	public static final int executableQueueSize = 100;

	public static LinkedList<Executable>[] executableQueue = new LinkedList[executableQueueSize];

	public static int time = 0;

	static {
	    for(int i = 0;i < executableQueueSize;i++){
            executableQueue[i] = new LinkedList();
        }

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (Rnd.nextInt(40) == 0) {
					underTile[i][j] = Tile.water;
				}
				else if (Rnd.nextInt(100) == 0) {
					underTile[i][j] = Tile.stone;
				}
				else if (Rnd.nextInt(150) == 0) {
					underTile[i][j] = Tile.tree;
				}
				else if (Rnd.nextInt(180) == 0) {
					underTile[i][j] = Tile.iron;
				}
				else if (Rnd.nextInt(2000) == 0) {
					underTile[i][j] = Tile.gate;
					CreateDemon createDemon = new CreateDemon(i, j);
					queueExecutable(createDemon, 1);
				}
				else {
					underTile[i][j] = Tile.grass;
					if (Rnd.nextInt(300) == 0) {
						addUnit(new Person(i, j));
					}
				}
			}
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

    public static Tile overTile(int x, int y) {
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

	public static Searchable has(int x, int y, Object identity) {
		if(underTile(x, y).is(identity)) {
			return underTile(x, y);
		}
		if(overTile(x, y) != null && overTile(x, y).is(identity)) {
			return overTile(x, y);
		}
		Unit unit = unit(x, y);
		while(unit != null) {
			if(unit.is(identity)) {
				return unit;
			}
			unit = unit.next;
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
	
	public static boolean steppable(int x, int y) {
		if (underTile(x, y).steppable && (overTile(x, y) == null || overTile(x, y).steppable)) {
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
