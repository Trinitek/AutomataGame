package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.gui.util.ImageUtil;

public class TileTypes {

    public static final TileType STONE =
            new TileType((byte) 0, "stone", ImageUtil.loadImage("images/stone.png"), 50, 0);

    public static final TileType VM_255_BYTE_RAM =
            new TileType((byte) 1, "virtual machine", ImageUtil.loadImage("images/digital.png"), 1000, 5);

    public static final TileType CGOL =
            new TileType((byte) 2, "CGOL", ImageUtil.loadImage("images/automata.png"), 10, 0);

    public static final TileType GRASS =
            new FertileTileType((byte) 3, "stone", ImageUtil.loadImage("images/grass.png"), 25, 1);

    public static final TileType DIRT =
            new FertileTileType((byte) 4, "stone", ImageUtil.loadImage("images/dirt.png"), 45, 1);

    public static final TileType WATER =
            new WaterTileType((byte) 5, "water", ImageUtil.loadImage("images/water.png"), 5000, 0);

    public static final TileType VIRUS =
            new ViralTileType((byte) 6, "greedy virus", ImageUtil.loadImage("images/virus.png"), 2000, 1);

    // TODO: rename
    public static final TileType ENERGY_ORE =
            new TileType((byte) 7, "energy ore", ImageUtil.loadImage("images/energy_ore.png"), 100, 0);

    public static final TileType TALL_GRASS =
            new FertileTileType((byte) 8, "tall grass", ImageUtil.loadImage("images/tall_grass.png"), 100, 1);

    public static final TileType AIR =
            new TileType((byte) 9, "stone", ImageUtil.loadImage("images/stone.png"), 50, 0);

    public static final TileType MINER =
            new MinerTileType((byte) 10, "miner test", ImageUtil.loadImage("images/miner.png"), 50, 0);

    public static final TileType EQUAL_VIRUS =
            new EqualVirusTileType((byte) 11, "shockwave test virus", ImageUtil.loadImage("images/shockwave.png"), 50, 0);

    public static final TileType WOOD =
            new TileType((byte) 12, "wood", ImageUtil.loadImage("images/wood.png"), 50, 0);

    public static final TileType LEAVES =
            new TileType((byte) 13, "leaves", ImageUtil.loadImage("images/leaves.png"), 50, 0);
}
