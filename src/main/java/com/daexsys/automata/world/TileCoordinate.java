package com.daexsys.automata.world;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.tiletypes.TileType;

/**
 * TileCoordinate represents a location in a world where a tile can be placed.
 * No information regarding the layer is given, and must be provided seperately.
 */
public final class TileCoordinate {

    public final World world;
    public final int x;
    public final int y;
    public final int layer;

    public TileCoordinate(int layer, World world, int x, int y) {
        this.layer = layer;
        this.world = world;
        this.x = x;
        this.y = y;
    }

    public TileCoordinate add(int x, int y) {
        return new TileCoordinate(layer, this.world, this.x + x, this.y + y);
    }

    public Tile getTile() {
        if(x > 0 && y > 0) {
            return world.getTileAt(layer, x, y);
        }

        return null;
    }

    public void setTileTypeAtLocation(int layer, TileType tile) {
        world.setTileTypeAt(layer, x, y, tile);
    }

    @Override
    public String toString() {
        return "{x=" + x + ", y=" + y + "}";
    }
}
