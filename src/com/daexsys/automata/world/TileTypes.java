package com.daexsys.automata.world;

import com.daexsys.automata.gui.util.ImageUtil;

public class TileTypes {

    public static TileType STONE =
            new TileType((byte) 0, "stone", ImageUtil.loadImage("images/stone.png"), 50, 0);

    public static TileType VM_255_BYTE_RAM =
            new TileType((byte) 1, "digital", ImageUtil.loadImage("images/digital.png"), 50, 5);

    public static TileType AUTOMATA_SIMPLE =
            new TileType((byte) 2, "automata", ImageUtil.loadImage("images/automata.png"), 10, 1);

    public static TileType GRASS =
            new TileType((byte) 3, "stone", ImageUtil.loadImage("images/grass.png"), 25, 1);

    public static TileType DIRT =
            new TileType((byte) 4, "stone", ImageUtil.loadImage("images/dirt.png"), 45, 1);

    public static TileType WATER =
            new TileType((byte) 5, "stone", ImageUtil.loadImage("images/water.png"), 500, 1);

    public static TileType VIRUS =
            new TileType((byte) 6, "virus", ImageUtil.loadImage("images/virus.png"), 2000, 1);

    // TODO: rename
    public static TileType ENERGY_ORE =
            new TileType((byte) 7, "energy ore", ImageUtil.loadImage("images/energy_ore.png"), 5000, 0);
}
