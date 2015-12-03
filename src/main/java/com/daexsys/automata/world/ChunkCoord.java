package com.daexsys.automata.world;

/**
 * Represents the location of a chunk.
 *
 * Very close to TileCoordinate in structure, but separate for two reasons:
 * 1. they have very different meanings
 * 2. they will likely diverge in the future
 */
public final class ChunkCoord {

    public static ChunkCoord of(World world, int x, int y) {
        return new ChunkCoord(world, x, y);
    }

    public static ChunkCoord forWorldCoords(TileCoord tileCoord) {
        return forWorldCoords(tileCoord.world, tileCoord.x, tileCoord.y);
    }

    public static ChunkCoord forWorldCoords(World world, int x, int y) {
        int rx = x / Chunk.DEFAULT_CHUNK_SIZE;
        int ry = y / Chunk.DEFAULT_CHUNK_SIZE;

        return new ChunkCoord(world, rx, ry);
    }

    public final World world;
    public final int x;
    public final int y;

    private ChunkCoord(World world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
    }

    public Chunk getChunk() {
        return world.getChunkManager().getChunk(this);
    }

    public int localifyX(int x) {
        return x - (this.x * Chunk.DEFAULT_CHUNK_SIZE);
    }

    public int localifyY(int y) {
        return y - (this.y * Chunk.DEFAULT_CHUNK_SIZE);
    }

    public int amplifyLocalX(int x) {
        return x + (this.x * Chunk.DEFAULT_CHUNK_SIZE);
    }

    public int amplifyLocalY(int y) {
        return y + (this.y * Chunk.DEFAULT_CHUNK_SIZE);
    }

    public TileCoord localifyCoordinates(int x, int y) {
        return TileCoord.of(WorldLayer.GROUND, world,
                localifyX(x),
                localifyY(y));
    }

    public ChunkCoord add(int x, int y) {
        return new ChunkCoord(world, this.x + x, this.y + y);
    }

    public boolean is(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ChunkCoord) {
            ChunkCoord chunkObject = (ChunkCoord) o;
            return chunkObject.world == world
                    && chunkObject.x == x
                    && chunkObject.y == y;
        }

        return false;
    }

    public boolean chunkExists() {
        return world.getChunkManager().getChunk(x, y) != null;
    }

    @Override
    public int hashCode() {
        return x * Short.MAX_VALUE + y * Byte.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "{world=" + world + ", x=" + x + ", y=" + y + "}";
    }
}
