package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoordinate;
import com.daexsys.automata.world.WorldLayers;

import java.awt.image.BufferedImage;

public class WaterTileType extends TileType {

    public WaterTileType(byte id, String blockName, BufferedImage image, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        if(this == TileType.WATER && tile.getEnergy() > 0) {

            int offsetX = tile.getWorld().getRandom().nextInt(3) - 1;
            int offsetY = tile.getWorld().getRandom().nextInt(3) - 1;

            try {//
                if (tile.getWorld().getTileAt(
                        WorldLayers.ABOVE_GROUND,
                        tile.getCoordinate().x + offsetX,
                        tile.getCoordinate().y + offsetY)
                        .getType() == TileType.AIR
                        && tile.getEnergy() > 0
                        ) {

                    TileCoordinate newCoordinate = tile.getCoordinate().add(offsetX, offsetY);

                    // Create a new water and split the energy of this one amongst the two.
                    tile.getWorld().queueChangeAt(
                            newCoordinate.x,
                            newCoordinate.y,
                            TileType.WATER,
                            tile.getEnergy()
                    );

                    tile.setEnergy(tile.getEnergy() / 2);
                }
            } catch (Exception ignore) {}
        }
    }
}
