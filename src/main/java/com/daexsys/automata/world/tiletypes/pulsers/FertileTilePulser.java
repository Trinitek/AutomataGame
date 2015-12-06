package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;

public class FertileTilePulser implements TilePulser {

    @Override
    public void pulse(Tile tile) {

        if (tile.getEnergy() > 1) {
            tile.getTileVM().step();
        }

        if(tile.getType() == TileType.DIRT && tile.getEnergy() > 500) {
            tile.getCoordinate().queueChange(TileType.GRASS);
        }

        else if(tile.getType() == TileType.GRASS && tile.getEnergy() > 1000) {
            tile.getCoordinate().queueChange(TileType.TALL_GRASS);
        }
    }
}
