package com.daexsys.automata.world;

import com.daexsys.automata.Tile;

import java.awt.image.BufferedImage;

public class ViralTileType extends TileType {

    public ViralTileType(byte id, String blockName, BufferedImage image, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        if(this == TileTypes.VIRUS && tile.getEnergy() > 0) {

            int i = tile.getWorld().getRandom().nextInt(3) - 1;
            int j = tile.getWorld().getRandom().nextInt(3) - 1;

            try {//
                if (tile.getWorld().getTileAt(tile.getCoordinate().x + i, tile.getCoordinate().y + j).getTileType() != TileTypes.VIRUS && tile.getEnergy() > 0) {
                    tile.getWorld().queueChangeAt(tile.getCoordinate().x + i, tile.getCoordinate().y + j, TileTypes.VIRUS, tile.getEnergy() / 2);
                    tile.setEnergy(tile.getEnergy() / 2);
                }
            } catch (Exception ignore) {}
        }
    }
}
