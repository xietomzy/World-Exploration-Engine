package byow.Core;

public class Room implements Comparable<Room>{
    public int w;
    public int h;
    // Bottom Left Coordinate of Room
    public Position p;
    public static Room toComp;

    Room(int width, int height, Position pos) {
        w = width;
        h = height;
        p = pos;
    }

    double distance(Room other) {
        return Position.distance(this.p, other.p);
    }

    @Override
    public int compareTo(Room other) {
        return toComp != null ? (int) (this.distance(toComp) - other.distance(toComp)) : 0;
    }

    @Override
    public boolean equals(Object other) {
        return this.p.equals(((Room) other).p);
    }
}
