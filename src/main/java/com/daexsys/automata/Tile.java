package com.daexsys.automata;

import com.daexsys.automata.world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Tile implements Pulsable {

    private static final int RAM_AMOUNT = 255; // 255 bytes

    private TileCoordinate coordinate;
    private TileType tileType;
    private byte[] tileData;
    private int energy;

    public Tile(TileCoordinate tileCoordinate, TileType type) {
        this.coordinate = tileCoordinate;
        this.tileType = type;
        this.energy = type.getDefaultEnergy();
    }

    public void pulse() {
        lazyInit(); // Init byte array if not done already

        tileType.pulse(this);

        /* Randomly lose energy to entropy. */
        if(getWorld().getRandom().nextBoolean())
            energy -= tileType.getDefaultDecayRate();

        /* If energy is below or equal to 0, this tile will be destroyed */
        if(energy <= 0)
            destruct();
    }

    public void destruct() {
        getWorld().queueChangeAt(coordinate.x, coordinate.y, TileTypes.DIRT);
    }

    public int getEnergy() {
        return energy;
    }

    public void lazyInit() {
        if(tileData == null) {
            tileData = new byte[RAM_AMOUNT];
            getTileData()[0] = (byte) new Random().nextInt(2);
        }
    }

    public TileType getTileType() {
        return tileType;
    }

    public byte[] getTileData() {
        return tileData;
    }

    public TileCoordinate getCoordinate() {
        return coordinate;
    }

    public WorldModel getWorld() {
        return coordinate.worldModel;
    }

    public Optional<List<Tile>> getNeighbors() {
        WorldModel world = getWorld();
        int x = coordinate.x;
        int y = coordinate.y;

        try {
            Tile up = world.getTileAt    (x, y - 1);
            Tile down = world.getTileAt  (x, y + 1);
            Tile left = world.getTileAt  (x - 1, y);
            Tile right = world.getTileAt (x + 1, y);
            Tile up_left = world.getTileAt    (x - 1, y - 1);
            Tile up_right = world.getTileAt   (x + 1, y - 1);
            Tile down_left = world.getTileAt  (x - 1, y + 1);
            Tile down_right = world.getTileAt (x + 1, y + 1);

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
        } catch (AccessOutOfWorldException ignore) {}

        return Optional.empty();
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
