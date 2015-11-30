package com.daexsys.automata.world;

import com.daexsys.automata.Pulsable;
import com.daexsys.automata.Tile;
import com.daexsys.automata.world.tiletypes.TileType;
import com.daexsys.automata.world.tiletypes.TileTypes;

import java.util.Random;

// TODO: split up into chunks
// TODO: Need logging framework.
public final class World implements Pulsable {

    private long seed;
    private Random random;

    private ChunkManager chunkManager;

    public World(final int size) {
        seed = System.currentTimeMillis();
        random = new Random(seed);

        chunkManager = new ChunkManager(this);

        // pre-generate world sections
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chunkManager.getChunk(i, j);
            }
        }

        System.out.println("Creating world of size: " + size);

        /* Add lakes */
        for (int i = 0; i < size / 20; i++) {
            int lakeX = random.nextInt(size);
            int lakeY = random.nextInt(size);
            int lakeSize = random.nextInt(15) + 3;

            for (int j = lakeX - lakeSize; j < lakeX + lakeSize; j++) {
                for (int k = lakeY - lakeSize; k < lakeY + lakeSize; k++) {
                    int x = j - lakeX;
                    int y = k - lakeY;

                    if((x * x + y * y) < lakeSize * lakeSize) {
                        setTileTypeAt(WorldLayers.GROUND, j, k, TileTypes.WATER);
                    }
                }
            }
        }

        for (int i = 0; i < size / 5; i++) {
            int lakeX = random.nextInt(size);
            int lakeY = random.nextInt(size);
            int lakeSize = random.nextInt(15) + 3;

            for (int j = lakeX - lakeSize; j < lakeX + lakeSize; j++) {
                for (int k = lakeY - lakeSize; k < lakeY + lakeSize; k++) {
                    int x = j - lakeX;
                    int y = k - lakeY;

                    if((x * x + y * y) < lakeSize * lakeSize) {
                        if(random.nextBoolean()) {
                            setTileTypeAt(WorldLayers.ABOVE_GROUND, j, k, TileTypes.STONE);
                        } else {
                            setTileTypeAt(WorldLayers.ABOVE_GROUND, j, k, TileTypes.ENERGY_ORE);
                        }
                    }
                }
            }
        }
    }

    public Tile getTileAt(int layer, int x, int y) {
        ChunkCoordinate chunkCoordinate = ChunkCoordinate.forWorldCoords(this, x, y);

        Chunk chunk = getChunkManager().getChunk(chunkCoordinate);

        TileCoordinate tileCoordinate = chunkCoordinate.localifyCoordinates(x, y);
        return chunk.getTile(layer, tileCoordinate.x, tileCoordinate.y);
    }

    /**
     * @return whether or not the block was successfully set
     */
    public boolean setTileTypeAt(int layer, int x, int y, TileType tileType) {
        ChunkCoordinate chunkCoordinate = ChunkCoordinate.forWorldCoords(this, x, y);

        if((layer == 0 || layer == 1)) {

            Chunk chunk =
                    getChunkManager().getChunk(chunkCoordinate);

//            System.out.println(x + " " + y);
//            System.out.println(chunkCoordinate);
//            System.out.println(chunkCoordinate.localifyCoordinates(x, y));

            chunk.flashWithNewType(layer, chunkCoordinate.localifyX(x), chunkCoordinate.localifyY(y), tileType);
            return true;
        }

        return false;
    }

    public boolean isObstructionAt(int x, int y) {
        ChunkCoordinate chunkCoordinate = ChunkCoordinate.forWorldCoords(this, x, y);
        Chunk chunk = getChunkManager().getChunk(chunkCoordinate);

        return chunk.getTile(WorldLayers.ABOVE_GROUND,
                chunkCoordinate.localifyX(x),
                chunkCoordinate.localifyY(y)).
                getTileType() != TileTypes.AIR;
    }


    public void queueChangeAt(int x, int y, TileType tileType) {
        Tile newTile = new Tile(new TileCoordinate(this, x, y), tileType);
//        queuedTileChangeStack.push(new QueuedTileChange(x, y, newTile));
    }

    public void queueChangeAt(int x, int y, TileType tileType, int newEnergy) {
        Tile newTile = new Tile(new TileCoordinate(this, x, y), tileType);
        newTile.setEnergy(newEnergy);

//        queuedTileChangeStack.push(new QueuedTileChange(x, y, newTile));
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

    @Override
    public void pulse() {
        getChunkManager().pulse();
    }

    @Override
    public String toString() {
        return "{world, seed=" + seed + "}";
    }
}
