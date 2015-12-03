package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;

import java.awt.image.BufferedImage;

public class FertileTileType extends TileType {

    public FertileTileType(byte id, String blockName, String image, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        /* Get energy from the sun to grow */
        tile.setEnergy(tile.getEnergy() + 2);

        if(this == TileType.DIRT && tile.getEnergy() > 500) {
            tile.getCoordinate().queueChange(TileType.GRASS);
        }

        else if(this == TileType.GRASS && tile.getEnergy() > 1000) {
            tile.getCoordinate().queueChange(TileType.TALL_GRASS);
        }
    }
}
