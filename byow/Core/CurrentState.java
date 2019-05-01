package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

public class CurrentState implements Serializable {
    private TETile[][] world;
    private Random random;
    private long seed;
    private Avatar avatar;
    private String name;
    private Police police;

    public CurrentState(TETile[][] curr, Random r, long s, Avatar a, String n, Police p) {
        world = curr;
        random = r;
        seed = s;
        avatar = a;
        name = n;
        police = p;
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

    public String name() {return name;}

    public Police police() {return police;}
}
