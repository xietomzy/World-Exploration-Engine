package byow.Core;

//import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

//import java.awt.*;
import java.util.*;

public class RandomWorldGenerator {
    //private static final long SEED = 1234567890;
    //private static final Random R = new Random(SEED);

    //private static final int WIDTH = 80;
    //private static final int HEIGHT = 35;

    public static class Room implements Comparable<Room>{
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
    }

    public static ArrayList<Room> genRooms(int numRooms, Random R, int width, int height) {
        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < numRooms; i++) {
            Room r;
            do {
                int wi = R.nextInt(7 - 2) + 1;
                int he = R.nextInt(7 - 2) + 1;
                int x = R.nextInt(width - wi - 3) + 1;
                int y = R.nextInt(height - he - 3) + 1;
                Position p = new Position(x, y);
                r = new Room(wi, he, p);
            } while (checkOverlap(rooms, r));
            rooms.add(r);
        }
        return rooms;
    }

    public static boolean checkOverlap(ArrayList<Room> rooms, Room comp) {
        for (Room r : rooms) {
            if ((comp.p.getY() + comp.h > r.p.getY() - 1 && comp.p.getY() < r.p.getY() + r.h + 1)
                    && (comp.p.getX() + comp.w > r.p.getX() - 1 && comp.p.getX() < r.p.getX() + r.w + 1)) {
                return true;
            }
        }
        return false;
    }

    public static Position pickRandomEdgePoint(Room r, Random R) {
        int x = r.p.getX();
        int y = r.p.getY();
        int width = r.w;
        int height = r.h;
        int whichSide = R.nextInt(4);
        if (whichSide == 0) {
            return new Position(R.nextInt(width) + x, y);
        } else if (whichSide == 1) {
            return new Position(x + width - 1, R.nextInt(height) + y);
        } else if (whichSide == 2) {
            return new Position(R.nextInt(width) + x, y + height - 1);
        } else {
            return new Position(x, R.nextInt(height) + y);
        }
    }

    public static void drawHallway(TETile[][] world, Position a, Position b, Random R) {
        int xDiff = a.getX() - b.getX();
        int yDiff = a.getY()  - b.getY();
        if (RandomUtils.bernoulli(R)) {
            Position corner = new Position(b.getX(), a.getY());
            drawRoomAtLocation(new Room(Math.abs(xDiff) + 1, 1, xDiff < 0 ? a : corner), world, Tileset.FLOOR);
            drawRoomAtLocation(new Room(1, Math.abs(yDiff), yDiff < 0 ? corner : b), world, Tileset.FLOOR);
        } else {
            Position corner = new Position(a.getX(), b.getY());
            drawRoomAtLocation(new Room(1, Math.abs(yDiff) + 1, yDiff < 0 ? a : corner), world, Tileset.FLOOR);
            drawRoomAtLocation(new Room(Math.abs(xDiff), 1, xDiff < 0 ? corner : b), world, Tileset.FLOOR);
        }
    }

    public static void drawHallwaysNew(ArrayList<Room> rooms, TETile[][] world, Random R) { //Here there could be a lot of optimization but whatever
        WeightedQuickUnionUF hallConnections = new WeightedQuickUnionUF(rooms.size());
        HashMap<Room, Integer> room2index = new HashMap<>();
        for (int i = 0; i < rooms.size(); i++) {
            room2index.put(rooms.get(i), i);
        }

        for (Room r : rooms) {
            Room.toComp = r;
            ArrayList<Room> closestRooms = (ArrayList<Room>) rooms.clone(); //O(N)
            Collections.sort(closestRooms); //O(NlogN)
            closestRooms.remove(0); //O(N)
            for (Room comp : closestRooms) {
                if (!hallConnections.connected(room2index.get(r), room2index.get(comp))) {
                    drawHallway(world, pickRandomEdgePoint(r, R), pickRandomEdgePoint(comp, R), R);
                    hallConnections.union(room2index.get(r), room2index.get(comp));
                    //System.out.println(room2index.get(r) + "-->" + room2index.get(comp));
                    break;
                }
            }
        }
    }

    public static void drawHallwaysOld(ArrayList<Room> rooms, TETile[][] world) {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room one = rooms.get(i);
            Room two = rooms.get(i + 1);
            int xDiff = two.p.getX() - one.p.getX();
            int yDiff = two.p.getY() - one.p.getY();
            if (xDiff > 0) {
                for (int j = one.p.getX(); j < xDiff + one.p.getX(); j++) {
                    if (!world[j][one.p.getY()].description().equals("num")) {
                        world[j][one.p.getY()] = Tileset.FLOOR;
                    }
                }
            }
            if (xDiff < 0) {
                for (int j = one.p.getX(); j > xDiff + one.p.getX(); j--) {
                    if (!world[j][one.p.getY()].description().equals("num")) {
                        world[j][one.p.getY()] = Tileset.FLOOR;
                    }
                }
            }
            if (yDiff > 0) {
                for (int j = one.p.getY(); j < yDiff + one.p.getY(); j++) {
                    if (!world[one.p.getX() + xDiff][j].description().equals("num")) {
                        world[one.p.getX() + xDiff][j] = Tileset.FLOOR;
                    }
                }
            }
            if (yDiff < 0) {
                for (int j = one.p.getY(); j > yDiff + one.p.getY(); j--) {
                    if (!world[one.p.getX() + xDiff][j].description().equals("num")) {
                        world[one.p.getX() + xDiff][j] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    public static void drawRoomAtLocation(Room r, TETile[][] world, TETile tile) {
        int x = r.p.getX();
        int y = r.p.getY();
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
            if (world[side][y].description().equals("num")
                    || world[side][y].equals(Tileset.FLOOR)) {
                return true;
            }
        }
        for (int side : yDirections) {
            if (world[x][side].description().equals("num")
                    || world[x][side].equals(Tileset.FLOOR)) {
                return true;
            }
        }
        //checks corners
        for (int xSide : xDirections) {
            for (int ySide : yDirections) {
                if (world[xSide][ySide].description().equals("num")
                        || world[xSide][ySide].equals(Tileset.FLOOR)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void drawWalls(TETile[][] world) {
        for (int x = 0; x < world.length - 1; x += 1) {
            for (int y = 0; y < world[0].length - 1; y += 1) {
                if (world[x][y].equals(Tileset.NOTHING)) {
                    if (wallCheck(world, x, y)) {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    /*
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        ArrayList<Room> rooms = genRooms(R.nextInt(11) + 10);
        for (int i = 0; i < rooms.size(); i++) {
            TETile tile =  new TETile((char) (i + 48), Color.blue, Color.white, "num");
            //TETile tile = Tileset.FLOOR;
            drawRoomAtLocation(rooms.get(i), world, tile);
        }
        drawHallways(rooms, world);
        drawWalls(world);
        ter.renderFrame(world);
    }
    */
}
