package byow.Core;

import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Police implements Serializable {
    private static final TETile POLICE_TILE = Tileset.LINE_INTEGRAL;
    private static final char[] DIRECTIONS = new char[] {'w', 'a', 's', 'd'};
    private Position pos;
    private char direction;
    private StringInputDevice policeMover;

    public Police(TETile[][] world, Random R, ArrayList<Room> rooms, Avatar a) {
        Room policeRoom = rooms.get(R.nextInt(rooms.size()));
        // ensures police do not start in the same room as the avatar
        while (policeRoom.equals(a.getRoom())) {
            policeRoom = rooms.get(R.nextInt(rooms.size()));
        }

        pos = RandomWorldGenerator.pickRandomRoomPoint(policeRoom, R);
        // ensures that no two policemen are in the same spot
        while (!world[pos.getX()][pos.getY()].equals(Tileset.FLOOR)) {
            pos = RandomWorldGenerator.pickRandomRoomPoint(policeRoom, R);
        }
        world[pos.getX()][pos.getY()] = POLICE_TILE;

        direction = DIRECTIONS[R.nextInt(4)];

        policeMover = new StringInputDevice("" + direction);
    }

    public Position getPosition() {
        return pos;
    }

    public void move(TETile[][] world, Random R, boolean draw) {
        if (!policeMover.possibleNextInput()) {
            policeMover = new StringInputDevice("" + direction);
        }
        direction = policeMover.getNextKey();
        //System.out.println(direction);
        int x = pos.getX();
        int y = pos.getY();
        // left
        if (direction == 'a') {
            if (world[x - 1][y].equals(Tileset.WALL) || checkAvatarCollision(world, x - 1, y)) {
                char[] differentDir = new char[] {'w', 's', 'd'};
                direction = differentDir[R.nextInt(3)];
                //System.out.println(direction);
                return;
            }
            world[x - 1][y] = POLICE_TILE;
            //world[x - 1][y].draw(x - 1, y);
            world[x][y] = Tileset.FLOOR;
            pos = new Position(x - 1, y);

        }
        // up
        if (direction == 'w') {
            if (world[x][y + 1].equals(Tileset.WALL) || checkAvatarCollision(world, x, y + 1)) {
                char[] differentDir = new char[] {'a', 's', 'd'};
                direction = differentDir[R.nextInt(3)];
                return;
            }
            world[x][y + 1] = POLICE_TILE;
            //world[x][y + 1].draw(x, y + 1);
            world[x][y] = Tileset.FLOOR;
            pos = new Position(x, y + 1);

        }
        // right
        if (direction == 'd') {
            if (world[x + 1][y].equals(Tileset.WALL) || checkAvatarCollision(world, x + 1, y)) {
                char[] differentDir = new char[] {'w', 's', 'a'};
                direction = differentDir[R.nextInt(3)];
                return;
            }
            world[x + 1][y] = POLICE_TILE;
            //world[x + 1][y].draw(x + 1, y);
            world[x][y] = Tileset.FLOOR;
            pos = new Position(x + 1, y);

        }
        // down
        if (direction == 's') {
            if (world[x][y - 1].equals(Tileset.WALL) || checkAvatarCollision(world, x, y - 1)) {
                char[] differentDir = new char[] {'w', 'a', 'd'};
                direction = differentDir[R.nextInt(3)];
                return;
            }
            world[x][y - 1] = POLICE_TILE;
            //world[x][y - 1].draw(x, y - 1);
            world[x][y] = Tileset.FLOOR;
            pos = new Position(x, y - 1);

        }
        if (draw) {
            if (checkIfInAvatarSight(world, pos.getX(), pos.getY())) {
                // draws police's new position
                world[pos.getX()][pos.getY()].draw(pos.getX(), pos.getY());
            }
            if (checkIfInAvatarSight(world, x, y)) {
                // erases police's previous position
                world[x][y].draw(x, y);
            }

            StdDraw.show();
        }
    }

    public boolean checkAvatarCollision(TETile[][] world, int x, int y) {
        return world[x][y].equals(Tileset.AVATAR);
    }

    public boolean checkIfInAvatarSight(TETile[][] world, int x, int y) {
        for (int i = x - 4; i <= x + 4; i++) {
            for (int j = y - 4; j <= y + 4; j++) {
                try {
                    if (checkAvatarCollision(world, i, j)) {
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    //continue
                }
            }
        }
        return false;
    }
}
