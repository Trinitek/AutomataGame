package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoordinate;

import java.awt.image.BufferedImage;

public class ShockwaveVirusTileType extends TileType {

    public ShockwaveVirusTileType(byte id, String blockName, BufferedImage image, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                TileCoordinate newCoordinate = tile.getCoordinate().add(x, y);

                tile.getWorld().queueChangeAt(
                    newCoordinate.x,
                    newCoordinate.y,
                    TileTypes.SHOCKWAVE_VIRUS,
                    tile.getEnergy() / 2
                );
            }
        }
    }
}
