package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class MainMenu {
    private double width;
    private double height;
    private final double offset = 0.05;

    public MainMenu(int w, int h) {
        width = w;
        height = h;
    }

    /**
     *
     * @source TERenderer
     */
    public void initialize() {
        StdDraw.setCanvasSize((int) width * 600, (int) height * 600);

        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);

        StdDraw.enableDoubleBuffering();
    }

    public void drawMenu() {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.text(width / 2, height - offset * 2, "Bruh Moment: The Game");
        StdDraw.text(width / 2, height / 2, "Load Game (L)");
        StdDraw.text(width / 2, height / 2 + offset, "New Game (N)");
        StdDraw.text(width / 2, height / 2 - offset, "Save Files (S)");
        StdDraw.text(width / 2, height / 2 - 2 * offset, "Delete Save (D)");
        StdDraw.text(width / 2, height / 2 - 3 * offset, "Choose Name (B)");
        StdDraw.text(width / 2, height / 2 - 4 * offset, "Quit (Q)");
        StdDraw.show();
    }

    public void saveFiles() {
        StdDraw.clear(new Color(0, 0,0));
        StdDraw.text(width / 2, height / 2 + offset, "Save File (1)");
        StdDraw.text(width / 2, height / 2, "Save File (2)");
        StdDraw.text(width / 2, height / 2 - offset, "Save File (3)");
        StdDraw.text(width / 2, height / 2 - 2 * offset, "Back (B)");
        StdDraw.show();
    }

    public void deleteFile() {
        saveFiles();
        StdDraw.text(width / 2, height / 2 + 2 * offset, "Choose which save to delete");
        StdDraw.show();
    }

    public void select(char selection) {
        StdDraw.setPenColor(new Color(255, 0, 255));
        if (selection == 'N' || selection == 'n') {
            StdDraw.text(width / 2, height / 2 + offset, "New Game (N)");
        } else if (selection == 'L' || selection == 'l') {
            StdDraw.text(width / 2, height / 2, "Load Game (L)");
        }
        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.show();
    }

    public void showInput(String input) {
        StdDraw.text(width / 2, height / 2 + offset * 2, input);
        StdDraw.show();
    }

    public void drawName(String s) {
        StdDraw.setPenColor(new Color(0, 0, 0));
        StdDraw.filledRectangle(width / 2, height / 2, offset * 30, offset / 2);
        StdDraw.show();

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    // Allows user to enter name; pressing space leads back to the main menu
    public String chooseName() {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.text(width / 2, height / 2 + 3 * offset, "Pick your thief's name:");
        StdDraw.text(width / 2, height / 2 + 2 * offset, "(Can't be too long -- max 20 chars)");
        StdDraw.text(width / 2, height / 2 - 2 * offset, "Press Space to finish");
        StdDraw.show();

        int cap = 20;
        int count = 0;
        String input = "";
        while (count != cap) {
            if (StdDraw.hasNextKeyTyped()) {
                char next = StdDraw.nextKeyTyped();
                if (next == '#') {
                    //initialize();
                    return input;
                }
                count += 1;
                input = input + next;
                drawName(input);
            }
        }
        //initialize();
        return input;
    }
}
