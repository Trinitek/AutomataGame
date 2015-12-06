package com.daexsys.automata;

import com.daexsys.automata.vm.TileVM;
import com.daexsys.automata.vm.VM;
import com.daexsys.automata.world.*;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.ArrayList;
import java.util.List;

/**
 * Tile instances represent the state of a single 1x1 automaton cell in the world.
 */
public class Tile implements Pulsable {

    private static final int RAM_AMOUNT = 256;

    /*
        The location of this tile in a world.
     */
    private final TileCoord coordinate;

    /*
        The type of tile this tile is. Used to store information
        common to all tiles of a specific kind.
     */
    private TileType tileType;

    /*
        The amount of usable energy stored in this tile.
        It is assumed to be of the type 'WIRED'.
     */
    private int energy;

    /*  The amount of heat stored in this tile.
        Heat energy is unusable, and will be radiated off.
        However, sufficient heat buildup will lead to machine breakage,
        and potentially fire.

        Current unused.
     */
    private double heat;

    /*
        The memory of the tile's VM.
     */
    private byte[] tileData;
    private VM tileVM = new TileVM(this);

    public Tile(TileCoord tileCoordinate, TileType type) {
        this.coordinate = tileCoordinate;
        this.tileType = type;
        this.energy = type.getDefaultEnergy();
        this.tileData = type.getProgram();
    }

    @Override
    public void pulse() {
        /* Don't tick this tile if it is air, air is boring */
        if(!getType().areTilesPulsable())
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

    private static List<Tile> neighbors = new ArrayList<>();
    public static List<Tile> getMooreNeighborhood(Tile tile) {
        neighbors.clear();

        World world = tile.getWorld();
        int x = tile.getCoordinate().x;
        int y = tile.getCoordinate().y;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(!(i == 0 && j == 0)) {
                    Tile current = world.sampleTileAt(tile.getCoordinate().layer, x + i, y + j);

                    if (current != null) {
                        neighbors.add(current);
                    }
                }
            }
        }

        return neighbors;
    }

    public static int getMooreNeighborhoodEqualTo(Tile tile, TileType tileType) {
        World world = tile.getWorld();
        int x = tile.getCoordinate().x;
        int y = tile.getCoordinate().y;
        int amount = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(!(i == 0 && j == 0)) {
                    Tile current = world.sampleTileAt(tile.getCoordinate().layer, x + i, y + j);

                    if (current != null) {
                        if(current.getType() == tileType) {
                            amount++;
                        }
                    }
                }
            }
        }

        return amount;
    }
}
