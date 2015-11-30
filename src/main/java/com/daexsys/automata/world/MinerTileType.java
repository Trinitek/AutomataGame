package com.daexsys.automata.world;

import com.daexsys.automata.Tile;

import java.awt.image.BufferedImage;
import java.util.Random;

public class MinerTileType extends TileType {

    public MinerTileType(byte id, String blockName, BufferedImage image, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        byte direction = tile.getTileData()[0];
        TileCoordinate target;

        if(direction == 0) {
            target = new TileCoordinate(
                    tile.getCoordinate().worldModel, tile.getCoordinate().x + 1, tile.getCoordinate().y);
        } else {
            target = new TileCoordinate(
                    tile.getCoordinate().worldModel, tile.getCoordinate().x - 1, tile.getCoordinate().y);
        }

        if(tile.getWorld().isObstructionAt(target.x, target.y)) {
            final int breakRate = 3;

            Tile tileToWeaken = null;
            try {
                tileToWeaken = tile.getWorld().getTileAt(Layer.ABOVE_GROUND, target.x, target.y);
                tileToWeaken.setEnergy(tileToWeaken.getEnergy() - breakRate);

                if(tileToWeaken.getEnergy() <= 0) {
                    tile.getWorld().setTileTypeAt(tile.getCoordinate().x, tile.getCoordinate().y, TileTypes.DIRT);
                    tileToWeaken.getWorld().setTileTypeAt(Layer.ABOVE_GROUND, target.x, target.y, TileTypes.AIR);
                    tile.getWorld().setTileTypeAt(tile.getCoordinate().x, tile.getCoordinate().y, TileTypes.MINER);
                    Tile newOne = tile.getWorld().getTileAt(Layer.GROUND, tile.getCoordinate().x, tile.getCoordinate().y);
                    try {
                        newOne.getTileData()[0] = direction;
                    } catch (Exception ignore) {} // for now
                }
            } catch (AccessOutOfWorldException e) {
                e.printStackTrace();
            }
        } else {

            tile.getWorld().setTileTypeAt(tile.getCoordinate().x, tile.getCoordinate().y, TileTypes.DIRT);
            tile.getWorld().setTileTypeAt(target.x, target.y, TileTypes.MINER);
        }
    }
}
