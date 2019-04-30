package byow.Core;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;

    Position(int X, int Y) {
        x = X;
        y = Y;
    }

    /** @source Modified Point class method from proj 2AB to work with
     * similar Position class
     */

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    /**
     * Returns the euclidean distance (L2 norm) squared between two points
     * (x1, y1) and (x2, y2). Note: This is the square of the Euclidean distance,
     * i.e. there's no square root.
     */
    private static double distance(int x1, int x2, int y1, int y2) {
        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }

    /**
     * Returns the euclidean distance (L2 norm) squared between two points.
     * Note: This is the square of the Euclidean distance, i.e.
     * there's no square root.
     */
    public static double distance(Position p1, Position p2) {
        return distance(p1.getX(), p2.getX(), p1.getY(), p2.getY());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Position otherPoint = (Position) other;
        return getX() == otherPoint.getX() && getY() == otherPoint.getY();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y);
    }

    @Override
    public String toString() {
        return String.format("Point x: %.10f, y: %.10f", x, y);
    }
}
