package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;

import java.awt.image.BufferedImage;

public class SmokeTileType extends TileType {

    public SmokeTileType(
            byte id,
            String blockName,
            String imageUrl,
            int defaultEnergy,
            int defaultDecayRate
    ) {
        super(id, blockName, imageUrl, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void destruct(Tile tile) {
        tile.getCoordinate().queueChange(TileType.AIR);
    }
}
