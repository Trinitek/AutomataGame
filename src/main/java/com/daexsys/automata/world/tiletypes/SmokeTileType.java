package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;

import java.awt.image.BufferedImage;

public class SmokeTileType extends TileType {

    public SmokeTileType(
            byte id,
            String blockName,
            BufferedImage image,
            int defaultEnergy,
            int defaultDecayRate
    ) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void destruct(Tile tile) {
        tile.getCoordinate().queueChange(TileType.AIR);
    }
}
