package com.daexsys.automata.world;

import com.daexsys.automata.Pulsable;
import com.daexsys.automata.Tile;
import com.daexsys.automata.event.tile.TileAlterCause;
import com.daexsys.automata.event.tile.TileAlterEvent;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.Stack;

/**
 * A chunk is a square sub-unit of the world.
 *
 * Contains a 2x16x16 matrix of tiles. (layers, x, y)
 */
public final class Chunk implements Pulsable {

    public static final int DEFAULT_CHUNK_SIZE = 16;

    private ChunkCoord chunkCoordinate;
    private Tile[][][] contents;

    private Stack<QueuedTileChange> queuedTileChangeStack;
    public boolean homogenous = false;

    public Chunk(ChunkCoord chunkCoord) {
        this.chunkCoordinate = chunkCoord;
        init();
    }

    public Chunk(World world, int x, int y) {
        this.chunkCoordinate = ChunkCoord.of(world, x, y);
        init();
    }

    private void init() {
        contents = new Tile[2][DEFAULT_CHUNK_SIZE][DEFAULT_CHUNK_SIZE];
        queuedTileChangeStack = new Stack<>();

        fillLayerWith(0, TileType.AIR);
        fillLayerWith(1, TileType.AIR);
    }

    public void pulse() {
        // Pulse all tiles (for now, eventually this needs to be handled in a more intelligent way)
        for (int i = 0; i < contents[WorldLayer.GROUND].length; i++) {
            for (int j = 0; j < contents[WorldLayer.GROUND][i].length; j++) {
                contents[WorldLayer.GROUND][i][j].pulse();
                contents[WorldLayer.ABOVE_GROUND][i][j].pulse();
            }
        }
    }

    public void depositQueue() {
        while(!queuedTileChangeStack.isEmpty()) {
            QueuedTileChange queuedTileChange = queuedTileChangeStack.pop();

            setTile(queuedTileChange.layer, queuedTileChange.x, queuedTileChange.y, queuedTileChange.t, TileAlterCause.AUTOMATA_SPREAD);
        }
    }

    public Tile getTile(int layer, int x, int y) {
        if(x < 0 || y < 0 || x > 15 || y > 15) {
            return null;
//            int nX = getChunkCoordinate().amplifyLocalX(x);
//            int yX = getChunkCoordinate().amplifyLocalX(y);
//
//            Chunk correctChunk = getChunkCoordinate().getWorld().getChunkManager()
//                    .getChunk(ChunkCoord.forWorldCoords(getChunkCoordinate().getWorld(), nX, yX));
//
//            ChunkCoord correctCoord = correctChunk.getChunkCoordinate();
//            return correctChunk.getTile(layer, correctCoord.localifyX(nX), correctCoord.localifyY(yX));
        } else {
            return contents[layer][x][y];
        }
    }

    public void setTile(int layer, int x, int y, Tile tile, TileAlterCause tileAlterCause) {
        if(homogenous)
            pingNearby();

        homogenous = false;

        if(layer == WorldLayer.GROUND) {
            if(contents[WorldLayer.ABOVE_GROUND][x][y].getType() == TileType.AIR) {
                if (x >= 0 && y >= 0)
                    alterTile(layer, x, y, tile, tileAlterCause);
            }
        } else {
            if (x >= 0 && y >= 0)
                alterTile(layer, x, y, tile, tileAlterCause);
        }

        tile.getTileVM().initialize();
    }

    private void alterTile(int layer, int x, int y, Tile tile, TileAlterCause tileAlterCause) {
        contents[layer][x][y] = tile;

        TileAlterEvent tileAlterEvent = new TileAlterEvent(tile, tileAlterCause);
        tile.getWorld().getGame().fireEvent(tileAlterEvent);
    }

    public void flashWithNewType(int layer, int x, int y, TileType tileType, TileAlterCause tileAlterCause) {
        if(homogenous)
            pingNearby();

        homogenous = false;

        TileCoord coordinate = new TileCoord(layer, chunkCoordinate.world,
                getChunkCoordinate().amplifyLocalX(x),
                getChunkCoordinate().amplifyLocalY(y)
        );

        if(x >= 0 && y >= 0) {
            Tile newTile = new Tile(coordinate, tileType);
            alterTile(layer, x, y, newTile, tileAlterCause);
            newTile.getTileVM().initialize();
        }
    }

    public void fillLayerWith(int layer, TileType tileType) {
        for (int i = 0; i < DEFAULT_CHUNK_SIZE; i++) {
            for (int j = 0; j < DEFAULT_CHUNK_SIZE; j++) {
                flashWithNewType(layer, i, j, tileType, TileAlterCause.GENERATION);
            }
        }
    }

    public void queueChangeAt(int layer, int x, int y, Tile t) {
        queuedTileChangeStack.push(new QueuedTileChange(layer, x, y, t));
    }

    private void pingNearby() {
        World world = getChunkCoordinate().world;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                ChunkCoord shiftedCoordinate = getChunkCoordinate().add(i, j);

                if(shiftedCoordinate.x >= 0 && shiftedCoordinate.y >= 0) {
                    world.getChunkManager().getChunk(shiftedCoordinate);
                }
            }
        }
    }

    /**
     * Whether or not a chunk is comprised of only one tile type
     */
    public boolean isHomogenous() {
        return homogenous;
    }

    public ChunkCoord getChunkCoordinate() {
        return chunkCoordinate;
    }

    public Tile[][][] getContents() {
        return contents;
    }

    @Override
    public String toString() {
        String buildUp = "";

        for (int j = 0; j < Chunk.DEFAULT_CHUNK_SIZE; j++) {
            String currentRow = "";

            for (int i = 0; i < Chunk.DEFAULT_CHUNK_SIZE; i++) {
                Tile tile = getTile(0, i, j);
                currentRow += tile.getType().getID() + ", ";
            }

            buildUp += currentRow + "\n";
        }

        return buildUp;
    }
}
