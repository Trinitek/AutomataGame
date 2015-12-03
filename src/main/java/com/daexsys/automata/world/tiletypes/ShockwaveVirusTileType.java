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
                TileCoordinate shiftedCoord = tile.getCoordinate().add(x, y);

                try {
                    if (shiftedCoord.getTile().getType() !=
                            TileType.SHOCKWAVE_VIRUS) {

                        tile.getWorld().queueChangeAt(
                                shiftedCoord.x,
                                shiftedCoord.y,
                                TileType.SHOCKWAVE_VIRUS,
                                tile.getEnergy() / 2
                        );
                    }

                } catch (NullPointerException ignore){}
            }
        }
    }
}
