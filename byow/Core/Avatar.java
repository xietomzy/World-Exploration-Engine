package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class Avatar {
    private Tileset ava;

    public Avatar(Tileset avatar, TETile[][] world, Random R, ArrayList<Room> rooms) {
        ava = avatar;
        Room randomRoom = rooms.get(R.nextInt(rooms.size()));git s
    }


}
