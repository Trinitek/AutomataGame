package com.daexsys.automata.world;

import com.daexsys.automata.Tile;

/**
 * A chunk is a square sub-unit of the world.
 */
public final class Chunk {

    public static final int DEFAULT_CHUNK_SIZE = 16;

    private int x;
    private int y;
    private Tile[][][] contents;

    public Chunk(int x, int y) {
        this.x = x;
        this.y = y;

        contents = new Tile[2][DEFAULT_CHUNK_SIZE][DEFAULT_CHUNK_SIZE];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Tile[][][] getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
