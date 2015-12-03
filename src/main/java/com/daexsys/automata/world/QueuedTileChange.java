package com.daexsys.automata.world;

import com.daexsys.automata.Tile;

public class QueuedTileChange {

    public final int layer;
    public final int x;
    public final int y;
    public final Tile t;

    public QueuedTileChange(int layer, int x, int y, Tile t) {
        this.layer = layer;
        this.x = x;
        this.y = y;
        this.t = t;
    }

    @Override
    public String toString() {
        return "QueuedTileChange{" +
                "x=" + x +
                ", y=" + y +
                ", t=" + t +
                '}';
    }
}
