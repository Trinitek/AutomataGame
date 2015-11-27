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

        /* If grass or dirt gain energy from the sun */
        if(tileType == TileTypes.GRASS || tileType == TileTypes.DIRT || tileType == TileTypes.TALL_GRASS) {
            energy += 2;
        }
        /* Dirt will eventually grow grass */
        if(tileType == TileTypes.DIRT && energy > 500) {
            getWorld().queueChangeAt(coordinate.x, coordinate.y, TileTypes.GRASS);
        }

        if(tileType == TileTypes.GRASS && energy > 1000) {
            getWorld().queueChangeAt(coordinate.x, coordinate.y, TileTypes.TALL_GRASS);
        }

        /* Randomly lose energy to entropy */
        if(getWorld().getRandom().nextBoolean())
            energy -= tileType.getDefaultDecayRate();

        Optional<List<Tile>> neighborOptional = getNeighbors();
        int number = 0;
        if(neighborOptional.isPresent()) {
            List<Tile> tiles = neighborOptional.get();
            for (Tile t : tiles) {
                if (t.getTileType() == TileTypes.AUTOMATA_SIMPLE) {
                    number++;
                }
            }
        }
        if(tileType == TileTypes.AUTOMATA_SIMPLE) {
            if (number < 2) {
                getWorld().queueChangeAt(coordinate.x, coordinate.y, TileTypes.DIRT);
            }
            else if (number > 3) { getWorld().queueChangeAt(coordinate.x, coordinate.y, TileTypes.DIRT); }
        } else if (number == 3) {
            if(tileType != TileTypes.WATER) {
                getWorld().queueChangeAt(coordinate.x, coordinate.y, TileTypes.AUTOMATA_SIMPLE);
            }
        }

        if(tileType == TileTypes.VIRUS && energy > 0) {

            int i = getWorld().getRandom().nextInt(3) - 1;
            int j = getWorld().getRandom().nextInt(3) - 1;

            try {//
                if (getWorld().getTileAt(coordinate.x + i, coordinate.y + j).getTileType() != TileTypes.VIRUS && energy > 0) {
                    getWorld().queueChangeAt(coordinate.x + i, coordinate.y + j, TileTypes.VIRUS, energy / 2);
                    energy /= 2;
                }
            } catch (Exception ignore) {}
        }

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
