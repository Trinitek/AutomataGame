package com.daexsys.automata.event.tile;

import com.daexsys.automata.Tile;
import com.daexsys.automata.event.Event;

public class TileAlterEvent implements Event {

    private Tile tile;
    private TilePlacementReason tileAlterCause;

    public TileAlterEvent(Tile tile, TilePlacementReason tileAlterCause) {
        this.tile = tile;
        this.tileAlterCause = tileAlterCause;
    }

    public Tile getTile() {
        return tile;
    }

    public TilePlacementReason getTileAlterCause() {
        return tileAlterCause;
    }
}
