package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.WorldLayers;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

public class TileType {

    private byte id;
    private String blockName;
    private BufferedImage image;

    private int defaultDecayRate = 1;
    private int defaultEnergy = 10;

    public TileType(byte id, String blockName, BufferedImage image, int defaultEnergy, int defaultDecayRate) {
        this.id = id;
        this.blockName = blockName;
        this.image = image;
        this.defaultEnergy = defaultEnergy;
        this.defaultDecayRate = defaultDecayRate;
    }

    public int getDefaultDecayRate() {
        return defaultDecayRate;
    }

    public String getBlockName() {
        return blockName;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getDefaultEnergy() {
        return defaultEnergy;
    }

    public byte getId() {
        return id;
    }

    public void pulse(Tile tile) {
        Optional<List<Tile>> neighborOptional = tile.getNeighbors(WorldLayers.GROUND);
        int number = 0;
        if(neighborOptional.isPresent()) {
            List<Tile> tiles = neighborOptional.get();
            for (Tile t : tiles) {
                if(t != null) {
                    if (t.getTileType() == TileTypes.AUTOMATA_SIMPLE) {
                        number++;
                    }
                }
            }
        }
        if(this == TileTypes.AUTOMATA_SIMPLE) {
            if (number < 2) {
                tile.getWorld().queueChangeAt(tile.getCoordinate().x, tile.getCoordinate().y, TileTypes.DIRT);
            }
            else if (number > 3) { tile.getWorld().queueChangeAt(tile.getCoordinate().x, tile.getCoordinate().y, TileTypes.DIRT); }
        } else if (number == 3) {
            if(this != TileTypes.WATER) {
                tile.getWorld().queueChangeAt(tile.getCoordinate().x, tile.getCoordinate().y, TileTypes.AUTOMATA_SIMPLE);
            }
        }
    }
}
