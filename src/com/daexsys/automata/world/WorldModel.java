package com.daexsys.automata.world;

import com.daexsys.automata.Pulsable;
import com.daexsys.automata.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

// TODO: split up into chunks
public class WorldModel implements Pulsable {

    private Random random = new Random();
    private Tile[][][] tiles;

    private List<Chunk> chunkList = new ArrayList<Chunk>();

    private Stack<QueuedTileChange> queuedTileChangeStack = new Stack<QueuedTileChange>();

    public WorldModel(final int size) {
        tiles = new Tile[2][size][size];

        System.out.println("Creating world of size: " + size);

        /* Fill world with grass */
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tiles[Layer.GROUND][x][y] =
                        new Tile(new TileCoordinate(this, x, y), TileTypes.GRASS);

                tiles[Layer.ABOVE_GROUND][x][y] =
                        new Tile(new TileCoordinate(this, x, y), TileTypes.AIR);
            }
        }

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
                        setTileTypeAt(j, k, TileTypes.WATER);
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
                            setTileTypeAt(Layer.ABOVE_GROUND, j, k, TileTypes.STONE);
                        } else {
                            setTileTypeAt(Layer.ABOVE_GROUND, j, k, TileTypes.ENERGY_ORE);
                        }
                    }
                }
            }
        }

        System.out.println("Initialized world model: " + tiles[0].length * tiles[0][0].length + " tiles loaded");
    }

    public Tile getTileAt(int x, int y) throws AccessOutOfWorldException {
        return getTileAt(0, x, y);
    }

    public void queueChangeAt(int x, int y, TileType tileType) {
        Tile newTile = new Tile(new TileCoordinate(this, x, y), tileType);
        queuedTileChangeStack.push(new QueuedTileChange(x, y, newTile));
    }

    public void queueChangeAt(int x, int y, TileType tileType, int newEnergy) {
        Tile newTile = new Tile(new TileCoordinate(this, x, y), tileType);
        newTile.setEnergy(newEnergy);

        queuedTileChangeStack.push(new QueuedTileChange(x, y, newTile));
    }

    public void setTileTypeAt(int x, int y, TileType tileType) {
        setTileTypeAt(Layer.GROUND, x, y, tileType);
    }

    /* Need changes */
    public void setTileTypeAt(int layer, int x, int y, TileType tileType) {
        if(x > -1 && y > -1 && x < tiles[Layer.GROUND].length && y < tiles[0][0].length)
            if((layer == 0 && !isObstructionAt(x, y)) || layer == 1) {
                tiles[layer][x][y] = new Tile(new TileCoordinate(this, x, y), tileType);
            }
    }

    public Tile getTileAt(int layer, int x, int y) throws AccessOutOfWorldException {
        if(x < 0 || y < 0 || x > tiles[0].length - 1|| y > tiles[0][0].length - 1)
            throw new AccessOutOfWorldException();

        return tiles[layer][x][y];
    }

//    public void setAboveGroundTile(int x, int y, TileType tileType) {
//        setTileTypeAt();
//    }

    public void setTileAt(Tile type, int x, int y) {
        if(x > -1 && y > -1)
            tiles[Layer.GROUND][x][y] = type;
    }

    public boolean isObstructionAt(int x, int y) {
        if(x >= 0 && y >= 0 && x < tiles[Layer.ABOVE_GROUND][0].length) {
            return tiles[Layer.ABOVE_GROUND][x][y].getTileType() != TileTypes.AIR;
        }

        return false;
    }

    public Tile[][][] getTiles() {
        return tiles;
    }

    public Random getRandom() {
        return random;
    }

    public Chunk getChunk(int x, int y) {
        for(Chunk c : chunkList) {
            if(c.getX() == x && c.getY() == y) {
                return c;
            }
        }

        return null;
    }

    @Override
    public void pulse() {
        // Pulse all tiles (for now, eventually this needs to be handled in a more intelligent way)
        for (int i = 0; i < tiles[Layer.GROUND].length; i++) {
            for (int j = 0; j < tiles[Layer.GROUND][i].length; j++) {
                tiles[Layer.GROUND][i][j].pulse();
            }
        }

        while(!queuedTileChangeStack.isEmpty()) {
            QueuedTileChange queuedTileChange = queuedTileChangeStack.pop();

            setTileAt(queuedTileChange.t, queuedTileChange.x, queuedTileChange.y);
        }
    }
}
