package com.daexsys.automata.world;

import com.daexsys.automata.Pulsable;
import com.daexsys.automata.Tile;
import com.daexsys.automata.world.tiletypes.TileType;
import com.daexsys.automata.world.tiletypes.TileTypes;

import java.util.Stack;

/**
 * A chunk is a square sub-unit of the world.
 *
 * Contains a 2x16x16 matrix of tiles. (layers, x, y)
 */
public final class Chunk implements Pulsable {

    public static final int DEFAULT_CHUNK_SIZE = 16;

    private ChunkCoordinate chunkCoordinate;
    private Tile[][][] contents;
    private Stack<QueuedTileChange> queuedTileChangeStack;
    private boolean homogenous = false;

    public Chunk(World world, int x, int y) {
        this.chunkCoordinate = ChunkCoordinate.of(world, x, y);

        contents = new Tile[2][DEFAULT_CHUNK_SIZE][DEFAULT_CHUNK_SIZE];
        queuedTileChangeStack = new Stack<QueuedTileChange>();

        fillLayerWith(WorldLayers.ABOVE_GROUND, TileTypes.AIR);
        fillLayerWith(WorldLayers.GROUND, TileTypes.TALL_GRASS);
        homogenous = true;
    }

    public void fillLayerWith(int layer, TileType tileType) {
        for (int i = 0; i < DEFAULT_CHUNK_SIZE; i++) {
            for (int j = 0; j < DEFAULT_CHUNK_SIZE; j++) {
                flashWithNewType(layer, i, j, tileType);
            }
        }
    }

    public void pulse() {
//        if(homogenous) return;

        // Pulse all tiles (for now, eventually this needs to be handled in a more intelligent way)
        for (int i = 0; i < contents[WorldLayers.GROUND].length; i++) {
            for (int j = 0; j < contents[WorldLayers.GROUND][i].length; j++) {
                contents[WorldLayers.GROUND][i][j].pulse();
                contents[WorldLayers.ABOVE_GROUND][i][j].pulse();
            }
        }
    }

    public void depositQueue() {
        while(!queuedTileChangeStack.isEmpty()) {
            QueuedTileChange queuedTileChange = queuedTileChangeStack.pop();

            setTile(WorldLayers.GROUND, queuedTileChange.x, queuedTileChange.y, queuedTileChange.t);
        }
    }

    public Tile getTile(int layer, int x, int y) {
        if(x >= 0 && y >= 0)
            return contents[layer][x][y];

        return null;
    }

    public void setTile(int layer, int x, int y, Tile tile) {
        if(homogenous)
            pingNearby();

        homogenous = false;

        if(x >= 0 && y >= 0)
            contents[layer][x][y] = tile;
    }

    public void flashWithNewType(int layer, int x, int y, TileType tileType) {
        if(homogenous)
            pingNearby();

        homogenous = false;

        TileCoordinate coordinate = new TileCoordinate(chunkCoordinate.world,
                getChunkCoordinate().amplifyLocalX(x),
                getChunkCoordinate().amplifyLocalY(y)
        );

        if(x >= 0 && y >= 0)
            contents[layer][x][y] = new Tile(coordinate, tileType);
    }

    public void queueChangeAt(int layer, int x, int y, Tile t) {
        queuedTileChangeStack.push(new QueuedTileChange(x, y, t));
    }

    private void pingNearby() {
        World world = getChunkCoordinate().world;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                world.getChunkManager().getChunk(getChunkCoordinate().x + i, getChunkCoordinate().y + j);
            }
        }
    }

    /**
     * Whether or not a chunk is comprised of only one tile type
     */
    public boolean isHomogenous() {
        return homogenous;
    }

    public ChunkCoordinate getChunkCoordinate() {
        return chunkCoordinate;
    }

    public Tile[][][] getContents() {
        return contents;
    }
}
