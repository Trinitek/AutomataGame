package com.daexsys.automata;

import com.daexsys.automata.vm.TileVM;
import com.daexsys.automata.vm.VM;
import com.daexsys.automata.world.*;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.ArrayList;
import java.util.List;

public class Tile implements Pulsable {

    private static final int RAM_AMOUNT = 256; // 256 bytes

    private final TileCoord coordinate;
    private TileType tileType;

    private VM tileVM = new TileVM(this);

    private int energy;
    private byte[] tileData;

    public Tile(TileCoord tileCoordinate, TileType type) {
        this.coordinate = tileCoordinate;
        this.tileType = type;
        this.energy = type.getDefaultEnergy();
        this.tileData = type.getProgram();
    }

    public void pulse() {
        if(getType() == TileType.AIR)
            return;

        if(getCoordinate().layer == WorldLayer.GROUND)
            tileType.pulse(this);

        /* Randomly lose energy to entropy. */
        if(getWorld().getRandom().nextBoolean())
            energy -= tileType.getDefaultDecayRate();

        /* If energy is below or equal to 0, this tile will be destroyed */
        if(energy <= 0) {
            tileType.destruct(this);
        }
    }

    public int getEnergy() {
        return energy;
    }

    public TileType getType() {
        return tileType;
    }

    public byte[] getTileData() {
        if(tileData == null) {
            tileData = new byte[RAM_AMOUNT];
        }

        return tileData;
    }

//    public void setType(TileType tileType) {
//        this.tileType = tileType;
//        energy = tileType.getDefaultEnergy();
//    }

    public TileCoord getCoordinate() {
        return coordinate;
    }

    public World getWorld() {
        return coordinate.world;
    }

    public Chunk getChunk() {
        return getWorld().getChunkManager().getChunk(getCoordinate().x / Chunk.DEFAULT_CHUNK_SIZE, getCoordinate().y / Chunk.DEFAULT_CHUNK_SIZE);
    }
//
//    List<Tile> neighbors = new ArrayList<Tile>();
//    public Optional<List<Tile>> getNeighbors(int layer) {
//        neighbors.clear();
//
//        Tile up =   coordinate.add(0, -1).getTile().get();
//        Tile down = coordinate.add(0, +1).getTile().get();
//        Tile left = coordinate.add(-1, 0).getTile().get();
//        Tile right = coordinate.add(+1, 0).getTile().get();
//        Tile up_left = coordinate.add(-1, -1).getTile().get();
//        Tile up_right = coordinate.add(+1, -1).getTile().get();
//        Tile down_left = coordinate.add(+1, 1).getTile().get();
//        Tile down_right = coordinate.add(+1, -1).getTile().get();
//
//        neighbors.add(up);
//        neighbors.add(down);
//        neighbors.add(left);
//        neighbors.add(right);
//        neighbors.add(up_left);
//        neighbors.add(up_right);
//        neighbors.add(down_left);
//        neighbors.add(down_right);
//
//        return Optional.of(neighbors);
//    }

    List<Tile> neighbors = new ArrayList<>();

    public List<Tile> getNeighbors(int layer) {
        neighbors.clear();

        World world = getWorld();
        int x = coordinate.x;
        int y = coordinate.y;

        Tile up = world.sampleTileAt(layer, x, y - 1);
        Tile down = world.sampleTileAt(layer, x, y + 1);
        Tile left = world.sampleTileAt(layer, x - 1, y);
        Tile right = world.sampleTileAt(layer, x + 1, y);
        Tile up_left = world.sampleTileAt(layer, x - 1, y - 1);
        Tile up_right = world.sampleTileAt(layer, x + 1, y - 1);
        Tile down_left = world.sampleTileAt(layer, x - 1, y + 1);
        Tile down_right = world.sampleTileAt(layer, x + 1, y + 1);

        neighbors.add(up);
        neighbors.add(down);
        neighbors.add(left);
        neighbors.add(right);
        neighbors.add(up_left);
        neighbors.add(up_right);
        neighbors.add(down_left);
        neighbors.add(down_right);

        return neighbors;
    }

//
//    private List<Optional<Tile>> storedTiles = new ArrayList<>();
//    private List<Tile> neighbors = new ArrayList<>();
//
//    public com.google.common.base.Optional<List<Tile>> getNeighbors() {
//        storedTiles.clear();
//        neighbors.clear();
//
//        storedTiles.add(coordinate.add(0, +1).getTile());
//        storedTiles.add(coordinate.add(0, -1).getTile());
//        storedTiles.add(coordinate.add(+1, 0).getTile());
//        storedTiles.add(coordinate.add(-1, 0).getTile());
//
//        storedTiles.add(coordinate.add(+1, +1).getTile());
//        storedTiles.add(coordinate.add(-1, +1).getTile());
//        storedTiles.add(coordinate.add(-1, -1).getTile());
//        storedTiles.add(coordinate.add(+1, -1).getTile());
//
//        for(com.google.common.base.Optional<Tile> tiles : storedTiles) {
//            if(tiles.isPresent()) {
//                neighbors.add(tiles.get());
//            }
//        }
//
//        return com.google.common.base.Optional.of(neighbors);
//    }

    public VM getTileVM() {
        return tileVM;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
