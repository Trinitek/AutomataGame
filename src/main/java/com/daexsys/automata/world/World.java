package com.daexsys.automata.world;

import com.daexsys.automata.Pulsable;
import com.daexsys.automata.Tile;
import com.daexsys.automata.world.terrain.DesertTerrain;
import com.daexsys.automata.world.terrain.TemperateTerrain;
import com.daexsys.automata.world.terrain.TerrainGenerator;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.Random;

public final class World implements Pulsable {

    private long seed;
    private Random random;

    private int ticksPulsed = 0;

    private ChunkManager chunkManager;
    private TerrainGenerator terrainGenerator;

    public World() {
        seed = System.currentTimeMillis();
        random = new Random(seed);

        terrainGenerator = random.nextBoolean() ? new TemperateTerrain(this) : new DesertTerrain(this);

        chunkManager = new ChunkManager(this);

        // pre-generate world sections
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                chunkManager.getChunk(i, j);
            }
        }

//        /* Add lakes */
//        for (int i = 0; i < size / 20; i++) {
//            int lakeX = random.nextInt(size);
//            int lakeY = random.nextInt(size);
//            int lakeSize = random.nextInt(15) + 3;
//
//            for (int j = lakeX - lakeSize; j < lakeX + lakeSize; j++) {
//                for (int k = lakeY - lakeSize; k < lakeY + lakeSize; k++) {
//                    int x = j - lakeX;
//                    int y = k - lakeY;
//
//                    if((x * x + y * y) < lakeSize * lakeSize) {
//                        setTileTypeAt(WorldLayers.GROUND, j, k, TileTypes.WATER);
//                    }
//                }
//            }
//        }
//
//        for (int i = 0; i < size / 5; i++) {
//            int lakeX = random.nextInt(size);
//            int lakeY = random.nextInt(size);
//            int lakeSize = random.nextInt(15) + 3;
//
//            for (int j = lakeX - lakeSize; j < lakeX + lakeSize; j++) {
//                for (int k = lakeY - lakeSize; k < lakeY + lakeSize; k++) {
//                    int x = j - lakeX;
//                    int y = k - lakeY;
//
//                    if((x * x + y * y) < lakeSize * lakeSize) {
//                        if(random.nextBoolean()) {
//                            setTileTypeAt(WorldLayers.ABOVE_GROUND, j, k, TileTypes.STONE);
//                        } else {
//                            setTileTypeAt(WorldLayers.ABOVE_GROUND, j, k, TileTypes.ENERGY_ORE);
//                        }
//                    }
//                }
//            }
//        }
    }

    public Tile getTileAt(int layer, int x, int y) {
        Chunk chunk = getChunkManager().getChunk(
                x / Chunk.DEFAULT_CHUNK_SIZE, y / Chunk.DEFAULT_CHUNK_SIZE);

        ChunkCoordinate chunkCoordinate = chunk.getChunkCoordinate();

        return chunk.getTile(layer, chunkCoordinate.localifyX(x), chunkCoordinate.localifyY(y));
    }

    /*
        Yes, this method is optimization hell. But the huge TPS boost is worth it from
        not instantiating 1-2 object per tile per tick for the entire map!
     */
    private Chunk lastSampledChunk;
    public Tile sampleTileAt(int layer, int x, int y) {
        int chunkCoordX = x / Chunk.DEFAULT_CHUNK_SIZE;
        int chunkCoordY = y / Chunk.DEFAULT_CHUNK_SIZE;

        Chunk chunk = null;

        if(lastSampledChunk != null) {
            if(lastSampledChunk.getChunkCoordinate().is(chunkCoordX, chunkCoordY)) {
                chunk = lastSampledChunk;
            } else {
                if(getChunkManager().doesChunkExist(chunkCoordX, chunkCoordY)) {
                    chunk = getChunkManager().getChunk(chunkCoordX, chunkCoordY);
                }
            }
        } else {
            if(getChunkManager().doesChunkExist(chunkCoordX, chunkCoordY)) {
                chunk = getChunkManager().getChunk(chunkCoordX, chunkCoordY);
            }
        }
        lastSampledChunk = chunk;

        if(chunk == null) return null;

        return chunk.getTile(
                layer,
                chunk.getChunkCoordinate().localifyX(x),
                chunk.getChunkCoordinate().localifyY(y)
        );
    }

    /**
     * @return whether or not the block was successfully set
     */
    public boolean setTileTypeAt(int layer, int x, int y, TileType tileType) {
        ChunkCoordinate chunkCoordinate = ChunkCoordinate.forWorldCoords(this, x, y);

        if((layer == 0 || layer == 1)) {
            Chunk chunk = getChunkManager().getChunk(chunkCoordinate);
            chunk.flashWithNewType(layer, chunkCoordinate.localifyX(x), chunkCoordinate.localifyY(y), tileType);
            return true;
        }

        return false;
    }

    public boolean setTileTypeAt(int layer, TileCoord coord, TileType tileType) {
        ChunkCoordinate chunkCoordinate = ChunkCoordinate.forWorldCoords(this, coord.x, coord.y);

        Chunk chunk = getChunkManager().getChunk(chunkCoordinate);
        chunk.flashWithNewType(layer, chunkCoordinate.localifyX(coord.x), chunkCoordinate.localifyY(coord.y), tileType);
        return true;
    }

    public boolean isObstructionAt(int x, int y) {
        ChunkCoordinate chunkCoordinate = ChunkCoordinate.forWorldCoords(this, x, y);
        Chunk chunk = getChunkManager().getChunk(chunkCoordinate);

//        return chunk.getTile(WorldLayers.ABOVE_GROUND,
//                chunkCoordinate.localifyX(x),
//                chunkCoordinate.localifyY(y)).
//                getType() != TileTypes.AIR;

        return false;
    }

    public void queueChangeAt(TileCoord tileCoord, TileType tileType) {
        ChunkCoordinate chunkCoordinate = ChunkCoordinate.forWorldCoords(tileCoord);
        Tile newTile = new Tile(tileCoord, tileType);

        Chunk chunk = getChunkManager().getChunk(chunkCoordinate);
        chunk.queueChangeAt(tileCoord.layer, chunkCoordinate.localifyX(tileCoord.x), chunkCoordinate.localifyY(tileCoord.y), newTile);
    }

    public void queueChangeAt(TileCoord coord, TileType tileType, int newEnergy) {
        ChunkCoordinate chunkCoordinate = ChunkCoordinate.forWorldCoords(this, coord.x, coord.y);
        Tile newTile = new Tile(coord, tileType);
        newTile.setEnergy(newEnergy);

        Chunk chunk = chunkCoordinate.getChunk();

        chunk.queueChangeAt(
                coord.layer,
                chunkCoordinate.localifyX(coord.x),
                chunkCoordinate.localifyY(coord.y),
                newTile
        );
    }

    public Random getRandom() {
        return random;
    }

    public ChunkManager getChunkManager() {
        return chunkManager;
    }

    public long getSeed() {
        return seed;
    }

    public void pulse() {
        ticksPulsed++;
        getChunkManager().pulse();
    }

    public int getTicksPulsed() {
        return ticksPulsed;
    }

    public TerrainGenerator getTerrainGenerator() {
        return terrainGenerator;
    }

    @Override
    public String toString() {
        return "{world, seed=" + seed + "}";
    }
}
