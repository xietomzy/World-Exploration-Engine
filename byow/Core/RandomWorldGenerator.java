package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class RandomWorldGenerator {
    private static final long SEED = 98467459;
    private static final Random R = new Random(SEED);

    private static final int WIDTH = 100;
    private static final int HEIGHT = 40;

    private static class Position {
        private int x;
        private int y;

        Position(int X, int Y) {
            x = X;
            y = Y;
        }
    }

    private static class Room {
        private int w;
        private int h;
        // Bottom Left Coordinate of Room
        private Position p;

        Room(int width, int height, Position pos) {
            w = width;
            h = height;
            p = pos;
        }
    }

    public static ArrayList<Room> genRooms(int numRooms) {
        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < numRooms; i++) {
            Room r;
            do {
                int width = R.nextInt(15) + 1;
                int height = R.nextInt(15) + 1;
                int x = R.nextInt(WIDTH - width - 1);
                int y = R.nextInt(HEIGHT - height - 1);
                Position p = new Position(x, y);
                r = new Room(width, height, p);
            } while (checkOverlap(rooms, r));
            rooms.add(r);
        }
        return rooms;
    }

    public static boolean checkOverlap(ArrayList<Room> rooms, Room comp) {
        for (Room r : rooms) {
            if (comp.p.y + comp.h > r.p.y && comp.p.y < r.p.y + r.h
                    && comp.p.x + comp.w > r.p.x && comp.p.x < r.p.x + r.w) {
                return true;
            }
        }
        return false;
    }

    public static void drawRoomAtLocation(Room r, TETile[][] world, TETile tile) {
        int x = r.p.x;
        int y = r.p.y;
        int w = r.w;
        int h = r.h;
        for (int i = x; i < w + x; i += 1) {
            for (int j = y; j < h + y; j += 1) {
                try {
                    world[i][j] = tile;
                } catch (IndexOutOfBoundsException e) {
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        ArrayList<Room> rooms = genRooms(10);
        for (int i = 0; i < rooms.size(); i++) {
            drawRoomAtLocation(rooms.get(i), world, new TETile((char) (i + 48), Color.blue, Color.white, "num"));
        }
        ter.renderFrame(world);
    }
}
