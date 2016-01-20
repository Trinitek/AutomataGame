package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.gui.Texture;
import com.daexsys.automata.gui.util.ImageUtil;
import com.daexsys.automata.world.tiletypes.pulsers.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TileType {

    public static TileType
            STONE,
            VM_256,
            CGOL,
            GRASS,
            DIRT,
            WATER,
            GREEDY_VIRUS,
            ENERGY_ORE,
            TALL_GRASS,
            AIR,
            MINER,
            SHOCKWAVE_VIRUS,
            WOOD,
            LEAVES,
            SAND,
            BOMB,
            SMOKE,
            BOT,
            AMOEBA,
            HIGH_LIFE,
            SMART_DIRT,
            CORAL,
            ENERGY_TOWER,
            SOLAR_PANEL
    ;

    private static List<TileType> types = new ArrayList<>(32);

    public static TileType getTileFromId(byte id) {
        return types.get(id);
    }

    private byte id;
    private String blockName;
    private byte[] defaultProgram;
    private TilePulser tilePulser;
    private int defaultDecayRate = 1;
    private int defaultEnergy = 10;
    private int energyCap = 20;
    private TypeBehavior typeBehavior = TypeBehavior.NON_DETERMINISTIC;
    private BufferedImage image;
    private String imageLocation;
    private Texture texture;
    private boolean areTilesPulsable = true;

    public void pulse(Tile tile) {
        if(tilePulser != null) {
            tilePulser.pulse(tile);
        } else {
            tile.getTileVM().step();
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

    public String getImageLocation () { return this.imageLocation; }

    public Texture getTexture() { return this.texture; }

    public int getDefaultEnergy() {
        return defaultEnergy;
    }

    public byte getID() {
        return id;
    }

    public byte[] getProgram() {
        return defaultProgram;
    }

    public TypeBehavior getTypeBehavior() {
        return typeBehavior;
    }

    public boolean areTilesPulsable() {
        return areTilesPulsable;
    }

    public int getEnergyCap() {
        return energyCap;
    }

    public static class Builder {
        private TileRegistry tileRegistry;
        private TileType tileType = new TileType();

        public Builder(TileRegistry tileRegistry) {
            this.tileRegistry = tileRegistry;
        }

        public Builder setID(byte id) {
            tileType.id = id;
            return this;
        }

        public Builder setVMProgram(String location) {
            File programFile;
            FileInputStream programStream;
            if (location != null) {
                try {
                    programFile = new File(location);
                    programStream = new FileInputStream(programFile);
                    tileType.defaultProgram = new byte[(int) programFile.length()];
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
            tileType.imageLocation = location;
            try {
                tileType.texture = new Texture(location);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
            return this;
        }

        public Builder setEnergy(int energy) {
            tileType.defaultEnergy = energy;
            return this;
        }

        public Builder setEnergyCap(int energyCap) {
            tileType.energyCap = energyCap;
            return this;
        }

        public Builder setDecayRate(int rate) {
            tileType.defaultDecayRate = rate;
            return this;
        }

        public Builder setAreTilesPulsable(boolean bool) {
            tileType.areTilesPulsable = bool;
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

        public Builder setBehavior(TypeBehavior typeBehavior) {
            tileType.typeBehavior = typeBehavior;
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

    public static void createTiles(TileRegistry registry) {
        STONE = new Builder(registry)
                .setID((byte) 0)
                .setName("stone")
                .setImageLocation("iamge/stone.png")
                .setEnergy(100)
                .setDecayRate(0)
                .build();
        VM_256 = new Builder(registry)
                .setID((byte) 1)
                .setName("vm")
                .setImageLocation("images/digital.png")
                .setEnergy(5000)
                .setDecayRate(0)
                .build();
        //noinspection ArraysAsListWithZeroOrOneArgument
        CGOL = new Builder(registry)
                .setID((byte) 2)
                .setName("CGoL")
                .setImageLocation("images/automata.png")
                .setEnergy(10)
                .setEnergyCap(5)
                .setDecayRate(0)
                .setBehavior(TypeBehavior.DETERMINISTIC)
                .setPulser(new CGoLTilePulser(
                        Arrays.asList(3),
                        Arrays.asList(2, 3)))
                .build();
        GRASS = new Builder(registry)
                .setID((byte) 3)
                .setName("grass")
                .setImageLocation("images/grass.png")
                .setEnergy(25)
                .setDecayRate(0)
                .setEnergyCap(500)
                .setPulser(new FertileTilePulser())
                .build();
        DIRT = new Builder(registry)
                .setID((byte) 4)
                .setName("dirt")
                .setImageLocation("images/dirt.png")
                .setEnergy(45)
                .setDecayRate(1)
                .setPulser(new FertileTilePulser())
                .build();
        WATER = new Builder(registry)
                .setID((byte) 5)
                .setName("water")
                .setImageLocation("images/water.png")
                .setEnergy(5000)
                .setDecayRate(0)
                .setPulser(new WaterTilePulser())
                .build();
        GREEDY_VIRUS = new Builder(registry)
                .setID((byte) 6)
                .setName("greedy virus")
                .setImageLocation("images/virus.png")
                .setEnergy(2000)
                .setDecayRate(1)
                .setPulser(new ViralPulser())
                .build();
        ENERGY_ORE = new Builder(registry)
                .setID((byte) 7)
                .setName("energy ore")
                .setImageLocation("images/energy_ore.png")
                .setEnergy(100)
                .setDecayRate(0)
                .build();
        TALL_GRASS = new Builder(registry)
                .setID((byte) 8)
                .setName("tall grass")
                .setImageLocation("images/tall_grass.png")
                .setEnergy(100)
                .setDecayRate(1)
                .setPulser(new FertileTilePulser())
                .build();
        AIR = new Builder(registry)
                .setID((byte) 9)
                .setName("air")
                .setImageLocation("images/stone.png")
                .setEnergy(50)
                .setDecayRate(0)
                .setAreTilesPulsable(false)
                .build();
        MINER = new Builder(registry)
                .setID((byte) 10)
                .setName("miner test")
                .setImageLocation("images/miner.png")
                .setEnergy(50)
                .setDecayRate(0)
                .setPulser(new MinerPulser())
                .build();
        SHOCKWAVE_VIRUS = new Builder(registry)
                .setID((byte) 11)
                .setName("shockwave virus")
                .setImageLocation("images/shockwave.png")
                .setVMProgram("vmexec/shockwave.vme")
                .setEnergy(50)
                .setDecayRate(0)
                .setPulser(new ShockwaveVirusPulser())
                .build();
        WOOD = new Builder(registry)
                .setID((byte) 12)
                .setName("wood")
                .setImageLocation("images/wood.png")
                .setEnergy(50)
                .setDecayRate(0)
                .build();
        LEAVES = new Builder(registry)
                .setID((byte) 13)
                .setName("leaves")
                .setImageLocation("images/leaves.png")
                .setEnergy(50)
                .setDecayRate(0)
                .build();
        SAND = new Builder(registry)
                .setID((byte) 14)
                .setName("sand")
                .setImageLocation("images/sand.png")
                .setEnergy(50)
                .setDecayRate(0)
                .build();
        BOMB = new Builder(registry)
                .setID((byte) 15)
                .setName("bomb")
                .setImageLocation("images/bomb_block.png")
                .setEnergy(50)
                .setDecayRate(10)
                .setPulser(new BombTilePulser())
                .build();
        SMOKE = new Builder(registry)
                .setID((byte) 16)
                .setName("smoke")
                .setImageLocation("images/steam.png")
                .setEnergy(50)
                .setDecayRate(5)
                .setBehavior(TypeBehavior.DETERMINISTIC)
                .setPulser(new SmokeTilePulser())
                .build();
        BOT = new Builder(registry)
                .setID((byte) 17)
                .setName("bot")
                .setImageLocation("images/bot.png")
                .setEnergy(Integer.MAX_VALUE)
                .setDecayRate(0)
                .setPulser(new BotTilePulser())
                .build();
        AMOEBA = new Builder(registry)
                .setID((byte) 18)
                .setName("amoeba")
                .setImageLocation("images/amoeba.png")
                .setEnergy(10)
                .setDecayRate(0)
                .setBehavior(TypeBehavior.DETERMINISTIC)
                .setPulser(new CGoLTilePulser(
                        Arrays.asList(3, 5, 6, 7, 8),
                        Arrays.asList(5, 6, 7, 8)))
                .build();
        HIGH_LIFE = new Builder(registry)
                .setID((byte) 19)
                .setName("highlife")
                .setImageLocation("images/highlife.png")
                .setEnergy(10)
                .setDecayRate(0)
                .setBehavior(TypeBehavior.DETERMINISTIC)
                .setPulser(new CGoLTilePulser(
                        Arrays.asList(3, 6),
                        Arrays.asList(2, 3)))
                .build();
        SMART_DIRT = new Builder(registry)
                .setID((byte) 20)
                .setName("smart dirt")
                .setImageLocation("images/dirt.png")
                .setVMProgram("vmexec/dirt.vme")
                .setEnergy(45)
                .setDecayRate(1)
                .setPulser(new FertileTilePulser())
                .build();
        //noinspection ArraysAsListWithZeroOrOneArgument
        CORAL = new Builder(registry)
                .setID((byte) 21)
                .setName("highlife")
                .setImageLocation("images/cottencandy.png")
                .setEnergy(10)
                .setDecayRate(0)
                .setBehavior(TypeBehavior.DETERMINISTIC)
                .setPulser(new CGoLTilePulser(
                        Arrays.asList(3),
                        Arrays.asList(4, 5, 6, 7, 8)))
                .build();
        ENERGY_TOWER = new Builder(registry)
                .setID((byte) 22)
                .setName("energytower")
                .setImageLocation("images/energytower.png")
                .setEnergy(150000)
                .setDecayRate(1)
                .setEnergyCap(150000)
                .setPulser(new EnergyTowerPulser())
                .build();
        SOLAR_PANEL = new Builder(registry)
                .setID((byte) 23)
                .setName("solar panel")
                .setImageLocation("images/solarpanel.png")
                .setEnergy(0)
                .setEnergyCap(5)
                .setDecayRate(1)
                .build();

    }

    @Override
    public String toString() {
        return "{tile: " + getName() + " (id:" + getID() + ")}";
    }
}
