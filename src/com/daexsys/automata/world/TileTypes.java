package com.daexsys.automata.world;

import com.daexsys.automata.gui.util.ImageUtil;

public class TileTypes {

    public static TileType STONE =
            new TileType((byte) 0, "stone", ImageUtil.loadImage("images/stone.png"), 50);

    public static TileType DIGITAL =
            new TileType((byte) 1, "digital", ImageUtil.loadImage("images/digital.png"), 50);

    public static TileType AUTOMATA_SIMPLE =
            new TileType((byte) 2, "automata", ImageUtil.loadImage("images/automata.png"), 50);
}
