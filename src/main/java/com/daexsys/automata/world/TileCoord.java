package com.daexsys.automata.world;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.tiletypes.TileType;
import com.google.common.base.Optional;

/**
 * TileCoord represents a location in a world where a tile can be placed.
 */
public final class TileCoord {

    public final World world;
    public final int layer;
    public final int x;
    public final int y;

    public TileCoord(int layer, World world, int x, int y) {
        this.layer = layer;
        this.world = world;
        this.x = x;
        this.y = y;
    }

    /**
     * Might sometimes be faster than constructing a whole new one.
     */
    public static TileCoord of(int layer, World world, int x, int y) {
        return world.getTileAt(layer, x, y).getCoordinate();
    }

    public TileCoord add(int x, int y) {
        return new TileCoord(layer, this.world, this.x + x, this.y + y);
    }

    public TileCoord sub(int x, int y) {
        return new TileCoord(layer, this.world, this.x - x, this.y - y);
    }

    public Chunk getChunk() {
        return ChunkCoord.forWorldCoords(this).getChunk();
    }

    public Optional<Tile> getTile() {
        Chunk chunk = getChunk();

        if(chunk != null) {
            return Optional.of(
                    chunk.getTile(
                            layer,
                            chunk.getChunkCoordinate().localifyX(x),
                            chunk.getChunkCoordinate().localifyY(y)
                    )
            );
        }

        return Optional.absent();
    }

    public void setTileType(TileType tileType) {
        world.setTileTypeAt(layer, x, y, tileType);
    }

    public void queueChange(TileType tileType) {
        world.queueChangeAt(this, tileType);
    }

    public void queueChange(TileType tileType, int newEnergy) {
        world.queueChangeAt(this, tileType, newEnergy);
    }

    @Override
    public String toString() {
        return "{tilecoord: x=" + x + ", y=" + y + "}";
    }
}
