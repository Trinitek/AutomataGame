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

    public Chunk(World world, int x, int y) {
        this.chunkCoordinate = new ChunkCoordinate(world, x, y);

        contents = new Tile[2][DEFAULT_CHUNK_SIZE][DEFAULT_CHUNK_SIZE];
        queuedTileChangeStack = new Stack<QueuedTileChange>();

        fillLayerWith(WorldLayers.ABOVE_GROUND, TileTypes.AIR);
        fillLayerWith(WorldLayers.GROUND, TileTypes.DIRT);
    }

    public void fillLayerWith(int layer, TileType tileType) {
        for (int i = 0; i < DEFAULT_CHUNK_SIZE; i++) {
            for (int j = 0; j < DEFAULT_CHUNK_SIZE; j++) {
                flashWithNewType(layer, i, j, tileType);
            }
        }
    }

    public void pulse() {
        // Pulse all tiles (for now, eventually this needs to be handled in a more intelligent way)
        for (int i = 0; i < contents[WorldLayers.GROUND].length; i++) {
            for (int j = 0; j < contents[WorldLayers.GROUND][i].length; j++) {
                contents[WorldLayers.GROUND][i][j].pulse();
            }
        }

        while(!queuedTileChangeStack.isEmpty()) {
            QueuedTileChange queuedTileChange = queuedTileChangeStack.pop();

            setTile(WorldLayers.GROUND, queuedTileChange.x, queuedTileChange.y, queuedTileChange.t);
        }
    }

    public Tile getTile(int layer, int x, int y) {
        return contents[layer][x][y];
    }

    public void setTile(int layer, int x, int y, Tile tile) {
        contents[layer][x][y] = tile;
    }

    public void flashWithNewType(int layer, int x, int y, TileType tileType) {
        contents[layer][x][y] = new Tile(new TileCoordinate(chunkCoordinate.world, x, y), tileType);
    }

    public ChunkCoordinate getChunkCoordinate() {
        return chunkCoordinate;
    }

    public Tile[][][] getContents() {
        return contents;
    }
}