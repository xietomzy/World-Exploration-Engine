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
import java.util.Set;

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
    private int currentSave = 0;
    private String name;
    private Police police;
    //be able to choose save file
    //be able to delete save file
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        KeyboardInputSource k = new KeyboardInputSource();
        runGame(k);
        System.exit(0);
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

        StringInputDevice s = new StringInputDevice(input);
        runGame(s);

        return world;
    }

    private void startUp(InputSource inp) {
        Set<Character> menuOptions = Set.of('N', 'n', 'L', 'l', 'S', 's', 'B', 'b', 'Q', 'q');

        MainMenu m = new MainMenu(1, 1);
        m.initialize();
        m.drawMenu();

        char first;// = inp.getNextKey();

        boolean runAgain;
        do {
            runAgain = false;
            m.drawMenu();
            do {
                first = inp.getNextKey();
            } while (!menuOptions.contains(first));
            m.select(first);
            if (first == 'N' || first == 'n') {
                long seed = getSeed(inp);
                R = new Random(seed);

                m.showInput(seed + "");
                StdDraw.pause(1500);

                world = genWorld(R);
                avatar = new Avatar(Tileset.AVATAR, world, R, rooms);
                police = new Police(world, R, rooms, avatar);
                //if (inp instanceof KeyboardInputSource) {
                ter.initialize(WIDTH, HEIGHT);
                ter.renderFrame(world);
                //}

                currentState = new CurrentState(world, R, seed, avatar, name, police);
            } else if (first == 'L' || first == 'l') {
                currentState = loadState("./bruh_moment_save_data_1");
                world = currentState.world();
                R = currentState.random();
                avatar = currentState.avatar();
                police = currentState.police();
                name = currentState.name();

                m.showInput("Loading world with seed " + currentState.seed() + "...");
                StdDraw.pause(1500);

                //if (inp instanceof KeyboardInputSource) {
                ter.initialize(WIDTH, HEIGHT);
                ter.renderFrame(world);
                //}
            } else if (first == 'Q' || first == 'q') {
                System.exit(0);
            } else if (first == 'S' || first == 's') {
                m.saveFiles();
                char choice = inp.getNextKey();

                if (choice == 'B' || choice == 'b') {
                    runAgain = true;
                } else {
                    if (choice == '1') {
                        currentSave = 1;
                    } else if (choice == '2') {
                        currentSave = 2;
                    } else if (choice == '3') {
                        currentSave = 3;
                    }
                    loadSave(currentSave);

                    m.showInput("Loading save file " + currentSave + " with seed " + currentState.seed() + "...");
                    StdDraw.pause(1500);

                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(world);
                }
            } else if (first == 'B' || first == 'b') {
                name = m.chooseName();
                runAgain = true;
            }
        } while (runAgain);
    }

    private void runGame(InputSource inp) {
        startUp(inp);
        if (inp instanceof KeyboardInputSource) {
            char next = 'a';
            boolean hasNextKey;
            //boolean nameShown;
            String tileHover = "";
            HUD.showName(WIDTH, HEIGHT, name);
            do {
                hasNextKey = StdDraw.hasNextKeyTyped();
                if (hasNextKey) {
                    next = Character.toUpperCase(StdDraw.nextKeyTyped());
                    moveAvatar(next);
                    //ter.renderFrame(world);
                }
                tileHover = HUD.returnTile(world, WIDTH, HEIGHT, tileHover);
                police.move(world, R);
            } while (!hasNextKey || next != ':');
        } else if (inp instanceof StringInputDevice) {
            while (inp.possibleNextInput()) {
                char next = inp.getNextKey();
                if (next == ':') {
                    break;
                }
                moveAvatar(next);
            }
            ter.renderFrame(world);
        }
        try {
            char last = inp.getNextKey();
            if (last == 'q' || last == 'Q') {
                currentState.setCurrentState(world);
                saveWorld(currentState);
            }
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("No more keys to digest");
        }
    }

    private void loadSave(int saveNum) {
        currentState = loadState("./bruh_moment_save_data_" + saveNum);
        world = currentState.world();
        R = currentState.random();
        avatar = currentState.avatar();
        police = currentState.police();
        name = currentState.name();
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
        avatar.move(world, next);
    }

    private File firstOpenPath() {
        for (int i = 1; i <= 3; i++) {
            File f = new File("./bruh_moment_save_data_" + i);
            if (!f.exists()) {
                return f;
            }
        }
        return new File("./bruh_moment_save_data_1");
    }

    /**
     *
     * @source SaveDemo
     */
    private void saveWorld(CurrentState currentState) {
        File f;
        if (currentSave == 0) {
            f = firstOpenPath();
        } else {
            f = new File("./bruh_moment_save_data_" + currentSave);
        }
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

    private CurrentState loadState(String file) {
        File f = new File(file);
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
