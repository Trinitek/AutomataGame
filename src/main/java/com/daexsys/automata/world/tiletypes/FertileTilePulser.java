package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;

public class FertileTilePulser implements TilePulser {

    @Override
    public void pulse(Tile tile) {
        /* Get energy from the sun to grow */
        tile.setEnergy(tile.getEnergy() + 2);

        if(tile.getType() == TileType.DIRT && tile.getEnergy() > 500) {
            tile.getCoordinate().queueChange(TileType.GRASS);
        }

        else if(tile.getType() == TileType.GRASS && tile.getEnergy() > 1000) {
            tile.getCoordinate().queueChange(TileType.TALL_GRASS);
        }
    }
}
