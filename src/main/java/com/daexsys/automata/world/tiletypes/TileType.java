package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.gui.util.ImageUtil;
import com.daexsys.automata.world.WorldLayer;
import com.google.common.base.Optional;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileType {

    public static final TileType STONE =
            new TileType((byte) 0, "stone", "images/stone.png", 50, 0);

    public static final TileType VM_255_BYTE_RAM =
            new TileType((byte) 1, "virtual machine", "images/digital.png", 1000, 5);

    public static final TileType CGOL =
            new TileType((byte) 2, "CGoL", "images/automata.png", 10, 0);

    public static final TileType GRASS =
            new FertileTileType((byte) 3, "stone", "images/grass.png", 25, 1);

    public static final TileType DIRT =
            new FertileTileType((byte) 4, "stone", "images/dirt.png", 45, 1);

    public static final TileType WATER =
            new WaterTileType((byte) 5, "water", "images/water.png", 5000, 0);

    public static final TileType GREEDY_VIRUS =
            new ViralTileType((byte) 6, "greedy virus", "images/virus.png", 2000, 1);

    // TODO: rename
    public static final TileType ENERGY_ORE =
            new TileType((byte) 7, "energy ore", "images/energy_ore.png", 100, 0);

    public static final TileType TALL_GRASS =
            new FertileTileType((byte) 8, "tall grass", "images/tall_grass.png", 100, 1);

    public static final TileType AIR =
            new TileType((byte) 9, "air", "images/stone.png", 50, 0);

    public static final TileType MINER =
            new MinerTileType((byte) 10, "miner test", "images/miner.png", 50, 0);

    public static final TileType SHOCKWAVE_VIRUS =
            new ShockwaveVirusTileType((byte) 11, "shockwave virus", "images/shockwave.png", 50, 0);

    public static final TileType WOOD =
            new TileType((byte) 12, "wood", "images/wood.png", 50, 0);

    public static final TileType LEAVES =
            new TileType((byte) 13, "leaves", "images/leaves.png", 50, 0);

    public static final TileType SAND =
            new TileType((byte) 14, "sand", "images/sand.png", 50, 0);

    public static final TileType BOMB =
            new BombTileType((byte) 15, "bomb", "images/bomb_block.png", 50, 10);

    public static final TileType SMOKE =
            new SmokeTileType((byte) 16, "steam", "images/steam.png", 50, 5);

    private byte id;
    private String blockName;
    private BufferedImage image;

    private int defaultDecayRate = 1;
    private int defaultEnergy = 10;

    private static ArrayList<TileType> types = new ArrayList<>();

    public TileType(byte id, String blockName, String image, int defaultEnergy, int defaultDecayRate) {
        this.id = id;
        this.blockName = blockName;
        this.image = ImageUtil.loadImage(image);
        this.defaultEnergy = defaultEnergy;
        this.defaultDecayRate = defaultDecayRate;
        types.add(this);
    }

    public int getDefaultDecayRate() {
        return defaultDecayRate;
    }

    public String getTileName() {
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

    public static TileType getTileFromId(byte id) {
        return types.get(id);
    }

    public void pulse(Tile tile) {
        List<Tile> neighborOptional = tile.getNeighbors(0);
        int number = 0;

        for (Tile t : neighborOptional) {
            if(t != null) {
                if (t.getType() == TileType.CGOL) {
                    number++;
                }
            }
        }
        if(this == TileType.CGOL) {
            if (number < 2) {
                tile.getCoordinate().queueChange(TileType.DIRT);
            }
            else if (number > 3) { tile.getCoordinate().queueChange(TileType.DIRT); }
        } else if (number == 3) {
            if(this != TileType.WATER) {
                tile.getCoordinate().queueChange(TileType.CGOL);
            }
        }
    }

    public void destruct(Tile tile) {
        tile.getCoordinate().queueChange(TileType.DIRT);
    }
}
