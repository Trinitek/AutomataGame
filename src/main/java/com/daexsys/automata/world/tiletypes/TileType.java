package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.gui.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileType {

    public static final TileType STONE =
            new TileType((byte) 0, "stone", "images/stone.png", "vmexec/stone.vme", 50, 0);

    public static final TileType VM_255_BYTE_RAM =
            new TileType((byte) 1, "virtual machine", "images/digital.png", null, 1000, 5);

    public static final TileType CGOL =
            new CGoLTileType((byte) 2, "CGoL", "images/automata.png", "vmexec/cgol.vme", 10, 0);

    public static final TileType GRASS =
            new FertileTileType((byte) 3, "stone", "images/grass.png", "vmexec/grass.vme", 25, 1);

    public static final TileType DIRT =
            new FertileTileType((byte) 4, "stone", "images/dirt.png", "vmexec/dirt.vme", 45, 1);

    public static final TileType WATER =
            new WaterTileType((byte) 5, "water", "images/water.png", "vmexec/water.vme", 5000, 0);

    public static final TileType GREEDY_VIRUS =
            new ViralTileType((byte) 6, "greedy virus", "images/virus.png", "vmexec/virus.vme", 2000, 1);

    // TODO: rename
    public static final TileType ENERGY_ORE =
            new TileType((byte) 7, "energy ore", "images/energy_ore.png", "vmexec/energy_ore.vme", 100, 0);

    public static final TileType TALL_GRASS =
            new FertileTileType((byte) 8, "tall grass", "images/tall_grass.png", "vmexec/tall_grass.vme", 100, 1);

    public static final TileType AIR =
            new TileType((byte) 9, "air", "images/stone.png", "vmexec/air.vme", 50, 0);

    public static final TileType MINER =
            new MinerTileType((byte) 10, "miner test", "images/miner.png", "vmexec/miner.vme", 50, 0);

    public static final TileType SHOCKWAVE_VIRUS =
            new ShockwaveVirusTileType((byte) 11, "shockwave virus", "images/shockwave.png", "vmexec/shockwave.vme", 50, 0);

    public static final TileType WOOD =
            new TileType((byte) 12, "wood", "images/wood.png", "vmexec/wood.vme", 50, 0);

    public static final TileType LEAVES =
            new TileType((byte) 13, "leaves", "images/leaves.png", "vmexec/leaves.vme", 50, 0);

    public static final TileType SAND =
            new TileType((byte) 14, "sand", "images/sand.png", "vmexec/sand.vme", 50, 0);

    public static final TileType BOMB =
            new BombTileType((byte) 15, "bomb", "images/bomb_block.png", "vmexec/bomb.vme", 50, 10);

    public static final TileType SMOKE =
            new SmokeTileType((byte) 16, "steam", "images/steam.png", "vmexec/smoke.vme", 50, 5);

    private byte id;
    private String blockName;
    private BufferedImage image;

    private int defaultDecayRate = 1;
    private int defaultEnergy = 10;
    private byte[] defaultProgram = new byte[]{0x50};

    private static List<TileType> types = new ArrayList<>();

    public TileType(byte id, String blockName, String imageUrl, String programUrl, int defaultEnergy, int defaultDecayRate) {
        this.id = id;
        this.blockName = blockName;
        this.image = ImageUtil.loadImage(imageUrl);
        this.defaultEnergy = defaultEnergy;
        this.defaultDecayRate = defaultDecayRate;

        FileInputStream programStream;
        try {
            programStream = new FileInputStream(new File(programUrl));
            //noinspection ResultOfMethodCallIgnored
            programStream.read(this.defaultProgram);
            programStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void pulse(Tile tile) {}

    public void destruct(Tile tile) {
        tile.getCoordinate().queueChange(TileType.DIRT);
    }
}
