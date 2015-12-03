package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoord;
import com.google.common.base.Optional;

import java.awt.image.BufferedImage;

/**
 * Virus tile type that spreads out, consuming everything indefinitely.
 * While it has an 'energy' value, it doesn't really respect it and never stops.
 *
 * After a few ticks of expansion, it develops a pulsing pattern. It is unknown why
 * this is but it likely has something to do with the tile.getEnergy() / 2 operation
 * and the initial energy level of the tile.
 */
public class ShockwaveVirusTileType extends TileType {

    public ShockwaveVirusTileType(
            byte id,
            String blockName,
            String imageUrl,
            String programUrl,
            int defaultEnergy,
            int defaultDecayRate
    ) {
        super(id, blockName, imageUrl, programUrl, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        // Speed of light of the shockwave virus
        int c = 1;

        for (int x = -c; x <= c; x++) {
            for (int y = -c; y <= c; y++) {
                TileCoord shiftedCoord = tile.getCoordinate().add(x, y);
                Optional<Tile> residentTile = shiftedCoord.getTile();

                if (residentTile.isPresent()) {
                    if (residentTile.get().getType() != TileType.SHOCKWAVE_VIRUS){
                        shiftedCoord.queueChange(TileType.SHOCKWAVE_VIRUS, tile.getEnergy() / 2);
                    }
                }
            }
        }
    }
}
