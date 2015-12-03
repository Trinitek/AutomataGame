package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.gui.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TileType {

    public static final TileType STONE =
            new TileType.Builder().setID((byte) 0).setName("stone").setImageLocation("images/stone.png")
                    .setEnergy(100).setDecayRate(0).build();

    public static final TileType VM_255_BYTE_RAM =
            new TileType.Builder().setID((byte) 1).setName("vm").setImageLocation("images/digital.png")
                    .setEnergy(5000).setDecayRate(0).build();

    public static final TileType CGOL =
            new TileType.Builder()
                    .setID((byte) 2)
                    .setName("CGoL")
                    .setImageLocation("images/automata.png")
                    .setEnergy(10)
                    .setDecayRate(0)
                    .setPulser(new CGoLTilePulser())
                    .build();

    public static final TileType GRASS =
            new TileType.Builder().setID((byte) 3).setName("grass").setImageLocation("images/grass.png")
                    .setEnergy(25).setDecayRate(0).setPulser(new FertileTilePulser()).build();

    public static final TileType DIRT =
            new TileType.Builder().setID((byte) 4).setName("dirt").setImageLocation("images/dirt.png")
                    .setEnergy(45).setDecayRate(1).setPulser(new FertileTilePulser()).build();

    public static final TileType WATER =
            new TileType.Builder().setID((byte) 5).setName("water").setImageLocation("images/water.png")
                    .setEnergy(5000).setDecayRate(0).setPulser(new WaterTilePulser()).build();

    public static final TileType GREEDY_VIRUS =
            new TileType.Builder().setID((byte) 6).setName("greedy virus").setImageLocation("images/virus.png")
                    .setEnergy(2000).setDecayRate(1).setPulser(new ViralPulser()).build();

    // TODO: rename
    public static final TileType ENERGY_ORE =
            new TileType.Builder().setID((byte) 7).setName("energy ore").setImageLocation("images/energy_ore.png")
                    .setEnergy(100).setDecayRate(0).build();

    public static final TileType TALL_GRASS =
            new TileType.Builder().setID((byte) 8).setName("tall grass").setImageLocation("images/tall_grass.png")
                    .setEnergy(100).setDecayRate(1).setPulser(new FertileTilePulser()).build();

    public static final TileType AIR =
            new TileType.Builder().setID((byte) 9).setName("air").setImageLocation("images/stone.png")
                    .setEnergy(50).setDecayRate(0).build();

    public static final TileType MINER =
            new TileType.Builder().setID((byte) 10).setName("miner test").setImageLocation("images/miner.png")
                    .setEnergy(50).setDecayRate(0).build();

    //new ShockwaveVirusTileType((byte) 11, "shockwave virus", "images/shockwave.png", 50, 0);
    public static final TileType SHOCKWAVE_VIRUS =
            new TileType.Builder().setID((byte) 11).setName("shockwave virus").setImageLocation("images/shockwave.png")
                    .setEnergy(50).setDecayRate(0).setPulser(new ShockwaveVirusPulser()).build();

    //new TileType((byte) 12, "wood", "images/wood.png", 50, 0);
    public static final TileType WOOD =
            new TileType.Builder().setID((byte) 12).setName("wood").setImageLocation("images/wood.png")
                    .setEnergy(50).setDecayRate(0).build();

    //new TileType((byte) 13, "leaves", "images/leaves.png", 50, 0);
    public static final TileType LEAVES =
            new TileType.Builder().setID((byte) 13).setName("leaves").setImageLocation("images/leaves.png")
                    .setEnergy(50).setDecayRate(0).build();

    //new TileType((byte) 14, "sand", "images/sand.png", 50, 0);leaves
    public static final TileType SAND =
            new TileType.Builder().setID((byte) 14).setName("sand").setImageLocation("images/sand.png")
                    .setEnergy(50).setDecayRate(0).build();

    //new BombTileType((byte) 15, "bomb", "images/bomb_block.png", 50, 10);
    public static final TileType BOMB =
            new TileType.Builder().setID((byte) 15).setName("bomb").setImageLocation("images/bomb_block.png")
                    .setEnergy(50).setDecayRate(10).setPulser(new BombTilePulser()).build();

    //new SmokeTileType((byte) 16, "steam", "images/steam.png", 50, 5);
    public static final TileType SMOKE =
            new TileType.Builder().setID((byte) 16).setName("smoke").setImageLocation("images/steam.png")
                    .setEnergy(50).setDecayRate(5).setPulser(new SmokeTilePulser()).build();

    //new BotTileType((byte) 17, "bot", "images/bot.png", Integer.MAX_VALUE, 0)
    public static final TileType BOT =
            new TileType.Builder().setID((byte) 17).setName("bot").setImageLocation("images/bot.png")
                    .setEnergy(Integer.MAX_VALUE).setDecayRate(0).setPulser(new BotTilePulser()).build();

    private byte id;
    private String blockName;
    private BufferedImage image;
    private byte[] defaultProgram;

    private TilePulser tilePulser;

    private int defaultDecayRate = 1;
    private int defaultEnergy = 10;

    public int getDefaultDecayRate() {
        return defaultDecayRate;
    }

    public String getName() {
        return blockName;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getDefaultEnergy() {
        return defaultEnergy;
    }

    public byte getID() {
        return id;
    }

    public byte[] getProgram() {
        return defaultProgram;
    }

    public void pulse(Tile tile) {
        tilePulser.pulse(tile);
    }

    public void destruct(Tile tile) {
        tile.getCoordinate().queueChange(TileType.DIRT);
    }

    static class Builder {
        private TileType tileType = new TileType();

        public Builder setID(byte id) {
            tileType.id = id;
            return this;
        }

        public Builder setVMProgram(String location) {
            FileInputStream programStream;
            if (location != null) {
                try {
                    programStream = new FileInputStream(new File(location));
                    //noinspection ResultOfMethodCallIgnored
                    programStream.read(tileType.defaultProgram);
                    programStream.close();
                } catch (IOException e) {
                    System.err.println("Can't find VME file '" + location + "'");
                }
            }
            return this;
        }

        public Builder setImageLocation(String location) {
            tileType.image = ImageUtil.loadImage(location);
            return this;
        }

        public Builder setEnergy(int energy) {
            tileType.defaultEnergy = energy;
            return this;
        }

        public Builder setDecayRate(int rate) {
            tileType.defaultDecayRate = rate;
            return this;
        }

        public Builder setName(String name) {
            tileType.blockName = name;
            return this;
        }

        public Builder setPulser(TilePulser tilePulser) {
            tileType.tilePulser = tilePulser;
            return this;
        }

        public TileType build() {
//            types.set(tileType.getID(), tileType);
            return tileType;
        }
    }

    private static List<TileType> types = new ArrayList<>(32);

    public static TileType getTileFromId(byte id) {
        return types.get(id);
    }
}
