package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.*;

import java.awt.image.BufferedImage;

public class MinerTileType extends TileType {

    public MinerTileType(byte id, String blockName, String imageUrl, String programUrl, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, imageUrl, programUrl, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        super.pulse(tile);

        byte direction = tile.getTileData()[0];
        TileCoord target;

        if(direction == 0) {
            target = tile.getCoordinate().add(1, 0);
        } else {
            target = tile.getCoordinate().sub(1, 0);
        }

        if(tile.getWorld().isObstructionAt(target.x, target.y)) {
            final int breakRate = 3;

            Tile tileToWeaken = null;
            tileToWeaken = tile.getWorld().getTileAt(WorldLayer.ABOVE_GROUND, target.x, target.y);
            tileToWeaken.setEnergy(tileToWeaken.getEnergy() - breakRate);

            if(tileToWeaken.getEnergy() <= 0) {
                tile.getWorld().setTileTypeAt(WorldLayer.GROUND, tile.getCoordinate(), TileType.DIRT);
                tileToWeaken.getWorld().setTileTypeAt(WorldLayer.ABOVE_GROUND, target, TileType.AIR);
                tile.getWorld().setTileTypeAt(WorldLayer.GROUND, tile.getCoordinate(), TileType.MINER);
                Tile newOne = tile.getWorld().getTileAt(WorldLayer.GROUND, tile.getCoordinate().x, tile.getCoordinate().y);
                try {
                    newOne.getTileData()[0] = direction;
                } catch (Exception ignore) {} // for now
            }
        } else {

            tile.getWorld().setTileTypeAt(WorldLayer.GROUND, tile.getCoordinate().x, tile.getCoordinate().y, TileType.DIRT);
            tile.getWorld().setTileTypeAt(WorldLayer.GROUND, target.x, target.y, TileType.MINER);
        }
    }
}
