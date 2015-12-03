package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.*;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;

public class MinerTilePulser implements TilePulser {

    @Override
    public void pulse(Tile tile) {
        byte direction = tile.getTileData()[0];
        TileCoord target;

        if(direction == 0) {
            target = tile.getCoordinate().add(1, 0);
        } else {
            target = tile.getCoordinate().sub(1, 0);
        }

        if(tile.getWorld().isObstructionAt(target.x, target.y)) {
            final int breakRate = 3;

            Tile tileToWeaken = tile.getWorld().getTileAt(WorldLayer.ABOVE_GROUND, target.x, target.y);
            tileToWeaken.setEnergy(tileToWeaken.getEnergy() - breakRate);

            System.out.println(tileToWeaken.getEnergy());
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
