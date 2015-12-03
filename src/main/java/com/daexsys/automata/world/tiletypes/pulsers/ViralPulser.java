package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoord;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;

public class ViralPulser implements TilePulser {

    @Override
    public void pulse(Tile tile) {
        if(tile.getType() == TileType.GREEDY_VIRUS && tile.getEnergy() > 0) {

            int offsetX = tile.getWorld().getRandom().nextInt(3) - 1;
            int offsetY = tile.getWorld().getRandom().nextInt(3) - 1;

            try {//
                if (tile.getWorld().getTileAt(
                        WorldLayer.GROUND,
                        tile.getCoordinate().x + offsetX,
                        tile.getCoordinate().y + offsetY)
                            .getType() != TileType.GREEDY_VIRUS
                            && tile.getEnergy() > 0
                        ) {

                    TileCoord newCoordinate = tile.getCoordinate().add(offsetX, offsetY);

                    // Create a new virus and split the energy of this one amonst the two.
                    tile.getWorld().queueChangeAt(
                            newCoordinate,
                            TileType.GREEDY_VIRUS,
                            tile.getEnergy() / 2
                    );
                    tile.setEnergy(tile.getEnergy() / 2);
                }
            } catch (Exception ignore) {}
        }
    }
}
