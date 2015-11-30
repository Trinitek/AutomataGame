package com.daexsys.automata.world;

/**
 * Represents the location of a chunk.
 *
 * Very close to TileCoordinate in structure, but separate for two reasons:
 * 1. they have very different meanings
 * 2. they will likely diverge in the future
 */
public final class ChunkCoordinate {

    public final World world;
    public final int x;
    public final int y;

    private ChunkCoordinate(World world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
    }

    public boolean is(int x, int y) {
        return this.x == x && this.y == y;
    }

    public static ChunkCoordinate of(World world, int x, int y) {
        return new ChunkCoordinate(world, x, y);
    }

    @Override
    public String toString() {
        return "{x=" + x + ", y=" + y + "}";
    }
}
