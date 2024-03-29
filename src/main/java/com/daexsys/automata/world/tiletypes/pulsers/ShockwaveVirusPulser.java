package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoord;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;
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
public class ShockwaveVirusPulser implements TilePulser {

    @Override
    public void pulse(Tile tile) {
        // Speed of light of the shockwave virus
        /*int c = 1;

        for (int x = -c; x <= c; x++) {
            for (int y = -c; y <= c; y++) {
                TileCoord shiftedCoord = tile.getCoordinate().add(x, y);
                Tile residentTile = tile.getWorld().getTileAt(shiftedCoord.layer, shiftedCoord.x, shiftedCoord.y);

                if (residentTile != null) {
                    if (residentTile.getType() != TileType.SHOCKWAVE_VIRUS){
                        shiftedCoord.queueChange(TileType.SHOCKWAVE_VIRUS, tile.getEnergy() / 2);
                    }
                }
            }
        }*/
        tile.getTileVM().step();
    }
}
