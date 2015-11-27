package com.daexsys.automata.world;

import com.daexsys.automata.Pulsable;
import com.daexsys.automata.Tile;

import java.util.Random;
import java.util.Stack;

// TODO: split up into chunks
public class WorldModel implements Pulsable {

    private Random random = new Random();
    private Tile[][] tiles;

    private Stack<QueuedTileChange> queuedTileChangeStack = new Stack<QueuedTileChange>();

    public WorldModel(final int size) {
        tiles = new Tile[size][size];

        // TODO: create tiletype api

        /* Fill world with grass */
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tiles[x][y] = new Tile(new TileCoordinate(this, x, y), TileTypes.GRASS);
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
                            setTileTypeAt(j, k, TileTypes.STONE);
                        } else {
                            setTileTypeAt(j, k, TileTypes.ENERGY_ORE);
                        }
                    }
                }
            }
        }

        System.out.println("Initialized world model: " + size * size + " tiles loaded");
    }

    public Tile getTileAt(int x, int y) throws AccessOutOfWorldException {
        if(x < 0 || y < 0 || x > tiles.length - 1|| y > tiles.length - 1) throw new AccessOutOfWorldException();

        return tiles[x][y];
    }

    public void setTileAt(Tile type, int x, int y) {
        if(x > -1 && y > -1)
        tiles[x][y] = type;
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
        if(x > -1 && y > -1 && x < tiles.length && y < tiles.length)
            tiles[x][y] = new Tile(new TileCoordinate(this, x, y), tileType);
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Random getRandom() {
        return random;
    }

    @Override
    public void pulse() {

        // Pulse all tiles (for now, eventually this needs to be handled in a more intelligent way)
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].pulse();
            }
        }

        while(!queuedTileChangeStack.isEmpty()) {
            QueuedTileChange queuedTileChange = queuedTileChangeStack.pop();

            setTileAt(queuedTileChange.t, queuedTileChange.x, queuedTileChange.y);
        }
    }
}
