package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class HUD {
    public static void returnTile(TETile[][] world, int width, int height, TERenderer ter) {
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
        Font newFont = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(newFont);
        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.text(6, height - 1, description);

        Font font = new Font("Monaco", Font.BOLD, 16 - 2);
        StdDraw.setFont(font);
    }
}
