package com.daexsys.automata.world;

import com.daexsys.automata.gui.util.ImageUtil;

public class TileTypes {

    public static TileType STONE =
            new TileType((byte) 0, "stone", ImageUtil.loadImage("images/stone.png"), 50);

    public static TileType VM_255_BYTE_RAM =
            new TileType((byte) 1, "digital", ImageUtil.loadImage("images/digital.png"), 50);

    public static TileType AUTOMATA_SIMPLE =
            new TileType((byte) 2, "automata", ImageUtil.loadImage("images/automata.png"), 10);

    public static TileType GRASS =
            new TileType((byte) 3, "stone", ImageUtil.loadImage("images/grass.png"), 25);

    public static TileType DIRT =
            new TileType((byte) 4, "stone", ImageUtil.loadImage("images/dirt.png"), 45);

    public static TileType WATER =
            new TileType((byte) 5, "stone", ImageUtil.loadImage("images/water.png"), 500);

    public static TileType VIRUS =
            new TileType((byte) 6, "virus", ImageUtil.loadImage("images/virus.png"), 2000);
}
