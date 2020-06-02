package byow.Core;

import java.io.Serializable;

public class Room implements Comparable<Room>, Serializable {
    private int w;
    private int h;
    // Bottom Left Coordinate of Room
    private Position p;
    private static Room toComp;

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

    @Override
    public int hashCode() {
        return p.hashCode();
    }

    public int w() {
        return w;
    }

    public int h() {
        return h;
    }

    public Position p() {
        return p;
    }

    public static void setToComp(Room c) {
        toComp = c;
    }
}
