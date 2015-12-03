package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.WorldLayer;

import java.awt.image.BufferedImage;

public class SmokeTileType extends TileType {

    public SmokeTileType(byte id, String blockName, BufferedImage image, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void destruct(Tile tile) {
        super.destruct(tile);

        tile.getWorld().queueChangeAt(
                WorldLayer.ABOVE_GROUND,
                tile.getCoordinate().x, tile.getCoordinate().y,
                TileType.AIR
        );
    }
}
