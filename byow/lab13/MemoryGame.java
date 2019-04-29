package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();


        rand = new Random(seed);
        round = 0;
    }

    public String generateRandomString(int n) {
        String random = "";
        for (int i = 0; i < n; i++) {
            random += CHARACTERS[rand.nextInt(CHARACTERS.length)];
        }
        return random;
    }

    public void drawFrame(String s) {
        StdDraw.clear();
        StdDraw.setFont(new Font("Helvetica", Font.BOLD, 30));
        StdDraw.text((double) width / 2, (double) height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (char c : letters.toCharArray()) {
            drawFrame(Character.toString(c));
            StdDraw.pause(1000);
            StdDraw.clear();
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        String mem = "";
        while (mem.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                mem += StdDraw.nextKeyTyped();
                drawFrame(mem);
            }
        }
        return mem;
    }

    public void startGame() {
        String randStr = "", guess = "";
        while (randStr.equals(guess)) {
            round++;
            drawFrame("Round " + round);
            StdDraw.pause(1000);
            randStr = generateRandomString(round);
            flashSequence(randStr);
            guess = solicitNCharsInput(round);
        }
        drawFrame("Major oof, you made it to round " + round);
    }

}
