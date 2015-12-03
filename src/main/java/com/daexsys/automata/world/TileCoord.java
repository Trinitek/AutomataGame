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

    private Chunk chunk;

    public TileCoord(int layer, World world, int x, int y) {
        this.layer = layer;
        this.world = world;
        this.x = x;
        this.y = y;
    }

    private TileCoord(int layer, World world, int x, int y, Chunk chunk) {
        this.layer = layer;
        this.world = world;
        this.x = x;
        this.y = y;
        this.chunk = chunk;
    }

    /**
     * Might sometimes be faster than constructing a whole new one.
     */
    public static TileCoord of(int layer, World world, int x, int y) {
        return world.getTileAt(layer, x, y).getCoordinate();
    }

    public TileCoord add(int x, int y) {
        if(chunk != null) {
            if (x > 0 && y > 0) {
                int tcx = this.x / Chunk.DEFAULT_CHUNK_SIZE;
                int tcy = this.y / Chunk.DEFAULT_CHUNK_SIZE;

                int ncx = (this.x + x) / Chunk.DEFAULT_CHUNK_SIZE;
                int ncy = (this.y + x) / Chunk.DEFAULT_CHUNK_SIZE;

                if (tcx == ncx && tcy == ncy) {
                    return new TileCoord(layer, this.world, this.x + x, this.y + y, chunk);
                }
            }
        }

        return new TileCoord(layer, this.world, this.x + x, this.y + y);
    }

    public TileCoord sub(int x, int y) {
        return new TileCoord(layer, this.world, this.x - x, this.y - y);
    }

    public Chunk getChunk() {
        if(chunk == null) {
            chunk = ChunkCoordinate.forWorldCoords(this).getChunk();
        }

        return chunk;
    }

    public Optional<Tile> getTile() {
        if(chunk != null) {
            getChunk().getTile(layer, x / Chunk.DEFAULT_CHUNK_SIZE, y / Chunk.DEFAULT_CHUNK_SIZE);
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
        return "{x=" + x + ", y=" + y + "}";
    }
}
