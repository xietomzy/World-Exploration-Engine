package byow.Core;

import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        //TERenderer ter = new TERenderer();
        //ter.initialize(WIDTH, HEIGHT);
        StringInputDevice s = new StringInputDevice(input);
        char first = s.getNextKey();
        if (first != 'n' && first != 'N') {
            return null;
        }
        long seed = 0;
        while (s.possibleNextInput()) {
            char next = s.getNextKey();
            if (Character.isDigit(next)) {
                seed *= 10;
                seed += next - 48;
            } else {
                break;
            }
        }
        System.out.println(seed);
        Random R = new Random(seed);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        ArrayList<RandomWorldGenerator.Room> rooms = RandomWorldGenerator.genRooms(R.nextInt(11)
                + 10, R, WIDTH, HEIGHT);
        for (int i = 0; i < rooms.size(); i++) {
            TETile tile =  new TETile((char) (i + 48), Color.blue, Color.white, "num");
            //TETile tile = Tileset.FLOOR;
            RandomWorldGenerator.drawRoomAtLocation(rooms.get(i), world, tile);
        }
        RandomWorldGenerator.drawHallways(rooms, world);
        RandomWorldGenerator.drawWalls(world);
        //ter.renderFrame(world);


        return world;
    }
}
