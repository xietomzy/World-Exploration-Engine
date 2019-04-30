package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Avatar implements Serializable {
    private TETile ava;
    private Position pos;
    public Avatar(TETile avatar, TETile[][] world, Random R, ArrayList<Room> rooms) {
        ava = avatar;
        Room randomRoom = rooms.get(R.nextInt(rooms.size()));
        pos = RandomWorldGenerator.pickRandomRoomPoint(randomRoom, R);
        world[pos.getX()][pos.getY()] = ava;
    }

    public TETile getAvatar() {
        return ava;
    }

    public Position getPosition() {
        return pos;
    }

    public void move(TETile[][] world, char direction) {
        int x = getPosition().getX();
        int y = getPosition().getY();
        // left
        if (direction == 'A' || direction == 'a') {
            if (world[x - 1][y].equals(Tileset.WALL)) {
                return;
            }
            world[x - 1][y] = getAvatar();
            world[x][y] = Tileset.FLOOR;
            pos = new Position (x - 1, y);
        }
        // up
        if (direction == 'W' || direction == 'w') {
            if (world[x][y + 1].equals(Tileset.WALL)) {
                return;
            }
            world[x][y + 1] = ava;
            world[x][y] = Tileset.FLOOR;
            pos = new Position (x, y + 1);
        }
        // right
        if (direction == 'D' || direction == 'd') {
            if (world[x + 1][y].equals(Tileset.WALL)) {
                return;
            }
            world[x + 1][y] = ava;
            world[x][y] = Tileset.FLOOR;
            pos = new Position (x + 1, y);
        }
        // down
        if (direction == 'S' || direction == 's') {
            if (world[x][y - 1].equals(Tileset.WALL)) {
                return;
            }
            world[x][y - 1] = ava;
            world[x][y] = Tileset.FLOOR;
            pos = new Position (x, y - 1);
        }
    }

}
