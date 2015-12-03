package com.daexsys.automata;

import com.daexsys.automata.vm.TileVM;
import com.daexsys.automata.vm.VM;
import com.daexsys.automata.world.*;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tile implements Pulsable {

    private static final int RAM_AMOUNT = 256; // 256 bytes

    private VM tileVM = new TileVM(this);

    private final TileCoordinate coordinate;
    private TileType tileType;

    private int energy;
    private byte[] tileData;

    public Tile(TileCoordinate tileCoordinate, TileType type) {
        this.coordinate = tileCoordinate;
        this.tileType = type;
        this.energy = type.getDefaultEnergy();
    }

    public void pulse() {
        if(getType() == TileType.AIR)
            return;

        tileType.pulse(this);

        ((Pulsable) tileVM).pulse();

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

    public TileCoordinate getCoordinate() {
        return coordinate;
    }

    public TileCoordinate getWorldCoordinate() {
        return getChunk().getChunkCoordinate().localifyCoordinates(getCoordinate().x, getCoordinate().y);
    }

    public World getWorld() {
        return coordinate.world;
    }

    public Chunk getChunk() {
        return getWorld().getChunkManager().getChunk(getCoordinate().x / Chunk.DEFAULT_CHUNK_SIZE, getCoordinate().y / Chunk.DEFAULT_CHUNK_SIZE);
    }

    public Optional<List<Tile>> getNeighbors(int layer) {
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

        List<Tile> neighbors = new ArrayList<Tile>();
        neighbors.add(up);
        neighbors.add(down);
        neighbors.add(left);
        neighbors.add(right);
        neighbors.add(up_left);
        neighbors.add(up_right);
        neighbors.add(down_left);
        neighbors.add(down_right);

        return Optional.of(neighbors);
    }

    public VM getTileVM() {
        return tileVM;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
