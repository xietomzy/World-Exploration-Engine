package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
//import byow.SaveDemo.Editor;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

//import java.awt.Color;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;//30;
    private Random R;
    private ArrayList<Room> rooms;
    private Avatar avatar;
    private CurrentState currentState;
    private TETile[][] world;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        KeyboardInputSource k = new KeyboardInputSource();
        MainMenu m = new MainMenu(1, 1);
        m.initialize();
        char first = k.getNextKey();
        m.select(first);
        if (first == 'N' || first == 'n') {
            long seed = getSeed(k);
            Random R = new Random(seed);

            m.showInput(seed + "");
            StdDraw.pause(1500);

            world = genWorld(R);
            avatar = new Avatar(Tileset.AVATAR, world, R, rooms);

            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(world);

            currentState = new CurrentState(world, R, seed, avatar);
        } else if (first == 'L' || first == 'l') {
            currentState = loadState();
            world = currentState.world();
            R = currentState.random();
            avatar = currentState.avatar();

            m.showInput("Loading world with seed " + currentState.seed() + "...");
            StdDraw.pause(1500);

            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(world);
        }

        char next = 'a';
        boolean hasNextKey = false;
        String tileHover = "";
        do {
            hasNextKey = StdDraw.hasNextKeyTyped();
            if (hasNextKey) {
                next = Character.toUpperCase(StdDraw.nextKeyTyped());
                moveAvatar(next);
                //next = k.getNextKey();
                ter.renderFrame(world);
            }
            tileHover = HUD.returnTile(world, WIDTH, HEIGHT, ter, tileHover);
            /*Font newFont = new Font("Monaco", Font.BOLD, 16);
            StdDraw.setFont(newFont);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(6, HEIGHT - 1, description);
            StdDraw.show();
            Font font = new Font("Monaco", Font.BOLD, 16 - 2);
            StdDraw.setFont(font);*/


        } while (!hasNextKey || next != ':');


        char last = k.getNextKey();
        if (last == 'q' || last == 'Q') {
            currentState.setCurrentState(world);
            saveWorld(currentState);
            System.exit(0);
        } else if (last == '!') {
            //Quit without saving
            System.exit(0);
        }
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
        ter.initialize(WIDTH, HEIGHT); //REMEMBER TO COMMENT THIS OUT FOR AUTOGRADER
        StringInputDevice s = new StringInputDevice(input);
        char first = s.getNextKey();
        if (first != 'n' && first != 'N') {
            return null;
        }
        long seed = getSeed(s);
        Random R = new Random(seed);
        TETile[][] world = genWorld(R);
        ter.renderFrame(world); //REMEMBER TO COMMENT THIS OUT FOR AUTOGRADER


        return world;
    }

    private long getSeed(InputSource inp) {
        long seed = 0;
        while (inp.possibleNextInput()) {
            char next = inp.getNextKey();
            if (Character.isDigit(next)) {
                seed *= 10;
                seed += next - 48;
            } else {
                break;
            }
        }
        return seed;
    }

    private TETile[][] genWorld(Random R) {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        rooms = RandomWorldGenerator.genRooms(R.nextInt(16)
                + 30, R, WIDTH, HEIGHT);
        RandomWorldGenerator.drawHallwaysNew(rooms, world, R);
        for (int i = 0; i < rooms.size(); i++) {
            //TETile tile =  new TETile((char) (i + 48), Color.blue, Color.white, "num");
            TETile tile = Tileset.FLOOR;
            RandomWorldGenerator.drawRoomAtLocation(rooms.get(i), world, tile);
        }
        RandomWorldGenerator.drawWalls(world);
        return world;
    }

    private void moveAvatar(char next) {
        //while (true) {
        //System.out.println(HUD.returnTile());
        //if (inp.possibleNextInput()) {
        //char next = inp.getNextKey();
        //System.out.println(next);
        avatar.move(world, next);
        //}
        //}
    }

    /**
     *
     * @source SaveDemo
     */
    private void saveWorld(CurrentState currentState) {
        File f = new File("./bruh_moment_save_data");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(currentState);
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private CurrentState loadState() {
        File f = new File("./bruh_moment_save_data");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (CurrentState) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        /* In the case no CurrentState has been saved yet, we return a new one. */
        return null;
    }
}
