package com.daexsys.automata.event.tile;

import com.daexsys.automata.Tile;
import com.daexsys.automata.event.Event;

public class TileAlterEvent implements Event {

    private Tile tile;
    private TileAlterCause tileAlterCause;

    public TileAlterEvent(Tile tile, TileAlterCause tileAlterCause) {
        this.tile = tile;
        this.tileAlterCause = tileAlterCause;
    }

    public Tile getTile() {
        return tile;
    }

    public TileAlterCause getTileAlterCause() {
        return tileAlterCause;
    }
}
