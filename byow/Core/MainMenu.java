package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class MainMenu {
    private int width;
    private int height;

    public MainMenu(int w, int h) {
        width = w;
        height = h;
    }

    /**
     *
     * @source TERenderer
     */
    public void initialize() {
        StdDraw.setCanvasSize(width, height);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();

        StdDraw.text(width / 2, height - 60, "Bruh Moment: The Game");
        StdDraw.text(width / 2, height / 2, "Load Game (L)");
        StdDraw.text(width / 2, height / 2 + 30, "New Game (N)");
        StdDraw.text(width / 2, height / 2 - 30, "Quit (Q)");

        StdDraw.show();
    }

    public void select(char selection) {
        StdDraw.setPenColor(new Color(255, 0, 255));
        if (selection == 'N' || selection == 'n') {
            StdDraw.text(width / 2, height / 2 + 30, "New Game (N)");
        } else if (selection == 'L' || selection == 'l') {
            StdDraw.text(width / 2, height / 2, "Load Game (L)");
        }
        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.show();
    }

    public void showInput(String input) {
        StdDraw.text(width / 2, height / 2 + 80, input);
        StdDraw.show();
    }
}
