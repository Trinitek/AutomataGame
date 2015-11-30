package com.daexsys.automata.world;

public class TileCoordinate {

    public final World worldModel;
    public final int x;
    public final int y;

    public TileCoordinate(World worldModel, int x, int y) {
        this.worldModel = worldModel;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y;
    }
}
