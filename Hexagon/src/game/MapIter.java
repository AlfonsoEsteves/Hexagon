package game;

import java.util.Iterator;

public class MapIter {

    public static MapIterable of(int radius) {
        return new MapIterable(radius);
    }

    public static class MapIterable implements Iterable<int[]> {

        public MapIterator mapIterator;

        public MapIterable(int radius){
            mapIterator = new MapIterator(radius);
        }

        @Override
        public Iterator<int[]> iterator() {
            return mapIterator;
        }
    }

    public static class MapIterator implements Iterator<int[]> {

        public int radius;
        public int[] position;

        public MapIterator(int radius) {
            this.radius = radius;
            position = new int[2];
            position[0] = -radius - 1;// Substract one because the next method first modifies then returns
            position[1] = -radius;
        }

        @Override
        public boolean hasNext() {
            if(position[0] == radius && position[1] == radius) {
                return false;
            }
            else{
                return true;
            }
        }

        @Override
        public int[] next() {
            position[0]++;
            if(position[1] < 0) {
                if (position[0] - position[1] > radius) {
                    position[1]++;
                    position[0] = -radius;
                }
            }
            else{
                if (position[0] > radius) {
                    position[1]++;
                    position[0] = -radius + position[1];
                }
            }
            return position;
        }
    }

}
