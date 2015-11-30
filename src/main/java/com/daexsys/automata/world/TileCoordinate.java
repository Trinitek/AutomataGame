package com.daexsys.automata.world;

/**
 * TileCoordinate represents a location in a world where a tile can be placed.
 * No information regarding the layer is given, and must be provided seperately.
 */
public final class TileCoordinate {

    public final World world;
    public final int x;
    public final int y;

    public TileCoordinate(World world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{x=" + x + ", y=" + y + "}";
    }
}
