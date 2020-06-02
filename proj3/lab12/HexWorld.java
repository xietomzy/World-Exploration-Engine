package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        int startX = p.x - (s - 1);
        int startY = p.y - (s - 1);
        addLinesBottom(world, s, t, new Position(startX, startY), 3 * (s - 1) + 1);
        addLinesTop(world, s, t, new Position(startX, startY - 1), 3 * (s - 1) + 1);

    }

    private static void addLinesTop(TETile[][] world, int s, TETile t, Position start, int length) {
        for (int i = start.x; i < start.x + length; i++) {
            world[i][start.y] = t;
        }
        if (length > s) {
            addLinesTop(world, s, t, new Position(start.x + 1, start.y - 1), length - 2);
        }
    }

    private static void addLinesBottom(TETile[][] world, int s, TETile t, Position start, int length) {
        for (int i = start.x; i < start.x + length; i++) {
            world[i][start.y] = t;
        }
        if (length > s) {
            addLinesBottom(world, s, t, new Position(start.x + 1, start.y + 1), length - 2);
        }
    }

    public static class Position {
        private int x;
        private int y;

        Position(int X, int Y) {
            x = X;
            y = Y;
        }
    }
}
