package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

public class CurrentState implements Serializable {
    private TETile[][] world;
    private Random random;
    private long seed;
    private Avatar avatar;

    public CurrentState(TETile[][] curr, Random r, long s, Avatar a) {
        world = curr;
        random = r;
        seed = s;
        avatar = a;
    }

    public void setCurrentState(TETile [][] currWorld) {
        world = currWorld;
    }

    public TETile[][] world() {
        return world;
    }

    public Random random() {
        return random;
    }

    public long seed() {
        return seed;
    }

    public Avatar avatar() {
        return avatar;
    }
}
