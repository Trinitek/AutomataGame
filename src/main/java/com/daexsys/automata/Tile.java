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

    @Override
    public void pulse() {
        /* Don't tick this tile if it is air, air is boring */
        if(getType() == TileType.AIR)
            return;

        /* Do type-specific logic */
        getType().pulse(this);

        /* Allow the tile the opportunity to collect energy from an emission.
         * e.g. an energy tower, the sun, geothermal */
        getWorld().getGame().getEmissionManager().collectFor(this);

        /* If energy is below or equal to 0, this tile will be destroyed. */
        if(getEnergy() <= 0)
            getType().destruct(this);

        /* 1/2 chance to lose energy to heat. */
        // TODO: this should be changed. Should the second law be so kind?
        // TODO: Potentially change put this in an 'EntropyManager'.
        if(getWorld().getRandom().nextBoolean())
            energy -= getType().getDefaultDecayRate();
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
        return getCoordinate().world;
    }

    public Chunk getChunk() {
        return getCoordinate().getChunk();
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
