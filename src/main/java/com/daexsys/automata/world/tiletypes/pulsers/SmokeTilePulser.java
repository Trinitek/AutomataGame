package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;

public class SmokeTilePulser implements TilePulser {

    public void pulse(Tile t) {

    }

    public void destruct(Tile tile) {
        tile.getCoordinate().queueChange(TileType.AIR);
    }
}
