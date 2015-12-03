package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;

import java.awt.image.BufferedImage;

public class SmokeTilePulser implements TilePulser {

    public void destruct(Tile tile) {
        tile.getCoordinate().queueChange(TileType.AIR);
    }

    @Override
    public void pulse(Tile t) {

    }
}
