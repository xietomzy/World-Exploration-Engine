package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Avatar implements Serializable {
    private TETile ava;
    private Position pos;
    private Room room;
    public Avatar(TETile avatar, TETile[][] world, Random R, ArrayList<Room> rooms) {
        ava = avatar;
        room = rooms.get(R.nextInt(rooms.size()));
        pos = RandomWorldGenerator.pickRandomRoomPoint(room, R);
        world[pos.getX()][pos.getY()] = ava;
    }

    public TETile getAvatar() {
        return ava;
    }

    public Position getPosition() {
        return pos;
    }

    public Room getRoom() {
        return room;
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
            world[x - 1][y].draw(x - 1, y);
            world[x][y] = Tileset.FLOOR;
            world[x][y].draw(x, y);
            pos = new Position (x - 1, y);
        }
        // up
        if (direction == 'W' || direction == 'w') {
            if (world[x][y + 1].equals(Tileset.WALL)) {
                return;
            }
            world[x][y + 1] = ava;
            world[x][y + 1].draw(x, y + 1);
            world[x][y] = Tileset.FLOOR;
            world[x][y].draw(x, y);
            pos = new Position (x, y + 1);
        }
        // right
        if (direction == 'D' || direction == 'd') {
            if (world[x + 1][y].equals(Tileset.WALL)) {
                return;
            }
            world[x + 1][y] = ava;
            world[x + 1][y].draw(x + 1, y);
            world[x][y] = Tileset.FLOOR;
            world[x][y].draw(x, y);
            pos = new Position (x + 1, y);
        }
        // down
        if (direction == 'S' || direction == 's') {
            if (world[x][y - 1].equals(Tileset.WALL)) {
                return;
            }
            world[x][y - 1] = ava;
            world[x][y - 1].draw(x, y - 1);
            world[x][y] = Tileset.FLOOR;
            world[x][y].draw(x, y);
            pos = new Position (x, y - 1);
        }
        StdDraw.show();
    }

}
