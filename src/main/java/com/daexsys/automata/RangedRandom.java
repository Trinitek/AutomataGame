package com.daexsys.automata;

import java.util.Random;

public class RangedRandom extends Random {

    public RangedRandom(long seed) {
        super(seed);
    }

    public int getIntInRange(int range) {
        return nextInt(range * 2 + 1) - range;
    }

    public static void main(String[] args) {
        RangedRandom rangedRandom = new RangedRandom(3);

        while(true) {
            System.out.println(rangedRandom.getIntInRange(3));
        }
    }
}
