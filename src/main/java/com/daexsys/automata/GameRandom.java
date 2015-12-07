package com.daexsys.automata;

import java.util.Random;

public class GameRandom extends Random {

    public GameRandom(long seed) {
        super(seed);
    }

    public int getIntInRange(int range) {
        return nextInt(range * 2 + 1) - range;
    }

    public double getEntropyFor(Tile tile) {
        return 0;
    }

    public static void main(String[] args) {
        GameRandom rangedRandom = new GameRandom(3);

        while(true) {
            System.out.println(rangedRandom.getIntInRange(3));
        }
    }
}
