package com.daexsys.automata.world;

import com.daexsys.automata.Tile;

public class QueuedTileChange {

    public final int x;
    public final int y;
    public final Tile t;

    public QueuedTileChange(int x, int y, com.daexsys.automata.Tile t) {
        this.x = x;
        this.y = y;
        this.t = t;
    }
}
