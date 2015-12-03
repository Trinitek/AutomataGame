package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.gui.util.ImageUtil;
import com.daexsys.automata.world.tiletypes.pulsers.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TileType {

    private static List<TileType> types = new ArrayList<>(32);

    public static TileType getTileFromId(byte id) {
        return types.get(id);
    }

    public static final TileType STONE =
            new TileType.Builder().setID((byte) 0).setName("stone").setImageLocation("images/stone.png")
                    .setEnergy(100).setDecayRate(0).build();

    public static final TileType VM_256 =
            new TileType.Builder().setID((byte) 1).setName("vm").setImageLocation("images/digital.png")
                    .setEnergy(5000).setDecayRate(0).build();

    public static final TileType CGOL;
    static {
        List<Integer> birthRules = new ArrayList<>();
        birthRules.add(3);

        List<Integer> stayAliveRules = new ArrayList<>();
        stayAliveRules.add(2);
        stayAliveRules.add(3);

        CGOL = new TileType.Builder()
                .setID((byte) 2)
                .setName("CGoL")
                .setImageLocation("images/automata.png")
                .setEnergy(10)
                .setDecayRate(0)
                .setPulser(new CGoLTilePulser(birthRules, stayAliveRules))
                .build();
    }

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
                    .setEnergy(50).setDecayRate(0).setPulser(new MinerPulser()).build();

    public static final TileType SHOCKWAVE_VIRUS =
            new TileType.Builder().setID((byte) 11).setName("shockwave virus").setImageLocation("images/shockwave.png")
                    .setEnergy(50).setDecayRate(0).setPulser(new ShockwaveVirusPulser()).build();

    public static final TileType WOOD =
            new TileType.Builder().setID((byte) 12).setName("wood").setImageLocation("images/wood.png")
                    .setEnergy(50).setDecayRate(0).build();

    public static final TileType LEAVES =
            new TileType.Builder().setID((byte) 13).setName("leaves").setImageLocation("images/leaves.png")
                    .setEnergy(50).setDecayRate(0).build();

    public static final TileType SAND =
            new TileType.Builder().setID((byte) 14).setName("sand").setImageLocation("images/sand.png")
                    .setEnergy(50).setDecayRate(0).build();

    public static final TileType BOMB =
            new TileType.Builder().setID((byte) 15).setName("bomb").setImageLocation("images/bomb_block.png")
                    .setEnergy(50).setDecayRate(10).setPulser(new BombTilePulser()).build();

    public static final TileType SMOKE =
            new TileType.Builder().setID((byte) 16).setName("smoke").setImageLocation("images/steam.png")
                    .setEnergy(50).setDecayRate(5).setPulser(new SmokeTilePulser()).build();

    public static final TileType BOT =
            new TileType.Builder().setID((byte) 17).setName("bot").setImageLocation("images/bot.png")
                    .setEnergy(Integer.MAX_VALUE).setDecayRate(0).setPulser(new BotTilePulser()).build();

//    public static final TileType AMOEBA;
//    static {
//        List<Integer> birthRules = new ArrayList<>();
//        birthRules.add(3);
//        birthRules.add(5);
//        birthRules.add(6);
//        birthRules.add(7);
//        birthRules.add(8);
//
//        List<Integer> stayAliveRules = new ArrayList<>();
//        stayAliveRules.add(5);
//        stayAliveRules.add(6);
//        stayAliveRules.add(7);
//        stayAliveRules.add(8);
//
//        AMOEBA = new TileType.Builder()
//                .setID((byte) 18)
//                .setName("amoeba")
//                .setImageLocation("images/automata.png")
//                .setEnergy(10)
//                .setDecayRate(0)
//                .setPulser(new CGoLTilePulser(birthRules, stayAliveRules))
//                .build();
//    }


    private byte id;
    private String blockName;
    private byte[] defaultProgram;
    private TilePulser tilePulser;
    private int defaultDecayRate = 1;
    private int defaultEnergy = 10;
    private BufferedImage image;

    public void pulse(Tile tile) {
        if(tilePulser != null) {
            tilePulser.pulse(tile);
        }
    }

    public void destruct(Tile tile) {
        if(tilePulser != null) {
            tilePulser.destruct(tile);
        } else {
            tile.getCoordinate().queueChange(TileType.DIRT);
        }
    }

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

    public static class Builder {
        private TileRegistry tileRegistry;
        private TileType tileType = new TileType();

        public Builder() {}

        public Builder(TileRegistry tileRegistry) {
            this.tileRegistry = tileRegistry;
        }

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
            if(tileRegistry != null) {
                tileRegistry.registerType(tileType);
            }

            types.add(tileType);

            return tileType;
        }
    }

    @Override
    public String toString() {
        return "{tile: " + getName() + " (id:" + getID() + ")}";
    }
}
