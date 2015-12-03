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

        tileType.pulse(this);

        /* Randomly lose energy to entropy. */
        if(getWorld().getRandom().nextBoolean())
            energy -= tileType.getDefaultDecayRate();

        /* If energy is below or equal to 0, this tile will be destroyed */
        if(energy <= 0) {
            tileType.destruct(this);
        }
    }

    private List<Tile> neighbors = new ArrayList<>();
    public List<Tile> getMooreNeighborhood(int layer) {
        neighbors.clear();

        World world = getWorld();
        int x = coordinate.x;
        int y = coordinate.y;
//
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(!(i == 0 && j == 0)) {
                    Tile current = world.sampleTileAt(layer, x + i, y + j);

                    if (current != null) {
                        neighbors.add(current);
                    }
                }
            }
        }

        return neighbors;
    }

    public int getMooreNeighborhoodEqualTo(int layer, TileType type) {
        World world = getWorld();
        int x = coordinate.x;
        int y = coordinate.y;
        int amount = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(!(i == 0 && j == 0)) {
                    Tile current = world.sampleTileAt(layer, x + i, y + j);

                    if (current != null) {
                        if(current.getType() == type) {
                            amount++;
                        }
                    }
                }
            }
        }

        return amount;
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

    public TileCoord getCoordinate() {
        return coordinate;
    }

    public World getWorld() {
        return coordinate.world;
    }

    public Chunk getChunk() {
        return getWorld().getChunkManager()
                .getChunk(getCoordinate().x / Chunk.DEFAULT_CHUNK_SIZE, getCoordinate().y / Chunk.DEFAULT_CHUNK_SIZE);
    }

    public VM getTileVM() {
        return tileVM;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public String toString() {
        return "{tile " + getType() + " " + getCoordinate() + "}";
    }
}
