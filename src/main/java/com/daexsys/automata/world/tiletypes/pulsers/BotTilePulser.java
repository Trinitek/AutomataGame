package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.gui.listeners.KeyboardHandler;
import com.daexsys.automata.world.TileCoord;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;

import java.awt.event.KeyEvent;

public class BotTilePulser implements TilePulser {

    @Override
    public void pulse(Tile tile) {
        boolean moved = false;

        int cuttingStrength = 10;

        if(KeyboardHandler.isDown(KeyEvent.VK_I)) {
            TileCoord focus = tile.getCoordinate().add(0, -1);

            Tile fT = tile.getWorld().getTileAt(focus.layer, focus.x, focus.y);

            if(fT != null) {
                if (fT.getType() == TileType.AIR) {
                    focus.queueChange(tile.getType());
                    moved = true;
                } else {
                    fT.setEnergy(fT.getEnergy() - cuttingStrength);
                    if(fT.getEnergy() <= 0) {
                        fT.getCoordinate().setTileType(TileType.AIR);
                    }
                }
            }
        }

        else if(KeyboardHandler.isDown(KeyEvent.VK_K)) {
            TileCoord focus = tile.getCoordinate().add(0, 1);

            Tile fT = tile.getWorld().getTileAt(focus.layer, focus.x, focus.y);

            if(fT != null) {
                if (fT.getType() == TileType.AIR) {
                    focus.queueChange(tile.getType());
                    moved = true;
                } else {
                    fT.setEnergy(fT.getEnergy() - cuttingStrength);
                    if(fT.getEnergy() <= 0) {
                        fT.getCoordinate().setTileType(TileType.AIR);
                    }
                }
            }
        }

        else if(KeyboardHandler.isDown(KeyEvent.VK_J)) {
            TileCoord focus = tile.getCoordinate().add(-1, 0);

            Tile fT = tile.getWorld().getTileAt(focus.layer, focus.x, focus.y);

            if(fT != null) {
                if (fT.getType() == TileType.AIR) {
                    focus.queueChange(tile.getType());
                    moved = true;
                } else {
                    fT.setEnergy(fT.getEnergy() - cuttingStrength);
                    if(fT.getEnergy() <= 0) {
                        fT.getCoordinate().setTileType(TileType.AIR);
                    }
                }
            }
        }

        else if(KeyboardHandler.isDown(KeyEvent.VK_L)) {
            TileCoord focus = tile.getCoordinate().add(1, 0);

            Tile fT = tile.getWorld().getTileAt(focus.layer, focus.x, focus.y);

            if(fT != null) {
                if (fT.getType() == TileType.AIR) {
                    focus.queueChange(tile.getType());
                    moved = true;
                } else {
                    fT.setEnergy(fT.getEnergy() - cuttingStrength);
                    if(fT.getEnergy() <= 0) {
                        fT.getCoordinate().setTileType(TileType.AIR);
                    }
                }
            }
        }

        if(moved) {
            tile.getCoordinate().setTileType(TileType.AIR);
            new TileCoord(WorldLayer.GROUND, tile.getWorld(), tile.getCoordinate().x, tile.getCoordinate().y).setTileType(TileType.DIRT);
        }
    }
}
