package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoordinate;
import com.daexsys.automata.world.WorldLayers;

import java.awt.image.BufferedImage;

public class ViralTileType extends TileType {

    public ViralTileType(byte id, String blockName, BufferedImage image, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        if(this == TileTypes.VIRUS && tile.getEnergy() > 0) {

            int offsetX = tile.getWorld().getRandom().nextInt(3) - 1;
            int offsetY = tile.getWorld().getRandom().nextInt(3) - 1;

            try {//
                if (tile.getWorld().getTileAt(
                        WorldLayers.GROUND,
                        tile.getCoordinate().x + offsetX,
                        tile.getCoordinate().y + offsetY)
                            .getTileType() != TileTypes.VIRUS
                            && tile.getEnergy() > 0
                        ) {

                    TileCoordinate newCoordinate = tile.getCoordinate().add(offsetX, offsetY);

                    // Create a new virus and split the energy of this one amonst the two.
                    tile.getWorld().queueChangeAt(
                            newCoordinate.x,
                            newCoordinate.y,
                            TileTypes.VIRUS,
                            tile.getEnergy() / 2
                    );
                    tile.setEnergy(tile.getEnergy() / 2);
                }
            } catch (Exception ignore) {}
        }
    }
}
