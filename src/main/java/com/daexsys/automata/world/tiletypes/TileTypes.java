package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.gui.util.ImageUtil;
import com.daexsys.automata.world.tiletypes.FertileTileType;
import com.daexsys.automata.world.tiletypes.MinerTileType;
import com.daexsys.automata.world.tiletypes.TileType;
import com.daexsys.automata.world.tiletypes.ViralTileType;

public class TileTypes {

    public static final TileType STONE =
            new TileType((byte) 0, "stone", ImageUtil.loadImage("images/stone.png"), 50, 0);

    public static final TileType VM_255_BYTE_RAM =
            new TileType((byte) 1, "computer", ImageUtil.loadImage("images/digital.png"), 1000, 5);

    public static final TileType AUTOMATA_SIMPLE =
            new TileType((byte) 2, "automata", ImageUtil.loadImage("images/automata.png"), 10, 1);

    public static final TileType GRASS =
            new FertileTileType((byte) 3, "stone", ImageUtil.loadImage("images/grass.png"), 25, 1);

    public static final TileType DIRT =
            new FertileTileType((byte) 4, "stone", ImageUtil.loadImage("images/dirt.png"), 45, 1);

    public static final TileType WATER =
            new TileType((byte) 5, "stone", ImageUtil.loadImage("images/water.png"), 500, 1);

    public static final TileType VIRUS =
            new ViralTileType((byte) 6, "virus", ImageUtil.loadImage("images/virus.png"), 2000, 1);

    // TODO: rename
    public static final TileType ENERGY_ORE =
            new TileType((byte) 7, "energy ore", ImageUtil.loadImage("images/energy_ore.png"), 100, 0);

    public static final TileType TALL_GRASS =
            new FertileTileType((byte) 8, "tall grass", ImageUtil.loadImage("images/tall_grass.png"), 100, 1);

    public static final TileType AIR =
            new TileType((byte) 9, "stone", ImageUtil.loadImage("images/stone.png"), 50, 0);

    public static final TileType MINER =
            new MinerTileType((byte) 10, "berzerker", ImageUtil.loadImage("images/miner.png"), 50, 0);
}