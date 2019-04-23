package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class RandomWorldGenerator {
    private static final long SEED = 1337;
    private static final Random R = new Random(SEED);

    private static final int WIDTH = 80;
    private static final int HEIGHT = 35;

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
                int width = R.nextInt(7 - 2) + 1;
                int height = R.nextInt(7 - 2) + 1;
                int x = R.nextInt(WIDTH - width - 3) + 1;
                int y = R.nextInt(HEIGHT - height - 3) + 1;
                Position p = new Position(x, y);
                r = new Room(width, height, p);
            } while (checkOverlap(rooms, r));
            rooms.add(r);
        }
        System.out.println(rooms.get(3).p.x);
        System.out.println(rooms.get(3).p.y);
        System.out.println(rooms.get(3).w);
        System.out.println(rooms.get(3).h);
        return rooms;
    }

    public static boolean checkOverlap(ArrayList<Room> rooms, Room comp) {
        for (Room r : rooms) {
            if ((comp.p.y + comp.h > r.p.y - 1 && comp.p.y < r.p.y + r.h + 1)
                    && (comp.p.x + comp.w > r.p.x - 1 && comp.p.x < r.p.x + r.w + 1)) {
                return true;
            }
        }
        return false;
    }

    public static void drawHallways(ArrayList<Room> rooms, TETile[][] world) {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room one = rooms.get(i);
            Room two = rooms.get(i + 1);
            int xDiff = two.p.x - one.p.x;
            int yDiff = two.p.y - one.p.y;
            if (xDiff > 0) {
                for (int j = one.p.x; j < xDiff + one.p.x; j++) {
                    if (!world[j][one.p.y].description().equals("num")) {
                        world[j][one.p.y] = Tileset.FLOOR;
                    }
                }
            }
            if (xDiff < 0) {
                for (int j = one.p.x; j > xDiff + one.p.x; j--) {
                    if (!world[j][one.p.y].description().equals("num")) {
                        world[j][one.p.y] = Tileset.FLOOR;
                    }
                }
            }
            if (yDiff > 0) {
                for (int j = one.p.y; j < yDiff + one.p.y; j++) {
                    if (!world[one.p.x + xDiff][j].description().equals("num")) {
                        world[one.p.x + xDiff][j] = Tileset.FLOOR;
                    }
                }
            }
            if (yDiff < 0) {
                for (int j = one.p.y; j > yDiff + one.p.y; j--) {
                    if (!world[one.p.x + xDiff][j].description().equals("num")) {
                        world[one.p.x + xDiff][j] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    public static void drawRoomAtLocation(Room r, TETile[][] world, TETile tile) {
        int x = r.p.x;
        int y = r.p.y;
        int w = r.w;
        int h = r.h;
        for (int i = x; i < w + x; i += 1) {
            for (int j = y; j < h + y; j += 1) {
                //try {
                world[i][j] = tile;
                //} catch (IndexOutOfBoundsException e) {
                //    return;
                //}
            }
        }
    }

    //better way to do this....
    //checks if you should put a wall; returns true if you should
    public static boolean wallCheck(TETile[][] world, int x, int y) {
        ArrayList<Integer> xDirections = new ArrayList<>();
        ArrayList<Integer> yDirections = new ArrayList<>();
        //up
        int up = y + 1;
        //down
        int down = y - 1;
        //right
        int right = x + 1;
        //left
        int left = x - 1;

        if (!(down < 0)) {
            yDirections.add(down);
        }
        if (!(up > world[0].length)) {
            yDirections.add(up);
        }
        if (!(left < 0)) {
            xDirections.add(left);
        }
        if (!(right > world.length)) {
            xDirections.add(right);
        }
        for (int side : xDirections) {
            if (world[side][y].description().equals("num") || world[side][y].equals(Tileset.FLOOR)) {
                return true;
            }
        }
        for (int side : yDirections) {
            if (world[x][side].description().equals("num") || world[x][side].equals(Tileset.FLOOR)) {
                return true;
            }
        }
        //checks corners
        for (int xSide : xDirections) {
            for (int ySide : yDirections) {
                if (world[xSide][ySide].description().equals("num") || world[xSide][ySide].equals(Tileset.FLOOR)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void drawWalls(TETile[][] world) {
        for (int x = 0; x < WIDTH - 1; x += 1) {
            for (int y = 0; y < HEIGHT - 1; y += 1) {
                if (world[x][y].equals(Tileset.NOTHING)) {
                    if (wallCheck(world, x, y)) {
                        world[x][y] = Tileset.WALL;
                    }
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
            TETile tile =  new TETile((char) (i + 48), Color.blue, Color.white, "num");
            //TETile tile = Tileset.FLOOR;
            drawRoomAtLocation(rooms.get(i), world, tile);
        }
        drawHallways(rooms, world);
        drawWalls(world);
        ter.renderFrame(world);
    }
}
