package com.daexsys.automata.vm;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.World;

public class HardwareWorldInteraction {

    private TileVM tileVM;

    public HardwareWorldInteraction(TileVM tileVM) {
        this.tileVM = tileVM;
    }

    public void placeBlock(int relativeX, int relativeY, Tile tileType) {
        Tile tile = tileVM.getTile();
        World world = tile.getWorld();
        world.setTileAt(tileType, relativeX, relativeY);
    }

    public TileVM getTileVM() {
        return tileVM;
    }
}
