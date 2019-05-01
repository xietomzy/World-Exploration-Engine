package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class HUD {
    public static String returnTile(TETile[][] world, int width, int height, String str) {
        //StdDraw.clear();
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x == width) {
            x = width - 1;
        }
        if (y == height) {
            y = height - 1;
        }
        //StdDraw.mouseEntered();
        String description = world[x][y].description();

        if (!(str == description)) {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.filledRectangle(6, height - 1, 6, 1);
            StdDraw.show();

            Font newFont = new Font("Monaco", Font.BOLD, 16);
            StdDraw.setFont(newFont);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(6, height - 1, description);
            StdDraw.show();

            Font font = new Font("Monaco", Font.BOLD, 16 - 2);
            StdDraw.setFont(font);
            //ter.renderFrame(world);
        }

        return description;
    }

    public static void showName(int width, int height, String name) {
        if (name == "" || name == null) {
            return;
        }
        Font newFont = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(newFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2, height - 1, name + " the Thief");
        StdDraw.show();

        Font font = new Font("Monaco", Font.BOLD, 16 - 2);
        StdDraw.setFont(font);
    }
}
