package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Avatar implements Serializable {
    private TETile ava;
    private Position pos;
    private Room room;
    private double width;
    private double height;
    public Avatar(TETile avatar, TETile[][] world, Random R, ArrayList<Room> rooms, int w, int h) {
        ava = avatar;
        room = rooms.get(R.nextInt(rooms.size()));
        pos = RandomWorldGenerator.pickRandomRoomPoint(room, R);
        width = (double) w;
        height = (double) h;
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
            //world[x][y].draw(x, y);
            pos = new Position (x - 1, y);
            drawSquareLighting(world, pos.getX(), pos.getY());
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
            drawSquareLighting(world, pos.getX(), pos.getY());
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
            drawSquareLighting(world, pos.getX(), pos.getY());
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
            drawSquareLighting(world, pos.getX(), pos.getY());
        }
        StdDraw.show();
    }

    public void moveString(TETile[][] world, char direction) {
        int x = getPosition().getX();
        int y = getPosition().getY();
        // left
        if (direction == 'A' || direction == 'a') {
            if (world[x - 1][y].equals(Tileset.WALL)) {
                return;
            }
            world[x - 1][y] = getAvatar();
            //world[x - 1][y].draw(x - 1, y);
            world[x][y] = Tileset.FLOOR;
            //world[x][y].draw(x, y);
            pos = new Position (x - 1, y);
            //drawSquareLighting(world, pos.getX(), pos.getY());
        }
        // up
        if (direction == 'W' || direction == 'w') {
            if (world[x][y + 1].equals(Tileset.WALL)) {
                return;
            }
            world[x][y + 1] = ava;
            //world[x][y + 1].draw(x, y + 1);
            world[x][y] = Tileset.FLOOR;
            //world[x][y].draw(x, y);
            pos = new Position (x, y + 1);
            //drawSquareLighting(world, pos.getX(), pos.getY());
        }
        // right
        if (direction == 'D' || direction == 'd') {
            if (world[x + 1][y].equals(Tileset.WALL)) {
                return;
            }
            world[x + 1][y] = ava;
            //world[x + 1][y].draw(x + 1, y);
            world[x][y] = Tileset.FLOOR;
            //world[x][y].draw(x, y);
            pos = new Position (x + 1, y);
            //drawSquareLighting(world, pos.getX(), pos.getY());
        }
        // down
        if (direction == 'S' || direction == 's') {
            if (world[x][y - 1].equals(Tileset.WALL)) {
                return;
            }
            world[x][y - 1] = ava;
            //world[x][y - 1].draw(x, y - 1);
            world[x][y] = Tileset.FLOOR;
            //world[x][y].draw(x, y);
            pos = new Position (x, y - 1);
            //drawSquareLighting(world, pos.getX(), pos.getY());
        }
        //StdDraw.show();
    }

    // Only shows 9x9 tile square of the map
    public void drawSquareLighting(TETile[][] world, int x, int y) {
        StdDraw.setPenColor(new Color(0, 0, 0));
        StdDraw.filledRectangle(width / 2, height / 2, width / 2, height / 2);
        for (int i = x - 4; i <= x + 4; i++) {
            for (int j = y - 4; j <= y + 4; j++) {
                try {
                    world[i][j].draw(i, j);
                } catch (Exception e) {
                }
            }
        }
    }

}
