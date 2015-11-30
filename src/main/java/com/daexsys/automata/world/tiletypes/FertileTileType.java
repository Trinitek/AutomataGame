package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;

import java.awt.image.BufferedImage;

public class FertileTileType extends TileType {

    public FertileTileType(byte id, String blockName, BufferedImage image, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        /* Get energy from the sun to grow */
        tile.setEnergy(tile.getEnergy() + 2);

        if(this == TileTypes.DIRT && tile.getEnergy() > 500) {
            tile.getWorld().queueChangeAt(tile.getCoordinate().x, tile.getCoordinate().y, TileTypes.GRASS);
        }

        if(this == TileTypes.GRASS && tile.getEnergy() > 1000) {
            tile.getWorld().queueChangeAt(tile.getCoordinate().x, tile.getCoordinate().y, TileTypes.TALL_GRASS);
        }
    }
}
