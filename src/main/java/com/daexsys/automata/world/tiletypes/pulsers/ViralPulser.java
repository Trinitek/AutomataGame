package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoord;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;
import com.google.common.base.Optional;

/**
 * A tile type that quickly spreads, destroys, then withers away.
 */
public class ViralPulser implements TilePulser {

    @Override
    public void pulse(Tile tile) {
        if(tile.getType() == TileType.GREEDY_VIRUS && tile.getEnergy() > 0) {
            int offsetX = tile.getWorld().getRandom().getIntInRange(1);
            int offsetY = tile.getWorld().getRandom().getIntInRange(1);

            TileCoord newCoordinate = tile.getCoordinate().add(offsetX, offsetY);
            Optional<Tile> tileOptional = newCoordinate.getTile();

            if(tileOptional.isPresent()) {
                Tile theTile = tileOptional.get();

                // If tile is not already a greedy virus
                if (theTile.getType() != TileType.GREEDY_VIRUS) {
                    // Create a new virus and split the energy of this one among the two.
                    newCoordinate.queueChange(TileType.GREEDY_VIRUS, tile.getEnergy() / 2);
                    tile.setEnergy(tile.getEnergy() / 2);
                }
            }
        }
    }
}
