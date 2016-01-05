package com.daexsys.automata.world;

import com.daexsys.automata.Game;
import com.daexsys.automata.Pulsable;
import com.daexsys.automata.GameRandom;
import com.daexsys.automata.Tile;
import com.daexsys.automata.event.tile.TilePlacementReason;
//import com.daexsys.automata.world.terrain.GenerationContext;
import com.daexsys.automata.world.terrain.DesertTerrain;
import com.daexsys.automata.world.terrain.TemperateTerrain;
import com.daexsys.automata.world.terrain.TerrainGenerator;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.List;

public final class World implements Pulsable {

    private long seed;
    private GameRandom random;

    private int ticksPulsed = 0;

    private Game game;
    private ChunkManager chunkManager;
    private TerrainGenerator terrainGenerator;

    private int time;

    public World(Game game) {
        this.game = game;
        seed = System.currentTimeMillis();
        random = new GameRandom(seed);

        terrainGenerator = random.nextBoolean() ? new TemperateTerrain(this) : new DesertTerrain(this);

//        terrainGenerator = new GenerationContext(this, 0);
        chunkManager = new ChunkManager(this);

        // pre-generate world sections

        int size = 200;
////        /* Add lakes */
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
//                        setTileTypeAt(WorldLayer.GROUND, j, k, TileType.WATER);
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
//                            setTileTypeAt(WorldLayer.ABOVE_GROUND, j, k, TileType.STONE);
//                        } else {
//                            setTileTypeAt(WorldLayer.ABOVE_GROUND, j, k, TileType.ENERGY_ORE);
//                        }
//                    }
//                }
//            }
//        }
    }

    public Game getGame() {
        return game;
    }

    public Tile getTileAt(int layer, int x, int y) {
        Chunk chunk = getChunkManager().getChunk(
                x / Chunk.DEFAULT_CHUNK_SIZE, y / Chunk.DEFAULT_CHUNK_SIZE);

        ChunkCoord chunkCoordinate = chunk.getChunkCoordinate();

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

        if(chunk == null)
            return null;

        return chunk.getTile(
                layer,
                chunk.getChunkCoordinate().localifyX(x),
                chunk.getChunkCoordinate().localifyY(y)
        );
    }

    public boolean setTileTypeAt(int layer, int x, int y, TileType tileType) {
        return setTileTypeAt(layer, x, y, tileType, TilePlacementReason.AUTOMATA_SPREAD);
    }

    /**
     * @return whether or not the block was successfully set
     */
    public boolean setTileTypeAt(int layer, int x, int y, TileType tileType, TilePlacementReason tileAlterCause) {
        ChunkCoord chunkCoordinate = ChunkCoord.forWorldCoords(this, x, y);

        if((layer == 0 || layer == 1)) {
            Chunk chunk = getChunkManager().getChunk(chunkCoordinate);

            if(chunk != null) {
                chunk.flashWithNewType(layer, chunkCoordinate.localifyX(x),
                        chunkCoordinate.localifyY(y), tileType, tileAlterCause);
            } else {
                return false;
            }

            return true;
        }

        return false;
    }

    public boolean setTileTypeAt(int layer, TileCoord coord, TileType tileType) {
        ChunkCoord chunkCoordinate = ChunkCoord.forWorldCoords(this, coord.x, coord.y);

        Chunk chunk = getChunkManager().getChunk(chunkCoordinate);
        chunk.flashWithNewType(layer, chunkCoordinate.localifyX(coord.x),
                chunkCoordinate.localifyY(coord.y), tileType, TilePlacementReason.AUTOMATA_SPREAD);
        return true;
    }

    public boolean setTileTypeAt(int layer, TileCoord coord, TileType tileType, TilePlacementReason tileAlterCause) {
        ChunkCoord chunkCoordinate = ChunkCoord.forWorldCoords(this, coord.x, coord.y);

        Chunk chunk = getChunkManager().getChunk(chunkCoordinate);
        chunk.flashWithNewType(layer, chunkCoordinate.localifyX(coord.x), chunkCoordinate.localifyY(coord.y), tileType, tileAlterCause);
        return true;
    }

    public boolean isObstructionAt(int x, int y) {
        ChunkCoord chunkCoordinate = ChunkCoord.forWorldCoords(this, x, y);
        Chunk chunk = getChunkManager().getChunk(chunkCoordinate);

        if(chunk != null) {
            return chunk.getTile(WorldLayer.ABOVE_GROUND,
                    chunkCoordinate.localifyX(x),
                    chunkCoordinate.localifyY(y)).
                    getType() != TileType.AIR;
        }

        return true;
    }

    public void queueChangeAt(TileCoord tileCoord, TileType tileType) {
        ChunkCoord chunkCoordinate = ChunkCoord.forWorldCoords(tileCoord);
        Tile newTile = new Tile(tileCoord, tileType);

        Chunk chunk = getChunkManager().getChunk(chunkCoordinate);
        try {
            chunk.queueChangeAt(tileCoord.layer, chunkCoordinate.localifyX(tileCoord.x), chunkCoordinate.localifyY(tileCoord.y), newTile);
        } catch (Exception e) {}
    }

    public void queueChangeAt(TileCoord coord, TileType tileType, int newEnergy) {
        ChunkCoord chunkCoordinate = ChunkCoord.forWorldCoords(this, coord.x, coord.y);
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

    public List<Tile> getTilesInRange(TileCoord tileCoord, double range) {
        return null;
    }

    public GameRandom getRandom() {
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
