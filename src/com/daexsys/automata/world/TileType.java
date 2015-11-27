package com.daexsys.automata.world;

import java.awt.image.BufferedImage;

public class TileType {

    private byte id;
    private String blockName;
    private BufferedImage image;

    private int defaultEnergy = 50;

    public TileType(byte id, String blockName, BufferedImage image, int defaultEnergy) {
        this.id = id;
        this.blockName = blockName;
        this.image = image;
        this.defaultEnergy = defaultEnergy;
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
}
