package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;

public interface TilePulser {

    void pulse(Tile t);

    default void destruct(Tile tile) {
        tile.getCoordinate().queueChange(TileType.DIRT);
    }
}
