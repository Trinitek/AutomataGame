package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoord;
import com.daexsys.automata.world.WorldLayer;

import java.awt.image.BufferedImage;

public class ViralTileType extends TileType {

    public ViralTileType(byte id, String blockName, String imageUrl, String programUrl, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, imageUrl, programUrl, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        if(this == TileType.GREEDY_VIRUS && tile.getEnergy() > 0) {

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
